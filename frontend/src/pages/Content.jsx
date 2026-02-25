import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { contentService } from '../api/services/contentService'
import { useApi } from '../hooks/useApi'
import ContentCard from '../components/ContentCard'
import '../styles/page.css'

function Content() {
  const { isAdmin } = useAuth()
  const navigate = useNavigate()

  const fetchFn = isAdmin ? contentService.getAllAdmin : contentService.getAll
  const { data: content, loading, error, refetch } = useApi(fetchFn, [isAdmin])

  async function handleDelete(id) {
    try {
      await contentService.removeAdmin(id)
      refetch()
    } catch (err) {
      alert(err.message || 'Failed to delete post.')
    }
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="glow-text">CONTENT LIBRARY</h1>
        <p>Explore our collection of articles and guides</p>
        {isAdmin && (
          <button className="new-post-btn" onClick={() => navigate('/content/new')}>
            + NEW POST
          </button>
        )}
      </div>

      {error && (
        <div className="error-message">
          {error}
          <button onClick={refetch} style={{ marginLeft: '1rem' }}>RETRY</button>
        </div>
      )}

      {loading ? (
        <div className="loading">LOADING CONTENT...</div>
      ) : (content ?? []).length === 0 ? (
        <p style={{ textAlign: 'center', opacity: 0.5, padding: '4rem' }}>No posts yet.</p>
      ) : (
        <div className="content-list">
          {(content ?? []).map((item) => (
            <ContentCard key={item.id} content={item} onDelete={handleDelete} />
          ))}
        </div>
      )}
    </div>
  )
}

export default Content
