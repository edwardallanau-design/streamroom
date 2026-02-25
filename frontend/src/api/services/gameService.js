import api from '../client'

export const gameService = {
  getAll: () => api.get('/games'),
  getById: (id) => api.get(`/games/${id}`),
  getFeatured: () => api.get('/games/featured'),
  create: (data) => api.post('/games', data),
  update: (id, data) => api.put(`/games/${id}`, data),
  remove: (id) => api.delete(`/games/${id}`),
}
