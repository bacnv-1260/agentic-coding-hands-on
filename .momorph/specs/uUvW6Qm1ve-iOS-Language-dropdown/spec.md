# Feature Specification: [iOS] Language Dropdown

**Screen ID**: `uUvW6Qm1ve`
**Frame ID**: `6891:15508`
**Frame Name**: `[iOS] Language dropdown`
**File Key**: `9ypp4enmFmdK3YAFJLIu6C`
**Figma Link**: https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/uUvW6Qm1ve
**Screen Image**: ![Language Dropdown](https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/6eb96921-008c-45b6-9c10-6f9a9f5f231a)
**Group**: Authentication
**Created**: 2026-05-05
**Status**: Done

---

## Overview

Màn hình hiển thị **trạng thái dropdown ngôn ngữ đang mở** trên màn hình Login. Khi người dùng click vào Language Selector ở góc trên bên phải màn hình Login, một dropdown nhỏ hiện ra cho phép chọn ngôn ngữ hiển thị.

Đây **không phải là một NavDestination riêng** — nó là một overlay composable (BottomSheet / Popup) được render trực tiếp trên màn hình Login. Sau khi chọn, giao diện cập nhật ngay lập tức không cần reload.

**Ngôn ngữ hỗ trợ (theo Figma):** `VN` (Tiếng Việt) và `EN` (Tiếng Anh)

> ⚠️ **Lưu ý quan trọng**: Figma design chỉ hiển thị 2 options — VN và EN. Option JA (Tiếng Nhật) được định nghĩa trong spec Login nhưng **không xuất hiện trong Figma dropdown này**. Cần xác nhận với PM/Designer về việc có hỗ trợ JA hay không. Hiện tại implementation đang có 3 options (VN/EN/JA).

---

## User Scenarios & Testing

### User Story 1 — Chọn ngôn ngữ từ dropdown (Priority: P1)

Người dùng đang ở màn hình Login muốn đổi ngôn ngữ hiển thị trước khi đăng nhập. Họ click vào Language Selector, dropdown mở ra với danh sách 2 ngôn ngữ, chọn một option, giao diện cập nhật ngay lập tức và dropdown đóng lại.

**Why this priority**: Core use case — không có tính năng này user không thể đổi ngôn ngữ. Ảnh hưởng toàn bộ nội dung text trên màn hình Login.

**Independent Test**: Mở màn hình Login → click Language Selector → kiểm tra dropdown xuất hiện với 2 options → chọn EN → kiểm tra UI chuyển sang English và dropdown đóng.

**Acceptance Scenarios**:

1. **Given** người dùng đang ở màn hình Login với ngôn ngữ hiện tại là VN, **When** click vào Language Selector ở góc trên phải, **Then** dropdown mở ra hiển thị 2 options: VN (cờ Việt Nam) và EN (cờ Anh) — VN ở trên, EN ở dưới; option đang chọn (VN) có nền highlight khác biệt
2. **Given** dropdown đang mở, **When** click vào option EN, **Then** dropdown đóng lại, Language Selector hiển thị "EN" + cờ Anh, toàn bộ text trên màn hình Login chuyển sang Tiếng Anh ngay lập tức (không reload)
3. **Given** dropdown đang mở (ngôn ngữ EN), **When** click vào option VN, **Then** dropdown đóng lại, Language Selector hiển thị "VN" + cờ Việt Nam, toàn bộ text chuyển sang Tiếng Việt ngay lập tức
4. **Given** người dùng mở ứng dụng lần đầu tiên, **When** quan sát Language Selector, **Then** hiển thị "VN" + cờ Việt Nam là giá trị mặc định
5. **Given** dropdown đang mở, **When** click lại vào Language Selector (toggle), **Then** dropdown đóng lại, giá trị đã chọn được giữ nguyên
6. **Given** dropdown đang mở, **When** click ra ngoài vùng dropdown (vào nội dung Login phía dưới), **Then** dropdown đóng lại, giá trị đã chọn được giữ nguyên

---

### User Story 2 — Persist ngôn ngữ sau khi thoát app (Priority: P2)

Sau khi người dùng chọn ngôn ngữ EN, thoát app và mở lại, hệ thống phải nhớ lựa chọn ngôn ngữ trước đó.

**Why this priority**: UX cơ bản — tránh việc user phải chọn lại ngôn ngữ mỗi lần mở app. Liên quan đến `DataStore<Preferences>` persistence.

