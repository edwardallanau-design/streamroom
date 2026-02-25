import React from 'react'
import '../styles/twitch.css'

function TwitchEmbed() {
  const twitchChannel = import.meta.env.VITE_TWITCH_CHANNEL || 'your_channel_name'

  return (
    <div className="twitch-embed">
      <div className="twitch-container">
        <iframe
          src={`https://player.twitch.tv/?channel=${twitchChannel}&parent=localhost`}
          allow="autoplay"
          title="Twitch Stream"
        ></iframe>
      </div>
      <div className="twitch-chat">
        <iframe
          src={`https://www.twitch.tv/embed/${twitchChannel}/chat?parent=localhost`}
          title="Twitch Chat"
        ></iframe>
      </div>
    </div>
  )
}

export default TwitchEmbed
