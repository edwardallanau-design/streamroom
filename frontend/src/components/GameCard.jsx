import React from 'react'
import '../styles/cards.css'

function GameCard({ game }) {
  return (
    <div className="game-card">
      <div className="card-image">
        {game.coverImage && (
          <img src={game.coverImage} alt={game.title} />
        )}
        <div className="card-overlay"></div>
      </div>
      <div className="card-content">
        <h3>{game.title}</h3>
        <p className="genre">{game.genre}</p>
        <p className="developer">{game.developer}</p>
        <p>{game.description}</p>
      </div>
    </div>
  )
}

export default GameCard
