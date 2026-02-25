import api from '../client'

const authService = {
  async login(username, password) {
    const response = await api.post('/auth/login', { username, password })
    return response.data
  },

  logout() {
    localStorage.removeItem('admin_token')
  },
}

export default authService
