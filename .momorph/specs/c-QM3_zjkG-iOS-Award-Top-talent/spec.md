# Feature Specification: [iOS] Award_Top Talent

**Screen ID**: `c-QM3_zjkG`
**Frame ID**: `6885:10259`
**Frame Name**: `[iOS] Award_Top talent`
**File Key**: `9ypp4enmFmdK3YAFJLIu6C`
**Figma Link**: https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/c-QM3_zjkG
**Group**: Awards
**Created**: 2026-05-06
**Status**: Done

---

## Overview

Màn hình chi tiết giải thưởng Awards — hiển thị thông tin đầy đủ về một hạng mục giải thưởng SAA 2025. Người dùng có thể xem hình ảnh biểu tượng, mô tả tiêu chí, số lượng và giá trị giải thưởng, đồng thời tìm hiểu về phong trào Sun\*Kudos. Điều hướng giữa các hạng mục qua dropdown phía trên.

---

## User Scenarios & Testing

### User Story 1 — Xem chi tiết hạng mục giải thưởng Top Talent (Priority: P1)

Người dùng bấm "Chi tiết" từ award card trên Home, hoặc chọn tab "Awards" ở bottom nav, để xem đầy đủ thông tin về giải thưởng Top Talent.

**Why this priority**: Đây là nội dung core của màn hình — tiêu chí, số lượng và giá trị giải thưởng phải hiển thị chính xác từ API.

**Independent Test**: Từ Home, bấm "Chi tiết" trên award card "Best Innovation" → xác nhận màn hình Award Detail mở với tên giải đúng, mô tả, số lượng và giá trị giải thưởng hiển thị.

**Acceptance Scenarios**:

1. **Given** người dùng điều hướng tới Award Detail, **When** màn hình load, **Then** hiển thị: hình ảnh huy hiệu giải thưởng (graphic tròn), icon + tên giải (ví dụ: "Top Talent"), đoạn mô tả tiêu chí, section "Số lượng giải thưởng" với giá trị + đơn vị (ví dụ: "10 Cá nhân" cho Top Talent), section "Giá trị giải thưởng" với số tiền + ghi chú (ví dụ: "7.000.000 VNĐ" cho Top Talent)
   > ⚠️ Dữ liệu thực tế thay đổi theo hạng mục giải — khung Figma hiện tại đang render "Top Project" (02 Tập thể, 15.000.000 VNĐ) làm ví dụ — tất cả giá trị lấy từ API, không hardcode
2. **Given** dữ liệu giải thưởng đang tải, **When** màn hình render, **Then** loading state hiển thị (skeleton hoặc spinner)
3. **Given** API trả lỗi, **When** load thất bại, **Then** error state hiển thị với nút retry
4. **Given** `image_url` của giải thưởng là null, **When** render hình ảnh, **Then** placeholder/fallback hiển thị thay thế

---

### User Story 2 — Chuyển đổi hạng mục giải qua dropdown (Priority: P1)

Người dùng muốn xem thông tin của giải thưởng khác mà không cần quay về Home. Họ bấm dropdown "Top Talent" phía trên, chọn hạng mục khác.

**Why this priority**: Dropdown là navigation chính giữa 6 hạng mục giải thưởng — không có nó user phải quay lại Home để mỗi lần xem giải khác.

**Independent Test**: Trên Award Detail → bấm dropdown "Top Talent" → xác nhận hiện danh sách các hạng mục → chọn một hạng mục khác → xác nhận nội dung màn hình cập nhật đúng.

**Acceptance Scenarios**:

1. **Given** người dùng đang ở Award Detail, **When** bấm dropdown, **Then** danh sách tất cả hạng mục giải thưởng (từ API) hiển thị
2. **Given** dropdown đang mở, **When** chọn một hạng mục, **Then** màn hình cập nhật toàn bộ nội dung (hình ảnh, tên, mô tả, số lượng, giá trị) của hạng mục được chọn
3. **Given** dropdown đang mở, **When** bấm ra ngoài hoặc bấm lại dropdown, **Then** dropdown đóng lại, lựa chọn hiện tại không thay đổi
4. **Given** hạng mục đang xem, **When** render dropdown trigger, **Then** tên hạng mục đang chọn hiển thị trong nút dropdown

