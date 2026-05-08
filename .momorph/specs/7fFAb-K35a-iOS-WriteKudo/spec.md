# Screen: [iOS] Sun*Kudos_Viết Kudo_default

## Screen Info

| Property | Value |
|----------|-------|
| **Screen ID** | `7fFAb-K35a` |
| **Figma Node ID** | `6885:9271` |
| **Figma Link** | https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/7fFAb-K35a |
| **Screen Group** | Kudos |
| **Spec Status** | spec |
| **Platform** | iOS / Android |
| **Discovered At** | 2026-05-07 |
| **Last Updated** | 2026-05-07 |

---

## Description

Màn hình soạn và gửi Kudos mới. Người dùng điền form gồm: người nhận (dropdown tìm kiếm), danh hiệu (text input), lời nhắn (rich-text textarea), hashtag (tối đa 5), ảnh đính kèm (tối đa 5), và tùy chọn gửi ẩn danh. Form nằm trong card nổi đặt trên nền keyvisual toàn màn hình. Hai nút hành động cố định ở dưới cùng: **Hủy** và **Gửi đi**.

---

## User Stories

### US-1: Send a kudo to a colleague

**As** an authenticated user,  
**I want** to compose and send a kudo to a colleague,  
**So that** I can express appreciation and recognition.

**Acceptance Scenarios:**

**Scenario 1 — Happy path: all fields filled, kudo sent**
- **Given** the user is on the Write Kudo screen
- **When** they select a recipient (B.2), enter a title (B.4), write a message (D), and add at least one hashtag (E.2), then tap "Gửi đi" (I)
- **Then** the form is submitted to the server, and the user is navigated to `[iOS] Sun*Kudos_Gửi lời chúc Kudos`

**Scenario 2 — Required field missing: recipient not selected**
- **Given** the user has filled Title (B.4), Message (D), and Hashtag (E) but has not selected a recipient
- **Then** the Send button (I) is disabled; the user cannot submit the form until a recipient is chosen

**Scenario 3a — Required field missing: title empty**
- **Given** no text has been entered in B.4
- **Then** the Send button (I) is disabled

**Scenario 3b — Title exceeds character limit**
- **Given** the user has filled all required fields with B.4 containing more than 100 characters
- **When** they tap "Gửi đi" (I)
- **Then** client validation fires; an inline error appears below B.4: "Title is too long (max 100 characters)."; submission is blocked

**Scenario 4 — Required field missing: no hashtag added**
- **Given** Recipient, Title, and Message are filled but no hashtag has been added
- **Then** the Send button (I) is disabled

**Scenario 5 — Send fails (network/server error)**
- **Given** the user taps "Gửi đi" with all fields valid
- **When** the server returns an error
- **Then** an error snackbar is shown; the form state is preserved; the user can retry

**Scenario 6 — Self-kudo attempt**
- **Given** the user is on the Write Kudo screen
- **When** they select themselves as the recipient in B.2
- **Then** an error is shown: "You cannot send a kudo to yourself.", and the recipient field is treated as invalid (Send button remains disabled)

---

### US-2: Cancel kudo creation

**As** an authenticated user,  
**I want** to cancel kudo creation and return to the previous screen,  
**So that** I can abandon a draft without saving.

**Scenario 1 — Cancel with empty form**
- **Given** the form is empty
- **When** the user taps "Hủy" (H) or presses the system back button
- **Then** the user is navigated back immediately with no confirmation

**Scenario 2 — Cancel with unsaved content**
- **Given** the user has entered any content in any field
- **When** the user taps "Hủy" (H) or presses the system back button
- **Then** a confirmation dialog is shown: "Bạn có chắc muốn hủy không? Dữ liệu đã nhập sẽ bị mất."
- **And** confirming → navigates back; dismissing → user stays on the form

---

### US-3: Send kudo anonymously

**As** an authenticated user,  
**I want** to send a kudo without revealing my identity,  
**So that** I can appreciate colleagues privately.

**Scenario 1 — Anonymous toggle enabled**
- **Given** the user checks the anonymous toggle (G)
- **When** the kudo is submitted
- **Then** `is_anonymous = true` is persisted; the recipient sees the kudo without the sender's name or avatar

