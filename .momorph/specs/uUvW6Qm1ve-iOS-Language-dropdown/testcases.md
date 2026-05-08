# Test Cases: [iOS] Language Dropdown

## Screen Info

| Property | Value |
|----------|-------|
| **Screen ID** | `uUvW6Qm1ve` |
| **Figma File Key** | `9ypp4enmFmdK3YAFJLIu6C` |
| **Figma Node ID** | `6891:15508` |
| **Figma Link** | https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/uUvW6Qm1ve |
| **Spec File** | `.momorph/specs/uUvW6Qm1ve-iOS-Language-dropdown/spec.md` |
| **Last Updated** | 2026-05-05 |

---

## Design Items Covered

| # | Item Name | Node ID | Type | Purpose |
|---|-----------|---------|------|---------|
| A | `mms_A_Dropdown-List` | `6891:15595` | Dropdown | Language dropdown container — opens/closes on click; VN default |
| A.1 | `mms_A.1_tiếng Việt` | `6891:15596` | Button (icon_text) | Vietnamese option — flag VN + label "VN"; selected state highlight |
| A.2 | `mms_A.2_tiếng Anh` | `6891:15597` | Button (icon_text) | English option — flag UK + label "EN"; default state when VN selected |

---

## Test Case Summary

| Category | Count |
|----------|-------|
| User Interface (GUI) | 5 |
| Function — Open / Close Dropdown | 4 |
| Function — Select Language | 5 |
| Function — Selected State & Visual Feedback | 3 |
| Function — Persist & Restore | 2 |
| Edge Cases | 3 |
| **Total** | **22** |

---

## Test Cases

### User Interface (GUI)

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_LANG_GUI_001 | User interface | GUI | Check layout | Dropdown trigger | Position and appearance | Màn hình Login đang hiển thị, ngôn ngữ = VN | 1. Quan sát khu vực góc trên phải của màn hình Login<br>2. Kiểm tra trigger Language Selector | Trigger hiển thị đúng vị trí góc trên phải; có cờ quốc gia + mã ngôn ngữ 2 ký tự + icon mũi tên; kích thước 90×32dp, bo góc 4dp | High |
| TC_LANG_GUI_002 | User interface | GUI | Initialize | Dropdown trigger | Default value | Mở ứng dụng lần đầu, chưa từng chọn ngôn ngữ | 1. Mở ứng dụng<br>2. Quan sát Language Selector trigger | Trigger hiển thị cờ Việt Nam + mã `"VN"` — đây là giá trị mặc định (spec FR-007) | High |
| TC_LANG_GUI_003 | User interface | GUI | Check layout | Dropdown list | Layout khi mở | Màn hình Login đang hiển thị | 1. Tap vào Language Selector trigger<br>2. Quan sát dropdown list hiển thị | Dropdown list xuất hiện dạng popup nhỏ anchor tại góc trên phải — **không** chiếm toàn màn hình; hiển thị đúng 2 options theo thứ tự: VN ở trên, EN ở dưới | High |
| TC_LANG_GUI_004 | User interface | GUI | Check layout | Dropdown list | Không bị cắt ở cạnh màn hình | Màn hình Login đang hiển thị | 1. Tap vào Language Selector trigger<br>2. Kiểm tra vị trí dropdown trên các thiết bị có màn hình nhỏ (360dp width) | Toàn bộ dropdown list nằm trong bounds màn hình, không bị cắt ở cạnh phải | Medium |
| TC_LANG_GUI_005 | User interface | GUI | Check display | Option item | Flag icon + label | Dropdown đang mở | 1. Quan sát từng option trong dropdown list | A.1: cờ Việt Nam (🇻🇳) + label `"VN"` màu trắng; A.2: cờ Anh (🇬🇧) + label `"EN"` màu trắng — đúng với Figma item descriptions | Medium |

---