**Independent Test**: Chọn EN → thoát app → mở lại → kiểm tra Language Selector vẫn hiển thị EN và UI text vẫn là English.

**Acceptance Scenarios**:

1. **Given** người dùng đã chọn EN và thoát app, **When** mở lại ứng dụng, **Then** màn hình Login hiển thị EN và giao diện bằng Tiếng Anh
2. **Given** người dùng chưa từng đổi ngôn ngữ, **When** mở lại ứng dụng, **Then** ngôn ngữ mặc định vẫn là VN

---

### Edge Cases

- **Chọn option đang active**: Click vào option đang được chọn → dropdown đóng, không có thay đổi gì, DataStore không được ghi lại
- **Dropdown vị trí**: Dropdown phải hiển thị trong bounds màn hình, không bị cắt ở cạnh phải (anchor ở góc trên phải)
- **Dismiss khi vuốt xuống** (nếu dùng BottomSheet): Vuốt xuống → đóng dropdown, giữ nguyên ngôn ngữ hiện tại
- **DataStore read failure khi start**: Nếu DataStore không đọc được giá trị đã lưu (I/O error, corruption), hệ thống fallback về `"VN"` và tiếp tục bình thường — không hiển thị error cho user
- **Rapid tap**: User tap nhanh liên tiếp vào 2 option khác nhau → chỉ option được chọn cuối cùng có hiệu lực; không có duplicate state updates

---

## Data Requirements

### Display Fields

| Field | Source | Type | Notes |
|-------|--------|------|-------|
| Flag icon (trigger) | `selectedLanguage` state | Image asset | Cờ tương ứng với ngôn ngữ đang chọn; thay đổi khi chọn option mới |
| Language code (trigger) | `selectedLanguage` state | `String` | Hiển thị mã 2 ký tự: `"VN"` hoặc `"EN"` |
| Flag icon (option A.1) | Static | Image asset | Luôn là cờ Việt Nam |
| Label text (option A.1) | Static | `String` | Luôn là `"VN"` |
| Flag icon (option A.2) | Static | Image asset | Luôn là cờ Anh |
| Label text (option A.2) | Static | `String` | Luôn là `"EN"` |
| Selected state (mỗi option) | So sánh option value vs `selectedLanguage` | `Boolean` | `true` nếu option value == `selectedLanguage` |

### Input Data

| Input | Type | Validation | Produced by |
|-------|------|------------|-------------|
| `selectedLanguage` (new value) | `String` | Phải nằm trong danh sách ngôn ngữ được kích hoạt; nếu không hợp lệ fallback `"VN"` | Click A.1 → `"VN"`, Click A.2 → `"EN"` |

---

## UI/UX Requirements *(từ Figma)*

### Screen Components

| # | Component | Node ID | Type | Mô tả | Interactions |
|---|-----------|---------|------|-------|--------------|
| A | `mms_A_Dropdown-List` | `6891:15595` | Dropdown container | Danh sách các ngôn ngữ có thể chọn; hiển thị như popup overlay trên màn hình Login ở góc trên phải | Click outside → đóng |
| A.1 | `mms_A.1_tiếng Việt` | `6891:15596` | Button (icon_text) | Option chọn Tiếng Việt — cờ VN + label "VN"; **selected state** (nền highlighted) khi đang chọn VN | Click → chọn VN, đóng dropdown |
| A.2 | `mms_A.2_tiếng Anh` | `6891:15597` | Button (icon_text) | Option chọn Tiếng Anh — cờ UK + label "EN"; default state khi VN đang được chọn | Click → chọn EN, đóng dropdown |

### Component States

| Component | State | Trigger | Behavior |
|-----------|-------|---------|----------|
| `mms_A.1_tiếng Việt` | **Selected** | Ngôn ngữ hiện tại = VN | Nền highlight rõ ràng, phân biệt với option không được chọn |
| `mms_A.1_tiếng Việt` | **Default** | Ngôn ngữ hiện tại = EN | Không có nền đặc biệt |
| `mms_A.1_tiếng Việt` | **Pressed** | Đang nhấn giữ | Hiển thị ripple/pressed feedback (Android Material behavior) |
| `mms_A.2_tiếng Anh` | **Selected** | Ngôn ngữ hiện tại = EN | Nền highlight rõ ràng, phân biệt với option không được chọn |
| `mms_A.2_tiếng Anh` | **Default** | Ngôn ngữ hiện tại = VN | Không có nền đặc biệt |
| `mms_A.2_tiếng Anh` | **Pressed** | Đang nhấn giữ | Hiển thị ripple/pressed feedback (Android Material behavior) |

