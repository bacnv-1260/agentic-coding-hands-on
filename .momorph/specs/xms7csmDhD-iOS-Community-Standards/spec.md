# Screen: [iOS] Sun*Kudos_Tiêu chuẩn cộng đồng

## Screen Info

| Property | Value |
|----------|-------|
| **Screen ID** | `xms7csmDhD` |
| **Figma Node ID** | `TBD` *(⚠️ Figma export rate-limited at spec creation time — verify node ID before implementation)* |
| **Figma Link** | https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/xms7csmDhD |
| **Screen Group** | Kudos |
| **Spec Status** | spec |
| **Platform** | iOS / Android |
| **Discovered At** | 2026-05-08 |
| **Last Updated** | 2026-05-08 |

---

## Description

Màn hình thông tin tĩnh hiển thị tiêu chuẩn cộng đồng (community standards) dành cho tính năng Sun*Kudos. Được mở từ nút **"Tiêu chuẩn cộng đồng"** (Figma node `6885:9303`) nằm trong **FormattingToolbar** (C section — phần tử cuối cùng bên phải của toolbar) trên màn hình Viết Kudo. Nội dung giải thích quy tắc viết Kudos đúng cách, ví dụ về danh hiệu, hashtag hợp lệ và nội dung bị cấm. Đây là màn hình thuần UI, không có API call.

---

## User Scenarios & Testing *(mandatory)*

### User Story 1 — Xem tiêu chuẩn cộng đồng từ màn hình Viết Kudo (Priority: P1)

Khi người dùng đang soạn Kudo và muốn hiểu rõ hơn về quy tắc đặt danh hiệu, họ nhấn link "Tiêu chuẩn cộng đồng" và màn hình này mở ra với đầy đủ nội dung hướng dẫn.

**Why this priority**: Đây là lý do duy nhất màn hình này tồn tại — cung cấp ngữ cảnh giúp người dùng điền form Viết Kudo đúng cách.

**Independent Test**: Từ màn hình Viết Kudo, nhấn link "Tiêu chuẩn cộng đồng" → màn hình này phải hiển thị với đầy đủ các section nội dung.

**Acceptance Scenarios**:

1. **Given** người dùng đang ở màn hình Viết Kudo, **When** họ nhấn nút "Tiêu chuẩn cộng đồng" trong **FormattingToolbar** (C section — phần tử cuối cùng bên phải của toolbar), **Then** ứng dụng điều hướng đến màn hình `Tiêu chuẩn cộng đồng` và hiển thị đầy đủ tiêu đề, các section nội dung hướng dẫn.
2. **Given** màn hình Tiêu chuẩn cộng đồng đang hiển thị, **When** nội dung dài hơn viewport, **Then** người dùng có thể cuộn xuống để xem toàn bộ nội dung.
3. **Given** màn hình đang hiển thị, **When** người dùng nhấn nút Back (arrow trái trên header), **Then** ứng dụng điều hướng trở lại màn hình Viết Kudo và form giữ nguyên trạng thái đã nhập.

---

### User Story 2 — Quay lại Viết Kudo sau khi đọc (Priority: P1)

Sau khi đọc xong tiêu chuẩn, người dùng quay lại form Viết Kudo để tiếp tục điền thông tin.

**Why this priority**: Đây là luồng back duy nhất, cần hoạt động chính xác để tránh mất dữ liệu form.

**Independent Test**: Từ màn hình này, nhấn Back → quay lại Viết Kudo với form giữ nguyên nội dung đã điền trước đó.

**Acceptance Scenarios**:

1. **Given** màn hình Tiêu chuẩn cộng đồng đang hiển thị, **When** người dùng nhấn back arrow trên header, **Then** ứng dụng pop màn hình này và quay lại Viết Kudo — form data không bị mất.
2. **Given** màn hình Tiêu chuẩn cộng đồng đang hiển thị, **When** người dùng nhấn nút Back vật lý của hệ thống, **Then** hành vi giống back arrow trên header.

