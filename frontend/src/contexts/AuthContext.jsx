import { createContext, useContext, useState } from 'react'
import authService from '../api/services/authService'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [role, setRole] = useState(() => localStorage.getItem('admin_role') || null)
  const [userId, setUserId] = useState(() => {
    const stored = localStorage.getItem('admin_userId')
    return stored ? Number(stored) : null
  })
  const [username, setUsername] = useState(() => localStorage.getItem('admin_username') || null)
  const [displayName, setDisplayName] = useState(() => localStorage.getItem('admin_displayName') || null)

  const isAdmin = role === 'ADMIN'
  const isModerator = role === 'MODERATOR'
  const isContentCreator = role === 'CONTENT_CREATOR'
  const canManageUsers = isAdmin || isModerator
  const canPublish = isAdmin || isModerator
  const canEditProfile = isAdmin || isModerator
  const hasAnyRole = !!role

  async function login(usernameArg, password) {
    const data = await authService.login(usernameArg, password)
    localStorage.setItem('admin_token', data.token)
    localStorage.setItem('admin_role', data.role)
    localStorage.setItem('admin_userId', String(data.userId))
    localStorage.setItem('admin_username', data.username)
    localStorage.setItem('admin_displayName', data.displayName || data.username)
    setRole(data.role)
    setUserId(data.userId)
    setUsername(data.username)
    setDisplayName(data.displayName || data.username)
    return data
  }

  function logout() {
    authService.logout()
    localStorage.removeItem('admin_role')
    localStorage.removeItem('admin_userId')
    localStorage.removeItem('admin_username')
    localStorage.removeItem('admin_displayName')
    setRole(null)
    setUserId(null)
    setUsername(null)
    setDisplayName(null)
  }

  return (
    <AuthContext.Provider value={{
      role, userId, username, displayName,
      isAdmin, isModerator, isContentCreator,
      canManageUsers, canPublish, canEditProfile, hasAnyRole,
      login, logout
    }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
