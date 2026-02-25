import { useState, useEffect, useRef } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { contentService } from '../api/services/contentService'
import '../styles/page.css'

function extractYouTubeId(url) {
  const m = url.match(/(?:v=|youtu\.be\/)([A-Za-z0-9_-]{11})/)
  return m ? m[1] : null
}

function extractTwitchClipId(url) {
  const m = url.match(/clips\.twitch\.tv\/([A-Za-z0-9_-]+)/)
  return m ? m[1] : null
}

function insertAtCursor(textarea, text) {
  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const before = textarea.value.slice(0, start)
  const after = textarea.value.slice(end)
  return { value: before + text + after, cursor: start + text.length }
}

function wrapSelection(textarea, before, after) {
  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const selected = textarea.value.slice(start, end) || 'text'
  const text = before + selected + after
  const val = textarea.value.slice(0, start) + text + textarea.value.slice(end)
  return { value: val, cursor: start + text.length }
}

const EMPTY_FORM = { title: '', description: '', featuredImage: '', content: '', isPublished: false }

function ContentForm() {
  const { id } = useParams()
  const navigate = useNavigate()
  const isEdit = Boolean(id)

  const [form, setForm] = useState(EMPTY_FORM)
  const [loading, setLoading] = useState(isEdit)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const bodyRef = useRef(null)

  useEffect(() => {
    if (!isEdit) return
    contentService.getByIdAdmin(id)
      .then(r => {
        const c = r.data
        setForm({
          title: c.title || '',
          description: c.description || '',
          featuredImage: c.featuredImage || '',
          content: c.content || '',
          isPublished: c.isPublished || false,
        })
      })
      .catch(() => setError('Failed to load post.'))
      .finally(() => setLoading(false))
  }, [id, isEdit])

  function setField(key, value) {
    setForm(f => ({ ...f, [key]: value }))
  }

  function applyToolbar(action) {
    const ta = bodyRef.current
    if (!ta) return
    let result
    if (action === 'heading') {
      result = insertAtCursor(ta, '<h2>Heading</h2>\n')
    } else if (action === 'bold') {
      result = wrapSelection(ta, '<strong>', '</strong>')
    } else if (action === 'image') {
      const url = window.prompt('Image URL:')
      if (!url) return
      result = insertAtCursor(ta, `<img src="${url}" alt="" style="max-width:100%;margin:1rem 0" />\n`)
    } else if (action === 'youtube') {
      const url = window.prompt('YouTube URL:')
      if (!url) return
      const vid = extractYouTubeId(url)
      if (!vid) { alert('Could not extract YouTube video ID from that URL.'); return }
      result = insertAtCursor(ta,
        `<div style="position:relative;padding-bottom:56.25%;height:0;overflow:hidden;margin:1rem 0">` +
        `<iframe src="https://www.youtube.com/embed/${vid}" ` +
        `style="position:absolute;top:0;left:0;width:100%;height:100%" ` +
        `frameborder="0" allowfullscreen></iframe></div>\n`
      )
    } else if (action === 'twitch') {
      const url = window.prompt('Twitch Clip URL (clips.twitch.tv/...):')
      if (!url) return
      const clip = extractTwitchClipId(url)
      if (!clip) { alert('Could not extract Twitch Clip ID from that URL.'); return }
      result = insertAtCursor(ta,
        `<div style="position:relative;padding-bottom:56.25%;height:0;overflow:hidden;margin:1rem 0">` +
        `<iframe src="https://clips.twitch.tv/embed?clip=${clip}&parent=localhost" ` +
        `style="position:absolute;top:0;left:0;width:100%;height:100%" ` +
        `frameborder="0" allowfullscreen></iframe></div>\n`
      )
    }
    if (result) {
      setField('content', result.value)
      setTimeout(() => {
        ta.focus()
        ta.setSelectionRange(result.cursor, result.cursor)
      }, 0)
    }
  }

  async function handleSave() {
    if (!form.title.trim()) { setError('Title is required.'); return }
    if (!form.content.trim()) { setError('Body is required.'); return }
    setSaving(true)
    setError('')
    try {
      const payload = {
        title: form.title,
        description: form.description,
        content: form.content,
        featuredImage: form.featuredImage || null,
        isPublished: form.isPublished,
        isFeatured: false,
      }
      if (isEdit) {
        await contentService.updateAdmin(id, payload)
      } else {
        await contentService.createAdmin(payload)
      }
      navigate('/content')
    } catch (err) {
      setError(err.message || 'Failed to save.')
    } finally {
      setSaving(false)
    }
  }

  if (loading) return <div className="page-container"><p style={{ textAlign: 'center', padding: '4rem', opacity: 0.5 }}>Loading…</p></div>

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="glow-text">{isEdit ? 'EDIT POST' : 'NEW POST'}</h1>
      </div>

      <div className="post-form-wrap">
        {error && <p className="profile-edit-error">{error}</p>}

        <div className="post-form-fields">
          <label className="post-form-label">Title</label>
          <input
            className="post-form-input"
            value={form.title}
            onChange={e => setField('title', e.target.value)}
            placeholder="Post title"
          />

          <label className="post-form-label">Description <span style={{ opacity: 0.5 }}>(shown on card)</span></label>
          <textarea
            className="post-form-input"
            rows={2}
            value={form.description}
            onChange={e => setField('description', e.target.value)}
            placeholder="Short description visible on the content card"
          />

          <label className="post-form-label">Thumbnail URL</label>
          <input
            className="post-form-input"
            value={form.featuredImage}
            onChange={e => setField('featuredImage', e.target.value)}
            placeholder="https://..."
          />

          <label className="post-form-label">Body</label>
          <div className="post-form-toolbar">
            <button type="button" onClick={() => applyToolbar('heading')} title="Insert Heading">H2</button>
            <button type="button" onClick={() => applyToolbar('bold')} title="Bold"><strong>B</strong></button>
            <button type="button" onClick={() => applyToolbar('image')} title="Insert Image">IMG</button>
            <button type="button" onClick={() => applyToolbar('youtube')} title="Embed YouTube">YT</button>
            <button type="button" onClick={() => applyToolbar('twitch')} title="Embed Twitch Clip">TTV</button>
          </div>
          <textarea
            ref={bodyRef}
            className="post-form-body"
            value={form.content}
            onChange={e => setField('content', e.target.value)}
            placeholder="Write your post body here. Use the toolbar to embed images and videos."
          />

        </div>

        <div className="profile-edit-actions" style={{ marginTop: '2rem' }}>
          <button className="profile-save-btn" onClick={handleSave} disabled={saving}>
            {saving ? 'Saving…' : 'Save'}
          </button>
          <button className="profile-cancel-btn" onClick={() => navigate('/content')}>Cancel</button>
        </div>
      </div>
    </div>
  )
}

export default ContentForm
