import { useState, useEffect } from 'react'
import { createPortal } from 'react-dom'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { userManagementService } from '../api/services/userManagementService'
import '../styles/admin.css'
import '../styles/cards.css'

const ROLE_LABELS = {
  ADMIN: 'Admin',
  MODERATOR: 'Moderator',
  CONTENT_CREATOR: 'Content Creator',
}

function roleBadgeClass(role) {
  if (role === 'ADMIN') return 'role-badge role-admin'
  if (role === 'MODERATOR') return 'role-badge role-moderator'
  return 'role-badge role-content-creator'
}

function allowedRolesFor(actorRole) {
  if (actorRole === 'ADMIN') return ['ADMIN', 'MODERATOR', 'CONTENT_CREATOR']
  if (actorRole === 'MODERATOR') return ['MODERATOR', 'CONTENT_CREATOR']
  return []
}

function canActOn(actorRole, targetRole) {
  if (actorRole === 'ADMIN') return targetRole !== 'ADMIN'
  if (actorRole === 'MODERATOR') return targetRole === 'CONTENT_CREATOR'
  return false
}

const EMPTY_FORM = { username: '', password: '', displayName: '', email: '', role: 'CONTENT_CREATOR' }

function UserManagement() {
  const navigate = useNavigate()
  const { role: actorRole, userId: actorId, canManageUsers } = useAuth()

  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showCreateModal, setShowCreateModal] = useState(false)
  const [form, setForm] = useState(EMPTY_FORM)
  const [formError, setFormError] = useState('')
  const [saving, setSaving] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  // pending role changes: { [userId]: newRole }
  const [pendingRoles, setPendingRoles] = useState({})
  // role change confirmation: { user, newRole }
  const [roleChangeTarget, setRoleChangeTarget] = useState(null)

  useEffect(() => {
    if (!canManageUsers) {
      navigate('/admin')
      return
    }
    fetchUsers()
  }, [canManageUsers])

  function fetchUsers() {
    setLoading(true)
    userManagementService.getAll()
      .then(r => setUsers(r.data))
      .catch(() => setError('Failed to load users.'))
      .finally(() => setLoading(false))
  }

  async function handleCreate() {
    if (!form.username.trim() || !form.password.trim() || !form.email.trim()) {
      setFormError('Username, password and email are required.')
      return
    }
    setSaving(true)
    setFormError('')
    try {
      await userManagementService.create(form)
      setShowCreateModal(false)
      setForm(EMPTY_FORM)
      fetchUsers()
    } catch (err) {
      setFormError(err.response?.data?.message || err.message || 'Failed to create user.')
    } finally {
      setSaving(false)
    }
  }

  function handleRoleSelect(userId, newRole) {
    setPendingRoles(prev => ({ ...prev, [userId]: newRole }))
  }

  function handleRoleSave(user) {
    const newRole = pendingRoles[user.id]
    if (!newRole || newRole === user.role) return
    setRoleChangeTarget({ user, newRole })
  }

  async function handleRoleConfirm() {
    if (!roleChangeTarget) return
    const { user, newRole } = roleChangeTarget
    try {
      await userManagementService.updateRole(user.id, newRole)
      setUsers(prev => prev.map(u => u.id === user.id ? { ...u, role: newRole } : u))
      setPendingRoles(prev => { const next = { ...prev }; delete next[user.id]; return next })
    } catch (err) {
      alert(err.response?.data?.message || err.message || 'Failed to update role.')
    } finally {
      setRoleChangeTarget(null)
    }
  }

  async function handleDelete() {
    if (!deleteTarget) return
    try {
      await userManagementService.remove(deleteTarget.id)
      setUsers(prev => prev.filter(u => u.id !== deleteTarget.id))
      setDeleteTarget(null)
    } catch (err) {
      alert(err.response?.data?.message || err.message || 'Failed to delete user.')
      setDeleteTarget(null)
    }
  }

  const allowedRoles = allowedRolesFor(actorRole)

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="glow-text">USER MANAGEMENT</h1>
        <p>Manage roles and access for your team</p>
        <button className="new-post-btn" onClick={() => { setShowCreateModal(true); setForm(EMPTY_FORM); setFormError('') }}>
          + NEW USER
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      {loading ? (
        <div className="loading">LOADING USERS...</div>
      ) : (
        <div className="admin-container">
          <table className="users-table">
            <thead>
              <tr>
                <th>Username</th>
                <th>Display Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map(user => {
                const isSelf = user.id === actorId
                const canAct = !isSelf && canActOn(actorRole, user.role)
                const selectedRole = pendingRoles[user.id] ?? user.role
                const roleChanged = pendingRoles[user.id] && pendingRoles[user.id] !== user.role
                return (
                  <tr key={user.id}>
                    <td>{user.username}</td>
                    <td>{user.displayName || '—'}</td>
                    <td>{user.email}</td>
                    <td><span className={roleBadgeClass(user.role)}>{ROLE_LABELS[user.role] || user.role}</span></td>
                    <td>
                      <div className="user-actions">
                        {canAct && allowedRoles.length > 0 && (
                          <>
                            <select
                              className="role-select"
                              value={selectedRole}
                              onChange={e => handleRoleSelect(user.id, e.target.value)}
                            >
                              {allowedRoles.map(r => (
                                <option key={r} value={r}>{ROLE_LABELS[r]}</option>
                              ))}
                            </select>
                            {roleChanged && (
                              <button
                                className="role-save-btn"
                                onClick={() => handleRoleSave(user)}
                              >
                                Save
                              </button>
                            )}
                          </>
                        )}
                        {canAct && (
                          <button
                            className="user-delete-btn"
                            onClick={() => setDeleteTarget(user)}
                          >
                            Delete
                          </button>
                        )}
                        {isSelf && <span style={{ opacity: 0.4, fontSize: '0.8rem' }}>You</span>}
                      </div>
                    </td>
                  </tr>
                )
              })}
              {users.length === 0 && (
                <tr><td colSpan={5} style={{ textAlign: 'center', opacity: 0.5, padding: '2rem' }}>No users found.</td></tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {/* Create User Modal */}
      {showCreateModal && createPortal(
        <div className="user-modal-overlay" onClick={() => setShowCreateModal(false)}>
          <div className="user-modal" onClick={e => e.stopPropagation()}>
            <h3>New User</h3>
            {formError && <p style={{ color: '#ff4466', margin: 0, fontSize: '0.85rem' }}>{formError}</p>}
            <div className="admin-field">
              <label>Username</label>
              <input value={form.username} onChange={e => setForm(f => ({ ...f, username: e.target.value }))} placeholder="username" />
            </div>
            <div className="admin-field">
              <label>Password</label>
              <input type="password" value={form.password} onChange={e => setForm(f => ({ ...f, password: e.target.value }))} placeholder="password" />
            </div>
            <div className="admin-field">
              <label>Display Name</label>
              <input value={form.displayName} onChange={e => setForm(f => ({ ...f, displayName: e.target.value }))} placeholder="display name (optional)" />
            </div>
            <div className="admin-field">
              <label>Email</label>
              <input type="email" value={form.email} onChange={e => setForm(f => ({ ...f, email: e.target.value }))} placeholder="email@example.com" />
            </div>
            <div className="admin-field">
              <label>Role</label>
              <select className="role-select" style={{ width: '100%', padding: '10px' }} value={form.role} onChange={e => setForm(f => ({ ...f, role: e.target.value }))}>
                {allowedRoles.map(r => (
                  <option key={r} value={r}>{ROLE_LABELS[r]}</option>
                ))}
              </select>
            </div>
            <div className="user-modal-actions">
              <button className="user-modal-submit" onClick={handleCreate} disabled={saving}>
                {saving ? 'Creating…' : 'Create'}
              </button>
              <button className="user-modal-cancel" onClick={() => setShowCreateModal(false)}>Cancel</button>
            </div>
          </div>
        </div>,
        document.body
      )}

      {/* Role Change Confirm Modal */}
      {roleChangeTarget && createPortal(
        <div className="delete-modal-overlay" onClick={() => setRoleChangeTarget(null)}>
          <div className="delete-modal" onClick={e => e.stopPropagation()}>
            <div className="delete-modal-icon">⚠</div>
            <h4 className="delete-modal-title">CHANGE ROLE</h4>
            <p className="delete-modal-msg">
              Change <strong>{roleChangeTarget.user.username}</strong>'s role to{' '}
              <strong>{ROLE_LABELS[roleChangeTarget.newRole]}</strong>?
            </p>
            <div className="delete-modal-actions">
              <button className="delete-modal-confirm" onClick={handleRoleConfirm}>Confirm</button>
              <button className="delete-modal-cancel" onClick={() => setRoleChangeTarget(null)}>Cancel</button>
            </div>
          </div>
        </div>,
        document.body
      )}

      {/* Delete Confirm Modal */}
      {deleteTarget && createPortal(
        <div className="delete-modal-overlay" onClick={() => setDeleteTarget(null)}>
          <div className="delete-modal" onClick={e => e.stopPropagation()}>
            <div className="delete-modal-icon">⚠</div>
            <h4 className="delete-modal-title">DELETE USER</h4>
            <p className="delete-modal-msg">
              "<strong>{deleteTarget.username}</strong>" will be permanently removed.
            </p>
            <div className="delete-modal-actions">
              <button className="delete-modal-confirm" onClick={handleDelete}>Delete</button>
              <button className="delete-modal-cancel" onClick={() => setDeleteTarget(null)}>Cancel</button>
            </div>
          </div>
        </div>,
        document.body
      )}
    </div>
  )
}

export default UserManagement
