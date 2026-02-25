import { createContext, useContext, useState } from 'react'
import authService from '../api/services/authService'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [isAdmin, setIsAdmin] = useState(() => !!localStorage.getItem('admin_token'))

  async function login(username, password) {
    const data = await authService.login(username, password)
    localStorage.setItem('admin_token', data.token)
    setIsAdmin(true)
    return data
  }

  function logout() {
    authService.logout()
    setIsAdmin(false)
  }

  return (
    <AuthContext.Provider value={{ isAdmin, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
