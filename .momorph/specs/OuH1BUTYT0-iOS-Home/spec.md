# Feature Specification: [iOS] Home

**Screen ID**: `OuH1BUTYT0`
**Frame ID**: `6885:8978`
**Frame Name**: `[iOS] Home`
**File Key**: `9ypp4enmFmdK3YAFJLIu6C`
**Figma Link**: https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/OuH1BUTYT0
**Group**: Main
**Created**: 2026-05-05
**Status**: Done

---

## Overview

Màn hình chính (Home / Landing) của ứng dụng SAA 2025 sau khi đăng nhập thành công. Hiển thị thông tin sự kiện Sun* Annual Awards 2025 bao gồm: countdown đến ngày diễn ra, thông tin chủ đề "Root Further", danh sách hạng mục giải thưởng (cuộn ngang), giới thiệu tính năng Sun\*Kudos. Cung cấp điều hướng toàn cục qua Bottom Navigation Bar và FAB.

---

## User Scenarios & Testing

### User Story 1 — Xem thông tin sự kiện và countdown (Priority: P1)

Người dùng vừa đăng nhập, muốn biết sự kiện SAA 2025 diễn ra khi nào và chủ đề năm nay là gì. Họ thấy ngay trên Hero section countdown đang đếm ngược đến 26/12/2025, logo "ROOT FURTHER", địa điểm và label "Coming soon".

**Why this priority**: Hero section là nội dung đầu tiên người dùng thấy ngay khi vào app. Countdown timer phải hoạt động real-time — đây là giá trị chính của màn hình.

**Independent Test**: Mở Home screen → kiểm tra countdown hiển thị DAYS/HOURS/MINUTES với giá trị chính xác → đợi 1 phút → kiểm tra MINUTES giảm 1.

**Acceptance Scenarios**:

1. **Given** người dùng đã đăng nhập và mở Home screen, **When** màn hình load, **Then** Hero section hiển thị: ROOT FURTHER logo, label "Coming soon", 3 đơn vị countdown (DAYS/HOURS/MINUTES) với giá trị số chính xác, "Thời gian: 26/12/2025", "Địa điểm: Âu Cơ Art Center", text "Tường thuật trực tiếp tại Group Facebook Sun\* Family"
2. **Given** countdown đang chạy, **When** đợi 60 giây, **Then** MINUTES giảm 1 (hoặc HOURS/DAYS điều chỉnh cascade tương ứng)
3. **Given** thời điểm sự kiện đã đến (26/12/2025), **When** người dùng mở Home, **Then** countdown hiển thị 00 DAYS 00 HOURS 00 MINUTES hoặc thay bằng label "Event Ended"; label "Coming soon" không còn hiển thị
4. **Given** sự kiện chưa bắt đầu, **When** quan sát Hero section, **Then** label "Coming soon" hiển thị

---

### User Story 2 — Khám phá hạng mục giải thưởng (Priority: P1)

Người dùng muốn biết SAA 2025 có những giải thưởng nào. Họ cuộn xuống phần Awards section, thấy danh sách card cuộn ngang, swipe để xem thêm, bấm "Chi tiết" để vào trang chi tiết giải.

**Why this priority**: Awards là nội dung core của SAA. API call `GET /awards` phải hoạt động; empty state và loading state phải được xử lý đúng.

**Independent Test**: Mở Home → cuộn đến Awards section → xác nhận có ≥1 award card với ảnh + tên + description + "Chi tiết" link → bấm "Chi tiết" → xác nhận chuyển đến Award Detail screen đúng category.

**Acceptance Scenarios**:

1. **Given** API trả về danh sách awards, **When** người dùng cuộn đến section Awards, **Then** danh sách card hiển thị với ảnh thumbnail, tên giải (e.g. "Top Talent"), description ngắn, link "Chi tiết" kèm icon mũi tên
2. **Given** description dài, **When** hiển thị trên card, **Then** text bị cắt với ellipsis ở cuối
3. **Given** người dùng tap "Chi tiết" trên award card, **When** action xảy ra, **Then** điều hướng đến Award Detail screen của category tương ứng
4. **Given** người dùng swipe left/right trên danh sách cards, **When** swipe, **Then** danh sách cuộn mượt theo chiều ngang
5. **Given** API chưa trả về dữ liệu, **When** màn hình load, **Then** loading state hiển thị trong Awards section
6. **Given** API không trả về dữ liệu awards, **When** Awards section render, **Then** empty state hiển thị thay cho danh sách cards

