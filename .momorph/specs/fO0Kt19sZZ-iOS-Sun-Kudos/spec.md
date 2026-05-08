# Screen: [iOS] Sun*Kudos

## Screen Info

| Property | Value |
|----------|-------|
| **Screen ID** | `fO0Kt19sZZ` |
| **Figma Node ID** | `6885:9059` |
| **Figma Link** | https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/fO0Kt19sZZ |
| **Screen Group** | Kudos |
| **Spec Status** | ✅ reviewed |
| **Platform** | iOS / Android |
| **Discovered At** | 2026-05-05 |
| **Last Updated** | 2026-05-06 |

---

## Description

Màn hình chính của tính năng Sun*Kudos — nơi Sunner xem feed lời cảm ơn (Kudos), gửi Kudos mới, xem thống kê cá nhân, và mở Secret Box. Gồm 4 section chính:

1. **KV Header (A)**: Banner Kudos key visual + nút ghi nhận
2. **Highlight Kudos (B)**: Carousel 5 kudo nổi bật (top heart count) + Spotlight Board tương tác
3. **All Kudos (C)**: Feed dạng list đầy đủ với filter
4. **Personal Stats (D)**: Thống kê cá nhân + danh sách người nhận quà gần nhất

---

## Navigation Analysis

### Incoming Navigations (From)

| Source Screen | Trigger | Condition |
|---------------|---------|-----------|
| [iOS] Home | Tab bar "Kudos" | Always |
| [iOS] Home | Click "ABOUT KUDOS" | Always |
| [iOS] Home | FAB pen → Viết Kudo | Always (opens WriteKudo) |
| Tất cả Award screens | Tab bar "Kudos" | Always |
| [iOS] Sun*Kudos_Viết Kudo | Back / Cancel | Sau khi gửi kudo |
| [iOS] Sun*Kudos_View kudo | Back | Return from detail |
| [iOS] Profile người khác | Tab bar "Kudos" | Always |

### Outgoing Navigations (To)

| Target Screen | Trigger Element | Node ID | Confidence | Notes |
|---------------|-----------------|---------|------------|-------|
| [iOS] Sun*Kudos_Viết Kudo_default | A.1 — Button "Hôm nay, bạn muốn gửi kudos đến ai?" | `6885:9083` | high | Mở màn hình soạn Kudo |
| [iOS] Sun*Kudos_View kudo | B.3 — Tap card / "Xem chi tiết" | `6885:9091–9265` | high | Xem chi tiết một Kudo |
| [iOS] Sun*Kudos_All Kudos | C.2 — "View all Kudos ↗" | null | high | Trang danh sách đầy đủ |
| [iOS] Sun*Kudos_dropdown hashtag | B.1.1 — Hashtag filter button | null | high | Bottom sheet filter hashtag |
| [iOS] Sun*Kudos_dropdown phòng ban | B.1.2 — Phòng ban filter button | null | high | Bottom sheet filter phòng ban |
| [iOS] Open secret box | D.2 — "Mở Secret Box 🎁" | null | high | Chỉ khi secret_boxes_unopened > 0 |
| [iOS] Sun*Kudos_Search Sunner | Header search icon | `6885:9065;88:1869` | high | Tìm kiếm Sunner |
| [iOS] Profile người khác | B.3.1 / B.3.5 — tap avatar | various | high | Xem profile người gửi/nhận |
| Language Overlay | Header language selector | `6885:9065;88:1829` | high | Bottom sheet chọn ngôn ngữ |

### Navigation Rules
- **Back behavior**: Tab bar — không có stack back trên màn hình này
- **Deep link**: `saa://kudos` → mở màn hình này
- **Auth required**: Có — redirect về Login nếu token hết hạn (401)
- **Realtime**: Subscribe Supabase Realtime cho bảng `kudos` — kudo mới được push lên đầu feed

---

## Component Schema

### Layout Structure

