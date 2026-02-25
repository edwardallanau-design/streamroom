# StreamRoom Frontend

A cyberpunk-themed React 19 frontend for a Twitch streaming website with role-based content management.

## Features

- Twitch stream embed with live chat
- Responsive cyberpunk UI theme
- Role-based access control (ADMIN, MODERATOR, CONTENT_CREATOR)
- Admin dashboard with user management
- Content library with publish/unpublish workflow
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

Create a `.env` file:

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_TWITCH_CLIENT_ID=your_twitch_client_id
VITE_TWITCH_CHANNEL=your_channel_name
```

## Building

```bash
npm run build
```

---

## Roles & Permissions (Frontend)

Auth state is stored in `localStorage` and managed via `AuthContext`. The following flags are exposed:

| Flag | Description |
|---|---|
| `hasAnyRole` | True for any logged-in user |
| `isAdmin` | Role === ADMIN |
| `isModerator` | Role === MODERATOR |
| `isContentCreator` | Role === CONTENT_CREATOR |
| `canManageUsers` | ADMIN or MODERATOR |
| `canPublish` | ADMIN or MODERATOR |
| `canEditProfile` | ADMIN or MODERATOR |

---

## Admin Dashboard (`/admin`)

When logged in, `/admin` shows a dashboard instead of the login form:

- Displays username and role badge
- Links: Home, Games, Content Library, User Management (if `canManageUsers`), Profile
- **Change Password** button — opens a modal requiring current password verification
- **Logout** button

---

## User Management (`/admin/users`)

Available to ADMIN and MODERATOR roles.

- Table of users with role badges (ADMIN = magenta, MODERATOR = cyan, CONTENT_CREATOR = green)
- Role changes require selecting a new role, clicking **Save**, then confirming in a modal
- Delete requires confirmation modal
- New user creation via modal (username, password, display name, email, role)
- ADMINs can manage MODERATORs and CONTENT_CREATORs
- MODERATORs can only manage CONTENT_CREATORs

---

## Content Library (`/content`)

All logged-in roles can see the full content list including drafts (with PUBLISHED / DRAFT badge). Permissions per role:

| Action | ADMIN / MOD | CONTENT_CREATOR |
|---|---|---|
| View all posts | ✓ | ✓ |
| Create post | ✓ | ✓ |
| Edit post | Any | Own only |
| Delete post | Any | Own only |
| Publish / Unpublish | ✓ | ✗ |

**Publish workflow:** The Published checkbox has been removed from the post form. Instead, use the **Publish** / **Unpublish** button on each card in the content list or inside the content detail view. Both require confirmation before applying.

---

## Project Structure

```
src/
├── api/
│   ├── client.js                       # Axios instance with JWT interceptor
│   └── services/
│       ├── contentService.js           # Content CRUD + publishAdmin
│       ├── userManagementService.js    # Admin user CRUD
│       └── profileService.js
├── components/
│   ├── Header.jsx                      # DASHBOARD link (any role), USERS link (canManageUsers)
│   └── ContentCard.jsx                 # Edit / Publish / Delete with confirm modals
├── contexts/
│   └── AuthContext.jsx                 # Role state, computed permission flags, localStorage
├── hooks/
│   └── useApi.js
├── pages/
│   ├── AdminLogin.jsx                  # Login form / dashboard (role-aware)
│   ├── Content.jsx                     # Content list with role-aware fetch
│   ├── ContentDetail.jsx               # Post detail with Publish button
│   ├── ContentForm.jsx                 # Create / edit post (no publish checkbox)
│   ├── UserManagement.jsx              # Admin user management table
│   └── Profile.jsx
└── styles/
    ├── admin.css                       # Dashboard, role badges, user table, modals
    ├── cards.css                       # Content card, publish/unpublish button styles
    └── ...
```

---

## Technologies

- React 19
- React Router v7
- Axios
- Tailwind CSS
- Vite

## License

MIT
