# StreamRoom Frontend

A cyberpunk-themed React 19 frontend for a Twitch streaming website with content management system.

## Features

- Twitch stream embed with live chat
- Responsive cyberpunk UI theme
- Content management system
- Game showcase
- Featured content display

## Prerequisites

- Node.js 18+
- npm or yarn

## Installation

```bash
npm install
```

## Development

```bash
npm run dev
```

The application will be available at `http://localhost:5173`

## Environment Variables

Create a `.env` file based on `.env.example`:

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_TWITCH_CLIENT_ID=your_twitch_client_id
VITE_TWITCH_CHANNEL=your_channel_name
```

## Building

```bash
npm run build
```

## Project Structure

```
src/
├── components/      # Reusable React components
├── pages/          # Page components
├── api/            # API client
├── styles/         # CSS stylesheets
├── utils/          # Utility functions
├── App.jsx         # Main app component
└── main.jsx        # Entry point
```

## Technologies

- React 19
- React Router v6
- Axios
- Tailwind CSS
- React Icons
- Vite

## License

MIT