> **Ghi chú cấu trúc Figma (quan trọng cho implementation):**
> - `A.1` nằm ở ROOT LEVEL (không trong frame A) — 2 instance: `6885:9083` (default) + `6891:21267` (sticky khi scroll qua banner)
> - `B.2.1 ‹` và `B.2.2 ›` là floating overlay elements ở ROOT LEVEL, không phải child của `B_Highlight`
> - `B.5_slide` (pagination) là ROOT LEVEL sibling; `B.5.2` hiển thị text `"N/5"` (không phải dots)
> - `B.6. Spotlight board` là section frame đầy đủ (`6885:9099`) chứa cả header + `B.7`
> - `mms_C_All kudos` chứa **cả D.1 stats + D.3 gifts + Danh sách Kudo** bên trong — không có node D riêng

```
┌──────────────────────────────────────┐
│  Header (HomeHeader)                 │
│  [StatusBar] [Logo] [VN▼] [🔍] [🔔]│
├──────────────────────────────────────┤
│  mm_media_bg (background overlay)    │
│  A — KV Kudos [6885:9066]           │
│    "Hệ thống ghi nhận và cảm ơn"    │
│    KUDOS (gold logo text)            │
├──────────────────────────────────────┤
│  A.1 — Button "Hôm nay..." [9083]   │  ← root-level, bên dưới A
│  A.1 — Button "Hôm nay..." [21267]  │  ← root-level, sticky khi scroll
├──────────────────────────────────────┤
│  B — Highlight [6885:9084]          │
│    B.1 — header + filter [9085]     │
│      "Sun* Annual Awards 2025"       │
│      B.1.1 — Hashtag ▼              │
│      B.1.2 — Phòng ban ▼            │
│    B.2 — HIGHLIGHT KUDOS [9090]     │
│      B.3 — KudosHighlightCard ×3   │  ← 3 cards visible, 5 total
│        B.3.1 — Avatar người gửi    │
│        B.3.2 — Info người gửi      │
│        B.3.4 — Icon mũi tên →      │
│        B.3.5 — Avatar người nhận   │
│        B.3.6 — Info người nhận     │
│        B.4 — Kudos content         │
│          B.4.1 — Thời gian đăng   │
│          B.4.2 — Nội dung          │
│          B.4.3 — Hashtags          │
│          B.4.4 — Actions           │
├──────────────────────────────────────┤
│  B.2.1 ‹ [9094] (floating overlay)  │  ← ROOT-LEVEL, overlay trái carousel
│  B.2.2 › [9096] (floating overlay)  │  ← ROOT-LEVEL, overlay phải carousel
│  B.5 — Pagination [9098]            │  ← ROOT-LEVEL sibling
│      ‹  "2/5"  ›                    │  ← B.5.2 = text "N/5"
├──────────────────────────────────────┤
│  B.6. Spotlight board [9099]        │  ← full section (header + B.7)
│    header: "SPOTLIGHT BOARD"        │
│    B.7 — Spotlight [9101]           │
│      B.7.1 — "388 KUDOS"           │
│      B.7.2 — Pan/Zoom map [9217]   │
│      B.7.3 — Search input [9216]   │
│      [Sunner text nodes ×N]         │
├──────────────────────────────────────┤
│  C — mms_C_All kudos [6885:9220]   │  ← chứa cả C, D.1, D.3
│    C.1 — header [9221]             │
│      "Sun* Annual Awards 2025"      │
│      "ALL KUDOS" (+ C.2 link)       │
│    content [9222]                   │
│      D.1 — Thống kê [9223]         │
│        D.1.2 — Kudos nhận: N        │
│        D.1.3 — Kudos gửi: N        │
│        D.1.4 — Số tim: N           │
│        D.1.5 — Divider             │
│        D.1.6 — Box đã mở: N        │
│        D.1.7 — Box chưa mở: N      │
│      D.2 — "Mở Secret Box 🎁"      │
│      D.3 — Sunner nhận quà [9255]  │
│        D.3.1 — Title               │
│        D.3.2 — GiftRecipientRow ×N │
│      Danh sách Kudo [6891:15986]    │  ← kudo list (4+ cards)
│        C.3 — KudosPostCard ×N      │
│          C.3.1 — Sender info        │
│          C.3.2 — Arrow icon         │
│          C.3.3 — Recipient info     │
│          C.3.4 — Post time          │
│          C.3.5 — Content card       │
│        C.2 — "View all Kudos ↗"    │
├──────────────────────────────────────┤
│  Bottom NavBar (HomeNavBar) [9064]  │
│  [SAA] [Awards] [Kudos*] [Profile]  │
└──────────────────────────────────────┘
```

