# Screen: [iOS] Profile bản thân

## Screen Info

| Property | Value |
|----------|-------|
| **Screen ID** | `hSH7L8doXB` |
| **Figma Node ID** | `6885:10333` |
| **Figma Link** | https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/hSH7L8doXB |
| **Screen Group** | Main |
| **Spec Status** | ✅ reviewed |
| **Platform** | iOS / Android |
| **Discovered At** | 2026-05-06 |
| **Last Updated** | 2026-05-06 |

---

## Description

Màn hình Profile cá nhân của người dùng đang đăng nhập. Hiển thị: avatar và thông tin danh tính (tên, mã nhân viên, danh hiệu), bộ sưu tập icon/huy hiệu, bảng thống kê hoạt động (Kudos nhận/gửi, tim, Secret Box), và danh sách Kudos có thể lọc theo trạng thái (Đã gửi / Đã nhận). Tab Profile được hiển thị là active trên Bottom Navigation Bar.

---

## Navigation Analysis

### Incoming Navigations (From)

| Source Screen | Trigger | Condition |
|---------------|---------|-----------|
| Tất cả màn hình | Tab bar "Profile" | Always |
| [iOS] Home | Tab bar "Profile" | Always |
| [iOS] Sun*Kudos | Tab bar "Profile" | Always |
| [iOS] Award | Tab bar "Profile" | Always |

### Outgoing Navigations (To)

| Target Screen | Trigger Element | Node ID | Confidence | Notes |
|---------------|-----------------|---------|------------|-------|
| Notification List | Header — Bell icon | `I6885:10338;88:1830` | high | Xem danh sách thông báo |
| Search screen | Header — Search icon | `I6885:10338;88:1869` | high | Tìm kiếm Sunner |
| Language Overlay | Header — VN flag | `I6885:10338;88:1829` | high | Bottom sheet chọn ngôn ngữ |
| Secret Box screen/modal | "Mở Secret Box 🎁" button | `6885:10386` | high | Chỉ enabled khi secret_boxes_unopened > 0 |
| Kudos Detail screen | Kudos card — "Xem chi tiết ↗" | various | high | Xem chi tiết 1 Kudos |
| [iOS] Home | Bottom Nav — SAA 2025 | `6885:10394` | high | Tab 1 |
| [iOS] Award | Bottom Nav — Awards | `6885:10394` | high | Tab 2 |
| [iOS] Sun*Kudos | Bottom Nav — Kudos | `6885:10394` | high | Tab 3 |

### Navigation Rules
- **Back behavior**: Tab bar — không có stack back trên màn hình này
- **Deep link**: `saa://profile` → mở màn hình này
- **Auth required**: Có — redirect về Login nếu token hết hạn (401)
- **Profile data**: Load từ authenticated session (không cần userId param)

---

## Component Schema

### Layout Structure

