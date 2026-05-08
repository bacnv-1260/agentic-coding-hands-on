# [iOS] Home - Specification

**Screen ID**: `OuH1BUTYT0`
**Frame Node**: `6885:8978`

---

## 1. Screen Overview

- **Screen Name:** [iOS] Home
- **Purpose:** Post-login landing screen for the SAA 2025 app. Displays event info (countdown, theme, venue), award categories, and Kudos feature introduction. Provides global navigation.
- **User Access:** Authenticated users only. Unauthenticated access redirects to Login. Forbidden (403) access redirects to Access Denied.
- **Navigation:** Reached after successful login (from Login screen). Also reachable via Bottom Navigation Bar "SAA 2025" tab from other screens.

---

## 2. UI Elements

### 2.1 mms_1_header (Header)
- **Type:** navigation (sticky/fixed at top)
- **Description:** App top header bar. Contains: SAA 2025 branding logo (48×44dp), Language Switcher (VN flag + "VN" label + dropdown arrow), Search icon, Notification Bell icon.
- **Notification Badge:** Red dot (8×8dp, #D4271D) overlaid on Bell icon. Shown when unread notifications exist; hidden when count = 0.
- **Default:** Language Switcher shows "VN" flag + "VN" label + dropdown arrow.
- **Position:** Fixed at top of screen (stays visible while scrolling).

### 2.2 mms_2_content (Hero Section)
- **Type:** info_block
- **Description:** Primary hero section. Displays: ROOT FURTHER logo (247×109dp), "Coming soon" label (hidden after event starts), countdown timer (DAYS/HOURS/MINUTES), event date/venue/livestream info, ABOUT AWARD button (primary), ABOUT KUDOS button (secondary/outline).
- **Countdown Timer:** Real-time countdown to 26/12/2025 00:00:00 GMT+7. Updates every 1 second. When reaches 0: transitions to EventEnded state.
- **"Coming soon" label:** Shown before 26/12/2025; hidden after event starts.
- **Event date text:** "Thời gian: 26/12/2025" — prefix in Light 300 white; date value "26/12/2025" in Bold, gold (#FFEA9E).
- **Venue text:** "Địa điểm: Âu Cơ Art Center" — Light 300 white.
- **Livestream text:** "Tường thuật trực tiếp tại Group Facebook Sun* Family" — Light 300 white.

### 2.3 mms_2.2_Button (ABOUT AWARD)
- **Type:** button (primary style)
- **Label:** "ABOUT AWARD"
- **Description:** Navigates to Awards overview screen on click. Always enabled.

### 2.4 mms_2.3_Button (ABOUT KUDOS)
- **Type:** button (secondary/outline style)
- **Label:** "ABOUT KUDOS"
- **Description:** Navigates to Kudos overview screen on click. Always enabled.

### 2.5 mms_3_note (Theme Description)
- **Type:** label (static text block)
- **Description:** Multi-line paragraph describing the "Root Further" theme. Static; non-interactive.
- **Content:** "Root Further is not merely a name — it is the spirit every Sunner should aspire to: always looking deeper in every context and never ceasing to create, expanding oneself to overcome self-imposed limits."

### 2.6 mms_4_awards (Awards Section)
- **Type:** card container (horizontally scrollable)
- **Description:** Container for Awards section header (mms_4.1) and horizontally scrollable award card list (mms_4.2). Shows empty state when API returns no data.

### 2.7 mms_4.1_header (Awards Section Header)
- **Type:** label (section header)
- **Event label:** "Sun* Annual Awards 2025"
- **Section title:** "Hệ thống giải thưởng"
- **Default:** Static display.

### 2.8 mms_4.2_award list (Award Card List)
- **Type:** list_item (horizontally scrollable LazyRow)
- **Description:** Each card contains: thumbnail image (from API URL), award name, short description (truncated with ellipsis if too long), "Chi tiết" link with arrow icon.
- **Loading state:** Shown while API is loading.
- **Empty state:** Shown when API returns no awards.
- **Error state:** Shown when API call fails; includes retry button.

### 2.9 mms_5_kudos (Kudos Section)
- **Type:** info_block
- **Description:** Section introducing Sun* Kudos. Contains: header (5.1), banner image (5.2), "ĐIỂM MỚI CỦA SAA 2025" badge, description paragraph, "Chi tiết" button (5.3).
- **Visibility:** Hidden entirely when `isKudosAvailable = false`. No disabled placeholder shown.

### 2.10 mms_5.2_mm_media_Sunkudos (Kudos Banner)
- **Type:** image (decorative banner)
- **Description:** 335×145dp hero banner. Dark background (#0F0F0F) + Kudos background image + Kudos logo overlay (118×21dp end-right). Rounded corners 5dp.
- **Fallback:** Placeholder shown if image fails to load (FR-013).

### 2.11 mms_5.3_Button (Kudos Chi tiết)
- **Type:** button (primary style)
- **Label:** "Chi tiết"
- **Description:** Navigates to Kudos detail screen. Always enabled.

### 2.12 mms_6_float button (FAB)
- **Type:** button (floating action button, fixed position bottom-right)
- **Description:** 89×48dp pill shape. Contains: Pencil icon + "/" separator + S/Kudos icon.
- **Pencil icon:** Tap → opens Viết Kudo form (WriteKudo screen).
- **S/Kudos icon:** Tap → navigates to Kudos feed screen.
- **Double-click Prevention:** Rapid successive taps trigger navigation only once.
- **Position:** Fixed, bottom-right corner, above NavBar.

### 2.13 mms_7_nav bar (Bottom Navigation Bar)
- **Type:** navigation (fixed at bottom)
- **Description:** 4 tabs: "SAA 2025" (active on Home), "Awards", "Kudos", "Profile".
- **Active state:** Current tab highlighted in gold (#FFEA9E); inactive tabs in white reduced opacity.
- **Tab behavior:** Tap navigates to corresponding screen; launchSingleTop prevents duplicate stack entries.

---

## 3. Validation Rules

*(No user-input validation fields on this read-only screen.)*

---

## 4. User Interactions

### 4.1 Language Switcher tap
- **Element:** mms_1_header — Language Switcher
- **Trigger:** Tap
- **Expected Behavior:** Opens Language selection modal/dropdown ([iOS] Language dropdown — `uUvW6Qm1ve`).

### 4.2 Search icon tap
- **Element:** mms_1_header — Search icon
- **Trigger:** Tap
- **Expected Behavior:** Navigates to Search screen.

### 4.3 Bell icon tap
- **Element:** mms_1_header — Notification Bell
- **Trigger:** Tap
- **Expected Behavior:** Opens Notifications panel ([iOS] Notifications — `_b68CBWKl5`).

### 4.4 Notification badge display
- **Element:** mms_1_header — Bell badge dot
- **Trigger:** Screen load / unread count update
- **Expected Behavior:** Badge dot shown when unreadCount > 0; hidden when unreadCount = 0.

### 4.5 ABOUT AWARD button tap
- **Element:** mms_2.2_Button
- **Trigger:** Tap
- **Expected Behavior:** Navigates to Awards overview screen.

### 4.6 ABOUT KUDOS button tap
- **Element:** mms_2.3_Button
- **Trigger:** Tap
- **Expected Behavior:** Navigates to Kudos overview screen.

### 4.7 Award card swipe (horizontal scroll)
- **Element:** mms_4.2_award list
- **Trigger:** Swipe left/right
- **Expected Behavior:** Award card list scrolls horizontally.

### 4.8 Award card "Chi tiết" tap
- **Element:** mms_4.2_award list — "Chi tiết" link per card
- **Trigger:** Tap
- **Expected Behavior:** Navigates to Award Detail screen for the selected award category.

### 4.9 Awards error state retry
- **Element:** mms_4.2_award list — Retry button
- **Trigger:** Tap (when error state is displayed)
- **Expected Behavior:** Re-triggers awards API fetch.

### 4.10 Kudos "Chi tiết" button tap
- **Element:** mms_5.3_Button
- **Trigger:** Tap
- **Expected Behavior:** Navigates to Kudos detail/feed screen.

### 4.11 FAB Pencil icon tap
- **Element:** mms_6_float button — Pencil icon
- **Trigger:** Tap
- **Expected Behavior:** Opens Viết Kudo form (WriteKudo screen).

### 4.12 FAB Pencil icon rapid double tap
- **Element:** mms_6_float button — Pencil icon
- **Trigger:** Rapid successive taps (< 500ms apart)
- **Expected Behavior:** Navigation triggered only once (double-click prevention).

### 4.13 FAB S/Kudos icon tap
- **Element:** mms_6_float button — S/Kudos icon
- **Trigger:** Tap
- **Expected Behavior:** Navigates to Kudos feed screen.

### 4.14 Bottom nav "SAA 2025" tab tap
- **Element:** mms_7_nav bar
- **Trigger:** Tap (from another screen)
- **Expected Behavior:** Returns to Home screen; "SAA 2025" tab highlighted.

### 4.15 Bottom nav "Awards" tab tap
- **Element:** mms_7_nav bar
- **Trigger:** Tap
- **Expected Behavior:** Navigates to Awards screen; "Awards" tab highlighted.

### 4.16 Bottom nav "Kudos" tab tap
- **Element:** mms_7_nav bar
- **Trigger:** Tap
- **Expected Behavior:** Navigates to Kudos screen; "Kudos" tab highlighted.

### 4.17 Bottom nav "Profile" tab tap
- **Element:** mms_7_nav bar
- **Trigger:** Tap
- **Expected Behavior:** Navigates to Profile screen; "Profile" tab highlighted.

### 4.18 Screen vertical scroll
- **Element:** Scrollable content area (LazyColumn)
- **Trigger:** Swipe up/down
- **Expected Behavior:** Content scrolls vertically; header and FAB and NavBar stay fixed in place.

---

## 5. Functional / Business Rules

- **FR-001:** Real-time countdown to 26/12/2025 updating every second; shows DAYS, HOURS, MINUTES.
- **FR-002:** "Coming soon" label visible before event date; hidden after event starts.
- **FR-003:** Award categories loaded from API and rendered in horizontally scrollable card list.
- **FR-004:** Award card description text truncated with ellipsis when exceeding display width.
- **FR-005:** Tapping "Chi tiết" on award card navigates to correct Award Detail screen.
- **FR-006:** Empty state shown in Awards section when API returns no data.
- **FR-007:** Loading state shown in Awards section while API is in progress.
- **FR-008:** Notification badge dot shown on Bell icon when unread notifications > 0.
- **FR-009:** FAB pencil tap opens Viết Kudo form; FAB S/Kudos icon tap navigates to Kudos feed.
- **FR-010:** Double/rapid FAB tap triggers navigation only once.
- **FR-011:** Bottom Navigation active tab reflects current screen.
- **FR-012:** Kudos section (mms_5_kudos) hidden entirely when feature unavailable; no disabled placeholder shown.
- **FR-013:** Kudos banner image shows fallback placeholder when image fails to load.
- **TR-001:** Countdown uses device clock (server time not implemented yet; tech debt noted).
- **TR-003:** Home screen is post-login destination.
- **TR-004:** Any API call returning 401 redirects to Login; 403 redirects to Access Denied.

---

## 5. Security Considerations

- Authenticated users only — unauthenticated access should redirect to Login.
- Any API 401 response from awards/notifications call redirects to Login screen.
- Any API 403 response redirects to Access Denied screen.