---

## Design Items Detail

### A — KV Kudos (mms_A_KV Kudos)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9066` |
| **Type** | Frame (full-width banner) |
| **Interaction** | Không chứa A.1 — A.1 là root-level element riêng |

**Hiển thị:**
- Background: Ảnh keyvisual (`mm_media_bg` group: `MM_MEDIA_Keyvisual BG` + Shadow Left + Shadow Bottom)
- Subtitle text: "Hệ thống ghi nhận và cảm ơn" (`6885:9068`, trên logo)
- Text logo "KUDOS" dạng decorative (gold, ExtraBold) trong `kudo logo` frame (`6885:9069`)

> **Lưu ý cấu trúc:** `mm_media_bg` (`6885:9060`) là GROUP nằm ở root level, phía sau `mms_A_KV Kudos`.

---

### A.1 — Button ghi nhận (mms_A.1_Button ghi nhận)

| Property | Value |
|----------|-------|
| **Node ID (default)** | `6885:9083` |
| **Node ID (sticky)** | `6891:21267` |
| **Type** | Button (`icon_text`) — root-level element, không nằm trong frame A |
| **Placeholder** | `"Hôm nay, bạn muốn gửi kudos đến ai?"` |
| **Icon** | `mm_media_icon` (pen/edit icon, `I6885:9083;28:2013`) |
| **Interaction** | `on_click` → Viết Kudo screen |

**Hiển thị:** Input-like button với icon bút + placeholder text — trông như TextField nhưng là button.  
**Logic:**
- Hai instance: `6885:9083` hiển thị ngay dưới banner A; `6891:21267` là sticky version hiện ra khi scroll qua KV banner
- Luôn enabled, không có disable condition (TC_GUI_008)
- Click → Navigate đến `[iOS] Sun*Kudos_Viết Kudo_default` (cũng được đặt tên `[iOS] Sun*Kudos_Gửi lời chúc Kudos` trong MoMorph)

---

### B — Highlight (mms_B_Highlight)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9084` |
| **Type** | Frame (section container) |
| **Background** | Dark (inherits screen `#00101A`) |

---

### B.1 — Header + Filters (mms_B.1_header)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9085` |
| **Type** | Frame (sticky header của Highlight section) |

**Hiển thị:**
- Label: "Sun* Annual Awards 2025" (subtitle, small, secondary)
- Row filter: B.1.1 (Hashtag ▼) + B.1.2 (Phòng ban ▼)

---

### B.1.1 — Hashtag Filter Button

| Property | Value |
|----------|-------|
| **Type** | Button (`icon_text`, dropdown style) |
| **Label** | `"Hashtag"` (placeholder khi chưa chọn) |
| **Data Source** | `GET /api/v1/hashtags` |
| **Interaction** | `on_click` → Bottom sheet danh sách hashtag |

**Logic:**
- Tap → mở overlay `[iOS] Sun*Kudos_dropdown hashtag`
- Sau chọn: label cập nhật thành tên hashtag đã chọn
- Filter áp dụng AND logic với Phòng ban filter
- Khi thay đổi filter → carousel reset về card 1
- Filter ảnh hưởng cả Highlight Kudos (B) và All Kudos (C)

---

### B.1.2 — Phòng Ban Filter Button

| Property | Value |
|----------|-------|
| **Type** | Button (`icon_text`, dropdown style) |
| **Label** | `"Phòng ban"` (placeholder khi chưa chọn) |
| **Data Source** | `GET /api/v1/departments` |
| **Interaction** | `on_click` → Bottom sheet danh sách phòng ban |

**Logic:** Tương tự B.1.1, AND logic với Hashtag filter.

---

### B.2 — HIGHLIGHT KUDOS (mms_B.2_HIGHLIGHT KUDOS)

