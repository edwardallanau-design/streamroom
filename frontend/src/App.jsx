import React from 'react'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { AuthProvider } from './contexts/AuthContext'
import Layout from './components/Layout'
import Home from './pages/Home'
import Games from './pages/Games'
import Content from './pages/Content'
import ContentDetail from './pages/ContentDetail'
import Profile from './pages/Profile'
import AdminLogin from './pages/AdminLogin'
import NotFound from './pages/NotFound'
import './styles/globals.css'

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route element={<Layout />}>
            <Route path="/" element={<Home />} />
            <Route path="/games" element={<Games />} />
            <Route path="/content" element={<Content />} />
            <Route path="/content/:slug" element={<ContentDetail />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/admin" element={<AdminLogin />} />
            <Route path="*" element={<NotFound />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App