---

### User Story 3 — Nội dung đa ngôn ngữ (Priority: P2)

Toàn bộ tiêu đề màn hình, tên section và nội dung hướng dẫn phải hiển thị đúng ngôn ngữ được chọn (VN / EN).

**Why this priority**: Localization là yêu cầu toàn app; màn hình thông tin không phải ngoại lệ.

**Independent Test**: Chuyển ngôn ngữ sang EN → mở Tiêu chuẩn cộng đồng → tất cả text phải hiển thị tiếng Anh.

**Acceptance Scenarios**:

1. **Given** ngôn ngữ ứng dụng là "VN", **When** màn hình Tiêu chuẩn cộng đồng hiển thị, **Then** tiêu đề và nội dung hiển thị bằng tiếng Việt.
2. **Given** ngôn ngữ ứng dụng là "EN", **When** màn hình Tiêu chuẩn cộng đồng hiển thị, **Then** tiêu đề và nội dung hiển thị bằng tiếng Anh.

---

## Navigation Analysis

### Incoming Navigations (From)

| Source Screen | Trigger | Condition |
|---------------|---------|-----------|
| [iOS] Sun*Kudos_Viết Kudo_default | "Tiêu chuẩn cộng đồng" text — rightmost button inside **FormattingToolbar** (C section, `FormattingToolbar.kt` `onCommunityStandardsClick`). Figma Node `6885:9303` corresponds to this element. In code it is rendered as the last item of the toolbar row, **not** as a standalone link below B.4. | Always (bất kể trạng thái form) |

### Outgoing Navigations (To)

| Target Screen | Trigger Element | Node ID | Confidence | Notes |
|---------------|-----------------|---------|------------|-------|
| [iOS] Sun*Kudos_Viết Kudo_default | Back arrow trên header / System Back | Header back_arrow | high | Pop màn hình hiện tại; form data được preserve |

### Navigation Rules
- **Back behavior**: Pop màn hình; trả về WriteKudo với form state đã giữ bởi ViewModel
- **Deep link**: Không có deep link trực tiếp
- **Auth required**: Có (kế thừa session từ màn hình gọi tới)
- **Modal vs. Full-screen**: Full-screen push navigation (không phải bottom sheet hay dialog)
- **Bottom navigation bar**: Không hiển thị — màn hình này không có `BottomNavBar` (giống WriteKudo, thuần content screen)

---

## Component Schema

### Layout Structure

```
┌──────────────────────────────────────┐
│  Status Bar (system)                 │
├──────────────────────────────────────┤
│  Header                              │
│  ← back_arrow   "Tiêu chuẩn cộng    │
│                  đồng"  (centered)   │
│  ─────── HorizontalDivider ───────── │
├──────────────────────────────────────┤
│  Column + verticalScroll             │
│  ┌──────────────────────────────────┐│
│  │  Section 1: Kudos là gì?        ││  title + body text
│  │  ─────────────────────          ││
│  │  Section 2: Cách đặt danh hiệu  ││  title + body + examples
│  │  ─────────────────────          ││
│  │  Section 3: Hashtag hợp lệ      ││  title + body + examples
│  │  ─────────────────────          ││
│  │  Section 4: Nội dung bị cấm     ││  title + bullet list
│  │  ─────────────────────          ││
│  │  Section 5: Ghi nhận ẩn danh    ││  title + body
│  └──────────────────────────────────┘│
└──────────────────────────────────────┘
```

### Component Hierarchy

```
CommunityStandardsScreen
├── mms_Header (Organism)
│   ├── back_arrow (IconButton / Atom)
│   ├── Title "Tiêu chuẩn cộng đồng" (Text / Atom)
│   └── HorizontalDivider (Atom)
└── Column + verticalScroll (Organism)
    ├── SectionBlock × N (Molecule)
    │   ├── SectionTitle (Text / Atom)
    │   ├── SectionDivider (Atom)          ← optional between sections
    │   └── SectionBody (Text / Atom)
    └── BottomSpacer (Atom)                ← content bottom padding (no bottom nav bar on this screen)
```

