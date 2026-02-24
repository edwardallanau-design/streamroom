import React, { useState } from 'react'
import '../styles/admin.css'

function AdminPanel() {
  const [contentForm, setContentForm] = useState({
    title: '',
    description: '',
    content: '',
    featuredImage: '',
    isPublished: false,
    isFeatured: false,
  })

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target
    setContentForm(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    // Submit content via API
    console.log('Submitting content:', contentForm)
  }

  return (
    <div className="admin-panel">
      <h1 className="glow-text">CMS ADMIN PANEL</h1>

      <div className="admin-container">
        <form onSubmit={handleSubmit} className="content-form">
          <h2>CREATE NEW CONTENT</h2>

          <div className="form-group">
            <label>Title</label>
            <input
              type="text"
              name="title"
              value={contentForm.title}
              onChange={handleInputChange}
              placeholder="Enter content title"
              required
            />
          </div>

          <div className="form-group">
            <label>Description</label>
            <textarea
              name="description"
              value={contentForm.description}
              onChange={handleInputChange}
              placeholder="Enter brief description"
              rows="3"
            ></textarea>
          </div>

          <div className="form-group">
            <label>Content</label>
            <textarea
              name="content"
              value={contentForm.content}
              onChange={handleInputChange}
              placeholder="Enter full content"
              rows="6"
              required
            ></textarea>
          </div>

          <div className="form-group">
            <label>Featured Image URL</label>
            <input
              type="url"
              name="featuredImage"
              value={contentForm.featuredImage}
              onChange={handleInputChange}
              placeholder="Image URL"
            />
          </div>

          <div className="form-group checkbox">
            <input
              type="checkbox"
              name="isPublished"
              checked={contentForm.isPublished}
              onChange={handleInputChange}
              id="published"
            />
            <label htmlFor="published">Publish</label>
          </div>

          <div className="form-group checkbox">
            <input
              type="checkbox"
              name="isFeatured"
              checked={contentForm.isFeatured}
              onChange={handleInputChange}
              id="featured"
            />
            <label htmlFor="featured">Featured</label>
          </div>

          <button type="submit" className="submit-btn">
            PUBLISH CONTENT
          </button>
        </form>
      </div>
    </div>
  )
}

export default AdminPanel