### Navigation Flow

| Từ | Trigger | Đến |
|----|---------|-----|
| [iOS] Login | Click Language Selector (`mms_2.1_language`) | Language dropdown mở ra (overlay) |
| Language dropdown (chọn VN/EN) | Click option | Dropdown đóng → [iOS] Login (ngôn ngữ đã cập nhật) |
| Language dropdown | Click outside / dismiss | Dropdown đóng → [iOS] Login (không thay đổi) |

**Lưu ý**: Đây là overlay, **không** phải NavDestination. Không cần entry trong `NavRoutes.kt`.

### Accessibility Requirements

| Element | Content Description | Behavior khi TalkBack |
|---------|--------------------|-----------------------|
| Language Selector trigger (`mms_2.1_language`) | `"Chọn ngôn ngữ, hiện tại: [VN/EN]"` | Đọc ngôn ngữ đang chọn; thông báo khi dropdown mở/đóng |
| Flag icon trong trigger | Decorative — không cần content description riêng (label đủ) | — |
| Option A.1 (`mms_A.1_tiếng Việt`) | `"Tiếng Việt"` | Đọc tên ngôn ngữ; thông báo `"đang chọn"` nếu là selected state |
| Option A.2 (`mms_A.2_tiếng Anh`) | `"Tiếng Anh"` | Đọc tên ngôn ngữ; thông báo `"đang chọn"` nếu là selected state |
| Flag icon trong option | Decorative — không cần content description riêng | — |

---

## Requirements

### Functional Requirements

- **FR-001**: Hệ thống PHẢI hiển thị dropdown với danh sách các ngôn ngữ được kích hoạt trong build, theo thứ tự VN trên — EN dưới (và JA nếu được kích hoạt). Số lượng options phụ thuộc vào danh sách authoritative do PM/Designer xác nhận
- **FR-002**: Hệ thống PHẢI highlight option đang được chọn với nền khác biệt so với option không được chọn
- **FR-003**: Khi chọn một option, hệ thống PHẢI cập nhật Language Selector (flag + code) và toàn bộ text UI ngay lập tức mà không reload
- **FR-004**: Hệ thống PHẢI đóng dropdown sau khi người dùng chọn một option
- **FR-005**: Hệ thống PHẢI đóng dropdown khi người dùng click ra ngoài vùng dropdown
- **FR-006**: Giá trị ngôn ngữ đã chọn PHẢI được lưu vào `DataStore<Preferences>` (key: `"selected_language"`) để persist qua các lần mở app
- **FR-007**: Giá trị mặc định PHẢI là `"VN"` nếu chưa có lựa chọn được lưu

### Technical Requirements

- **TR-001**: Dropdown KHÔNG được là một NavDestination — phải implement bằng composable overlay (BottomSheet/Popup) trên màn hình Login
- **TR-002**: Ngôn ngữ được chọn phải được phản ánh ngay lập tức qua `ViewModel.uiState.selectedLanguage` (StateFlow)
- **TR-003**: Sử dụng `DataStore<Preferences>` (không dùng `SharedPreferences` thường) để lưu ngôn ngữ — kết hợp với `LanguageRepository` / `LanguagePreferenceDataSource` đã có
- **TR-004**: Không log giá trị ngôn ngữ đã chọn nếu có thể nhận diện người dùng (Timber only, no PII)
- **TR-005**: Validation ngôn ngữ — giá trị phải nằm trong danh sách các ngôn ngữ đã được kích hoạt trong build; nếu giá trị không hợp lệ thì fallback về `"VN"`. **Danh sách authoritative được quyết định bởi PM/Designer** (Figma hiện tại: `VN`, `EN`; implementation có thêm `JA` nhưng cần xác nhận — xem Out of Scope)
- **TR-006**: Nếu `DataStore` read thất bại khi khởi động (I/O error), hệ thống PHẢI fallback về `"VN"` mà không crash; lỗi được log qua Timber (class name only, không log giá trị)

### State Management

| State | Type | Scope | Source of Truth | Persistence |
|-------|------|-------|-----------------|-------------|
| `selectedLanguage` | `String` | Global (toàn app) | `DataStore<Preferences>` key `"selected_language"` | Across app restarts |
| `showLanguageSelector` | `Boolean` | Local (màn hình Login) | `LoginViewModel.uiState` (StateFlow) | Không persist — reset về `false` khi màn hình Login bị destroy |

