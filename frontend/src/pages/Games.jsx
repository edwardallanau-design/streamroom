import { gameService } from '../api/services/gameService'
import { useApi } from '../hooks/useApi'
import GameCard from '../components/GameCard'
import '../styles/page.css'

function Games() {
  const { data: games, loading, error, refetch } = useApi(() => gameService.getAll())

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="glow-text">GAME COLLECTION</h1>
        <p>All games featured on our channel</p>
      </div>

      {error && (
        <div className="error-message">
          {error}
          <button onClick={refetch} style={{ marginLeft: '1rem' }}>RETRY</button>
        </div>
      )}

      {loading ? (
        <div className="loading">LOADING GAMES...</div>
      ) : (
        <div className="games-grid-page">
          {(games ?? []).map((game) => (
            <GameCard key={game.id} game={game} />
          ))}
        </div>
      )}
    </div>
  )
}

export default Games