| Property | Value |
|----------|-----------|
| **Node ID** | `6885:9090` |
| **Type** | Frame (carousel container) |

**Cấu trúc:**
- B.2 là container frame chứa title "HIGHLIGHT KUDOS" + các B.3 card instance
- **B.2.3 — Nội dung Highlight Kudos** (design item `id: null`): nhóm logic mô tả carousel content (5 cards, API: `GET /api/v1/kudos/highlight`, sort by `heartCount DESC`, filter hashtag AND department)
- Các floating nav buttons (`B.2.1 ‹` + `B.2.2 ›`) nằm ở root level, không phải child của frame này

---

### B.3 — KudosHighlightCard (mms_B.3_KUDO - Highlight)

| Property | Value |
|----------|-------|
| **Node IDs** | `6885:9091`, `9092`, `9093`, `9263`, `9264`, `9265` (6 instances trong Figma; 3 visible, API drives count ≤5) |
| **Type** | Card (interactive) |
| **Data Source** | `GET /api/v1/kudos/highlight?hashtag_id=&department_id=` |
| **Query** | Top 5 by `heart_count DESC` |

**Hiển thị:**
- Row trên: B.3.1 (avatar gửi) + B.3.2 (tên + badge gửi) + B.3.4 (→) + B.3.5 (avatar nhận) + B.3.2 (tên + badge nhận)
- Row dưới (B.4): timestamp + nội dung + hashtags + actions

**Logic:**
- Swipe ngang: chuyển card
- Card center: full opacity; card bên cạnh: faded (TC_FUN_038)
- Nội dung message: truncate 3 dòng (TC_GUI_005)
- Tên người gửi: truncate với "..." khi quá dài (TC_GUI_005)

---

### B.3.1 — Avatar người gửi

| Property | Value |
|----------|-------|
| **Type** | CircularImage |
| **Data** | `users.avatar_url` (sender) |
| **Interaction** | `on_click` → Profile người gửi |
| **Fallback** | Default avatar khi không có ảnh |

---

### B.3.2 — Thông tin người gửi/nhận

| Property | Value |
|----------|-------|
| **Type** | Label group |
| **Data** | `users.full_name`, `users.employee_code`, `users.badge_type` |

**Hiển thị:**
- Tên: truncate + "..." khi quá dài (e.g., "Dương Xuân Huỳnh...")
- Employee code: text label (e.g., "CECV10")
- Badge: colored tag (e.g., "Legend Hero")

---

### B.3.4 — Icon mũi tên (mms_B.3.4_Icon mũi tên)

| Property | Value |
|----------|-------|
| **Type** | Info block (icon) |
| **Display** | Icon mũi tên phải (→) |
| **Interaction** | None (display only) |

---

### B.3.5 — Avatar người nhận

| Property | Value |
|----------|-------|
| **Type** | CircularImage |
| **Data** | `users.avatar_url` (recipient) |
| **Interaction** | `on_click` → Profile người nhận |
| **Fallback** | Default avatar khi không có ảnh |

---

### B.3.6 — Thông tin người nhận

| Property | Value |
|----------|-------|
| **Type** | Label group |
| **Data** | `users.name`, `users.avatar_url`, `users.department_id`, `users.hero_tier` |
| **Badge** | Star badge: 10 kudos→1★, 20→2★, 50→3★ (TC_FUN_006) |
| **Interaction** | Tap → Profile người nhận (TC_ACC_006) |

---

### B.4.1 — Thời gian đăng (mms_B.4.1_Thời gian đăng)

| Property | Value |
|----------|-------|
| **Type** | Label |
| **Data** | `kudos.created_at` (formatted) |
| **Format** | `"HH:mm - DD/MM/YYYY"` (e.g., "10:00 - 10/30/2025") |
| **Timezone** | Local time của user |

---

### B.4.2 — Nội dung Kudo (mms_B.4.2_Nội dung)

| Property | Value |
|----------|-------|
| **Type** | Label |
| **Data** | `kudos.title` (recognition category), `kudos.message` |
| **Max Lines** | 3 (truncate với "...") |
| **Interaction** | None — full content qua "Xem chi tiết" |