**Scenario 2 — Anonymous toggle disabled (default)**
- **Given** the anonymous toggle is unchecked (default)
- **When** the kudo is submitted
- **Then** the sender's full name and avatar are visible on the kudo card

---

### US-4: Attach images to a kudo

**As** an authenticated user,  
**I want** to attach up to 5 images to my kudo,  
**So that** I can include visual context.

**Scenario 1 — Add image**
- **Given** fewer than 5 images are attached
- **When** the user taps F.5 "Add Image"
- **Then** the system file/camera picker opens; after selection the image thumbnail appears in the row

**Scenario 2 — Attachment limit reached**
- **Given** 5 images are already attached
- **Then** the F.5 Add Image button is hidden

**Scenario 3 — Remove image**
- **When** the user taps `×` on any thumbnail (F.2–F.2b)
- **Then** that image is removed from the attachment list immediately

**Scenario 4 — Invalid file type or size**
- **When** the user selects a file that is not jpg/png/webp or exceeds 10 MB
- **Then** an error message is shown and the file is not added

---

### US-5: Navigate to community standards

**As** an authenticated user,  
**I want** to view the community standards while writing a kudo,  
**So that** I can understand the recognition criteria.

**Scenario 1**
- **When** the user taps B.5 "Tiêu chuẩn cộng đồng"
- **Then** the app navigates to `[iOS] Sun*Kudos_Tiêu chuẩn cộng đồng` (page or modal)

---

## Navigation Analysis

### Incoming Navigations (From)

| Source Screen | Trigger | Condition |
|---------------|---------|-----------|
| [iOS] Sun*Kudos | A.1 Button "Hôm nay, bạn muốn gửi kudos đến ai?" | Always |
| [iOS] Sun*Kudos | A.1 sticky instance (khi scroll qua banner) | Always |
| [iOS] Home | FAB Pencil icon | Always |
| [iOS] Sun*Kudos_Search Sunner | Tap vào user kết quả tìm kiếm | Prefill recipient |

### Outgoing Navigations (To)

| Target Screen | Trigger Element | Node ID | Confidence | Notes |
|---------------|-----------------|---------|------------|-------|
| [iOS] Sun*Kudos | Cancel Button (H) — "Hủy ✕" | `6891:16834` | high | Hủy và quay lại; xác nhận nếu có nội dung |
| [iOS] Sun*Kudos_Gửi lời chúc Kudos | Send Button (I) — "Gửi đi ▶" | `6891:16833` | high | Submit thành công → màn hình success |
| [iOS] Sun*Kudos_dropdown hashtag | E.2 Tag Group — click "+ Hashtag" | `6885:9328` | high | Mở overlay chọn hashtag |
| [iOS] Sun*Kudos_dropdown tên người nhận | B.2 Recipient Dropdown | `6885:9297` | high | Mở overlay tìm kiếm người nhận |
| [iOS] Sun*Kudos_Tiêu chuẩn cộng đồng | B.5 "Tiêu chuẩn cộng đồng" link | `6885:9303` | high | Mở trang tiêu chuẩn cộng đồng |

### Navigation Rules
- **Back behavior**: Cancel button hoặc navigation back → quay về màn hình trước; nếu form có nội dung chưa lưu → confirmation dialog
- **Deep link**: Không có deep link trực tiếp
- **Auth required**: Có — redirect về Login nếu token hết hạn (401)
- **Prefill**: Nếu navigate từ Search Sunner, trường Người nhận được prefill với user đã chọn

---

## Component Schema

### Layout Structure