---

### User Story 3 — Tìm hiểu phong trào Sun\*Kudos (Priority: P2)

Người dùng cuộn xuống dưới phần thông tin giải thưởng, đọc giới thiệu về Sun\*Kudos và bấm "Chi tiết" để xem thêm.

**Why this priority**: Sun\*Kudos là điểm mới của SAA 2025 — block này đóng vai trò cross-promotion từ Awards sang Kudos.

**Independent Test**: Cuộn xuống cuối Award Detail → xác nhận Kudos block hiển thị với ảnh banner, title "Sun\* Kudos", mô tả điểm mới SAA 2025, nút "Chi tiết" → bấm "Chi tiết" → xác nhận điều hướng đến Kudos screen.

**Acceptance Scenarios**:

1. **Given** người dùng cuộn xuống, **When** đến Kudos block, **Then** hiển thị: label "Phong trào ghi nhận", tiêu đề "Sun\* Kudos", ảnh banner Kudos, tiêu đề phụ "ĐIỂM MỚI CỦA SAA 2025", đoạn mô tả, nút "Chi tiết"
2. **Given** người dùng bấm nút "Chi tiết" trong Kudos block, **When** action, **Then** điều hướng đến màn hình Sun\*Kudos (`fO0Kt19sZZ`)
3. **Given** ảnh banner Kudos không tải được, **When** render, **Then** placeholder hiển thị

---

### User Story 4 — Điều hướng qua Header và Bottom Nav (Priority: P1)

Người dùng muốn điều hướng đến các màn hình khác (Kudos, Home, Notifications) từ màn hình Award Detail.

**Acceptance Scenarios**:

1. **Given** người dùng bấm tab "Kudos" ở Bottom Nav, **When** action, **Then** điều hướng đến Sun\*Kudos screen
2. **Given** người dùng bấm tab "SAA 2025" ở Bottom Nav, **When** action, **Then** điều hướng về Home screen
3. **Given** người dùng bấm icon chuông ở Header, **When** action, **Then** điều hướng đến Notifications screen
4. **Given** người dùng bấm icon ngôn ngữ ở Header, **When** action, **Then** dropdown chọn ngôn ngữ hiển thị inline
5. **Given** tab "Awards" đang active, **When** render Bottom Nav, **Then** tab "Awards" hiển thị highlight màu vàng

---

## Design Items

