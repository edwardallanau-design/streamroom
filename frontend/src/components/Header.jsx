import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import { FaBars, FaTimes } from 'react-icons/fa'
import '../styles/header.css'

function Header() {
  const [menuOpen, setMenuOpen] = useState(false)

  const toggleMenu = () => {
    setMenuOpen(!menuOpen)
  }

  return (
    <header className="header">
      <div className="header-container">
        <div className="logo">
          <Link to="/">
            <img src="/logo.png" alt="PiggyPlaysPH" className="logo-image" />
            <span className="logo-text">PiggyPlaysPH</span>
          </Link>
        </div>

        <button className="menu-toggle" onClick={toggleMenu}>
          {menuOpen ? <FaTimes /> : <FaBars />}
        </button>

        <nav className={`nav ${menuOpen ? 'active' : ''}`}>
          <Link to="/" className="nav-link">HOME</Link>
          <Link to="/games" className="nav-link">GAMES</Link>
          <Link to="/content" className="nav-link">CONTENT</Link>
          <Link to="/profile" className="nav-link">PROFILE</Link>
          <Link to="/" className="nav-link cta">ADMIN</Link>
        </nav>
      </div>
    </header>
  )
}

export default Header
