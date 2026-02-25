import { Component } from 'react'

class ErrorBoundary extends Component {
  constructor(props) {
    super(props)
    this.state = { hasError: false, error: null }
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error }
  }

  componentDidCatch(error, info) {
    console.error('ErrorBoundary caught:', error, info)
  }

  render() {
    if (this.state.hasError) {
      return (
        <div style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          height: '100%',
          padding: '2rem',
        }}>
          <div style={{
            border: '2px solid var(--cyberpunk-accent-alt)',
            padding: '2rem 3rem',
            textAlign: 'center',
            background: 'rgba(255,20,147,0.05)',
            boxShadow: '0 0 20px rgba(255,20,147,0.2)',
          }}>
            <h2 style={{ color: 'var(--cyberpunk-accent-alt)', marginBottom: '1rem' }}>
              SYSTEM ERROR
            </h2>
            <p style={{ color: 'var(--cyberpunk-text)', marginBottom: '1.5rem', fontSize: '0.85rem' }}>
              {this.state.error?.message || 'An unexpected error occurred.'}
            </p>
            <button onClick={() => this.setState({ hasError: false, error: null })}>
              RETRY
            </button>
          </div>
        </div>
      )
    }

    return this.props.children
  }
}

export default ErrorBoundary
