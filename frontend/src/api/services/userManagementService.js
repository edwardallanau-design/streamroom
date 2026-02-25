import api from '../client'

export const userManagementService = {
  getAll: () => api.get('/admin/users'),
  create: (data) => api.post('/admin/users', data),
  updateRole: (id, role) => api.put(`/admin/users/${id}/role`, { role }),
  remove: (id) => api.delete(`/admin/users/${id}`),
}
