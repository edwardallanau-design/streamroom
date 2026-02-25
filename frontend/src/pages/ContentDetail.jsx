import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { contentService } from '../api/services/contentService'
import '../styles/page.css'

function ContentDetail() {
  const { slug } = useParams()
  const [content, setContent] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchContent = async () => {
      try {
        const response = await contentService.getBySlug(slug)
        setContent(response.data)
      } catch (err) {
        setError('Content not found')
        console.error(err)
      } finally {
        setLoading(false)
      }
    }
    fetchContent()
  }, [slug])

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
          {content.author && <span>by {content.author.username}</span>}
        </div>
        <div className="article-content">
          {content.content}
        </div>
      </article>
    </div>
  )
}

export default ContentDetail
