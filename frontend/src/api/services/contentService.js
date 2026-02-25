import api from '../client'

export const contentService = {
  getAll: () => api.get('/content'),
  getBySlug: (slug) => api.get(`/content/slug/${slug}`),
  getFeatured: () => api.get('/content/featured'),
  getByAuthor: (authorId) => api.get(`/content/author/${authorId}`),
  create: (data, userId) => api.post('/content', data, { headers: { 'X-User-Id': userId } }),
  update: (id, data) => api.put(`/content/${id}`, data),
  remove: (id) => api.delete(`/content/${id}`),
}