```
┌──────────────────────────────────────┐
│  AppBar / Navigation Header          │
│  ← Back   "New Kudo"   (title)      │
├──────────────────────────────────────┤
│  [Keyvisual Background — full screen]│
│  ┌────────────────────────────────┐  │
│  │  FORM CARD (scrollable)        │  │
│  │                                │  │
│  │  A  Header Text                │  │  "Gửi lời cám ơn..."
│  │  ─────────────────────         │  │
│  │  B.1 Label: Người nhận *       │  │
│  │  B.2 Recipient Dropdown        │  │  Tìm kiếm ▼
│  │  ─────────────────────         │  │
│  │  B.3 Label: Danh hiệu *        │  │
│  │  B.4 Title Input               │  │  Danh tặng một danh hiệu...
│  │  B.5 Link: Tiêu chuẩn cộng đồng│  │  text-link
│  │  ─────────────────────         │  │
│  │  C  Formatting Toolbar         │  │  B I S # 🔗 "  
│  │  D  Message Textarea           │  │  placeholder text
│  │  D.1 Hint / Mention Hint       │  │  "@ + tên" hint
│  │  ─────────────────────         │  │
│  │  E.1 Label: Hashtag *          │  │
│  │  E.2 Tag Group                 │  │  + Hashtag chips (max 5)
│  │  ─────────────────────         │  │
│  │  F.1 Label: Image              │  │
│  │  F.2~F.2b Thumbnails (×4 max)  │  │  with ✕ remove
│  │  F.5 Add Image Button          │  │  + Image
│  │  ─────────────────────         │  │
│  │  G  Anonymous Toggle           │  │  ☐ Gửi ẩn danh
│  └────────────────────────────────┘  │
├──────────────────────────────────────┤
│  H  Cancel Button  │  I  Send Button │  fixed bottom row
├──────────────────────────────────────┤
│  Bottom NavBar                       │
│  [SAA] [Awards] [Kudos*] [Profile]  │
└──────────────────────────────────────┘
```

---

## Design Items Detail

### A — Header Text

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9292` |
| **Type** | TEXT (label) |
| **Text** | `"Gửi lời cám ơn và ghi nhận đến đồng đội"` |
| **Interaction** | None — static informational header |

**Logic:** Display only. Always visible at top of form card.

---

### B.1 — Người nhận Label

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9294` |
| **Type** | FRAME (label) |
| **Text** | `"Người nhận *"` |
| **Required indicator** | `*` (required field marker) |
| **Interaction** | None |

---

### B.2 — Recipient Search Dropdown

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9297` |
| **Type** | INSTANCE (dropdown) |
| **Placeholder** | `"Tìm kiếm"` |
| **Icon** | Chevron down `▼` |
| **DB Table** | `kudos` |
| **DB Column** | `recipient_id` |
| **Interaction** | `on_click` → opens recipient search overlay |

**Validation:**
| Condition | Error Message |
|-----------|---------------|
| Field empty on submit | "Please select a recipient." |
| Recipient is current user | "You cannot send a kudo to yourself." |

**Logic:**
- Click → Opens dropdown overlay (`[iOS] Sun*Kudos_dropdown tên người nhận`)
- Supports search by name or department (TC_VIETKUDO_FUN_014)
- Selecting a name closes the overlay and displays the recipient's name in the field (TC_VIETKUDO_FUN_015)
- Disabled if user list unavailable (TC_VIETKUDO_FUN_036); user list error does not block other fields
- If prefilled from Search Sunner: show selected user name (not placeholder)

**Edge case — no search results:**
- When the search query returns no matches, the overlay shows an empty state message; the field value is not changed

---

### B.3 — Danh hiệu Label

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9299` |
| **Type** | FRAME (label) |
| **Text** | `"Danh hiệu *"` |
| **Required indicator** | `*` (required field marker) |
| **Interaction** | None |

---

### B.4 — Danh hiệu Input (Title Input)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9302` |
| **Type** | INSTANCE (text_form) |
| **Placeholder** | `"Danh tặng một danh hiệu cho..."` |
| **DB Table** | `kudos` |
| **DB Column** | `title` |
| **Data type** | `string` |
| **Required** | Yes |
| **Max length** | 100 characters |
| **Interaction** | `on_click` → activates input, shows cursor + keyboard |

**Validation:**
| Condition | Error Message |
|-----------|---------------|
| Field empty on submit | "Please enter a title for this recognition." |
| Exceeds 100 characters | "Title is too long (max 100 characters)." |

---