### Function — Open / Close Dropdown

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_LANG_FUN_001 | Normal_Others | FUNCTION | Check component interaction | Dropdown (A) | Open — tap trigger | Màn hình Login, dropdown đang đóng | 1. Tap vào Language Selector trigger (90×32dp) | Dropdown list mở ra, hiển thị 2 options VN + EN; đây là overlay trên màn hình Login, **không** navigate sang màn hình mới (spec TR-001) | High |
| TC_LANG_FUN_002 | Normal_Others | FUNCTION | Check component interaction | Dropdown (A) | Close — tap outside | Dropdown đang mở | 1. Tap vào vùng nội dung Login bên ngoài dropdown list | Dropdown đóng lại; ngôn ngữ không thay đổi so với trước khi mở; trigger hiển thị giá trị cũ | High |
| TC_LANG_FUN_003 | Normal_Others | FUNCTION | Check component interaction | Dropdown (A) | Close — back button (Android) | Dropdown đang mở | 1. Nhấn nút Back của Android | Dropdown đóng lại; ngôn ngữ không thay đổi; người dùng ở lại màn hình Login | Medium |
| TC_LANG_FUN_004 | Normal_Others | FUNCTION | Check component interaction | Dropdown (A) | Toggle — tap trigger khi đang mở | Dropdown đang mở | 1. Tap lại vào Language Selector trigger | Dropdown đóng lại; giá trị đã chọn được giữ nguyên (spec US1 AC5) | Medium |

---

### Function — Select Language

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_LANG_FUN_005 | Normal_Others | FUNCTION | Check component interaction | Option A.1 (VN) | Select VN khi đang ở EN | Ngôn ngữ hiện tại = EN, dropdown đang đóng | 1. Tap Language Selector trigger<br>2. Tap vào option A.1 (VN) | Dropdown đóng ngay lập tức; trigger cập nhật thành cờ VN + `"VN"`; toàn bộ text màn hình Login re-render sang Tiếng Việt mà không reload app (spec FR-003) | High |
| TC_LANG_FUN_006 | Normal_Others | FUNCTION | Check component interaction | Option A.2 (EN) | Select EN khi đang ở VN | Ngôn ngữ hiện tại = VN, dropdown đang đóng | 1. Tap Language Selector trigger<br>2. Tap vào option A.2 (EN) | Dropdown đóng ngay lập tức; trigger cập nhật thành cờ EN + `"EN"`; toàn bộ text màn hình Login re-render sang Tiếng Anh mà không reload app (spec FR-003) | High |
| TC_LANG_FUN_007 | Normal_Others | FUNCTION | Check cross-component effect | Language change | UI text re-render VN→EN | Ngôn ngữ = VN, màn hình Login đang hiển thị | 1. Chọn EN từ dropdown<br>2. Quan sát Description Text và Copyright trên màn hình Login | Description Text: `"Start your journey with SAA 2025. Log in to explore!"`<br>Copyright: `"Copyright belongs to Sun* © 2025"`<br>Thay đổi ngay lập tức, không cần restart | High |
| TC_LANG_FUN_008 | Normal_Others | FUNCTION | Check cross-component effect | Language change | UI text re-render EN→VN | Ngôn ngữ = EN, màn hình Login đang hiển thị | 1. Chọn VN từ dropdown<br>2. Quan sát Description Text và Copyright trên màn hình Login | Description Text: `"Bắt đầu hành trình của bạn cùng SAA 2025. Đăng nhập để khám phá!"`<br>Copyright: `"Bản quyền thuộc về Sun* © 2025"`<br>Thay đổi ngay lập tức, không cần restart | High |
| TC_LANG_FUN_009 | Normal_Others | FUNCTION | Check business logic | Option list | Số lượng options | Màn hình Login, dropdown đang mở | 1. Tap Language Selector trigger<br>2. Đếm số option trong dropdown list | Dropdown list hiển thị đúng **2 options** (VN + EN) — không có option JA, không có option nào khác ngoài danh sách Figma (spec FR-001) | High |

---

### Function — Selected State & Visual Feedback

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_LANG_FUN_010 | Normal_Others | FUNCTION | Check component state | Option A.1 (VN) | Selected state khi VN active | Ngôn ngữ hiện tại = VN, dropdown đang mở | 1. Tap Language Selector trigger<br>2. Quan sát trạng thái hiển thị của A.1 (VN) so với A.2 (EN) | A.1 (VN) có nền highlight rõ ràng phân biệt với A.2 (EN); visual state phản ánh option đang được chọn (spec FR-002, Figma A.1 "selected có nền khác") | High |
| TC_LANG_FUN_011 | Normal_Others | FUNCTION | Check component state | Option A.2 (EN) | Selected state khi EN active | Ngôn ngữ hiện tại = EN, dropdown đang mở | 1. Tap Language Selector trigger<br>2. Quan sát trạng thái hiển thị của A.2 (EN) so với A.1 (VN) | A.2 (EN) có nền highlight rõ ràng phân biệt với A.1 (VN); A.1 ở default state không có nền đặc biệt (spec FR-002, Figma A.2 "selected có nền khác") | High |
| TC_LANG_FUN_012 | Normal_Others | FUNCTION | Check component state | Option item | Pressed / ripple feedback | Dropdown đang mở | 1. Nhấn giữ (long press) lên một option<br>2. Quan sát phản hồi thị giác | Hiển thị ripple effect / pressed feedback theo Material3 behavior; feedback xảy ra ngay khi chạm, không bị delay | Low |

