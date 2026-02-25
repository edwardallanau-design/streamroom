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

function ContentCard({ content, onDelete, onPublish }) {
  const { isAdmin, isModerator, isContentCreator, userId, canPublish } = useAuth()
  const navigate = useNavigate()
  const [confirmDelete, setConfirmDelete] = useState(false)
  const [confirmPublish, setConfirmPublish] = useState(false)

  const isOwner = content.author && content.author.id === userId
  const canEdit = isAdmin || isModerator || (isContentCreator && isOwner)
  const canDelete = isAdmin || isModerator || (isContentCreator && isOwner)
  const showBadge = canPublish || isContentCreator

  const nextPublished = !content.isPublished
  const publishLabel = content.isPublished ? 'Unpublish' : 'Publish'

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

  function handleConfirmDelete(e) {
    e.stopPropagation()
    onDelete(content.id)
  }

  function handleCancelDelete(e) {
    e.stopPropagation()
    setConfirmDelete(false)
  }

  function handlePublishClick(e) {
    e.stopPropagation()
    setConfirmPublish(true)
  }

  function handleConfirmPublish(e) {
    e.stopPropagation()
    setConfirmPublish(false)
    onPublish(content.id, nextPublished)
  }

  function handleCancelPublish(e) {
    e.stopPropagation()
    setConfirmPublish(false)
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
        {showBadge && (
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

        {(canEdit || canDelete || canPublish) && (
          <div className="card-admin-actions">
            {canEdit && <button className="card-action-btn card-edit-btn" onClick={handleEdit}>Edit</button>}
            {canPublish && (
              <button
                className={`card-action-btn ${content.isPublished ? 'card-unpublish-btn' : 'card-publish-btn'}`}
                onClick={handlePublishClick}
              >
                {publishLabel}
              </button>
            )}
            {canDelete && <button className="card-action-btn card-delete-btn" onClick={handleDeleteClick}>Delete</button>}
          </div>
        )}

        {confirmDelete && createPortal(
          <div className="delete-modal-overlay" onClick={handleCancelDelete}>
            <div className="delete-modal" onClick={e => e.stopPropagation()}>
              <div className="delete-modal-icon">⚠</div>
              <h4 className="delete-modal-title">DELETE POST</h4>
              <p className="delete-modal-msg">
                "<strong>{content.title}</strong>" will be permanently removed.
              </p>
              <div className="delete-modal-actions">
                <button className="delete-modal-confirm" onClick={handleConfirmDelete}>Delete</button>
                <button className="delete-modal-cancel" onClick={handleCancelDelete}>Cancel</button>
              </div>
            </div>
          </div>,
          document.body
        )}

        {confirmPublish && createPortal(
          <div className="delete-modal-overlay" onClick={handleCancelPublish}>
            <div className="delete-modal" onClick={e => e.stopPropagation()}>
              <div className="delete-modal-icon">{nextPublished ? '✓' : '⚠'}</div>
              <h4 className="delete-modal-title">{publishLabel.toUpperCase()} POST</h4>
              <p className="delete-modal-msg">
                {nextPublished
                  ? <>Publish "<strong>{content.title}</strong>"? It will be visible to all visitors.</>
                  : <>Unpublish "<strong>{content.title}</strong>"? It will be hidden from visitors.</>
                }
              </p>
              <div className="delete-modal-actions">
                <button className="delete-modal-confirm" onClick={handleConfirmPublish}>{publishLabel}</button>
                <button className="delete-modal-cancel" onClick={handleCancelPublish}>Cancel</button>
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