### B.5 — Awards Information Navigation Link

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9303` |
| **Type** | button — `text_link` subtype |
| **Text** | `"Tiêu chuẩn cộng đồng"` |
| **Interaction** | `on_click` → opens Community Standards screen/overlay |

**Logic:** Opens `[iOS] Sun*Kudos_Tiêu chuẩn cộng đồng` page or modal.  
Also serves as hint: "Ví dụ: Người truyền động lực cho tôi. / Danh hiệu sẽ hiển thị làm tiêu đề của Kudos của bạn." displayed as static hint text above B.5.

---

### C — Formatting Toolbar

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9306` |
| **Type** | FRAME (toolbar — `others`) |
| **Interaction** | None directly — children handle formatting |

**Children:**

| ID | No | Name | Icon | Action |
|----|----|------|------|--------|
| `6885:9307` | C.1 | Bold | `B` | Toggle bold on selected text |
| `6885:9309` | C.2 | Italic | `I` | Toggle italic on selected text |
| `6885:9311` | C.3 | Stroke (Strikethrough) | `S̶` | Toggle strikethrough on selected text |
| `6885:9313` | C.4 | Numbered List | `☰` | Toggle numbered list format |
| `6885:9315` | C.5 | Link | `🔗` | Open link insertion dialog |
| `6885:9317` | C.6 | Quote | `"` | Toggle blockquote format |

**Logic:** Toolbar controls apply formatting to the message textarea (D). Each button toggles independently. The active/toggled state is visually distinct from the default state.

**Edge cases:**
- Selecting text that already has bold/italic/etc. formatting → the corresponding toolbar button shows in its active state
- C.5 Link: tap opens a URL input dialog; if URL format is invalid, insertion is rejected (TC_VIETKUDO_FUN_012); if valid, link is inserted at cursor position (TC_VIETKUDO_FUN_022)
- Formatting state is scoped to the message textarea (D) only; no effect on other fields
- Double-tap prevention on toolbar buttons is NOT required (toggling on/off is the intended interaction)

---

### D — Message Textarea (Kudo Message Text Area)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9322` |
| **Type** | INSTANCE (text_form — rich text) |
| **Placeholder** | `"Hãy gửi gắm lời cám ơn và ghi nhận đến đồng đội tại đây nhé!"` |
| **DB Table** | `kudos` |
| **DB Column** | `message` |
| **Data type** | `string` (rich text / HTML) |
| **Required** | Yes |
| **Max length** | 1000 characters |
| **Interaction** | `on_click` → activates textarea, shows cursor + keyboard |

**Validation:**
| Condition | Error Message |
|-----------|---------------|
| Field empty on submit | "Please write your recognition message." |
| Exceeds 1000 characters | "Character limit reached. Please shorten your message." |
| Contains only whitespace | "Message cannot be empty." |

**Logic:**
- `@` + name triggers mention suggestions (inline autocomplete overlay showing matching colleagues)
- Selecting a name from the autocomplete list inserts the mention and closes the overlay (TC_VIETKUDO_FUN_025)
- Supports rich text formatting via toolbar (C)
- Mention hint visible below: `"Bạn có thể \"@ + tên\" để nhắc tới đồng nghiệp khác"`

---

### D.1 — Hint Label (Mention Hint)

| Property | Value |
|----------|-------|
| **Node ID** | _(root level, no explicit ID)_ |
| **Type** | label |
| **Text** | `"Bạn có thể \"@ + tên\" để nhắc tới đồng nghiệp khác"` |
| **Interaction** | None — static hint |

---

### E — Hashtag Section

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9324` |
| **Type** | FRAME (text_form container) |
| **DB Table** | `kudos` |
| **DB Column** | `tags` (JSON array of strings) |

#### E.1 — Hashtag Label

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9325` |
| **Text** | `"Hashtag *"` |
| **Required indicator** | `*` |
| **Interaction** | None |

