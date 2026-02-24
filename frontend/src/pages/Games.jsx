import React, { useEffect, useState } from 'react'
import { getGames } from '../api/client'
import GameCard from '../components/GameCard'
import '../styles/page.css'

function Games() {
  const [games, setGames] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchGames = async () => {
      try {
        const response = await getGames()
        setGames(response.data)
      } catch (err) {
        setError('Failed to load games')
        console.error(err)
      } finally {
        setLoading(false)
      }
    }
    fetchGames()
  }, [])

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="glow-text">GAME COLLECTION</h1>
        <p>All games featured on our channel</p>
      </div>

      {error && <div className="error-message">{error}</div>}

      {loading ? (
        <div className="loading">LOADING GAMES...</div>
      ) : (
        <div className="games-grid-page">
          {games.map((game) => (
            <GameCard key={game.id} game={game} />
          ))}
        </div>
      )}
    </div>
  )
}

export default Games