### Main Components

| Component | Type | Node ID | Description | Reusable |
|-----------|------|---------|-------------|----------|
| `mms_Header` | Organism | TBD | Header với back arrow + title + divider | Yes (shared across detail screens) |
| `SectionBlock` | Molecule | TBD | Khối nội dung gồm title + body text | No (screen-specific content) |
| `Column + verticalScroll` | Organism | TBD | Container cuộn dọc chứa toàn bộ sections (static fixed-count content) | — |

---

## UI/UX Requirements *(from Figma)*

### Screen Components

| No. | Node ID | Component Name | Type | Description | Interactions |
|-----|---------|----------------|------|-------------|--------------|
| 1 | TBD | `mms_Header` | info_block | Header: back_arrow (trái), tiêu đề "Tiêu chuẩn cộng đồng" (centered), HorizontalDivider phía dưới | back_arrow → pop back to WriteKudo |
| 2 | TBD | `mms_Content_ScrollView` | FRAME (scroll container) | Scrollable content container holding all section blocks (Android: `Column + verticalScroll`) | Vertical scroll |
| 3 | TBD | `mms_Section_1` | FRAME (content block) | Section 1: Mô tả mục đích của Kudos | None |
| 4 | TBD | `mms_Section_2` | FRAME (content block) | Section 2: Hướng dẫn đặt danh hiệu — tiêu chí và ví dụ | None |
| 5 | TBD | `mms_Section_3` | FRAME (content block) | Section 3: Cách sử dụng hashtag hợp lệ | None |
| 6 | TBD | `mms_Section_4` | FRAME (content block) | Section 4: Các nội dung bị cấm (prohibited content) | None |
| 7 | TBD | `mms_Section_5` | FRAME (content block) | Section 5: Quy tắc gửi ẩn danh | None |

> ⚠️ **Node IDs marked TBD**: Figma export bị rate-limit tại thời điểm tạo spec. Tất cả node ID cần được xác minh từ Figma trước khi implement.

### Navigation Flow

- **From**: `NavRoutes.WriteKudo` → navigate to `NavRoutes.CommunityStandards`
- **To**: `NavRoutes.WriteKudo` (back pop — không navigate forward từ màn hình này)
- **Back stack**: `CommunityStandards` được push lên stack trên WriteKudo; back pop trả về WriteKudo với ViewModel state nguyên vẹn

### Visual Requirements

- Responsive: Portrait only
- Scroll: Full vertical scroll trên toàn bộ nội dung
- Animations: Standard push/pop navigation transition
- Accessibility: back_arrow phải có `contentDescription`; tất cả text phải đủ contrast ratio

---

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Hệ thống PHẢI điều hướng đến màn hình Tiêu chuẩn cộng đồng khi người dùng nhấn nút "Tiêu chuẩn cộng đồng" trong **FormattingToolbar** (C section) trên màn hình Viết Kudo — `onCommunityStandardsClick` trong `FormattingToolbar.kt`.
- **FR-002**: Người dùng PHẢI có thể cuộn để xem toàn bộ nội dung khi nội dung vượt quá chiều cao viewport.
- **FR-003**: Nhấn back arrow trên header PHẢI pop màn hình và quay lại Viết Kudo.
- **FR-004**: Nhấn System Back PHẢI có hành vi tương đương back arrow.
- **FR-005**: Form data trên màn hình Viết Kudo PHẢI được preserved khi quay lại (ViewModel giữ state).
- **FR-006**: Tất cả text nội dung PHẢI hỗ trợ localization (VN / EN) qua `stringResource`.

### Technical Requirements

