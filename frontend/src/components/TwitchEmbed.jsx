import { useState } from 'react'
import '../styles/twitch.css'

function TwitchEmbed() {
  const twitchChannel = import.meta.env.VITE_TWITCH_CHANNEL || 'your_channel_name'
  const parent = window.location.hostname
  const [chatVisible, setChatVisible] = useState(true)

  return (
    <div className="twitch-embed">
      <div className="twitch-container">
        <iframe
          src={`https://player.twitch.tv/?channel=${twitchChannel}&parent=${parent}`}
          allow="autoplay; fullscreen"
          allowFullScreen
          title="Twitch Stream"
        ></iframe>
      </div>

      <button
        className="chat-toggle"
        onClick={() => setChatVisible(v => !v)}
        title={chatVisible ? 'Hide chat' : 'Show chat'}
      >
        {chatVisible ? '›' : '‹'}
      </button>

      <div className={`twitch-chat${chatVisible ? '' : ' twitch-chat--hidden'}`}>
        <iframe
          src={`https://www.twitch.tv/embed/${twitchChannel}/chat?parent=${parent}`}
          allowFullScreen
          title="Twitch Chat"
        ></iframe>
      </div>
    </div>
  )
}

export default TwitchEmbed
