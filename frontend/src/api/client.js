import axios from 'axios'

// In development, use localhost:8080/api
// In production (Docker), the nginx proxy will handle /api requests
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  headers: { 'Content-Type': 'application/json' },
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message = error.response?.data?.message || error.message || 'An unexpected error occurred'
    return Promise.reject(new Error(message))
  }
)

export default api
