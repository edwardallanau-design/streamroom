import React from 'react'
import { Link } from 'react-router-dom'
import '../styles/cards.css'

function ContentCard({ content }) {
  return (
    <Link to={`/content/${content.slug}`} className="content-card">
      <div className="card-image">
        {content.featuredImage && (
          <img src={content.featuredImage} alt={content.title} />
        )}
        <div className="card-overlay"></div>
      </div>
      <div className="card-content">
        <h3>{content.title}</h3>
        <p>{content.description}</p>
        <div className="card-meta">
          <span className="date">
            {new Date(content.createdAt).toLocaleDateString()}
          </span>
        </div>
      </div>
    </Link>
  )
}

export default ContentCard