- **TR-001**: Màn hình là `Composable` thuần UI — không có ViewModel riêng, không gọi API.
- **TR-002**: Sử dụng `navController.navigateUp()` để quay lại — không dùng `navigate()` sang WriteKudo (tránh tạo instance mới của WriteKudo trong stack). Dùng `navigateUp()` để đồng nhất với codebase hiện tại (`SaaNavHost.kt` L198: `onNavigateBack = { navController.navigateUp() }`).
- **TR-003**: Nội dung văn bản được lưu trong string resources (`strings.xml` / `strings-en.xml`), không hard-code trong Composable.
- **TR-004**: `Column + verticalScroll` không có header collapsing — `TopAppBar` luôn fixed ở trên cùng; scroll chỉ áp dụng cho content bên dưới header.

### Key Entities

- Không có entity mới — màn hình chỉ hiển thị static content từ string resources.

---

## API Dependencies

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| _(none)_ | — | Màn hình không gọi API — thuần static content | — |

---

## State Management

| State | Scope | Description |
|-------|-------|-------------|
| Scroll position | Local UI state | Scroll position không cần persist — reset về đầu mỗi lần mở |
| Language | `AppViewModel.selectedLanguage` (app-level) | Điều khiển locale → `stringResource()` render đúng ngôn ngữ |

- **Local state**: Chỉ scroll offset (volatile, không cần save/restore).
- **ViewModel state**: Không có ViewModel riêng — WriteKudo ViewModel giữ form state, không bị ảnh hưởng.

---

## Success Criteria *(mandatory)*

- **SC-001**: Màn hình hiển thị đầy đủ tiêu đề và tất cả sections nội dung community standards. ⚠️ **Blocker**: Số section và nội dung chính xác phải được xác minh từ Figma frame trước khi viết acceptance test (Figma API rate-limited tại thời điểm tạo spec).
- **SC-002**: Scroll hoạt động mượt mà; người dùng có thể xem toàn bộ nội dung.
- **SC-003**: Nhấn Back (header hoặc system) luôn quay lại Viết Kudo — form data không bị mất.
- **SC-004**: Nội dung hiển thị đúng ngôn ngữ đang được chọn (VN / EN).
- **SC-005**: Không có API call khi màn hình được hiển thị.

---

## Out of Scope

- Khả năng chỉnh sửa nội dung community standards từ app.
- Hyperlink tới trang ngoài (external browser).
- Animation đặc biệt cho từng section.
- Rich text / HTML rendering ngoài plain text.

---

## Dependencies

> ⚠️ **Blockers — must resolve before implementation starts:**
> 1. **All Figma Node IDs are TBD** — re-run `momorph_downloadFigmaImage` for node `xms7csmDhD` once rate-limit clears; fill in all `TBD` values in this spec and design-style.md.
> 2. **Figma text layer content unknown** — exact section titles, body copy, and section count must be verified before writing string resources.

- `NavRoutes.CommunityStandards` — **missing** from `NavRoutes.kt`. Add: `data object CommunityStandards : NavRoutes("community_standards")`
- `SaaNavHost.kt` — add `composable(NavRoutes.CommunityStandards.route) { CommunityStandardsScreen(onNavigateBack = { navController.navigateUp() }) }` inside the `NavHost` block.
- `WriteKudoScreen.kt` L218 — wire the TODO: `onCommunityStandardsClick = { navController.navigate(NavRoutes.CommunityStandards.route) }`
- String resources: add keys `community_standards_title`, `community_standards_s{N}_title`, `community_standards_s{N}_body` (use `sN` prefix consistently — see design-style.md §9) to `values/strings.xml` and `values-en/strings.xml`.
- Không có drawable mới cần thêm (màn hình không dùng hình minh họa).

---

## Related Screens

| Screen | Screen ID | Relationship |
|--------|-----------|--------------|
| [iOS] Sun*Kudos_Viết Kudo_default | `7fFAb-K35a` | Entry point — điều hướng đến màn hình này từ nút trong `FormattingToolbar` (`onCommunityStandardsClick`) |
| [iOS] Thể lệ | `zIuFaHAid4` | Màn hình thông tin tĩnh tương tự — có thể chia sẻ layout pattern |