| No | ID | Type | Name | Mô tả |
|----|----|------|------|-------|
| 1 | `6885:10264` | INSTANCE | mms_1_header | Header cố định: SAA logo (góc trái) + cụm action (ngôn ngữ/tìm kiếm/chuông) góc phải |
| 1.1 | `I6885:10264;88:1827` | INSTANCE | mms_1.1_mm_media_logo | Logo "Sun\* Annual Awards 2025" — không clickable |
| 1.2 | `I6885:10264;88:1828` | FRAME | mms_1.2_actions | Cụm action: nút ngôn ngữ + tìm kiếm + chuông (với badge đỏ khi có thông báo mới) |
| 2 | `6885:10265` | FRAME | mms_2_content | Container cuộn dọc toàn bộ nội dung chính |
| 2.1 | `6885:10266` | FRAME | mms_A_KV Kudos | Banner Kudos đầu trang: text "Hệ thống ghi nhận và cảm ơn" + logo KUDOS |
| 2.2 | `6885:10283` | FRAME | mms_B_Highlight | Khối tiêu đề giải thưởng + dropdown chọn hạng mục |
| B.1 | `6885:10284` | FRAME | mms_B.1_header | Header section: "Sun\* Annual Awards 2025" + "Hệ thống giải thưởng SAA 2025" + dropdown |
| 2.3 | `6885:10292` | FRAME | mms_2.3_award | Khối thông tin chi tiết giải thưởng (hình ảnh + tên + mô tả + số lượng + giá trị) |
| C2.1.2 | `6885:10297` | TEXT | mms_C2.1.2_Top Talent | Tiêu đề giải thưởng "Top Talent" (kèm icon) |
| C2.1.3 | `6885:10298` | TEXT | mms_C2.1.3_Vinh danh top cá nhân xuất sắc trên mọi phương diện | Đoạn văn mô tả tiêu chí giải thưởng |
| C2.1.2 | `6885:10303` | TEXT | mms_C2.1.2_quantity_label | Label "Số lượng giải thưởng" (kèm icon huy chương) |
| C2.1.3 | `6885:10305` | TEXT | mms_C2.1.3_quantity_value | Giá trị "10 Cá nhân" |
| C2.1.3 | `6885:10306` | TEXT | mms_C2.1.3_value_label | Label "Giá trị giải thưởng" (kèm icon) |
| C2.1.2 | `6885:10311` | TEXT | mms_C2.1.2_amount | Giá trị "7.000.000 VNĐ" + ghi chú "cho mỗi giải thưởng" |
| C2.1.3 | `6885:10313` | TEXT | mms_C2.1.3_award_image | Hình ảnh huy hiệu tròn giải thưởng (graphic) — ảnh thay đổi theo giải được chọn |
| C2.1.3 | `6885:10314` | TEXT | mms_C2.1.3_award_image_alt | Hình ảnh huy hiệu (trạng thái thay thế) — dùng khi `image_url` không tải được |
| 2.4 | `6885:10315` | FRAME | mms_2.4_kudos | Khối Sun\*Kudos: label + tiêu đề + ảnh + mô tả + nút "Chi tiết" |
| 3 | `6885:10332` | INSTANCE | mms_3_nav bar | Bottom Navigation Bar cố định — 4 tab (SAA 2025 / Awards\[active\] / Kudos / Profile) |

---

## Business Rules

| ID | Rule |
|----|------|
| BR-001 | Dữ liệu giải thưởng (tên, mô tả, số lượng, giá trị, hình ảnh) được lấy từ API `GET /awards/:id` |
| BR-002 | Dropdown chứa danh sách các hạng mục từ API `GET /awards` — không hardcode |
| BR-003 | Tab "Awards" trên Bottom Nav ở trạng thái active khi đang ở màn hình này |
| BR-004 | Nếu `image_url` null → hiển thị placeholder icon thay thế |
| BR-005 | Nút "Chi tiết" trong Kudos block điều hướng tới màn hình Sun\*Kudos — screen ID chưa xác nhận trong Figma (`linkedFrameId` để trống); cần xác nhận với designer trước khi code |
| BR-006 | Header cố định (sticky) ở đầu màn hình khi cuộn |
| BR-007 | Bottom Nav cố định (sticky) ở cuối màn hình khi cuộn |

---

## Navigation

| Trigger | Action | Destination |
|---------|--------|-------------|
| Tab "SAA 2025" ở Bottom Nav | `navigate` | `[iOS] Home` (`OuH1BUTYT0`) |
| Tab "Kudos" ở Bottom Nav | `navigate` | `[iOS] Sun*Kudos` (`fO0Kt19sZZ`) |
| Tab "Profile" ở Bottom Nav | `navigate` | `[iOS] Profile bản thân` (`hSH7L8doXB`) |
| Bấm icon chuông ở Header | `navigate` | `[iOS] Notifications` (`_b68CBWKl5`) |
| Bấm nút ngôn ngữ ở Header | `show inline dropdown` | Language dropdown (inline, không navigate) |
| Bấm nút tìm kiếm ở Header | `navigate` | Search screen |
| Bấm nút "Chi tiết" trong Kudos block | `navigate` | `[iOS] Sun*Kudos` (`fO0Kt19sZZ`) |
| Chọn hạng mục từ dropdown | `update state` | Cập nhật nội dung màn hình hiện tại |

---

## API Endpoints

| Endpoint | Method | Mục đích |
|----------|--------|---------|
| `GET /awards` | GET | Lấy danh sách hạng mục cho dropdown |
| `GET /awards/:id` | GET | Lấy chi tiết giải thưởng đang xem |
