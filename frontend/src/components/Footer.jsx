import React from 'react'
import '../styles/footer.css'

function Footer() {
  const currentYear = new Date().getFullYear()

  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-content">
          <div className="footer-section">
            <h3>StreamRoom</h3>
            <p>Your cyberpunk streaming hub</p>
          </div>
          <div className="footer-section">
            <h4>Links</h4>
            <ul>
              <li><a href="#about">About</a></li>
              <li><a href="#contact">Contact</a></li>
              <li><a href="#privacy">Privacy</a></li>
            </ul>
          </div>
          <div className="footer-section">
            <h4>Follow</h4>
            <ul>
              <li><a href="#twitch">Twitch</a></li>
              <li><a href="#discord">Discord</a></li>
              <li><a href="#twitter">Twitter</a></li>
            </ul>
          </div>
        </div>
        <div className="footer-bottom">
          <p>&copy; {currentYear} StreamRoom. All rights reserved.</p>
        </div>
      </div>
    </footer>
  )
}

export default Footer
