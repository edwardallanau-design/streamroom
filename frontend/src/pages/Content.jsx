import { contentService } from '../api/services/contentService'
import { useApi } from '../hooks/useApi'
import ContentCard from '../components/ContentCard'
import '../styles/page.css'

function Content() {
  const { data: content, loading, error, refetch } = useApi(() => contentService.getAll())

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="glow-text">CONTENT LIBRARY</h1>
        <p>Explore our collection of articles and guides</p>
      </div>

      {error && (
        <div className="error-message">
          {error}
          <button onClick={refetch} style={{ marginLeft: '1rem' }}>RETRY</button>
        </div>
      )}

      {loading ? (
        <div className="loading">LOADING CONTENT...</div>
      ) : (
        <div className="content-grid">
          {(content ?? []).map((item) => (
            <ContentCard key={item.id} content={item} />
          ))}
        </div>
      )}
    </div>
  )
}

export default Content