**Hiển thị:**
- Title: recognition category uppercase (e.g., "IDOL GIỚI TRẺ")
- Body: free-text kudos message, max 3 lines

---

### B.4.3 — Hashtags (mms_B.4.3_Hashtag)

| Property | Value |
|----------|-------|
| **Type** | Label row |
| **Data** | `kudos_hashtags → hashtags.name` |
| **Max** | 5 tags, 1 dòng (truncate nếu quá) |

---

### B.4.4 — Action row (mms_B.4.4_Action)

| Property | Value |
|----------|-------|
| **Type** | Action row |

**Hiển thị:**
- ❤️ Heart icon + count (số hearts nhận được)
- 🔗 "Copy Link" — copy URL của kudo vào clipboard
- ↗ "Xem chi tiết" — navigate đến View kudo

**Logic:**
- Heart: tap toggle like/unlike → `POST /api/v1/kudos/{id}/like`
- Copy Link: sao chép `shareUrl` vào clipboard, hiển thị toast xác nhận
- "Xem chi tiết": Navigate đến `[iOS] Sun*Kudos_View kudo`

---

### B.5 — Pagination + Nav (mms_B.5_slide)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9098` — **root-level sibling** (không nằm trong `B_Highlight`) |
| **Type** | Pagination component |
| **Children** | `IC` (prev icon, `I6885:9098;93:2085`) + text `"2/5"` (`I6885:9098;93:2086`) + `IC` (next icon, `I6885:9098;93:2087`) |

**Hiển thị:** ‹ icon + **text "N/5"** + › icon  

**B.5.1 — IC (prev icon, `I6885:9098;93:2085`):**
- Tap: chuyển carousel sang card trước (page - 1)
- Disabled khi đang ở card đầu tiên (page = 1)

**B.5.2 — Số trang (text "N/5"):**
- Format: `"2/5"`, `"3/5"`, v.v. — text hiển thị vị trí hiện tại / tổng số cards
- **Không phải dots** — là text indicator

**B.5.3 — IC (next icon, `I6885:9098;93:2087`):**
- Tap: chuyển carousel sang card tiếp theo (page + 1)
- Disabled khi ở card cuối cùng (page = 5)

---

### B.6 — Spotlight Board Section

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9099` — **root-level sibling** (frame đầy đủ) |
| **Type** | Frame section (chứa header + B.7 Spotlight) |

**Cấu trúc:**
- `header` instance (`6885:9100`) → "Sun* Annual Awards 2025" (subtitle) + "SPOTLIGHT BOARD" (title)
- `mms_B.7_Spotlight` (`6885:9101`) — nội dung interactive board

**Logic:** Header là display only. Interaction nằm trong B.7.

---

### B.7 — Spotlight Board (mms_B.7_Spotlight)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9101` (child của `B.6. Spotlight board` `6885:9099`) |
| **Type** | Interactive map/board |

**Hiển thị:**
- B.7.1: Label "388 KUDOS" (`6885:9219`) — tổng số kudos trên board
- B.7.2: Pan & zoom frame (`6885:9217`, `mms_B.7.2_Pan zoom fdsfs`) — background image map + các text node tên Sunner
- B.7.3: Search Sunner input (`6885:9216`, `mms_B.7.3_Tìm kiếm sunner`)
- Text nodes: tên Sunner phân tán khắp board (ví dụ: "Đỗ hoàng Hiệp", "Dương thúy An", "Nguyễn Bá Chức"...)

**Logic:**
- Pan/zoom: gesture để navigate board
- B.7.3: realtime search trong board → highlight vị trí Sunner
- Click trên node Sunner → navigate đến Profile người đó

---

### C — mms_C_All kudos (container section)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9220` |
| **Type** | Frame (large container — chứa cả stats, gifts, và kudo list) |

> **Lưu ý cấu trúc Figma:** Frame `mms_C_All kudos` chứa bên trong:
> 1. `header` (`6885:9221`) — C.1 section header
> 2. `content` (`6885:9222`) — chứa `D.1 stats`, `D.3 gifts`, `Danh sách Kudo`
>
> Tuy D.1 và D.3 nằm trong node C về mặt Figma, chúng vẫn là các **section logic độc lập** trong implementation.