#### E.2 — Tag Group

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9328` |
| **Type** | FRAME (tag_group) |
| **Add Button text** | `"+ Hashtag (Tối đa 5)"` |
| **Data type** | `array` |
| **Required** | Yes (min 1 tag) |
| **Max** | 5 tags |

**Validation:**
| Condition | Error Message |
|-----------|---------------|
| No hashtags added on submit | "Please add at least one hashtag." |
| More than 5 hashtags added | "Maximum 5 hashtags allowed." |

**Logic:**
- `+ Hashtag` → opens `[iOS] Sun*Kudos_dropdown hashtag` overlay
- Each added tag rendered as chip/pill with `×` remove button
- Add button disabled when 5 tags present

---

### F — Image Section

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9346` |
| **Type** | FRAME (image_upload) |
| **DB Table** | `kudos` |
| **DB Column** | `image_ids` (array of image refs) |
| **Required** | No (optional) |
| **Max** | 5 images |

#### F.1 — Image Label

| Node ID | Text |
|---------|------|
| `6885:9347` | `"Image"` |

#### F.2, F.2b, F.3, F.4 — Image Thumbnails

| No | Node ID | Description |
|----|---------|-------------|
| F.2 | `6885:9352` | Thumbnail 1 with `×` remove button |
| F.2b | `6885:9356` | Thumbnail 2 (additional) with `×` remove button |
| F.3 | `6885:9353` | Thumbnail 3 with `×` remove button |
| F.4 | `6885:9354` | Thumbnail 4 with `×` remove button |

**Logic per thumbnail:** Click `×` → removes this image from attachment list immediately.

#### F.5 — Add Image Button

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9355` |
| **Type** | INSTANCE (button — `icon_text`) |
| **Text** | `"+ Image"` (note: `"Tối đa 5"` displayed as separate note below button) |
| **Interaction** | `on_click` → opens system file/camera picker |

**Logic:** Hidden when 5 images already attached. After selection → thumbnail added to row. Client-side validation: image types only (jpg/png/webp), max 10MB per file (OWASP A04).

---

### G — Anonymous Toggle

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9363` |
| **Type** | FRAME (checkbox) |
| **Text** | `"Gửi lời cám ơn và ghi nhận ẩn danh"` |
| **DB Table** | `kudos` |
| **DB Column** | `is_anonymous` (Boolean) |
| **Default** | `false` (unchecked) |
| **Required** | No |
| **Interaction** | `on_click` → toggles anonymous mode |

**Logic:**
- Checked → sender's name hidden on published Kudos card; replaced with anonymous nickname
- Unchecked (default) → sender's full name and avatar visible

---

### H — Cancel Button

| Property | Value |
|----------|-------|
| **Node ID** | `6891:16834` |
| **Type** | INSTANCE (button — `text_link` subtype) |
| **Text** | `"Hủy"` |
| **Icon** | `✕` (close) |
| **Interaction** | `on_click` → discard form + navigate back |

**Logic:**
- If form is empty → navigate back immediately
- If form has any content → show confirmation dialog: "Bạn có chắc muốn hủy không? Dữ liệu đã nhập sẽ bị mất."
- Always enabled

---

### I — Send Button

| Property | Value |
|----------|-------|
| **Node ID** | `6891:16833` |
| **Type** | INSTANCE (button — `text_link` subtype) |
| **Text** | `"Gửi đi"` |
| **Icon** | `▶` (send/arrow) |
| **Interaction** | `on_click` → validate all fields then submit |

**Logic:**
- **Disabled** when any required field (B.2, B.4, D, E) is empty (see `isSubmitEnabled` derived state and Open Questions Q7)
- **Enabled** when all required fields contain content and no submission is in flight
- On click (enabled state):
  1. Client-side validation runs
  2. Show loading spinner on button
  3. POST to Supabase `kudos` table
  4. On success → navigate to `[iOS] Sun*Kudos_Gửi lời chúc Kudos`
  5. On failure → display error message; keep form intact for retry
- State: loading spinner shown during submission (button text replaced)

---

## API Requirements

### 1. Submit Kudo

| Property | Value |
|----------|-------|
| **Operation** | INSERT |
| **Table** | `kudos` |
| **Triggered by** | I — Send Button tap (after all client-side validation passes) |
| **Auth required** | Yes — authenticated user session |

**Request fields:**

