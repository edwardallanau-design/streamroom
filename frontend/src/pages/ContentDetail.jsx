import { useEffect, useState } from 'react'
import { createPortal } from 'react-dom'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { contentService } from '../api/services/contentService'
import '../styles/page.css'
import '../styles/cards.css'

function ContentDetail() {
  const { slug } = useParams()
  const navigate = useNavigate()
  const { isAdmin } = useAuth()
  const [content, setContent] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [confirmDelete, setConfirmDelete] = useState(false)

  useEffect(() => {
    contentService.getBySlug(slug)
      .then(r => setContent(r.data))
      .catch(() => setError('Content not found'))
      .finally(() => setLoading(false))
  }, [slug])

  async function handleDelete() {
    try {
      await contentService.removeAdmin(content.id)
      navigate('/content')
    } catch (err) {
      setError(err.message || 'Failed to delete post.')
    }
  }

  if (loading) return <div className="loading">LOADING...</div>
  if (error) return <div className="error-message">{error}</div>
  if (!content) return <div className="error-message">Content not found</div>

  return (
    <div className="page-container">
      <article className="article">
        {content.featuredImage && (
          <img src={content.featuredImage} alt={content.title} className="featured-image" />
        )}
        <h1 className="glow-text">{content.title}</h1>
        <div className="article-meta">
          <span>{new Date(content.createdAt).toLocaleDateString()}</span>
          {content.author && <span>by {content.author.displayName || content.author.username}</span>}
          {isAdmin && (
            <div style={{ marginLeft: 'auto', display: 'flex', gap: '0.5rem' }}>
              <button
                className="card-action-btn card-edit-btn"
                onClick={() => navigate(`/content/edit/${content.id}`)}
              >
                Edit
              </button>
              <button
                className="card-action-btn card-delete-btn"
                onClick={() => setConfirmDelete(true)}
              >
                Delete
              </button>
            </div>
          )}
        </div>
        {content.description && (
          <p className="article-description">{content.description}</p>
        )}
        <div
          className="post-body"
          dangerouslySetInnerHTML={{ __html: content.content }}
        />
      </article>

      {confirmDelete && createPortal(
        <div className="delete-modal-overlay" onClick={() => setConfirmDelete(false)}>
          <div className="delete-modal" onClick={e => e.stopPropagation()}>
            <div className="delete-modal-icon">⚠</div>
            <h4 className="delete-modal-title">DELETE POST</h4>
            <p className="delete-modal-msg">
              "<strong>{content.title}</strong>" will be permanently removed.
            </p>
            <div className="delete-modal-actions">
              <button className="delete-modal-confirm" onClick={handleDelete}>Delete</button>
              <button className="delete-modal-cancel" onClick={() => setConfirmDelete(false)}>Cancel</button>
            </div>
          </div>
        </div>,
        document.body
      )}
    </div>
  )
}

export default ContentDetail
