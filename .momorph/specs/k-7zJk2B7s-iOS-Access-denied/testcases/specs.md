# [iOS] Access Denied - Specification

## 1. Screen Overview

- **Screen Name:** [iOS] Access denied
- **Purpose:** Display an error screen when a user successfully passes Google OAuth but is denied access because the Google account domain is not permitted (`@sun-asterisk.com` required). Provides clear feedback and a path back to Home.
- **User Access:** Users who completed Google OAuth successfully but whose account domain is blocked by server-side RLS / business rule (403 / AccessDeniedException).
- **Navigation:**
  - **From:** `NavRoutes.Login` — triggered automatically when `LoginViewModel` emits `isAccessDenied = true`; Login is cleared from back stack on arrival (`popUpTo(Login) { inclusive = true }`)
  - **To:** `NavRoutes.Home` — when user taps "Go back to Home" button

---

## 2. UI Elements

### 2.1 mms_Header (Node 6885:9523)
- **Type:** info_block (FRAME)
- **Label:** N/A (container)
- **Description:** Header section of the access denied error page. Contains: back_arrow icon (top-left), title text, horizontal divider, description text.
  - **back_arrow**: Icon (arrow left) at top-left corner. Click → exit app (back stack is empty; Login was cleared).
  - **Title text**: "Access Denied" (localized: VN = "Truy cập bị từ chối", EN = "Access Denied")
  - **Horizontal divider**: Visual separator between title and description
  - **Description text**: "The resource you're looking for doesn't exist or has been removed." (same text in VN and EN)
- **Placeholder:** N/A
- **Default Value:** N/A (static content — always displayed)
- **Position:** Top section of screen

### 2.2 mms_2.1_mm_media_Not Found (Node 6885:9529)
- **Type:** RECTANGLE (image placeholder)
- **Label:** N/A
- **Description:** Illustrative image for the access denied error. Static decorative image, no interaction. ⚠️ Status: draft — asset not finalized.
- **Placeholder:** N/A
- **Default Value:** N/A
- **Position:** Middle section of screen
- **Note:** contentDescription = null (decorative)

### 2.3 mms_2.2_Button (Node 6885:9531)
- **Type:** INSTANCE (button)
- **Label:** "Go back to Home" (localized: VN = "Quay về trang chủ", EN = "Go back to Home")
- **Description:** Primary action button. Navigates user back to Home screen. Always in enabled state; no disabled state.
- **Placeholder:** N/A
- **Default Value:** State: always enabled
- **Position:** Bottom section of screen

### 2.4 mms_2_Open secret box (Container) (Node 6885:9522)
- **Type:** FRAME (card container)
- **Label:** N/A (functional name: "Khung Chứa Lỗi Truy Cập")
- **Description:** Outer container wrapping all error content (mms_Header + mms_2.1 + mms_2.2_Button). Displayed when server returns 403 / access denied.
- **Placeholder:** N/A
- **Default Value:** Always visible when screen is active

---

## 3. Validation Rules

No form inputs on this screen. No validation rules apply.

---

## 4. User Interactions

### 4.1 back_arrow (mms_Header) — Exit App
- **Element:** back_arrow icon button in mms_Header
- **Trigger:** Click / tap
- **Expected Behavior:** Exit the application. Back stack is empty (Login was cleared via `popUpTo(Login) { inclusive = true }` when navigating here). Uses `LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher?.onBackPressed()`.
- **Success State:** App exits (Activity destroyed)
- **Error Handling:** If dispatcher is null (edge case) — no crash; system Back button still functions

### 4.2 "Go back to Home" Button (mms_2.2_Button) — Navigate to Home
- **Element:** mms_2.2_Button
- **Trigger:** Click / tap (on_click)
- **Expected Behavior:** Navigate to `NavRoutes.Home` and clear Access Denied from back stack (`popUpTo(AccessDenied) { inclusive = true }`). User cannot press Back to return to Access Denied screen.
- **Success State:** Home screen displayed; Access Denied not in back stack
- **Error Handling:** N/A — button is always enabled

### 4.3 System Back Button
- **Element:** Android system back gesture / hardware back button
- **Trigger:** User presses system Back
- **Expected Behavior:** Same as back_arrow — exit app, because back stack is empty (Login was cleared)
- **Success State:** App exits

### 4.4 Language Switch (Cross-screen effect)
- **Element:** Language selector (on Login screen, app-level AppViewModel)
- **Trigger:** Language changed before or while Access Denied is displayed
- **Expected Behavior:** All text on Access Denied screen (title, description, button label) updates to the selected language without restarting the app
- **Success State:** Text displayed in correct language (VN or EN)

---

## 5. Functional / Business Rules

- **FR-001:** Screen is shown when `LoginViewModel.isAccessDenied = true` (triggered by 403 / AccessDeniedException from Google OAuth flow).
- **FR-002:** Login screen is cleared from back stack when navigating to this screen (`popUpTo(Login) { inclusive = true }`).
- **FR-003:** Tapping "Go back to Home" navigates to `NavRoutes.Home`.
- **FR-004:** Access Denied is cleared from back stack when navigating to Home (`popUpTo(AccessDenied) { inclusive = true }`).
- **FR-005:** All text (title, description, button label) uses `stringResource()` — supports localization (VN/EN).
- **FR-006:** back_arrow exits app (back stack is empty; no screen to pop back to).
- **BR-001:** This screen is for 403 / domain-not-permitted errors only. Other error types (network, 401, 404) do not trigger this screen.
- **BR-002:** No API calls occur when this screen is displayed — purely static UI.
- **BR-003:** No session is preserved after being denied; returning to Login starts fresh.

---

## Security Considerations

- This screen is only reachable after a Google OAuth attempt — not directly navigable.
- Login is cleared from back stack — user cannot return to Login by pressing Back (prevents back-navigation exploitation).
- No sensitive data is displayed on this screen.
- No API calls from this screen — no token leakage risk.