---

### User Story 3 — Tìm hiểu Sun\*Kudos và điều hướng (Priority: P1)

Người dùng muốn hiểu tính năng mới Sun\*Kudos là gì. Họ cuộn xuống Kudos section, đọc description, bấm "Chi tiết" để xem thêm.

**Why this priority**: Kudos là tính năng mới, người dùng cần được giới thiệu. FAB và "Chi tiết" button là entry points chính.

**Independent Test**: Mở Home → cuộn đến Kudos section → xác nhận Kudos banner hiển thị, có badge "ĐIỂM MỚI CỦA SAA 2025", description text, nút "Chi tiết" → bấm "Chi tiết" → xác nhận điều hướng đến Kudos screen.

**Acceptance Scenarios**:

1. **Given** tính năng Kudos khả dụng, **When** người dùng cuộn đến Kudos section, **Then** hiển thị: header "Sun\* Kudos", banner image (Sun\*Kudos branding), badge "ĐIỂM MỚI CỦA SAA 2025", description text, nút "Chi tiết"
2. **Given** người dùng tap "Chi tiết" trong Kudos section, **When** action, **Then** điều hướng đến Kudos detail/feed screen ([iOS] Sun\*Kudos — `fO0Kt19sZZ`)
3. **Given** Kudos banner image không tải được, **When** render, **Then** fallback placeholder hiển thị
4. **Given** tính năng Kudos bị disable, **When** render, **Then** Kudos section (mms_5_kudos) bị **ẩn hoàn toàn** — không hiển thị placeholder hay disabled state (align FR-012)

---

### User Story 4 — Điều hướng qua Bottom Navigation + FAB (Priority: P1)

Người dùng muốn chuyển đổi giữa các tab chính hoặc gửi Kudo mới từ bất kỳ điểm nào trên màn hình.

**Why this priority**: Bottom nav và FAB là navigation core — không có chúng user bị kẹt ở Home.

**Independent Test**: Mở Home → tap "Awards" tab → xác nhận điều hướng sang Awards screen → quay lại Home → tap FAB pencil icon → xác nhận mở form Viết Kudo.

**Acceptance Scenarios**:

1. **Given** người dùng ở Home screen (tab "SAA 2025" active), **When** tap tab "Awards", **Then** điều hướng đến [iOS] Award_Top talent, tab "Awards" highlighted
2. **Given** người dùng ở Home, **When** tap tab "Kudos", **Then** điều hướng đến [iOS] Sun\*Kudos, tab "Kudos" highlighted
3. **Given** người dùng ở Home, **When** tap tab "Profile", **Then** điều hướng đến [iOS] Profile bản thân, tab "Profile" highlighted
4. **Given** người dùng ở screen khác, **When** tap tab "SAA 2025", **Then** quay về Home, tab "SAA 2025" highlighted
5. **Given** người dùng tap Pencil icon trên FAB, **When** action, **Then** mở form Viết Kudo ([iOS] Sun\*Kudos_Viết Kudo_default)
6. **Given** người dùng tap S/Kudos icon trên FAB, **When** action, **Then** điều hướng đến [iOS] Sun\*Kudos feed
7. **Given** người dùng tap nhanh liên tục vào FAB pencil, **When** double/rapid tap, **Then** chỉ mở 1 lần (double-click prevention)

---

### User Story 5 — Header: Language Switcher + Notifications + Search (Priority: P2)

Người dùng muốn đổi ngôn ngữ, xem thông báo mới, hoặc tìm kiếm Sunner từ Header trên Home screen.

**Why this priority**: Header actions là global features — có thể test độc lập, nhưng secondary so với main content.

**Independent Test**: Mở Home → tap Language Switcher → xác nhận modal ngôn ngữ hiển thị → tap Bell icon → xác nhận Notifications panel hiển thị.

**Acceptance Scenarios**:

