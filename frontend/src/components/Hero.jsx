import React, { useEffect, useState } from 'react'
import { getContent, getFeaturedGames } from '../api/client'
import TwitchEmbed from './TwitchEmbed'
import ContentCard from './ContentCard'
import GameCard from './GameCard'
import '../styles/hero.css'

function Hero() {
  const [featuredContent, setFeaturedContent] = useState([])
  const [featuredGames, setFeaturedGames] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchFeaturedData = async () => {
      try {
        const contentRes = await getContent()
        const gamesRes = await getFeaturedGames()
        setFeaturedContent(contentRes.data.slice(0, 3))
        setFeaturedGames(gamesRes.data.slice(0, 4))
      } catch (error) {
        console.error('Error fetching featured data:', error)
      } finally {
        setLoading(false)
      }
    }
    fetchFeaturedData()
  }, [])

  return (
    <div className="hero">
      <div className="hero-background">
        <div className="cyber-grid"></div>
      </div>

      <div className="hero-content">
        <div className="hero-title">
          <h1 className="glow-text">&gt; ULTIMATE STREAMING DESTINATION</h1>
          <p>Where Gaming Meets Cyberpunk Aesthetics</p>
        </div>

        <div className="hero-sections">
          <div className="twitch-section">
            <h2>LIVE NOW</h2>
            <TwitchEmbed />
          </div>

          <div className="featured-section">
            <h2>FEATURED CONTENT</h2>
            {loading ? (
              <div className="loading">LOADING...</div>
            ) : (
              <div className="featured-grid">
                {featuredContent.map((item) => (
                  <ContentCard key={item.id} content={item} />
                ))}
              </div>
            )}
          </div>
        </div>

        <div className="games-showcase">
          <h2>GAMES WE PLAY</h2>
          {loading ? (
            <div className="loading">LOADING...</div>
          ) : (
            <div className="games-grid">
              {featuredGames.map((game) => (
                <GameCard key={game.id} game={game} />
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default Hero
