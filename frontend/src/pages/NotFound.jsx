import React from 'react'
import { Link } from 'react-router-dom'
import '../styles/page.css'

function NotFound() {
  return (
    <div className="page-container not-found">
      <div className="not-found-content">
        <h1 className="glow-text">404 - PAGE NOT FOUND</h1>
        <p>The page you're looking for doesn't exist in this cyberspace.</p>
        <Link to="/" className="back-home">
          &lt; RETURN TO HOME &gt;
        </Link>
      </div>
    </div>
  )
}

export default NotFound
