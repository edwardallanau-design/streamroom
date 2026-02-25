import { useState, useEffect } from 'react'
import { useAuth } from '../contexts/AuthContext'
import profileService from '../api/services/profileService'
import '../styles/profile.css'

function formatStat(n) {
  if (n == null) return '—'
  if (n >= 1000) return (n / 1000).toFixed(n % 1000 === 0 ? 0 : 1) + 'K'
  return String(n)
}

function parseJSON(raw) {
  if (!raw) return []
  try { return JSON.parse(raw) } catch { return [] }
}

function serializeSchedule(rows) {
  return JSON.stringify(rows.filter(r => r.days.trim() || r.time.trim()))
}

function serializeSocials(rows) {
  return JSON.stringify(rows.filter(r => r.name.trim() || r.url.trim()))
}

const DEFAULT_PROFILE = {
  displayName: '',
  tagline: '',
  bio: '',
  profileImage: '',
  twitchUsername: '',
  schedule: '[]',
  socials: '[]',
  followerCount: null,
  streamCount: null,
  hoursStreamed: null,
}

function Profile() {
  const { isAdmin } = useAuth()
  const [profile, setProfile] = useState(null)
  const [editing, setEditing] = useState(false)
  const [form, setForm] = useState(null)
  const [scheduleRows, setScheduleRows] = useState([])
  const [socialRows, setSocialRows] = useState([])
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    profileService.getProfile()
      .then(data => setProfile(data))
      .catch(() => setProfile(DEFAULT_PROFILE))
  }, [])

  function startEdit() {
    setForm({ ...profile })
    setScheduleRows(parseJSON(profile.schedule))
    setSocialRows(parseJSON(profile.socials))
    setError('')
    setEditing(true)
  }

  function cancelEdit() {
    setEditing(false)
    setForm(null)
    setScheduleRows([])
    setSocialRows([])
    setError('')
  }

  async function saveEdit() {
    setSaving(true)
    setError('')
    try {
      const payload = {
        ...form,
        schedule: serializeSchedule(scheduleRows),
        socials: serializeSocials(socialRows),
      }
      const updated = await profileService.updateProfile(payload)
      setProfile(updated)
      setEditing(false)
    } catch (err) {
      setError(err.message || 'Failed to save.')
    } finally {
      setSaving(false)
    }
  }

  function setField(key, value) {
    setForm(f => ({ ...f, [key]: value }))
  }

  // Schedule
  function addScheduleRow() {
    setScheduleRows(rows => [...rows, { days: '', time: '' }])
  }
  function updateScheduleRow(i, key, value) {
    setScheduleRows(rows => rows.map((r, idx) => idx === i ? { ...r, [key]: value } : r))
  }
  function removeScheduleRow(i) {
    setScheduleRows(rows => rows.filter((_, idx) => idx !== i))
  }

  // Socials
  function addSocialRow() {
    setSocialRows(rows => [...rows, { name: '', url: '' }])
  }
  function updateSocialRow(i, key, value) {
    setSocialRows(rows => rows.map((r, idx) => idx === i ? { ...r, [key]: value } : r))
  }
  function removeSocialRow(i) {
    setSocialRows(rows => rows.filter((_, idx) => idx !== i))
  }

  if (!profile) {
    return (
      <div className="page-container">
        <p style={{ textAlign: 'center', padding: '4rem', opacity: 0.5 }}>Loading…</p>
      </div>
    )
  }

  const scheduleList = parseJSON(profile.schedule)
  const socialList = parseJSON(profile.socials)

  return (
    <div className="page-container">
      {/* Card */}
      <div className="profile-card">
        <div className="profile-avatar-wrap">
          <div className="profile-avatar">
            <img src={profile.profileImage || '/logo.png'} alt={profile.displayName || 'StreamRoom'} />
          </div>
          <div className="profile-online">
            <span className="online-dot"></span>
            LIVE
          </div>
        </div>

        <div className="profile-identity">
          <h1 className="glow-text">{profile.displayName || 'StreamRoom'}</h1>
          <p className="profile-handle">@{profile.twitchUsername || 'streamroom'}</p>
          {profile.tagline && <p className="profile-tagline">{profile.tagline}</p>}
        </div>
{/* 
        <div className="profile-stats">
          <div className="stat">
            <span className="stat-value">{formatStat(profile.followerCount)}</span>
            <span className="stat-label">Followers</span>
          </div>
          <div className="stat">
            <span className="stat-value">{formatStat(profile.streamCount)}</span>
            <span className="stat-label">Streams</span>
          </div>
          <div className="stat">
            <span className="stat-value">{formatStat(profile.hoursStreamed)}</span>
            <span className="stat-label">Hours</span>
          </div>
        </div> */}

        {isAdmin && !editing && (
          <div className="profile-admin-actions">
            <button className="profile-edit-btn" onClick={startEdit}>Edit Profile</button>
          </div>
        )}
      </div>

      {/* Edit panel */}
      {editing && (
        <div className="profile-edit-panel">
          <h2 className="profile-edit-title">Edit Profile</h2>
          {error && <p className="profile-edit-error">{error}</p>}

          <div className="profile-edit-grid">
            <div className="profile-edit-section">
              <h3>Identity</h3>
              <label>Display Name</label>
              <input value={form.displayName || ''} onChange={e => setField('displayName', e.target.value)} />
              <label>Tagline</label>
              <input value={form.tagline || ''} onChange={e => setField('tagline', e.target.value)} />
              <label>Bio</label>
              <textarea rows={4} value={form.bio || ''} onChange={e => setField('bio', e.target.value)} />
              <label>Profile Image URL</label>
              <input value={form.profileImage || ''} onChange={e => setField('profileImage', e.target.value)} />
              <label>Twitch Username</label>
              <input value={form.twitchUsername || ''} onChange={e => setField('twitchUsername', e.target.value)} />
            </div>

            <div className="profile-edit-section">
              <h3>Stats</h3>
              <label>Followers</label>
              <input type="number" value={form.followerCount ?? ''} onChange={e => setField('followerCount', e.target.value ? Number(e.target.value) : null)} />
              <label>Streams</label>
              <input type="number" value={form.streamCount ?? ''} onChange={e => setField('streamCount', e.target.value ? Number(e.target.value) : null)} />
              <label>Hours Streamed</label>
              <input type="number" value={form.hoursStreamed ?? ''} onChange={e => setField('hoursStreamed', e.target.value ? Number(e.target.value) : null)} />
            </div>

            <div className="profile-edit-section profile-edit-section--full">
              <h3>Schedule</h3>
              {scheduleRows.map((row, i) => (
                <div key={i} className="schedule-edit-row">
                  <input
                    placeholder="Days (e.g. MON – WED)"
                    value={row.days}
                    onChange={e => updateScheduleRow(i, 'days', e.target.value)}
                  />
                  <input
                    placeholder="Time (e.g. 7 PM – 10 PM)"
                    value={row.time}
                    onChange={e => updateScheduleRow(i, 'time', e.target.value)}
                  />
                  <button className="schedule-remove-btn" onClick={() => removeScheduleRow(i)} title="Remove">✕</button>
                </div>
              ))}
              <button className="schedule-add-btn" onClick={addScheduleRow}>+ Add Row</button>
            </div>

            <div className="profile-edit-section profile-edit-section--full">
              <h3>Socials</h3>
              {socialRows.map((row, i) => (
                <div key={i} className="schedule-edit-row">
                  <input
                    placeholder="Name (e.g. Twitch)"
                    value={row.name}
                    onChange={e => updateSocialRow(i, 'name', e.target.value)}
                  />
                  <input
                    placeholder="URL (e.g. https://twitch.tv/...)"
                    value={row.url}
                    onChange={e => updateSocialRow(i, 'url', e.target.value)}
                  />
                  <button className="schedule-remove-btn" onClick={() => removeSocialRow(i)} title="Remove">✕</button>
                </div>
              ))}
              <button className="schedule-add-btn" onClick={addSocialRow}>+ Add Social</button>
            </div>
          </div>

          <div className="profile-edit-actions">
            <button className="profile-save-btn" onClick={saveEdit} disabled={saving}>
              {saving ? 'Saving…' : 'Save'}
            </button>
            <button className="profile-cancel-btn" onClick={cancelEdit}>Cancel</button>
          </div>
        </div>
      )}

      {/* Read-only body */}
      {!editing && (
        <div className="profile-body">
          <div className="profile-section">
            <h2>About</h2>
            <p>{profile.bio || 'No bio yet.'}</p>
          </div>

          <div className="profile-section">
            <h2>Schedule</h2>
            {scheduleList.length > 0 ? (
              <ul className="schedule-list">
                {scheduleList.map((row, i) => (
                  <li key={i}>
                    <span className="day">{row.days}</span>
                    <span className="time">{row.time}</span>
                  </li>
                ))}
              </ul>
            ) : (
              <p>No schedule set.</p>
            )}
          </div>

          <div className="profile-section">
            <h2>Socials</h2>
            {socialList.length > 0 ? (
              <div className="social-links">
                {socialList.map((s, i) => (
                  <a key={i} href={s.url} target="_blank" rel="noreferrer" className="social-link">{s.name}</a>
                ))}
              </div>
            ) : (
              <p style={{ opacity: 0.5, fontSize: '0.8rem' }}>No socials set.</p>
            )}
          </div>
        </div>
      )}
    </div>
  )
}

export default Profile