---

### C.1 — All Kudos Section Header

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9221` (header instance, `componentId: 6885:8015`) |
| **Type** | Section header (display only) |

**Hiển thị:**
- Subtitle: "Sun* Annual Awards 2025" (`I6885:9221;75:1884`)
- Title: "ALL KUDOS" (large, bold)
- `Frame 488` child (`I6885:9221;75:1886`) có thể chứa "View all Kudos ↗" link

---

### C.2 — View All Kudos Link

| Property | Value |
|----------|-------|
| **Type** | Button (`text_link`) |
| **Label** | `"View all Kudos ↗"` (icon ↗ — navigate/external link indicator) |
| **Interaction** | `on_click` → `[iOS] Sun*Kudos_All Kudos` |
| **API Target** | `GET /api/v1/kudos` (full paginated list) |

---

### C.3 — Kudos Post Card (Bài đăng KUDO)

| Property | Value |
|----------|-------|
| **Type** | Card (list item) |
| **Data Source** | `GET /api/v1/kudos?page=N&limit=N&hashtag_id=N&department_id=N` |
| **Filter** | hashtag_id AND department_id |
| **Condition** | `status='active'` AND `deleted_at IS NULL` |

**Card fields:**
- **C.3.1** — Sender info: `users.name`, `users.avatar_url`, `users.department_id`
  - Tap avatar/name → sender profile (TC_ACC_007)
  - Anonymous: sender masked, hiển thị `anonymous_nickname`
- **C.3.2** — Arrow icon (→, display only)
- **C.3.3** — Recipient info: `users.name`, `users.avatar_url`, `users.department_id`, `users.hero_tier`
  - Star badge: 10→1★, 20→2★, 50→3★ (TC_FUN_006)
  - Tap → recipient profile (TC_ACC_007)
- **C.3.4** — Post time: `kudos.created_at` — formatted `"HH:mm - DD/MM/YYYY"`
- **C.3.5** — Content card:
  - Title: `kudos.award_category_name`
  - Message: `kudos.message` — truncate 5 dòng (TC_GUI_003)
  - Hashtag tags: max 5, 1 dòng (TC_GUI_004)
  - Photos: tap ảnh → full image (TC_FUN_029)
  - Actions: ❤️ heart count + 🔗 Copy Link

---

### D.1 — Thống Kê Tổng Quát (mms_D.1_Thống kê tổng quat)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9223` (child của `mms_C_All kudos > content` `6885:9222`) |
| **Type** | Stats dashboard |
| **Data Source** | `GET /api/v1/users/me` (personal stats) |

**Hiển thị:**
- **D.1.2** — Số kudos nhận được: `user_stats.kudos_received` (e.g., 25)
- **D.1.3** — Số kudos đã gửi: `user_stats.kudos_sent` (e.g., 25)
- **D.1.4** — Số tim: `user_stats.hearts_received` (e.g., 25)
- **mms_S_Group 435** (`6885:9239`, `no: "S"`) — GROUP làm container layout cho hàng đầu (D.1.2 + D.1.3 + D.1.4), display only, không có logic riêng
- **D.1.5** — Divider (phân cách ngang)
- **D.1.6** — Secret box đã mở: `user_stats.secret_boxes_opened` (e.g., 25)
- **D.1.7** — Secret box chưa mở: `user_stats.secret_boxes_unopened` (e.g., 25)

---

### D.2 — Open Secret Box CTA

| Property | Value |
|----------|-------|
| **Type** | Button (`icon_text`) |
| **Label** | `"Mở Secret Box 🎁"` |
| **Enabled** | `user_stats.secret_boxes_unopened > 0` |
| **Disabled** | `user_stats.secret_boxes_unopened == 0` (TC_FUN_039) |
| **Interaction** | `on_click` → `[iOS] Open secret box` |

**Logic:**
- API flow:
  1. `GET /api/v1/users/me/secret-boxes/next` → get next box ID
  2. `POST /api/v1/users/me/secret-boxes/{boxId}/open` → mở box, nhận reward
