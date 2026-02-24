import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const getContent = () => api.get('/content')
export const getContentBySlug = (slug) => api.get(`/content/slug/${slug}`)
export const getFeaturedContent = () => api.get('/content/featured')
export const createContent = (data) => api.post('/content', data)
export const updateContent = (id, data) => api.put(`/content/${id}`, data)
export const deleteContent = (id) => api.delete(`/content/${id}`)

export const getGames = () => api.get('/games')
export const getGameById = (id) => api.get(`/games/${id}`)
export const getFeaturedGames = () => api.get('/games/featured')
export const createGame = (data) => api.post('/games', data)
export const updateGame = (id, data) => api.put(`/games/${id}`, data)
export const deleteGame = (id) => api.delete(`/games/${id}`)

export const checkHealth = () => api.get('/health')

export default api