| Field | Column | Type | Nullable | Notes |
|-------|--------|------|----------|-------|
| Recipient | `recipient_id` | UUID | No | FK → `profiles.id`; must not equal current user's ID |
| Title | `title` | string | No | Max 100 chars |
| Message | `message` | string (rich text) | No | Max 1000 chars; non-whitespace |
| Hashtags | `tags` | string[] (JSON array) | No | 1–5 items |
| Images | `image_ids` | string[] | Yes | 0–5 items; empty array if no images |
| Anonymous | `is_anonymous` | boolean | No | Default: `false` |
| Mentions | `mention_ids` | string[] (or embedded) | Yes | ⚠️ TBD — see Open Questions |

**Response handling:**
- Success (201) → navigate to `[iOS] Sun*Kudos_Gửi lời chúc Kudos`
- Error (4xx/5xx) → show error snackbar; preserve form state for retry

---

### 2. Fetch Recipient List (for B.2 dropdown)

| Property | Value |
|----------|-------|
| **Operation** | SELECT |
| **Table** | `profiles` (or `users`) |
| **Triggered by** | B.2 Recipient dropdown tap → opens search overlay |
| **Filtering** | Search by full name or department (server-side); exclude current user |
| **Auth required** | Yes |

**Pagination:** Required if user list is large — confirm max page size with backend.

---

### 3. Fetch Hashtag List (for E.2 dropdown)

| Property | Value |
|----------|-------|
| **Operation** | SELECT |
| **Table** | ⚠️ TBD — see Open Questions |
| **Triggered by** | E.2 "+ Hashtag" button tap → opens hashtag selection overlay |
| **Auth required** | Yes |

---

### 4. Upload Images (for F section)

| Property | Value |
|----------|-------|
| **Operation** | Storage upload |
| **Storage bucket** | TBD (likely `kudos-images` or `media`) |
| **Triggered by** | F.5 Add Image button → user selects file |
| **Validation** | Client-side: file type (jpg/png/webp), max 10 MB per file |
| **Auth required** | Yes |
| **Returns** | Image reference ID/URL to store in `kudos.image_ids` |

---

## State Management

### Local State (ViewModel-owned)

| State field | Type | Default | Notes |
|-------------|------|---------|-------|
| `recipientId` | String? | null | Selected recipient's ID |
| `recipientName` | String | "" | Display name for B.2 |
| `title` | String | "" | B.4 input value |
| `message` | String | "" | D textarea value (rich text) |
| `hashtags` | List\<String\> | emptyList() | E.2 selected tags (max 5) |
| `imageIds` | List\<String\> | emptyList() | F attached image refs (max 5) |
| `isAnonymous` | Boolean | false | G checkbox state |
| `isSending` | Boolean | false | True while server call in flight |
| `error` | String? | null | Snackbar error message |
| `formDirty` | Boolean | false | True when any field has been changed |

### Derived State (computed from local state)

| Derived property | Logic |
|-----------------|-------|
| `isSubmitEnabled` | `recipientId != null && title.isNotEmpty() && message.isNotEmpty() && hashtags.isNotEmpty() && !isSending` — ⚠️ see Open Questions Q7 for whitespace edge case |
| `canAddImage` | `imageIds.size < 5` |
| `canAddHashtag` | `hashtags.size < 5` |

### Global State / Side Effects

| Concern | Behaviour |
|---------|-----------|
| Auth session | If API returns 401, navigate to Login screen and clear session |
| Form prefill | If navigated from Search Sunner, ViewModel receives `recipientId` + `recipientName` as nav arg and sets them on init |
| Navigation args | Only primitive types passed via NavArgs (`recipientId: String?`, `recipientName: String?`) — no object passing |
| Cache invalidation | On successful submit, invalidate the kudos list cache in `[iOS] Sun*Kudos` so the new kudo appears on return |

---

## Accessibility Behavior