**Loading state**: Không cần — chọn ngôn ngữ là synchronous write; UI cập nhật optimistically trước khi DataStore write hoàn thành.
**Error state**: Nếu DataStore write thất bại, UI đã hiển thị ngôn ngữ mới (optimistic); retry không cần thiết vì ngôn ngữ sẽ về default ở lần mở app tiếp theo.

### Key Entities

- **`selectedLanguage: String`** — mã ngôn ngữ đang chọn (`"VN"` hoặc `"EN"`), lưu trong `DataStore` key `"selected_language"`
- **`showLanguageSelector: Boolean`** — trạng thái mở/đóng dropdown, quản lý trong `LoginViewModel.uiState`

---

## API Dependencies

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| Không có | — | Ngôn ngữ được quản lý hoàn toàn client-side qua DataStore | — |

---

## Success Criteria

- **SC-001**: Dropdown mở và đóng đúng cách; click outside đóng dropdown
- **SC-002**: Chọn EN → toàn bộ string resource trên Login screen chuyển sang English ngay lập tức (verified bằng `values-en/strings.xml`)
- **SC-003**: Sau khi thoát/mở lại app, ngôn ngữ đã chọn được khôi phục đúng
- **SC-004**: Option đang active có visual state rõ ràng khác với option không active

---

## Out of Scope

- Hỗ trợ ngôn ngữ JA (Tiếng Nhật) — chưa có trong Figma dropdown (blocked: localization team)
- Animation transition khi dropdown mở/đóng (không có spec từ Figma)
- Header language dropdown trên các màn hình khác ngoài Login (scope riêng)

---

## Dependencies

- [x] Screen flow documented (`.momorph/contexts/SCREENFLOW.md`)
- [x] `LanguageRepository` interface tồn tại (`domain/repository/LanguageRepository.kt`)
- [x] `LanguagePreferenceDataSource` tồn tại với `DataStore<Preferences>`
- [x] `LanguageSelectorBottomSheet.kt` composable đã implement (cần review để align với spec này)
- [x] `values/strings.xml`, `values-en/strings.xml` đã có
- [ ] JA localization strings — blocked (Localization Team)
- [ ] Xác nhận với PM/Designer: có hỗ trợ JA trong dropdown không?

---

## Notes

- Figma frame `[iOS] Language dropdown` là **visual snapshot** của màn hình Login khi dropdown đang mở — nền vẫn là keyvisual BG + Login content, chỉ thêm dropdown container overlay
- Dropdown container (`mms_A_Dropdown-List`) anchor tương ứng với vị trí Language Selector trong header (right-aligned) — implementer fetch CSS on-demand từ `query_section`
- Trong implementation Android hiện tại, `LanguageSelectorBottomSheet` dùng `ModalBottomSheet` — phù hợp về UX nhưng khác layout so với Figma (Figma dùng popup nhỏ góc trên phải, không phải full-width bottom sheet). Cần team quyết định có giữ BottomSheet hay đổi sang `DropdownMenu`

### Test Cases Reference (11 TCs từ MoMorph)

| TC ID | Area | Mô tả |
|-------|------|-------|
| TC_LANGDD_ACC_001 | Access | Dropdown accessible cho mọi user trên Login screen |
| TC_LANGDD_GUI_001 | GUI | Layout và structure của dropdown khi mở |
| TC_LANGDD_GUI_002 | GUI | Giá trị mặc định là VN |
| TC_LANGDD_FUN_001 | Function | Open dropdown |
| TC_LANGDD_FUN_002 | Function | Close dropdown (click lại) |
| TC_LANGDD_FUN_003 | Function | Chọn VN từ state EN |
| TC_LANGDD_FUN_004 | Function | Chọn EN từ state VN |
| TC_LANGDD_FUN_005 | Function | State transition VN inactive → active |
| TC_LANGDD_FUN_006 | Function | State transition EN inactive → active |
| TC_LANGDD_FUN_007 | Function | UI chuyển sang Tiếng Việt toàn màn hình |
| TC_LANGDD_FUN_008 | Function | UI chuyển sang Tiếng Anh toàn màn hình |
| TC_LANGDD_FUN_009 | Function | Validate chỉ có đúng 2 options (VN, EN) |
| TC_LANGDD_FUN_010 | Function | Thứ tự hiển thị: VN trước, EN sau |
