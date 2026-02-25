import { Outlet } from 'react-router-dom'
import Header from './Header'
import Footer from './Footer'
import ErrorBoundary from './common/ErrorBoundary'
import '../styles/layout.css'

function Layout() {
  return (
    <div className="layout">
      <div className="scanlines"></div>
      <Header />
      <main className="main-content">
        <ErrorBoundary>
          <Outlet />
        </ErrorBoundary>
      </main>
      <Footer />
    </div>
  )
}

export default Layout
