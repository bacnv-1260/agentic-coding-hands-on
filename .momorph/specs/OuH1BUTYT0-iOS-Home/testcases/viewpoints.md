# [iOS] Home - Reference Viewpoints

**Source screens:** Notification, Carousel, Fixed_Navigation, Global_navigation

---

## 1. Notification Bell (Bell notification : ON / Real-time OFF)

*Source: Notification screen — "Bell notification : ON\nReal-time showing notification: OFF"*

| Viewpoint | Expected Result |
|-----------|----------------|
| Bell icon present; unread count > 0 | Badge/indicator visible on bell |
| Bell icon present; unread count = 0 | Badge/indicator NOT visible on bell |
| Tap bell icon | Notification panel opens |
| Open panel: notification items shown | Each notification item displayed with correct title/content |

---

## 2. Carousel (Award card horizontal scroll)

*Source: Carousel — "Check carousel in published WEB/APP"*

| Viewpoint | Expected Result |
|-----------|----------------|
| Swipe left on card list when not at last card | Scrolls to show next card |
| Swipe right on card list when not at first card | Scrolls back to previous card |
| Tap on card item / "Chi tiết" link | Opens correct detail page |
| Link permitted to access, user logged in | Opens target page successfully |
| Link not permitted, user not logged in | Redirects to login page |

---

## 3. Fixed Navigation (Bottom NavBar + Header)

*Source: Fixed_Navigation — "Position"*

| Viewpoint | Expected Result |
|-----------|----------------|
| Scroll page down | NavBar stays fixed at bottom; Header stays fixed at top |
| Scroll page up | NavBar stays fixed at bottom; Header stays fixed at top |
| Zoom in/Zoom out | NavBar and Header positions do not change |

---

## 4. Global Navigation (Bottom NavBar items)

*Source: Global_navigation — "Check operation on menu"*

| Viewpoint | Expected Result |
|-----------|----------------|
| Verify icon + label for each tab | Each tab shows correct icon and label per design |
| Verify tab order | Tabs appear in order: SAA 2025, Awards, Kudos, Profile |
| Tap an inactive tab | Navigates to corresponding screen; tapped tab becomes active |
| Active tab styling | Active tab highlighted in gold; inactive tabs in reduced-opacity white |
