import { useState } from 'react'
import { Link } from 'react-router-dom'
import { createPortal } from 'react-dom'
import { useAuth } from '../contexts/AuthContext'
import api from '../api/client'
import '../styles/admin.css'

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

const EMPTY_PW = { current: '', next: '', confirm: '' }

function AdminLogin() {
  const { login, logout, hasAnyRole, role, displayName, canManageUsers } = useAuth()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const [showPwModal, setShowPwModal] = useState(false)
  const [pw, setPw] = useState(EMPTY_PW)
  const [pwError, setPwError] = useState('')
  const [pwSaving, setPwSaving] = useState(false)
  const [pwSuccess, setPwSuccess] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await login(username, password)
    } catch {
      setError('Invalid username or password.')
    } finally {
      setLoading(false)
    }
  }

  async function handleChangePassword() {
    setPwError('')
    if (!pw.current || !pw.next || !pw.confirm) {
      setPwError('All fields are required.')
      return
    }
    if (pw.next !== pw.confirm) {
      setPwError('New passwords do not match.')
      return
    }
    if (pw.next.length < 6) {
      setPwError('New password must be at least 6 characters.')
      return
    }
    setPwSaving(true)
    try {
      await api.post('/admin/profile/password', { currentPassword: pw.current, newPassword: pw.next })
      setPwSuccess(true)
      setPw(EMPTY_PW)
      setTimeout(() => {
        setPwSuccess(false)
        setShowPwModal(false)
      }, 1500)
    } catch (err) {
      setPwError(err.message || 'Failed to change password.')
    } finally {
      setPwSaving(false)
    }
  }

  if (hasAnyRole) {
    return (
      <div className="admin-login-wrap">
        <div className="admin-login-form" style={{ maxWidth: 420, gap: '1.5rem' }}>
          <div style={{ textAlign: 'center' }}>
            <h1 className="glow-text" style={{ fontSize: '1.2rem', marginBottom: '0.5rem' }}>DASHBOARD</h1>
            <p style={{ opacity: 0.6, margin: 0, fontSize: '0.85rem' }}>Logged in as</p>
            <p style={{ margin: '0.25rem 0 0.5rem', fontWeight: 'bold' }}>{displayName}</p>
            <span className={roleBadgeClass(role)}>{ROLE_LABELS[role] || role}</span>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
            <Link to="/" className="dashboard-link">Home</Link>
            <Link to="/games" className="dashboard-link">Games</Link>
            <Link to="/content" className="dashboard-link">Content Library</Link>
            {canManageUsers && <Link to="/admin/users" className="dashboard-link">User Management</Link>}
            <Link to="/profile" className="dashboard-link">Profile</Link>
            <button
              className="dashboard-link"
              style={{ background: 'none', border: '1px solid rgba(0,255,255,0.25)', cursor: 'pointer', textAlign: 'left', color: 'inherit', fontFamily: 'inherit' }}
              onClick={() => { setShowPwModal(true); setPw(EMPTY_PW); setPwError(''); setPwSuccess(false) }}
            >
              Change Password
            </button>
          </div>

          <button className="dashboard-logout-btn" onClick={logout}>
            Logout
          </button>
        </div>

        {showPwModal && createPortal(
          <div className="user-modal-overlay" onClick={() => setShowPwModal(false)}>
            <div className="user-modal" onClick={e => e.stopPropagation()}>
              <h3>Change Password</h3>
              {pwError && <p style={{ color: '#ff4466', margin: 0, fontSize: '0.85rem' }}>{pwError}</p>}
              {pwSuccess && <p style={{ color: '#00ff64', margin: 0, fontSize: '0.85rem' }}>Password changed successfully.</p>}
              <div className="admin-field">
                <label>Current Password</label>
                <input
                  type="password"
                  value={pw.current}
                  onChange={e => setPw(p => ({ ...p, current: e.target.value }))}
                  autoComplete="current-password"
                />
              </div>
              <div className="admin-field">
                <label>New Password</label>
                <input
                  type="password"
                  value={pw.next}
                  onChange={e => setPw(p => ({ ...p, next: e.target.value }))}
                  autoComplete="new-password"
                />
              </div>
              <div className="admin-field">
                <label>Confirm New Password</label>
                <input
                  type="password"
                  value={pw.confirm}
                  onChange={e => setPw(p => ({ ...p, confirm: e.target.value }))}
                  autoComplete="new-password"
                />
              </div>
              <div className="user-modal-actions">
                <button className="user-modal-submit" onClick={handleChangePassword} disabled={pwSaving || pwSuccess}>
                  {pwSaving ? 'Saving…' : 'Save'}
                </button>
                <button className="user-modal-cancel" onClick={() => setShowPwModal(false)}>Cancel</button>
              </div>
            </div>
          </div>,
          document.body
        )}
      </div>
    )
  }

  return (
    <div className="admin-login-wrap">
      <form className="admin-login-form" onSubmit={handleSubmit}>
        <h1 className="glow-text">Admin Login</h1>
        {error && <p className="admin-login-error">{error}</p>}
        <div className="admin-field">
          <label htmlFor="username">Username</label>
          <input
            id="username"
            type="text"
            value={username}
            onChange={e => setUsername(e.target.value)}
            autoComplete="username"
            required
          />
        </div>
        <div className="admin-field">
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            autoComplete="current-password"
            required
          />
        </div>
        <button type="submit" disabled={loading}>
          {loading ? 'Logging in…' : 'Login'}
        </button>
      </form>
    </div>
  )
}

export default AdminLogin
