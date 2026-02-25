import '../styles/profile.css'

function Profile() {
  return (
    <div className="page-container">
      <div className="profile-card">
        <div className="profile-avatar-wrap">
          <div className="profile-avatar">
            <img src="/logo.png" alt="StreamRoom" />
          </div>
          <div className="profile-online">
            <span className="online-dot"></span>
            LIVE
          </div>
        </div>

        <div className="profile-identity">
          <h1 className="glow-text">StreamRoom</h1>
          <p className="profile-handle">@streamroom</p>
          <p className="profile-tagline">Cyberpunk vibes. No cap.</p>
        </div>

        <div className="profile-stats">
          <div className="stat">
            <span className="stat-value">1.2K</span>
            <span className="stat-label">Followers</span>
          </div>
          <div className="stat">
            <span className="stat-value">348</span>
            <span className="stat-label">Streams</span>
          </div>
          <div className="stat">
            <span className="stat-value">4.8K</span>
            <span className="stat-label">Hours</span>
          </div>
        </div>
      </div>

      <div className="profile-body">
        <div className="profile-section">
          <h2>About</h2>
          <p>
            Welcome to StreamRoom — a cyberpunk-themed streaming hub built for gamers and content lovers.
            We stream a variety of games, host community events, and bring high-energy content with a neon aesthetic.
          </p>
        </div>

        <div className="profile-section">
          <h2>Schedule</h2>
          <ul className="schedule-list">
            <li><span className="day">MON – WED</span><span className="time">7 PM – 10 PM</span></li>
            <li><span className="day">FRI</span><span className="time">8 PM – 12 AM</span></li>
            <li><span className="day">SAT</span><span className="time">3 PM – 8 PM</span></li>
          </ul>
        </div>

        <div className="profile-section">
          <h2>Socials</h2>
          <div className="social-links">
            <a href="https://twitch.tv" target="_blank" rel="noreferrer" className="social-link twitch">Twitch</a>
            <a href="https://discord.com" target="_blank" rel="noreferrer" className="social-link discord">Discord</a>
            <a href="https://twitter.com" target="_blank" rel="noreferrer" className="social-link twitter">Twitter</a>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Profile