1. **Given** người dùng tap Language Switcher (VN flag + "VN"), **When** action, **Then** Language selection modal/dropdown hiển thị ([iOS] Language dropdown — `uUvW6Qm1ve`)
2. **Given** người dùng tap Search icon, **When** action, **Then** điều hướng đến Search screen
3. **Given** người dùng tap Notification Bell, **When** action, **Then** [iOS] Notifications panel hiển thị (`_b68CBWKl5`)
4. **Given** tài khoản có unread notifications, **When** Home load, **Then** badge count (dot đỏ) hiển thị trên Bell icon
5. **Given** tất cả notifications đã đọc, **When** Home load, **Then** Bell icon không hiển thị badge
6. **Given** người dùng đang ở Home với ngôn ngữ VN (default), **When** quan sát Language Switcher, **Then** hiển thị cờ VN + label "VN" + dropdown arrow

---

### Edge Cases

- Countdown khi đạt 0: hiển thị 00/00/00 hoặc "Event Ended" — cần confirm với designer
- Awards section khi mạng lỗi: hiển thị error state (retry button)
- Kudos banner khi image URL hỏng: fallback placeholder
- FAB rapid tap: prevent duplicate navigation
- Bottom nav khi ở Home, tap "SAA 2025" lại: không navigate (stay current) hoặc scroll to top

---

## UI/UX Requirements *(from Figma)*

### Background Layers *(non-design-item, decorative only)*

| Layer | Node ID | Description |
|-------|---------|-------------|
| mm_media_bg group | 6885:8979 | Keyvisual BG image + Shadow Left gradient + Shadow Bottom gradient — full-bleed behind all content |

### Screen Components

