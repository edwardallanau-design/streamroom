import React from 'react'
import { Outlet } from 'react-router-dom'
import Header from './Header'
import Footer from './Footer'
import '../styles/layout.css'

function Layout() {
  return (
    <div className="layout">
      <div className="scanlines"></div>
      <Header />
      <main className="main-content">
        <Outlet />
      </main>
      <Footer />
    </div>
  )
}

export default Layout
