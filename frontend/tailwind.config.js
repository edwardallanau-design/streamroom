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
          accent: '#00d4ff',
          'accent-alt': '#ff006e',
          'accent-tertiary': '#b300ff',
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
          '0%, 100%': { textShadow: '0 0 10px #00d4ff, 0 0 20px #00d4ff' },
          '50%': { textShadow: '0 0 20px #00d4ff, 0 0 30px #00d4ff, 0 0 40px #b300ff' },
        }
      }
    }
  },
  plugins: []
}