```
┌──────────────────────────────────────────┐
│  Header [6885:10338] (fixed)             │
│  [StatusBar] [Logo] [VN▼] [🔍] [🔔]    │
│  (overlay gradient: #00101A → transparent│
├──────────────────────────────────────────┤
│  mm_media_bg [6885:10334] (background)   │
│  Keyvisual BG + Shadow Left + Shadow Bot │
├──────────────────────────────────────────┤
│  1.1 — Profile Info [6885:10339]         │
│    ┌──────────────────────────┐          │
│    │  Avatar ⬤ 72×72dp       │          │
│    │  [6885:10340]            │          │
│    ├──────────────────────────┤          │
│    │  A.2 Name Frame          │          │
│    │  [6885:10341]            │          │
│    │  "Huỳnh Dương Xuân Nhật" │  ← gold │
│    │  [CEVC3 ⬤ Legend Hero]  │  ← white│
│    └──────────────────────────┘          │
├──────────────────────────────────────────┤
│  2 — Icon Collection [6885:10349]        │
│    [🔵][🔵][🔵][🔵][🔵][🔵]            │
│    "Bộ sưu tập icon của tôi"             │
├──────────────────────────────────────────┤
│  3 — Stats Container [6885:10358]        │
│  ┌──────────────────────────────────┐   │
│  │ Số Kudos bạn nhận được:      5   │   │
│  │ Số Kudos bạn đã gửi:        25   │   │
│  │ Số tim bạn nhận được:       25   │   │
│  │ ─────────────────────────────    │   │
│  │ Số Secret Box bạn đã mở:    25   │   │
│  │ Số Secret Box chưa mở:      25   │   │
│  │                                  │   │
│  │  [ 🎁 Mở Secret Box ────────── ] │   │
│  └──────────────────────────────────┘   │
├──────────────────────────────────────────┤
│  4 — Kudos Section Header [6885:10387]   │
│    "Sun* Annual Awards 2025"             │
│    ——— KUDOS ———                         │
├──────────────────────────────────────────┤
│  4.1 — Filter Dropdown [6885:10388]      │
│    [ Đã gửi (5)  ▾ ]                    │
│  A — Dropdown Overlay [6891:17101]       │  ← when open
│    ┌──────────────────┐                  │
│    │  [ Đã nhận (5) ] │                  │
│    │  [ Đã gửi (5)  ] │                  │
│    └──────────────────┘                  │
├──────────────────────────────────────────┤
│  5 — Kudos List [6885:10389]             │
│    ┌──────────────────────────────────┐  │
│    │  5.1 KudosHighlightCard          │  │
│    │  [Sender ──→ Recipient]          │  │
│    │  [Timestamp] [Category]          │  │
│    │  [Content text...]               │  │
│    │  [Hashtags] [❤ 1.000] [Copy][↗] │  │
│    └──────────────────────────────────┘  │
│    ┌──────────────────────────────────┐  │
│    │  5.2 KudosHighlightCard          │  │
│    └──────────────────────────────────┘  │
│    ┌──────────────────────────────────┐  │
│    │  5.3 KudosHighlightCard          │  │
│    └──────────────────────────────────┘  │
├──────────────────────────────────────────┤
│  Bottom NavBar [6885:10394] (fixed)      │
│  [SAA 2025] [Awards] [Kudos] [Profile*]  │
└──────────────────────────────────────────┘
```

---

## Screen Layout Map

| # | Component | Node ID | Y-position | Description |
|---|-----------|---------|-----------|-------------|
| 1 | App Header | `6885:10338` | 0–104 | Status bar + logo + lang + search + bell |
| bg | Background | `6885:10334` | — | Keyvisual bg group (Shadow Left/Bottom) |
| 1.1 | Profile Info | `6885:10339` | 144–288 | Avatar + name + team code + badge |
| 2 | Icon Collection | `6885:10349` | 312–372 | Row of earned icon badges + label |
| 3 | Stats Container | `6885:10358` | 396–632 | Stats grid + "Mở Secret Box" button |
| 4 | Kudos Section Header | `6885:10387` | 672–725 | "Sun* Annual Awards 2025" / "KUDOS" |
| 4.1 | Filter Dropdown | `6885:10388` | 741–781 | Active filter pill button |
| A | Dropdown Overlay | `6891:17101` | 788–880 | Overlay (Đã nhận / Đã gửi) — shown on tap |
| 5 | Kudos Card List | `6885:10389` | 805–1745 | Scrollable list of Kudos highlight cards |
| 6 | Bottom Nav Bar | `6885:10394` | 1769–1841 | Fixed bottom nav (Profile active) |

---

## User Scenarios & Testing

### User Story 1 — Xem thông tin profile cá nhân (Priority: P1)

Người dùng muốn kiểm tra tên, mã nhân viên, danh hiệu và bộ sưu tập huy hiệu của mình sau khi đăng nhập.

**Why this priority**: Đây là nội dung first-fold của màn hình Profile — người dùng thấy ngay khi vào. Data phải được load từ authenticated session.

