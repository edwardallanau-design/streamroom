import React, { useEffect, useState } from 'react'
import { getContent } from '../api/client'
import ContentCard from '../components/ContentCard'
import '../styles/page.css'

function Content() {
  const [content, setContent] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchContent = async () => {
      try {
        const response = await getContent()
        setContent(response.data)
      } catch (err) {
        setError('Failed to load content')
        console.error(err)
      } finally {
        setLoading(false)
      }
    }
    fetchContent()
  }, [])

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="glow-text">CONTENT LIBRARY</h1>
        <p>Explore our collection of articles and guides</p>
      </div>

      {error && <div className="error-message">{error}</div>}

      {loading ? (
        <div className="loading">LOADING CONTENT...</div>
      ) : (
        <div className="content-grid">
          {content.map((item) => (
            <ContentCard key={item.id} content={item} />
          ))}
        </div>
      )}
    </div>
  )
}

export default Content
