# [iOS] Home - Test Categories

**Screen ID**: `OuH1BUTYT0`

---

## ACCESSING

| Category | Sub_Category | Sub_Sub_Category |
|----------|-------------|------------------|
| Check access permission | Authenticated user | Logged-in user accesses Home |
| Check access permission | Unauthenticated user | Not logged-in user accesses Home |
| Check access permission | API 401 error | Award/notification API returns 401 |
| Check access permission | API 403 error | Award/notification API returns 403 |
| Check navigation path | From Login screen | After successful login |
| Check navigation path | From Bottom NavBar | From another main screen via "SAA 2025" tab |

---

## GUI

| Category | Sub_Category | Sub_Sub_Category |
|----------|-------------|------------------|
| Check layout | Screen-wide layout | Overall structure |
| Check layout | Awards section | Data-present state |
| Check layout | Kudos section | Conditional visibility |
| Check layout | Notification badge | Badge visibility state |
| Check layout | Awards section | Loading state |
| Check layout | Awards section | Empty state |
| Check layout | Awards section | Error state |

---

## FUNCTION

| Category | Sub_Category | Sub_Sub_Category |
|----------|-------------|------------------|
| Check functionality | Countdown timer | Real-time update |
| Check functionality | Countdown timer | "Coming soon" visibility |
| Check functionality | Awards API | Loading state |
| Check functionality | Awards API | Empty state |
| Check functionality | Awards API | Error state + retry |
| Check functionality | Award card | Chi tiết navigation |
| Check functionality | Award card | Horizontal scroll |
| Check functionality | Notification badge | Badge shown when unread > 0 |
| Check functionality | Notification badge | Badge hidden when unread = 0 |
| Check functionality | Bell icon | Open notifications panel |
| Check functionality | ABOUT AWARD button | Navigation |
| Check functionality | ABOUT KUDOS button | Navigation |
| Check functionality | Kudos section | Feature-flagged visibility |
| Check functionality | Kudos banner | Image fallback placeholder |
| Check functionality | Kudos Chi tiết button | Navigation |
| Check functionality | FAB Pencil icon | Open WriteKudo form |
| Check functionality | FAB Pencil icon | Double-tap prevention |
| Check functionality | FAB S/Kudos icon | Navigation to Kudos feed |
| Check functionality | Bottom NavBar | Awards tab navigation |
| Check functionality | Bottom NavBar | Kudos tab navigation |
| Check functionality | Bottom NavBar | Profile tab navigation |
| Check functionality | Bottom NavBar | SAA 2025 tab active state |
| Check functionality | Language Switcher | Open language selection |
| Check functionality | Search icon | Navigate to Search screen |
| Check functionality | TR-004 redirect | 401 → Login redirect |
| Check functionality | TR-004 redirect | 403 → Access Denied redirect |
