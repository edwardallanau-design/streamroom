import '../styles/footer.css'

function Footer() {
  const currentYear = new Date().getFullYear()

  return (
    <footer className="footer">
      <p>&copy; {currentYear} StreamRoom. All rights reserved.</p>
    </footer>
  )
}

export default Footer