| Requirement | Affected Component | Notes |
|-------------|-------------------|-------|
| All interactive elements must have `contentDescription` | B.2, B.5, C.1–C.6, E.2 add button, F thumbnails, F.5, G, H, I | Screen reader must announce element purpose |
| Minimum touch target 48dp × 48dp | C.1–C.6 toolbar buttons, F thumbnail remove buttons | Per Material3 and Constitution §4.4 |
| Send button must announce disabled state | I | `disabled` semantic must be conveyed to TalkBack |
| Error messages must be announced | B.2, B.4, D, E | Inline errors should use `LiveRegion` or focus shift |
| Checkbox must use `Role.Checkbox` semantic | G — Anonymous Toggle | TalkBack must read checked/unchecked state |
| Focus order follows visual top-to-bottom reading order | All | B.2 → B.4 → D → E.2 → F.5 → G → H → I |
| Keyboard / D-pad navigation must reach all fields | All inputs, buttons | Required for external keyboard and accessibility navigation |

---

## Business Rules

| Rule | Detail |
|------|--------|
| Self-kudos prevention | Recipient cannot be the current authenticated user |
| Required fields | B.2 Người nhận, B.4 Danh hiệu, D Message, E Hashtag — all must be non-empty |
| Anonymous mode | When `is_anonymous = true`, sender identity masked on published card |
| Image validation | Type: jpg/png/webp only; Max size per file: 10 MB; Max count: 5 |
| Message @mention | `@` trigger opens suggestion list of colleagues; selected mention stored as metadata |
| Character limits | Title ≤ 100 chars; Message ≤ 1000 chars |
| Hashtag limits | 1–5 tags required; each tag selected from the hashtag selection overlay (source: see Open Questions) |
| Form dirty check | If unsaved content exists on Cancel → show confirmation dialog before discard |

---

## States & Error Handling

| State | UI Behavior |
|-------|-------------|
| **Default (empty)** | All fields empty, Send button disabled, no error shown |
| **Partially filled** | Send button stays disabled until all required fields valid |
| **Validation error** | Field-level inline error message shown below each invalid field |
| **Loading** | Send button shows spinner; form inputs disabled; Cancel stays enabled |
| **Submit success** | Navigate to success screen; form cleared |
| **Submit failure** | Error snackbar shown; form state preserved; retry possible |
| **Prefilled (from Search)** | B.2 shows selected user name; rest of form empty |
| **Recipient list unavailable** | B.2 dropdown is disabled; other fields remain interactive |
| **+ Hashtag at limit** | E.2 add button is disabled when 5 tags are present (TC_VIETKUDO_FUN_034) |
| **+ Image at limit** | F.5 add button is hidden when 5 images are attached (TC_VIETKUDO_FUN_035) |

---

## Open Questions

The following items cannot be resolved from existing context and require product/tech clarification before implementation.

**Business Logic:**
- **Q1 (Hashtag source):** Are hashtags selected from a server-provided predefined list, or can users type free-form text? If predefined, which table/endpoint provides the list, and is it paginated?
- **Q2 (Confirm dialog scope):** Does the "unsaved changes" confirmation dialog trigger only when fields with text are dirty, or also when a recipient is selected or a hashtag is added?

**Data / Validation:**
- **Q3 (@mention storage):** Are @mentions stored as structured metadata (array of mentioned user IDs in a separate column, e.g. `mention_ids`) or embedded as markup within the `message` text field? This affects the API payload design.
- **Q4 (Image upload endpoint):** Which Supabase Storage bucket is used for kudo image attachments? Is there a pre-signed upload flow or direct upload?

**Technical:**
- **Q5 (Success screen nav args):** Does `[iOS] Sun*Kudos_Gửi lời chúc Kudos` need any data passed from this screen (e.g. kudo ID, recipient name) as navigation arguments?
- **Q6 (Offline behavior):** If the user submits without a network connection, should the kudo be queued locally and retried, or should an immediate error be shown?
- **Q7 (Validation trigger model):** TC_VIETKUDO_FUN_033 specifies a disabled-button pattern (button enabled only when all required fields are filled). However, TC_VIETKUDO_FUN_001/003/006/010 describe users tapping Send with empty required fields and receiving inline errors. Clarify: (a) should Send be disabled until all required fields have content (`isNotEmpty()`), or (b) should Send always be tappable with all validation on submit? This affects whether whitespace-only input (e.g. message with only spaces) triggers a disabled state or an on-submit error.