- Double-tap prevention: chỉ trigger 1 lần mở (TC_FUN_025)
- Sau mở: stats cập nhật (opened +1, unopened -1) (TC_FUN_024)

---

### D.3 — Sunner Nhận Quà Gần Nhất (mms_D.3_10 SUNNER nhận quà)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:9255` (child của `mms_C_All kudos > content` `6885:9222`) |
| **Type** | Frame (list section) |
| **Data Source** | `users`, `reward_recipients` tables |

**D.3.1 — Title:** "NHẬN QUÀ MỚI NHẤT" (static label)

**D.3.2 — GiftRecipientRow:**
- Avatar: circular photo (`users.avatar_url`) — fallback default avatar
- Tên: `users.full_name` (e.g., "Huỳnh Dương Xuân")
- Mô tả phần thưởng: `reward_name` (e.g., "Nhận được 1 áo phông SAA")
- `on_click` → Profile người nhận quà

---

## API Endpoints Summary

| Endpoint | Method | Section | Mục đích |
|----------|--------|---------|---------|
| `GET /api/v1/kudos/highlight` | GET | B | Top 5 kudos by heart count, supports filter |
| `GET /api/v1/kudos` | GET | C | Paginated all kudos list, supports filter |
| `GET /api/v1/hashtags` | GET | B.1.1 | Danh sách hashtag cho filter dropdown |
| `GET /api/v1/departments` | GET | B.1.2 | Danh sách phòng ban cho filter dropdown |
| `POST /api/v1/kudos/{id}/like` | POST | B.4.4 / C.3.5 | Toggle heart/like |
| `GET /api/v1/users/me` | GET | D.1 | Thống kê cá nhân |
| `GET /api/v1/users/me/secret-boxes/next` | GET | D.2 | Lấy ID secret box tiếp theo |
| `POST /api/v1/users/me/secret-boxes/{id}/open` | POST | D.2 | Mở secret box |

---

## Supabase Realtime

- Subscribe bảng `kudos` → insert events push kudo mới lên đầu feed (section C)
- No realtime cho Highlight (chỉ load khi filter thay đổi)

---

## State & Loading

| State | Behavior |
|-------|----------|
| Loading | Skeleton placeholder cho Highlight carousel và All Kudos list |
| Empty (no kudos) | Section C: hiển thị empty state "Chưa có Kudos nào" |
| Error | Sealed `Result<T>` — hiển thị lỗi inline, không để lộ exception ra UI |
| Filter active | Label filter button cập nhật, carousel reset về card 1 |
| Secret box = 0 | D.2 button disabled |

---

## Localization

| Key | VI | EN |
|-----|----|----|
| `kudos_screen_subtitle` | "Hệ thống ghi nhận và cảm ơn" | "Recognition and gratitude system" |
| `kudos_write_placeholder` | "Hôm nay, bạn muốn gửi kudos đến ai?" | "Who do you want to send kudos to today?" |
| `kudos_highlight_title` | "HIGHLIGHT KUDOS" | "HIGHLIGHT KUDOS" |
| `kudos_spotlight_title` | "SPOTLIGHT BOARD" | "SPOTLIGHT BOARD" |
| `kudos_all_title` | "ALL KUDOS" | "ALL KUDOS" |
| `kudos_view_all` | "Xem tất cả ↗" | "View all ↗" |
| `kudos_filter_hashtag` | "Hashtag" | "Hashtag" |
| `kudos_filter_department` | "Phòng ban" | "Department" |
| `kudos_open_secret_box` | "Mở Secret Box" | "Open Secret Box" |
| `kudos_latest_gifts_title` | "NHẬN QUÀ MỚI NHẤT" | "LATEST GIFT RECIPIENTS" |
| `kudos_copy_link` | "Copy Link" | "Copy Link" |
| `kudos_view_detail` | "Xem chi tiết" | "View detail" |
| `kudos_post_time_format` | `"HH:mm - dd/MM/yyyy"` | `"HH:mm - MM/dd/yyyy"` |
