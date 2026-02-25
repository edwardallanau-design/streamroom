/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx}"
  ],
  theme: {
    extend: {
      colors: {
        cyberpunk: {
          dark: '#0a0a0a',
          'dark-light': '#1a1a2e',
          accent: '#00FFFF',
          'accent-alt': '#FF1493',
          'accent-tertiary': '#6B0080',
          'accent-yellow': '#FFB800',
          text: '#e0e0e0',
          'text-light': '#ffffff'
        }
      },
      fontFamily: {
        cyberpunk: ['Orbitron', 'monospace'],
      },
      animation: {
        glow: 'glow 2s ease-in-out infinite',
        pulse: 'pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite',
      },
      keyframes: {
        glow: {
          '0%, 100%': { textShadow: '0 0 10px #00FFFF, 0 0 20px #00FFFF' },
          '50%': { textShadow: '0 0 20px #FF1493, 0 0 30px #FF1493, 0 0 40px #6B0080' },
        }
      }
    }
  },
  plugins: []
}
