import { useState } from 'react'
import { createPortal } from 'react-dom'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import '../styles/cards.css'

function timeAgo(dateStr) {
  const diff = Date.now() - new Date(dateStr).getTime()
  const mins = Math.floor(diff / 60000)
  if (mins < 60) return `${mins} min ago`
  const hrs = Math.floor(mins / 60)
  if (hrs < 24) return `${hrs} hr ago`
  const days = Math.floor(hrs / 24)
  return `${days} day${days !== 1 ? 's' : ''} ago`
}

function ContentCard({ content, onDelete }) {
  const { isAdmin } = useAuth()
  const navigate = useNavigate()
  const [confirmDelete, setConfirmDelete] = useState(false)

  function handleCardClick(e) {
    if (e.target.closest('.card-admin-actions')) return
    navigate(`/content/${content.slug}`)
  }

  function handleEdit(e) {
    e.stopPropagation()
    navigate(`/content/edit/${content.id}`)
  }

  function handleDeleteClick(e) {
    e.stopPropagation()
    setConfirmDelete(true)
  }

  function handleConfirm(e) {
    e.stopPropagation()
    onDelete(content.id)
  }

  function handleCancel(e) {
    e.stopPropagation()
    setConfirmDelete(false)
  }

  return (
    <div className="content-card" onClick={handleCardClick}>
      <div className="card-image-side">
        {content.featuredImage
          ? <img src={content.featuredImage} alt={content.title} />
          : <div className="card-image-placeholder" />
        }
      </div>

      <div className="card-body-side">
        {isAdmin && (
          <div className="card-badge-row">
            <span className={`card-badge ${content.isPublished ? 'badge-live' : 'badge-draft'}`}>
              {content.isPublished ? 'PUBLISHED' : 'DRAFT'}
            </span>
          </div>
        )}

        <h3 className="card-title">{content.title}</h3>
        {content.description && <p className="card-desc">{content.description}</p>}

        <div className="card-meta">
          Posted {timeAgo(content.createdAt)}
          {content.author && ` by ${content.author.displayName || content.author.username}`}
        </div>

        {isAdmin && (
          <div className="card-admin-actions">
            <button className="card-action-btn card-edit-btn" onClick={handleEdit}>Edit</button>
            <button className="card-action-btn card-delete-btn" onClick={handleDeleteClick}>Delete</button>
          </div>
        )}

        {confirmDelete && createPortal(
          <div className="delete-modal-overlay" onClick={handleCancel}>
            <div className="delete-modal" onClick={e => e.stopPropagation()}>
              <div className="delete-modal-icon">⚠</div>
              <h4 className="delete-modal-title">DELETE POST</h4>
              <p className="delete-modal-msg">
                "<strong>{content.title}</strong>" will be permanently removed.
              </p>
              <div className="delete-modal-actions">
                <button className="delete-modal-confirm" onClick={handleConfirm}>Delete</button>
                <button className="delete-modal-cancel" onClick={handleCancel}>Cancel</button>
              </div>
            </div>
          </div>,
          document.body
        )}
      </div>
    </div>
  )
}

export default ContentCard