| # | Component | Node ID | Type | Description | Interaction |
|---|-----------|---------|------|-------------|-------------|
| 1 | mms_1_header | 6885:9057 | navigation | Logo + Language Switcher + Search + Bell (opaque header gradient) | Tap Language → modal; Tap Search → Search screen; Tap Bell → Notifications |
| 2 | mms_2_content | 6885:8983 | info_block | Hero section: ROOT FURTHER logo, Coming soon label, countdown timer (DAYS/HOURS/MINUTES), event date/venue/livestream, ABOUT AWARD + ABOUT KUDOS buttons | Countdown auto-updates every second |
| 2.1 | mms_2.1_MM_MEDIA_Logo/RootFuther | 6885:8984 | image | "ROOT FURTHER" theme logo image (247×109dp) | None |
| 2.2 | mms_2.2_Button | 6885:9026 | button icon_text | "ABOUT AWARD" CTA — primary style (solid #FFEA9E, dark label) | Tap → Awards overview |
| 2.3 | mms_2.3_Button | 6885:9027 | button icon_text | "ABOUT KUDOS" CTA — secondary/outline style (10% #FFEA9E bg + border, white label) | Tap → Kudos overview |
| 3 | mms_3_note | 6885:9028 | label | Theme description paragraph ("Root Further is not merely…") | None (static) |
| 4 | mms_4_awards | 6885:9030 | card | Awards section container: header + horizontally scrollable award card list | Swipe horizontal |
| 4.1 | mms_4.1_header | 6885:9031 | label | Section header: "Sun* Annual Awards 2025" label + "Hệ thống giải thưởng" title + divider | None |
| 4.2 | mms_4.2_award list | 6885:9032 | list_item | Horizontally scrollable award cards (image + name + description truncated + "Chi tiết" link) | Swipe L/R; Tap "Chi tiết" → Award Detail |
| 5 | mms_5_kudos | 6885:9039 | info_block | Kudos section: header + banner image + badge + description + "Chi tiết" button | — |
| 5.1 | mms_5.1_header | 6885:9040 | label | Section header: "Phong trào ghi nhận" label + "Sun\* Kudos" title + divider | None |
| 5.2 | mms_5.2_mm_media_Sunkudos | 6885:9041 | image | Sun\*Kudos banner: dark bg + gold diagonal + KUDOS logo (335×145dp) | None (decorative) |
| 5.3 | mms_5.3_Button | 6885:9055 | button icon_text | "Chi tiết" CTA with arrow icon | Tap → Sun\*Kudos screen |
| 6 | mms_6_float button | 6885:9058 | button | FAB (89×48dp, pill shape): Pencil icon + "/" separator + S/Kudos icon | Pencil → Viết Kudo; S icon → Kudos feed |
| 7 | mms_7_nav bar | 6885:9056 | navigation | Bottom Tab Bar: SAA 2025 (active), Awards, Kudos, Profile | Tap tab → navigate |

### Navigation Flow


> ⚠️ `linkedFrameId` is `null` for all 15 items in MoMorph. Destination screen IDs below are inferred from SCREENFLOW — **must be verified before implementation**.

| From | Action | To Screen |
|------|--------|----------|
| Home | Tap "ABOUT AWARD" | [iOS] Award_Top talent (`c-QM3_zjkG`) |
| Home | Tap "ABOUT KUDOS" | [iOS] Sun\*Kudos (`fO0Kt19sZZ`) |
| Home | Tap award card "Chi tiết" | Award Detail for that category |
| Home | Tap Kudos "Chi tiết" | [iOS] Sun\*Kudos (`fO0Kt19sZZ`) |
| Home | Tap FAB Pencil | [iOS] Sun\*Kudos_Viết Kudo_default (`7fFAb-K35a`) |
| Home | Tap FAB S/Kudos | [iOS] Sun\*Kudos (`fO0Kt19sZZ`) |
| Home | Tap Language Switcher | [iOS] Language dropdown (`uUvW6Qm1ve`) |
| Home | Tap Bell icon | [iOS] Notifications (`_b68CBWKl5`) |
| Home | Tap Bottom Nav "Awards" | [iOS] Award_Top talent (`c-QM3_zjkG`) |
| Home | Tap Bottom Nav "Kudos" | [iOS] Sun\*Kudos (`fO0Kt19sZZ`) |
| Home | Tap Bottom Nav "Profile" | [iOS] Profile bản thân (`hSH7L8doXB`) |

### Visual Requirements

- Screen is **vertically scrollable** (full height ≈ 1942px)
- Header is **fixed/sticky** at top (position absolute over background)
- FAB is **fixed** at bottom-right (position absolute over content)
- Bottom NavBar is **fixed** at bottom
- Awards section is **horizontally scrollable** (content width 1040px)
- Keyvisual BG image is **full-bleed** behind all content

---

## Requirements

### Functional Requirements

- **FR-001**: System MUST display real-time countdown to 26/12/2025 (updating every second); showing DAYS, HOURS, MINUTES
- **FR-002**: System MUST show "Coming soon" label before event date; hide it after event starts
- **FR-003**: System MUST load award categories from API and render horizontally scrollable card list
- **FR-004**: Award card description text MUST be truncated with ellipsis when exceeding display width
- **FR-005**: System MUST navigate to correct Award Detail screen when "Chi tiết" is tapped on an award card
- **FR-006**: System MUST handle empty state for Awards section when API returns no data
- **FR-007**: System MUST handle loading state for Awards section while API is in progress
- **FR-008**: System MUST display notification badge (dot) on Bell icon when unread notifications exist
- **FR-009**: FAB pencil tap MUST open Viết Kudo form; FAB S/Kudos tap MUST navigate to Sun\*Kudos feed
- **FR-010**: Double/rapid tap on FAB MUST be prevented (only one navigation allowed)
- **FR-011**: Bottom Navigation tab active state MUST reflect current screen
- **FR-012**: System MUST **hide** the Kudos section (mms_5_kudos) entirely when the feature is unavailable — do NOT show a disabled placeholder *(behavior confirmed: hide, not disable)*
- **FR-013**: Kudos banner image MUST show fallback placeholder when image fails to load

### Technical Requirements

- **TR-001**: Countdown timer MUST use server time (not device clock) to prevent client-side drift; update interval ≤ 1 second
- **TR-002**: Award card list MUST use lazy loading / paging if award count is large
- **TR-003**: Home screen MUST be the default destination after successful login (NavHost start destination)
- **TR-004**: Home screen MUST handle 401 (redirect to Login) and 403 (redirect to Access Denied) from any API call
- **TR-005**: All navigation follows single `NavHost` pattern in `SaaNavHost.kt`; no direct Activity transitions
- **TR-006**: No business logic in Composables — countdown logic, API calls, badge count in ViewModel

### Key Entities

- **Award**: `id`, `category`, `name`, `description`, `thumbnailUrl` — loaded from `GET /awards`
- **CountdownTarget**: event date `2025-12-26T00:00:00+07:00` (hardcoded or from config API)
- **NotificationBadge**: `unreadCount: Int` — from `GET /notifications` or real-time subscription
- **KudosFeatureFlag**: boolean flag indicating if Kudos is available

---

## API Dependencies

| Endpoint | Method | Purpose | Triggered by | Status |
|----------|--------|---------|--------------|--------|
| `GET /awards` | GET | Load award categories for card list | Screen mount | Predicted |
| `GET /notifications?unread=true` | GET | Get unread count for Bell badge | Screen mount | Predicted |
| `GET /users/me` | GET | Verify auth, load user session | Screen mount (guard) | Predicted |

---

## State Management

| State | Scope | Source | Notes |
|-------|-------|--------|-------|
| `countdownTarget` | HomeViewModel | Hardcoded or remote config | 26/12/2025 00:00:00 GMT+7 |
| `timeRemaining` | HomeViewModel | Coroutine timer (tick every 1s) | Derived from countdownTarget |
| `isEventStarted` | HomeViewModel | Derived from countdownTarget | Controls "Coming soon" visibility |
| `awards` | HomeViewModel | `GET /awards` | List<Award>; empty = show empty state |
| `awardsLoading` | HomeViewModel | API call state | Shows loading skeleton |
| `unreadNotificationCount` | HomeViewModel | `GET /notifications` or Realtime | Controls Bell badge |
| `isKudosAvailable` | HomeViewModel | Feature flag or remote config | Controls Kudos section visibility |

---

## Success Criteria

- **SC-001**: Countdown displays correct value within ±1 second of server time
- **SC-002**: Award cards load and render within 3 seconds on 4G connection
- **SC-003**: Tapping any navigation element (nav bar, FAB, header actions) routes to correct screen within 300ms
- **SC-004**: No duplicate navigation occurs on rapid FAB taps
- **SC-005**: All 7 mms sections (header, hero content, theme note, awards, kudos, FAB, nav bar) render correctly on screen sizes from 360×800 to 414×896

---

## Out of Scope

- Search functionality (dedicated Search screen, not implemented here)
- Language switching logic (handled in Language dropdown screen)
- Secret box interaction (not on Home — accessible from Profile)
- Push notification delivery (handled by OS notification system)
- "Thể lệ" (Rules) page — accessible from Home per SCREENFLOW but no explicit design item found on this screen; confirm with designer

---

## Dependencies

- [x] Constitution document exists (`.momorph/guidelines/constitution.md`)
- [x] Screen flow documented (`.momorph/contexts/SCREENFLOW.md`)
- [ ] API specifications confirmed (`GET /awards` endpoint)
- [ ] Countdown target date confirmed with BTC (26/12/2025)
- [ ] Kudos feature flag mechanism defined
- [ ] Event-ended state behavior confirmed with designer

---

## Figma Design Items Reference

| Node ID | No. | Name | Status |
|---------|-----|------|--------|
| 6885:9057 | 1 | mms_1_header | completed |
| 6885:8983 | 2 | mms_2_content | completed |
| 6885:8984 | 2.1 | mms_2.1_MM_MEDIA_Logo/RootFuther | completed |
| 6885:9026 | 2.2 | mms_2.2_Button (ABOUT AWARD) | completed |
| 6885:9027 | 2.3 | mms_2.3_Button (ABOUT KUDOS) | completed |
| 6885:9028 | 3 | mms_3_note (theme description) | completed |
| 6885:9030 | 4 | mms_4_awards | completed |
| 6885:9031 | 4.1 | mms_4.1_header | completed |
| 6885:9032 | 4.2 | mms_4.2_award list | completed |
| 6885:9039 | 5 | mms_5_kudos | completed |
| 6885:9040 | 5.1 | mms_5.1_header | completed |
| 6885:9041 | 5.2 | mms_5.2_mm_media_Sunkudos | completed |
| 6885:9055 | 5.3 | mms_5.3_Button (Chi tiết) | completed |
| 6885:9058 | 6 | mms_6_float button (FAB) | completed |
| 6885:9056 | 7 | mms_7_nav bar | completed |

## Notes

- Screen total height ≈ 1942px (vertically scrollable)
- Screen width: 375px (iPhone standard)
- Awards section content width: 1040px (wider than screen → horizontal scroll)
- Countdown target date confirmed in design: "Thời gian: 26/12/2025"
- Event venue confirmed: "Âu Cơ Art Center"
- Livestream: "Tường thuật trực tiếp tại Group Facebook Sun\* Family"
- Theme description text (mms_3_note) is static; does NOT require API
- All 15 design items status: **completed**