---

### Function — Persist & Restore

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_LANG_FUN_013 | Normal_Others | FUNCTION | Check business logic | DataStore persistence | Lưu ngôn ngữ sau khi chọn | Ngôn ngữ = VN | 1. Chọn EN từ dropdown<br>2. Xác nhận UI hiển thị tiếng Anh<br>3. Đóng ứng dụng (không force-kill)<br>4. Mở lại ứng dụng<br>5. Quan sát Language Selector và nội dung | Language Selector hiển thị `"EN"` + cờ Anh; Description Text và Copyright vẫn bằng tiếng Anh; ngôn ngữ được phục hồi từ `DataStore<Preferences>` (spec FR-006, US2 AC1) | High |
| TC_LANG_FUN_014 | Normal_Others | FUNCTION | Check business logic | DataStore persistence | Default VN khi chưa lưu | Cài đặt mới, chưa từng chọn ngôn ngữ | 1. Cài app lần đầu / clear app data<br>2. Mở ứng dụng<br>3. Quan sát Language Selector | Language Selector hiển thị `"VN"` + cờ Việt Nam; giao diện bằng tiếng Việt; `DataStore` trả về default `"VN"` (spec FR-007, US2 AC2) | Medium |

---

### Edge Cases

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_LANG_EDG_001 | Abnormal_Others | FUNCTION | Check edge case | Option A | Chọn lại option đang active | Ngôn ngữ hiện tại = VN, dropdown đang mở | 1. Tap Language Selector trigger<br>2. Tap vào option A.1 (VN) — option đang được chọn | Dropdown đóng lại; ngôn ngữ không thay đổi; `DataStore` **không** được ghi lại (không có duplicate write) | Medium |
| TC_LANG_EDG_002 | Abnormal_Others | FUNCTION | Check edge case | Dropdown (A) | Rapid tap — 2 options liên tiếp | Dropdown đang mở | 1. Tap nhanh vào A.1 (VN) rồi ngay lập tức A.2 (EN) (< 100ms) | Chỉ lần chọn cuối cùng (EN) có hiệu lực; trigger hiển thị EN; `DataStore` chỉ ghi một lần với giá trị EN (spec Edge Case: Rapid tap) | Medium |
| TC_LANG_EDG_003 | Abnormal_Others | FUNCTION | Check edge case | DataStore | I/O error khi đọc ngôn ngữ đã lưu | Simulate DataStore corruption / I/O error | 1. Simulate lỗi I/O trên DataStore khi app khởi động<br>2. Quan sát Language Selector và giao diện | App không crash; Language Selector hiển thị `"VN"` (fallback mặc định); không hiển thị error message cho user; lỗi được log qua Timber (spec TR-006, Edge Case: DataStore read failure) | High |

---

## Notes

### Test Viewpoints Source
- `mcp_testviewpoint_get_test_viewpoints(screen="Select_box", item_type="Check default value")` — default value & value list
- `mcp_testviewpoint_get_test_viewpoints(screen="Select_box", item_type="Select value from list")` — selection interaction
- `mcp_testviewpoint_get_test_viewpoints(screen="Select_box", item_type="Event in Select box")` — dismiss events
- `mcp_testviewpoint_get_test_viewpoints(screen="Dialog_popup", item_type="Check open a dialog/popup")` — open trigger
- `mcp_testviewpoint_get_test_viewpoints(screen="Dialog_popup", item_type="Check layout/content/action of dialog/popup")` — layout & close actions

### Out of Scope
- JA (Japanese) option: Figma confirms only VN + EN (design items A.1 + A.2 only)
- Keyboard/hardware navigation: Android soft keyboard không áp dụng cho dropdown này
- Multi-select: đây là single-select dropdown
