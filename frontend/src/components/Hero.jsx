import TwitchEmbed from './TwitchEmbed'
import '../styles/hero.css'

function Hero() {
  return (
    <div className="hero">
      <div className="hero-background">
        <div className="cyber-grid"></div>
      </div>

      <div className="hero-content">
        <div className="twitch-section">
          <TwitchEmbed />
        </div>
      </div>
    </div>
  )
}

export default Hero