**Independent Test**: Mở Profile screen → xác nhận avatar, tên đầy đủ (gold), mã nhân viên (white), badge chip "Legend Hero" hiển thị → xác nhận bộ sưu tập hiển thị 6 huy hiệu tròn.

**Acceptance Scenarios**:

1. **Given** người dùng đã đăng nhập, **When** mở Profile screen, **Then** hiển thị: avatar tròn, tên đầy đủ (Montserrat Bold 18sp, màu gold #FFEA9E), mã nhân viên (Montserrat Regular 14sp, white), badge chip (ví dụ "Legend Hero")
2. **Given** người dùng có dữ liệu huy hiệu, **When** màn hình load, **Then** section "Bộ sưu tập icon của tôi" hiển thị ≥1 icon badge tròn tối màu trong một hàng ngang; label bên dưới hiển thị "Bộ sưu tập icon của tôi"
3. **Given** người dùng không có huy hiệu nào, **When** màn hình load, **Then** icon collection row hiển thị empty state (placeholder slot hoặc ẩn row)
4. **Given** avatar không tải được, **When** render, **Then** fallback placeholder tròn hiển thị thay thế
5. **Given** tên quá dài, **When** render, **Then** text bị truncate với ellipsis (1 dòng)

---

### User Story 2 — Xem thống kê hoạt động (Priority: P1)

Người dùng muốn kiểm tra nhanh số Kudos đã gửi/nhận, số tim nhận được, và số Secret Box còn lại.

**Why this priority**: Stats là KPI cá nhân quan trọng; Secret Box count trực tiếp ảnh hưởng đến enabled/disabled state của CTA button.

**Independent Test**: Mở Profile → cuộn đến Stats Container → xác nhận 5 dòng số liệu hiển thị đúng với label white + value gold; xác nhận đường phân cách nằm giữa nhóm Kudos/Tim và nhóm Secret Box.

**Acceptance Scenarios**:

1. **Given** API trả về stats, **When** Stats Container hiển thị, **Then** 5 stat rows hiển thị đúng thứ tự: (1) Số Kudos bạn nhận được, (2) Số Kudos bạn đã gửi, (3) Số tim bạn nhận được — **đường phân cách** — (4) Số Secret Box bạn đã mở, (5) Số Secret Box chưa mở
2. **Given** mỗi stat row, **When** render, **Then** label (Montserrat Light 300, 14sp, white, align-right tới giữa) và value số (Montserrat Bold 700, 14sp, gold #FFEA9E, align-right) được hiển thị trên cùng một hàng ngang
3. **Given** Stats Container, **When** render, **Then** container có: border `0.794px solid #998C5F`, background `#00070C`, border-radius `8px`, padding `12px`, gap `8px`
4. **Given** người dùng có ≥1 Secret Box chưa mở, **When** render, **Then** button "Mở Secret Box 🎁" active với background `#FFEA9E`, label `#00101A`, có icon bên phải
5. **Given** người dùng có 0 Secret Box chưa mở, **When** render, **Then** button "Mở Secret Box" bị disabled (opacity thấp hơn hoặc màu grayed-out)
6. **Given** người dùng tap "Mở Secret Box", **When** action, **Then** điều hướng đến Secret Box screen hoặc mở Secret Box modal
7. **Given** API chưa trả về data, **When** render, **Then** loading skeleton hiển thị trong container

---

### User Story 3 — Xem và lọc danh sách Kudos (Priority: P1)

Người dùng muốn xem lại các Kudos mình đã gửi hoặc đã nhận. Figma chỉ hiển thị 2 filter options trong dropdown (Đã nhận / Đã gửi); item `D.3.1` label "Spam" tồn tại trong design nhưng **không nằm trong dropdown overlay** — đây là status label riêng trên Kudos card.

**Why this priority**: Kudos list là phần nội dung chính chiếm phần lớn chiều dài màn hình. Filter dropdown là entry point duy nhất để phân loại.

**Independent Test**: Mở Profile → cuộn đến Kudos section → xác nhận header "KUDOS" hiển thị gold 22sp → xác nhận dropdown "Đã gửi (5)" có icon caret → tap dropdown → xác nhận overlay với **2 options** (Đã nhận / Đã gửi) mở → chọn "Đã nhận (5)" → xác nhận list cập nhật và overlay đóng.

**Acceptance Scenarios**:

1. **Given** màn hình Profile load xong, **When** cuộn đến Kudos section, **Then** hiển thị: sub-label "Sun* Annual Awards 2025" (Montserrat Regular 12sp, white), section title "KUDOS" (Montserrat Medium 500, 22sp, gold #FFEA9E)
2. **Given** filter mặc định là "Đã gửi", **When** render, **Then** dropdown pill hiển thị "Đã gửi (5)" với icon caret; pill có border `#998C5F`, background `rgba(255,234,158,0.10)`, border-radius `4px`
3. **Given** người dùng tap dropdown pill, **When** overlay mở, **Then** dropdown list hiện ra với đúng **2 items**: "Đã nhận (5)" và "Đã gửi (5)"; overlay có background `#00070C`, border `1px solid #998C5F`, border-radius `8px`, width `118dp`
4. **Given** overlay đang mở, **When** người dùng chọn 1 option, **Then** filter cập nhật, overlay đóng, danh sách Kudos reload với filter mới; dropdown pill text cập nhật theo lựa chọn
5. **Given** overlay đang mở, **When** người dùng tap ngoài overlay, **Then** overlay đóng không thay đổi filter
6. **Given** filter được chọn, **When** API trả về kết quả rỗng, **Then** empty state "Chưa có Kudos" hiển thị trong vùng danh sách

---

### User Story 4 — Xem chi tiết từng Kudos card (Priority: P1)

Người dùng muốn đọc nội dung một Kudos cụ thể — người gửi, người nhận, lời cảm ơn, hashtag, ảnh đính kèm.

**Why this priority**: Kudos card là đơn vị nội dung cốt lõi của màn hình. Mỗi card phải hiển thị đầy đủ thông tin và có thể tương tác.

**Independent Test**: Cuộn đến bất kỳ Kudos card nào → xác nhận hiển thị: avatar người gửi (trái) + mũi tên + avatar người nhận (phải); tên + mã đội + badge bên dưới mỗi avatar; timestamp; award category; nội dung lời cảm ơn; ảnh đính kèm (nếu có); hashtag; số tim; nút Copy Link; nút "Xem chi tiết" → tap "Xem chi tiết" → xác nhận mở Kudos Detail screen.

**Acceptance Scenarios**:

1. **Given** một Kudos card hiển thị, **When** render, **Then** header card gồm: avatar người gửi (tròn, trái), icon mũi tên giữa, avatar người nhận (tròn, phải), tên người gửi (10sp, Montserrat Regular, tối `#00101A`) + mã nhân viên (10sp, Medium, #999), badge danh hiệu; tương tự cho người nhận
2. **Given** Kudos card body, **When** render, **Then** hiển thị: timestamp (HH:mm - dd/MM/yyyy), award category label (14sp Bold, `#00101A`), nội dung lời cảm ơn (truncated nếu dài), ảnh đính kèm tối đa 4 ảnh dạng grid 2×2 (nếu có), chuỗi hashtag màu **đỏ `#D4271D`** (giống KudosHighlightCard trên Kudos screen — không phải gold)
3. **Given** Kudos card footer, **When** render, **Then** hiển thị: số tim ❤️ (e.g. 1.000), nút "Copy Link" với icon chain, nút "Xem chi tiết ↗" với icon external link
4. **Given** người dùng tap "Xem chi tiết", **When** action, **Then** điều hướng đến Kudos Detail screen của Kudos đó
5. **Given** Kudos card style, **When** render, **Then** card có: background `#FFF8E1` (light yellow tinted), border `1px solid #FFEA9E`, border-radius `8px`, padding `8px 12px`
6. **Given** ảnh đính kèm không tải được, **When** render, **Then** placeholder ảnh xám hiển thị giữ chỗ

---

### User Story 5 — Điều hướng qua Bottom Navigation (Priority: P1)

Người dùng muốn chuyển đổi giữa các tab chính từ màn hình Profile.

**Why this priority**: Bottom nav là entry point toàn cục — tab Profile phải được hiển thị active.

**Independent Test**: Mở Profile → xác nhận tab "Profile" (icon person) có trạng thái active/highlighted → tap "SAA 2025" → xác nhận chuyển về Home → tap "Awards" → xác nhận chuyển đến Awards Detail.

**Acceptance Scenarios**:

1. **Given** người dùng ở Profile screen, **When** Bottom Nav render, **Then** tab 4 (Profile, icon person) có trạng thái active; 3 tab còn lại inactive
2. **Given** người dùng tap tab "SAA 2025", **When** action, **Then** điều hướng về Home screen
3. **Given** người dùng tap tab "Awards", **When** action, **Then** điều hướng đến Awards Detail screen
4. **Given** người dùng tap tab "Kudos", **When** action, **Then** điều hướng đến Kudos Feed screen
5. **Given** Bottom Nav render, **When** kiểm tra, **Then** 4 tab lần lượt: SAA 2025 (home icon), Awards (trophy icon), Kudos (handshake icon), Profile (person icon)

---

## Design Items Detail

### 1 — App Header (`mms_1_header`, Node `6885:10338`)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:10338` |
| **Type** | INSTANCE (component `6885:8165`) |
| **Interaction** | Search → Search screen; Bell → Notification list; VN → Language overlay |

Tái sử dụng `HomeHeader` composable. Profile tab đang active nên header không có back button.

---

### 1.1 — Profile Info (`mms_1.1_member`, Node `6885:10339`)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:10339` |
| **Type** | FRAME |
| **Children** | Avatar (`6885:10340`), Name Frame (`6885:10341`) |
| **Interaction** | Avatar tap: behavior TBD (có thể open full-screen preview) |

**Logic**: Luôn hiển thị data của authenticated user. Không nhận userId từ navigation param.

---

### 2 — Icon Collection (`mms_2_icon collection`, Node `6885:10349`)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:10349` |
| **Type** | FRAME |
| **Children** | Icon list row (`6885:10350`) × 6 slots, Label (`6885:10357`) |
| **Interaction** | Badge tap: TBD (open icon detail/tooltip) |

**Logic**: Chỉ hiển thị icon badges đã được earn. Empty → ẩn row hoặc hiển thị empty state.

---

### 3 — Stats Container (`mms_D.1_Thống kê tổng quat`, Node `6885:10358`)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:10358` |
| **Type** | FRAME |
| **Background** | `#00070C` (`var(--Details-Container-2)`) |
| **Border** | `0.794px solid #998C5F` |
| **Children** | 5 stat rows + Divider `6885:10375` + "Mở Secret Box" button `6885:10386` |

**Button "Mở Secret Box"** (Node `6885:10386`):
- Enabled: `secret_boxes_unopened > 0`; bg `#FFEA9E`, label `#00101A`
- Disabled: `secret_boxes_unopened = 0`; opacity reduced

---

### 4 — Kudos Section Header (`mms_4_header`, Node `6885:10387`)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:10387` |
| **Type** | INSTANCE (component `6885:8015`) |
| **Children** | Sub-label "Sun* Annual Awards 2025" (`I6885:10387;75:1884`), Divider rect, Title "KUDOS" (`I6885:10387;75:1887`) |

---

### 4.1 — Filter Dropdown (`mms_dropdown`, Node `6885:10388`)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:10388` |
| **Type** | INSTANCE (component `6885:8322`) |
| **Default state** | "Đã gửi (5)" |
| **Interaction** | Tap → open Dropdown Overlay `6891:17101` |

---

### A — Dropdown Overlay (`mms_A_Dropdown-List`, Node `6891:17101`)

| Property | Value |
|----------|-------|
| **Node ID** | `6891:17101` |
| **Type** | FRAME (overlay — z-order top) |
| **Options** | `6891:17102` "Đã nhận (5)" + `6891:17103` "Đã gửi (5)" |
| **Dismiss** | Tap outside → close without change |

> ⚠️ **Figma chỉ có 2 options** (Đã nhận / Đã gửi). `D.3.1` "Spam" là status tag trên card, không phải filter option trong overlay này.

---

### 5 — Kudos List (`mms_5_kudos list`, Node `6885:10389`)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:10389` |
| **Type** | FRAME |
| **Children** | `6885:10390` / `6885:10391` / `6885:10392` — 3 KudosHighlightCard instances |
| **Scroll** | Vertical, infinite scroll |
| **Filter** | Controlled by dropdown 4.1 |

Tái sử dụng `KudosHighlightCard` composable đã có.

---

### 6 — Bottom Navigation Bar (`mms_6_nav bar`, Node `6885:10394`)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:10394` |
| **Type** | INSTANCE (component `6885:8121`) |
| **Active tab** | Profile (index 3) |

Tái sử dụng `HomeNavBar` composable.

---

## Requirements

### Functional Requirements

- **FR-001**: System MUST hiển thị avatar, tên, mã nhân viên, badge danh hiệu của authenticated user
- **FR-002**: System MUST hiển thị bộ sưu tập icon badge đã earned của user
- **FR-003**: System MUST tính và hiển thị 5 stats: Kudos nhận, Kudos gửi, Tim nhận, Secret Box đã mở, Secret Box chưa mở
- **FR-004**: Button "Mở Secret Box" MUST chỉ enabled khi `secret_boxes_unopened > 0`
- **FR-005**: Users MUST có thể filter danh sách Kudos theo "Đã nhận" / "Đã gửi" qua dropdown overlay — đúng 2 options
- **FR-006**: Hashtag text trong KudosHighlightCard MUST hiển thị màu đỏ `#D4271D` (không phải gold)
- **FR-007**: Filter dropdown overlay MUST dismiss khi tap outside mà không thay đổi filter
- **FR-008**: Tab Profile trong Bottom Nav MUST hiển thị active state (Profile screen active)

### Technical Requirements

- **TR-001**: Tất cả profile data load từ authenticated Supabase session — không truyền userId qua navigation param
- **TR-002**: Kudos list filter `sender_id = me` / `recipient_id = me` tùy option
- **TR-003**: Stats có thể được aggregate từ một Supabase RPC function thay vì nhiều queries riêng
- **TR-004**: `KudosHighlightCard` tái sử dụng từ `presentation/ui/kudos/components/KudosHighlightCard.kt`
- **TR-005**: `HomeHeader` tái sử dụng từ `presentation/ui/home/components/HomeHeader.kt`
- **TR-006**: `HomeNavBar` tái sử dụng từ `presentation/ui/home/components/HomeNavBar.kt`

---

## Edge Cases

- **Avatar không tải được**: Hiển thị circular placeholder (không crash)
- **Tên quá dài**: Truncate với ellipsis, maxLines = 1
- **0 icon badges**: Ẩn icon collection row hoặc hiển thị empty placeholder row
- **Stats = 0**: Hiển thị "0" — không ẩn row
- **Secret Box chưa mở = 0**: Button "Mở Secret Box" disabled, không navigates
- **Kudos list rỗng**: Empty state "Chưa có Kudos" (per filter hiện tại)
- **API lỗi**: Loading skeleton → error state với Retry
- **Kudos card ảnh không tải**: Placeholder xám giữ chỗ

---

## Success Criteria

- **SC-001**: Profile info (avatar + tên + badge) render < 500ms sau khi màn hình mở
- **SC-002**: Stats container hiển thị đúng 5 rows với giá trị thực từ API
- **SC-003**: Filter dropdown: chọn option → list cập nhật trong < 1s
- **SC-004**: KudosHighlightCard hiển thị hashtag màu đỏ `#D4271D`
- **SC-005**: Bottom Nav tab Profile có active state visible

---

## Design Item Index

| ID (Figma) | No | Type | Name (EN) | Kind |
|------------|-----|------|-----------|------|
| `6885:10338` | 1 | INSTANCE | App Header / Status Bar | navigation |
| `6885:10339` | 1.1 | FRAME | Profile Info Container | info_block |
| `6885:10341` | A.2 | FRAME | User Name & Badge | label |
| `6885:10349` | 2 | FRAME | Icon Collection Section | info_block |
| `6885:10351` | B2 | INSTANCE | Icon Badge Slot | list_item |
| `6885:10357` | A.3 | TEXT | Icon Collection Label | label |
| `6885:10358` | 3 | FRAME | Stats Container | info_block |
| `6885:10360` | D.1.2 | FRAME | Kudos Received Stat Row | label |
| `6885:10365` | D.1.3 | FRAME | Kudos Sent Stat Row | label |
| `6885:10370` | D.1.4 | FRAME | Hearts Received Stat Row | label |
| `6885:10375` | D.1.5 | RECTANGLE | Content Divider | info_block |
| `6885:10376` | D.1.6 | FRAME | Secret Box Opened Stat Row | label |
| `6885:10381` | D.1.7 | FRAME | Secret Box Unopened Stat Row | label |
| `6885:10386` | 12 | INSTANCE | Open Secret Box Button | button |
| `6885:10387` | 4 | INSTANCE | Kudos Section Header | navigation |
| `6885:10388` | 4.1 | INSTANCE | Kudos Filter Dropdown | dropdown |
| `6891:17101` | A | FRAME | Kudos Filter Dropdown Overlay | popup_dialog |
| `6885:10389` | 5 | FRAME | Kudos List Container | list_item |
| `6885:10390` | 5.1 | INSTANCE | Kudos Highlight Card 1 | list_item |
| `6885:10391` | 5.2 | INSTANCE | Kudos Highlight Card 2 | list_item |
| `6885:10392` | 5.3 | INSTANCE | Kudos Highlight Card 3 | list_item |
| `6885:10394` | 6 | INSTANCE | Bottom Navigation Bar | navigation |

---

## Data Dependencies

| Data Field | Source | API / Table |
|------------|--------|-------------|
| User full name | Authenticated session / profile API | `profiles.full_name` |
| Employee code | Authenticated session / profile API | `profiles.employee_code` |
| Achievement badge | Authenticated session / profile API | `profiles.hero_tier` |
| Avatar URL | Authenticated session / profile API | `profiles.avatar_url` |
| Icon badge collection | Profile API | `user_badges` table |
| Kudos received count | Stats API | `kudos WHERE recipient_id = me` |
| Kudos sent count | Stats API | `kudos WHERE sender_id = me` |
| Hearts received count | Stats API | `kudos_likes WHERE recipient via kudos` |
| Secret Box opened count | Stats API | `secret_boxes WHERE opened = true` |
| Secret Box unopened count | Stats API | `secret_boxes WHERE opened = false` |
| Kudos list (filtered) | Kudos API | `kudos WHERE sender_id = me OR recipient_id = me` |

---

## Navigation Map

| From | Action | Destination |
|------|--------|-------------|
| Header — Bell icon | tap | Notification List screen |
| Header — Search icon | tap | Search screen |
| Header — VN flag | tap | Language Switcher overlay |
| Stats — "Mở Secret Box" button | tap | Secret Box screen / modal |
| Kudos card — "Xem chi tiết" | tap | Kudos Detail screen |
| Kudos card — "Copy Link" | tap | Copy deep link to clipboard (toast) |
| Bottom Nav — SAA 2025 | tap | Home screen |
| Bottom Nav — Awards | tap | Awards Detail screen |
| Bottom Nav — Kudos | tap | Kudos Feed screen |
| Bottom Nav — Profile | tap | (current — no-op or scroll to top) |
