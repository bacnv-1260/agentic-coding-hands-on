# Test Cases: [iOS] Sun*Kudos

## Screen Info

| Property | Value |
|----------|-------|
| **Screen ID** | `fO0Kt19sZZ` |
| **Figma File Key** | `9ypp4enmFmdK3YAFJLIu6C` |
| **Figma Node ID** | `6885:9059` |
| **Figma Link** | https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/fO0Kt19sZZ |
| **Spec File** | `.momorph/contexts/screen_specs/ios_sun_kudos.md` |
| **Last Updated** | 2026-05-06 (initial test cases generated) |

---

## Design Items Covered

| # | Item Name | Node ID | Type | Purpose |
|---|-----------|---------|------|---------|
| 1 | `mms_A_KV Kudos` | `6885:9066` | Frame (banner) | Key visual banner — static display |
| 2 | `mms_A.1_Button ghi nhận` | `6885:9083` / `6891:21267` | Button (icon_text) | Write Kudos CTA (default + sticky) |
| 3 | `mms_B_Highlight` | `6885:9084` | Frame (section) | Highlight Kudos section container |
| 4 | `mms_B.1_header` | `6885:9085` | Frame | Section header + filter row |
| 5 | `mms_B.1.1_Hashtag` | N/A | Button (dropdown) | Hashtag filter — bottom sheet |
| 6 | `mms_B.1.2_Phòng ban` | N/A | Button (dropdown) | Department filter — bottom sheet |
| 7 | `mms_B.2_HIGHLIGHT KUDOS` | `6885:9090` | Frame (carousel) | Carousel container — up to 5 cards |
| 8 | `mms_B.2.1_‹ prev` | `6885:9094` | Button (icon, overlay) | Carousel prev nav |
| 9 | `mms_B.2.2_› next` | `6885:9096` | Button (icon, overlay) | Carousel next nav |
| 10 | `mms_B.3_KUDO - Highlight` | `6885:9091–9265` | Card | Highlight kudo card |
| 11 | `mms_B.3.1_Avatar gửi` | Various | CircularImage | Sender avatar |
| 12 | `mms_B.3.2_Info gửi/nhận` | Various | Label group | Sender/Recipient name + badge |
| 13 | `mms_B.3.5_Avatar nhận` | Various | CircularImage | Recipient avatar |
| 14 | `mms_B.3.6_Info nhận` | Various | Label group | Recipient name + star badge |
| 15 | `mms_B.4.2_Nội dung` | Various | Label (3-line truncate) | Kudos message body |
| 16 | `mms_B.4.3_Hashtag` | Various | Label row | Hashtag tags (max 5, 1 line) |
| 17 | `mms_B.4.4_Action` | Various | Action row | Heart + Copy Link + View detail |
| 18 | `mms_B.5_slide` | `6885:9098` | Pagination | ‹ "N/5" › text indicator |
| 19 | `mms_B.6._Spotlight board` | `6885:9099` | Frame (section) | Spotlight Board section |
| 20 | `mms_B.7_Spotlight` | `6885:9101` | Interactive board | Pan/zoom map + search |
| 21 | `mms_B.7.1_Total kudos` | `6885:9219` | Label | "388 KUDOS" total counter |
| 22 | `mms_B.7.2_Pan zoom` | `6885:9217` | Pan-zoom frame | Interactive Sunner map |
| 23 | `mms_B.7.3_Tìm kiếm` | `6885:9216` | Text input | Search Sunner in board |
| 24 | `mms_C_All kudos` | `6885:9220` | Frame (section) | All Kudos + Stats + Gifts container |
| 25 | `mms_C.1_header` | `6885:9221` | Section header | "ALL KUDOS" title + View all link |
| 26 | `mms_C.2_View all` | N/A | Button (text_link) | "View all Kudos ↗" navigate link |
| 27 | `mms_C.3_KudosPostCard` | N/A | Card (list item) | Post card in All Kudos feed |
| 28 | `mms_D.1_Thống kê` | `6885:9223` | Stats dashboard | Personal stats grid |
| 29 | `mms_D.2_Mở Secret Box` | N/A | Button (icon_text) | Open Secret Box CTA |
| 30 | `mms_D.3_10 SUNNER` | `6885:9255` | Frame (list) | Gift recipients list |

---

## Test Case Summary

| Category | Count |
|----------|-------|
| Access Control & Security | 6 |
| User Interface (GUI) | 12 |
| Function — Section A: Write Kudos Button | 3 |
| Function — Section B: Highlight Kudos Carousel | 10 |
| Function — Section B: Filter | 5 |
| Function — Section B: Pagination & Navigation | 4 |
| Function — Section B: Spotlight Board | 3 |
| Function — Section C: All Kudos Feed | 6 |
| Function — Section D: Personal Stats | 3 |
| Function — Section D: Secret Box | 5 |
| **Total** | **57** |

---

## Test Cases

### Access Control & Security

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_KUDOS_ACC_001 | Access control and security | ACCESSING | Check access permission | Unauthenticated user | Access Kudos screen | Người dùng chưa đăng nhập, ứng dụng đã cài đặt | 1. Mở ứng dụng SAA 2025<br>2. Truy cập tab "Kudos"<br>3. Quan sát màn hình hiển thị | Người dùng được chuyển hướng đến màn hình Login<br>Không thể vào Kudos screen | High |
| TC_KUDOS_ACC_002 | Access control and security | ACCESSING | Check access permission | Authenticated user | Access Kudos screen | Người dùng đã đăng nhập thành công, token còn hiệu lực | 1. Mở ứng dụng SAA 2025<br>2. Tap tab "Kudos" trên bottom navigation bar<br>3. Quan sát màn hình | Màn hình Sun*Kudos hiển thị thành công với đầy đủ các section (A, B, C, D) | High |
| TC_KUDOS_ACC_003 | Access control and security | ACCESSING | Check authentication | Token expiry | Mid-session token expiry | Người dùng đang ở màn hình Sun*Kudos, token hết hạn trong khi đang dùng | 1. Thực hiện một thao tác API (ví dụ: scroll feed hoặc like kudo)<br>2. Quan sát phản hồi khi token hết hạn (401) | Ứng dụng tự động redirect về màn hình Login<br>Không hiển thị raw exception ra UI | High |
| TC_KUDOS_ACC_004 | Access control and security | ACCESSING | Check authentication | Deep link | Open via saa://kudos | Thiết bị đã đăng nhập, app đang chạy background | 1. Mở deep link `saa://kudos`<br>2. Quan sát màn hình | Màn hình Sun*Kudos mở thành công | Medium |
| TC_KUDOS_ACC_005 | Access control and security | ACCESSING | Check navigation | Highlight card | Tap sender/recipient → Profile | Người dùng ở màn hình Sun*Kudos, Highlight section có dữ liệu | 1. Quan sát B.3 Highlight card<br>2. Tap vào avatar hoặc tên người nhận (B.3.5/B.3.6)<br>3. Quan sát điều hướng | Màn hình Profile của người nhận mở thành công (TC_ACC_006) | High |
| TC_KUDOS_ACC_006 | Access control and security | ACCESSING | Check navigation | Post card | Tap sender/recipient → Profile | Người dùng ở màn hình Sun*Kudos, All Kudos feed có dữ liệu | 1. Quan sát C.3 KudosPostCard<br>2. Tap vào avatar hoặc tên người gửi (C.3.1)<br>3. Sau đó tap vào avatar hoặc tên người nhận (C.3.3)<br>4. Quan sát điều hướng | Màn hình Profile của người tương ứng mở thành công (TC_ACC_007) | High |

---

### User Interface (GUI)

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_KUDOS_GUI_001 | User interface | GUI | Check layout | Screen-wide layout | Overall structure | Người dùng đã đăng nhập, mở màn hình Sun*Kudos | 1. Mở màn hình Sun*Kudos<br>2. Quan sát bố cục tổng thể<br>3. Kiểm tra thứ tự và vị trí từng section | Header (Logo + VN▼ + 🔍 + 🔔) hiển thị phía trên cùng<br>Section A (KV banner) ngay dưới header<br>A.1 Button nằm bên dưới banner<br>Section B (Highlight) tiếp theo<br>Spotlight Board (B.6) tiếp theo<br>Section C (All Kudos) + D.1/D.3 ở cuối<br>Bottom Navigation Bar luôn hiển thị | High |
| TC_KUDOS_GUI_002 | User interface | GUI | Check display | Section A | KV banner display | Người dùng đã đăng nhập, mở màn hình Sun*Kudos | 1. Quan sát section A (KV Kudos banner)<br>2. Kiểm tra ảnh nền, text subtitle, và logo text | Background keyvisual (`mm_media_bg`) hiển thị đúng<br>Subtitle "Hệ thống ghi nhận và cảm ơn" hiển thị phía trên logo<br>Text logo "KUDOS" màu gold (ExtraBold) hiển thị rõ ràng | High |
| TC_KUDOS_GUI_003 | User interface | GUI | Check display | C.3_KudosPostCard | Message truncation — 5 lines | Người dùng ở màn hình Sun*Kudos, feed có kudo với nội dung dài | 1. Quan sát C.3 KudosPostCard có nội dung message dài<br>2. Kiểm tra số dòng hiển thị trong ô nội dung | Nội dung `kudos.message` truncate sau 5 dòng và hiển thị "..." (TC_GUI_003)<br>Không bao giờ hiển thị quá 5 dòng trong card C.3 | High |
| TC_KUDOS_GUI_004 | User interface | GUI | Check display | C.3_KudosPostCard | Hashtag truncation — 1 line | Người dùng ở màn hình Sun*Kudos, feed có kudo với nhiều hashtag | 1. Quan sát C.3 KudosPostCard có >5 hashtag<br>2. Kiểm tra row hashtag | Hashtag tags hiển thị tối đa trên 1 dòng; tags vượt quá truncate hoặc ẩn (TC_GUI_004)<br>Không wrap sang dòng 2 | Medium |
| TC_KUDOS_GUI_005 | User interface | GUI | Check display | B.3_KudosHighlightCard | Message + name truncation — 3 lines | Người dùng ở màn hình Sun*Kudos, Highlight có kudo với nội dung dài | 1. Quan sát B.4.2 nội dung trong Highlight card<br>2. Kiểm tra truncation<br>3. Kiểm tra tên người gửi B.3.2 khi quá dài | Nội dung `kudos.message` trong Highlight card truncate sau 3 dòng với "..." (TC_GUI_005)<br>Tên người gửi truncate với "..." khi quá dài | High |
| TC_KUDOS_GUI_006 | User interface | GUI | Check display | B.3.6 / C.3.3 | Star badge thresholds | Người dùng ở màn hình Sun*Kudos, Highlight/feed có recipients với nhiều kudos | 1. Quan sát B.3.6 (recipient info trong Highlight card)<br>2. Tìm recipient có kudos nhận: 10, 20, 50<br>3. Kiểm tra star badge tương ứng | Recipient nhận 10 kudos → hiển thị 1★<br>Recipient nhận 20 kudos → hiển thị 2★<br>Recipient nhận 50 kudos → hiển thị 3★ (TC_FUN_006)<br>Recipient < 10 kudos → không có star badge | High |
| TC_KUDOS_GUI_007 | User interface | GUI | Check display | B.3 carousel | Active vs faded card state | Người dùng ở màn hình Sun*Kudos, Highlight carousel có ≥2 cards | 1. Quan sát carousel B.3<br>2. Kiểm tra card ở giữa (active)<br>3. Kiểm tra card hai bên (faded) | Card ở vị trí center: full opacity, hiển thị đầy đủ (TC_FUN_038)<br>Card bên cạnh: opacity thấp hơn (faded effect)<br>Hiệu ứng rõ ràng khi swipe chuyển card | High |
| TC_KUDOS_GUI_008 | User interface | GUI | Initialize | A.1_Button ghi nhận | Always enabled | Người dùng đã đăng nhập, mở màn hình Sun*Kudos | 1. Quan sát A.1 Button "Hôm nay, bạn muốn gửi kudos đến ai?"<br>2. Kiểm tra trạng thái button | Button luôn hiển thị ở trạng thái enabled<br>Icon bút (edit icon) và placeholder text hiển thị đúng<br>Không có điều kiện disable (TC_GUI_008) | High |
| TC_KUDOS_GUI_009 | User interface | GUI | Check display | D.2_Mở Secret Box | Disabled state | Người dùng đã đăng nhập, `secret_boxes_unopened = 0` | 1. Quan sát D.2 Button "Mở Secret Box 🎁"<br>2. Kiểm tra trạng thái khi chưa có box chưa mở | Button hiển thị ở trạng thái disabled (greyed out / không thể tap) (TC_FUN_039)<br>Label "Mở Secret Box 🎁" vẫn hiển thị nhưng không tương tác được | High |
| TC_KUDOS_GUI_010 | User interface | GUI | Check display | B.5_slide | Pagination text indicator | Người dùng ở màn hình Sun*Kudos, Highlight carousel có 5 cards | 1. Quan sát B.5 pagination component<br>2. Kiểm tra cấu trúc ‹ text › | Hiển thị dạng `"‹ N/5 ›"` (ví dụ: `"‹ 1/5 ›"`, `"‹ 2/5 ›"`)<br>**Không phải dots** — là text indicator<br>Text cập nhật khi swipe carousel | High |
| TC_KUDOS_GUI_011 | User interface | GUI | Check display | D.1_Thống kê | Personal stats grid | Người dùng đã đăng nhập, mở màn hình Sun*Kudos | 1. Scroll đến section D.1 Thống kê<br>2. Quan sát 5 stat items và divider | Hiển thị đầy đủ:<br>Kudos nhận: `user_stats.kudos_received`<br>Kudos gửi: `user_stats.kudos_sent`<br>Số tim: `user_stats.hearts_received`<br>Divider (D.1.5) nằm giữa 2 nhóm<br>Box đã mở: `user_stats.secret_boxes_opened`<br>Box chưa mở: `user_stats.secret_boxes_unopened` | High |
| TC_KUDOS_GUI_012 | User interface | GUI | Check state | Screen-wide | Skeleton loading | Người dùng đã đăng nhập, mở màn hình lần đầu (dữ liệu chưa load xong) | 1. Mở màn hình Sun*Kudos<br>2. Quan sát trạng thái trong lúc load | Skeleton placeholder hiển thị cho Highlight carousel (B) và All Kudos list (C) trong khi dữ liệu đang tải<br>Không hiển thị màn hình trắng hay nội dung trống đột ngột | Medium |

---

### Function — Section A: Write Kudos Button

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_KUDOS_FUN_001 | Normal_Others | FUNCTION | Check navigation | A.1_Button ghi nhận | Tap → WriteKudo screen | Người dùng ở màn hình Sun*Kudos, chưa scroll | 1. Tap vào A.1 Button "Hôm nay, bạn muốn gửi kudos đến ai?"<br>2. Quan sát điều hướng | Màn hình `[iOS] Sun*Kudos_Viết Kudo_default` mở thành công | High |
| TC_KUDOS_FUN_002 | Normal_Others | FUNCTION | Check component interaction | A.1_Button sticky | Sticky button visibility on scroll | Người dùng ở màn hình Sun*Kudos | 1. Scroll xuống qua KV banner (section A)<br>2. Quan sát sticky button phía trên màn hình | Sticky A.1 Button (`6891:21267`) xuất hiện ở vị trí fixed phía trên screen sau khi scroll qua banner<br>Default A.1 Button (`6885:9083`) không còn nhìn thấy sau khi scroll | High |
| TC_KUDOS_FUN_003 | Normal_Others | FUNCTION | Check navigation | A.1_Button sticky | Tap sticky → WriteKudo screen | Người dùng đã scroll qua KV banner, sticky button hiển thị | 1. Scroll xuống để sticky button hiện ra<br>2. Tap vào sticky A.1 Button<br>3. Quan sát điều hướng | Màn hình `[iOS] Sun*Kudos_Viết Kudo_default` mở thành công (giống hành vi default button) | High |

---

### Function — Section B: Highlight Kudos Carousel

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_KUDOS_FUN_004 | Normal_Others | FUNCTION | Check data loading | B.2_Highlight | Initial load — top 5 by heartCount | Người dùng đã đăng nhập, mở màn hình Sun*Kudos | 1. Quan sát section B.2 sau khi tải xong<br>2. Kiểm tra số lượng card và thứ tự | Carousel hiển thị đúng 5 Highlight Kudos (hoặc ít hơn nếu data < 5) (TC_FUN_001)<br>Thứ tự: `heart_count DESC` — card có nhiều heart nhất ở vị trí đầu tiên<br>API call: `GET /api/v1/kudos/highlight` | High |
| TC_KUDOS_FUN_005 | Normal_Others | FUNCTION | Check component interaction | B.2_Highlight | Swipe left/right | Người dùng ở màn hình Sun*Kudos, Highlight carousel có ≥2 cards | 1. Swipe phải sang trái trên carousel<br>2. Quan sát chuyển động card<br>3. Swipe trái sang phải | Swipe sang trái: chuyển đến card tiếp theo<br>Swipe sang phải: quay lại card trước<br>Hiệu ứng active/faded cập nhật theo card trung tâm (TC_FUN_019) | High |
| TC_KUDOS_FUN_006 | Normal_Others | FUNCTION | Check component interaction | B.4.4_Action | Heart toggle — like | Người dùng ở màn hình Sun*Kudos, Highlight card chưa được like | 1. Quan sát trạng thái heart icon trên B.4.4 (chưa like)<br>2. Tap vào heart icon<br>3. Quan sát thay đổi | Heart icon chuyển trạng thái sang liked<br>Heart count tăng 1<br>API call: `POST /api/v1/kudos/{id}/like` | High |
| TC_KUDOS_FUN_007 | Normal_Others | FUNCTION | Check component interaction | B.4.4_Action | Heart toggle — unlike | Người dùng ở màn hình Sun*Kudos, Highlight card đã được like | 1. Quan sát trạng thái heart icon (đã like)<br>2. Tap vào heart icon<br>3. Quan sát thay đổi | Heart icon quay lại trạng thái chưa like<br>Heart count giảm 1<br>API call để bỏ like | High |
| TC_KUDOS_FUN_008 | Normal_Others | FUNCTION | Check component interaction | B.4.4_Action | Copy Link | Người dùng ở màn hình Sun*Kudos, Highlight card có shareUrl | 1. Tap vào "Copy Link" (🔗) trên B.4.4<br>2. Quan sát feedback | URL (`shareUrl`) của kudo được sao chép vào clipboard<br>Toast xác nhận copy thành công hiển thị | Medium |
| TC_KUDOS_FUN_009 | Normal_Others | FUNCTION | Check navigation | B.4.4_Action | Xem chi tiết → View kudo | Người dùng ở màn hình Sun*Kudos, Highlight card hiển thị | 1. Tap vào "Xem chi tiết" (↗) trên B.4.4<br>2. Quan sát điều hướng | Màn hình `[iOS] Sun*Kudos_View kudo` mở và hiển thị chi tiết kudo tương ứng | High |
| TC_KUDOS_FUN_010 | Normal_Others | FUNCTION | Check component interaction | B.3_card | Tap card body → View kudo | Người dùng ở màn hình Sun*Kudos, Highlight card hiển thị | 1. Tap vào thân card B.3 (không phải button action cụ thể)<br>2. Quan sát điều hướng | Màn hình `[iOS] Sun*Kudos_View kudo` mở cho kudo được tap | High |
| TC_KUDOS_FUN_011 | Normal_Others | FUNCTION | Check data display | C.3.1_anonymous | Anonymous kudos — sender masked | Người dùng ở màn hình Sun*Kudos, feed có kudo ẩn danh (`is_anonymous = true`) | 1. Quan sát C.3 KudosPostCard cho kudo ẩn danh<br>2. Kiểm tra thông tin người gửi | Thông tin người gửi thực bị ẩn (tên, avatar, department không hiển thị)<br>Hiển thị `anonymous_nickname` thay thế<br>Không có thao tác navigate đến profile sender | High |
| TC_KUDOS_FUN_012 | Normal_Others | FUNCTION | Check component interaction | C.3.5_photos | Photo tap → full image | Người dùng ở màn hình Sun*Kudos, feed có kudo có đính kèm ảnh | 1. Quan sát C.3 KudosPostCard có ảnh đính kèm<br>2. Tap vào ảnh trong C.3.5<br>3. Quan sát | Ảnh mở ra ở chế độ xem toàn màn hình (TC_FUN_029) | Medium |
| TC_KUDOS_FUN_013 | Normal_Others | FUNCTION | Check component interaction | C.3_feed | View all Kudos link | Người dùng ở màn hình Sun*Kudos, section C hiển thị | 1. Tap vào C.2 "View all Kudos ↗"<br>2. Quan sát điều hướng | Màn hình `[iOS] Sun*Kudos_All Kudos` mở thành công với danh sách đầy đủ | High |

---

### Function — Section B: Filter

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_KUDOS_FUN_014 | Normal_Others | FUNCTION | Check component interaction | B.1.1_Hashtag filter | Open bottom sheet | Người dùng ở màn hình Sun*Kudos | 1. Tap vào B.1.1 Hashtag filter button<br>2. Quan sát overlay | Bottom sheet danh sách hashtag mở ra (`[iOS] Sun*Kudos_dropdown hashtag`)<br>Danh sách lấy từ `GET /api/v1/hashtags` | High |
| TC_KUDOS_FUN_015 | Normal_Others | FUNCTION | Check component interaction | B.1.2_Department filter | Open bottom sheet | Người dùng ở màn hình Sun*Kudos | 1. Tap vào B.1.2 Phòng ban filter button<br>2. Quan sát overlay | Bottom sheet danh sách phòng ban mở ra (`[iOS] Sun*Kudos_dropdown phòng ban`)<br>Danh sách lấy từ `GET /api/v1/departments` | High |
| TC_KUDOS_FUN_016 | Normal_Others | FUNCTION | Check business logic | B.1_Filter | Filter AND logic | Người dùng ở màn hình Sun*Kudos | 1. Chọn hashtag "Frontend" từ B.1.1<br>2. Chọn phòng ban "Engineering" từ B.1.2<br>3. Quan sát kết quả Highlight và All Kudos | Chỉ hiển thị kudos có **cả** hashtag "Frontend" **và** phòng ban "Engineering" (AND logic) (TC_FUN_004)<br>Áp dụng cho cả section B (Highlight carousel) và section C (All Kudos feed) | High |
| TC_KUDOS_FUN_017 | Normal_Others | FUNCTION | Check business logic | B.1_Filter | Filter reset carousel | Người dùng ở màn hình Sun*Kudos, đang xem carousel ở page 3 | 1. Scroll carousel đến page 3<br>2. Chọn hashtag filter<br>3. Quan sát vị trí carousel | Carousel reset về card đầu tiên (page 1) sau khi thay đổi filter (TC_FUN_005)<br>Pagination indicator cập nhật thành "1/N" | High |
| TC_KUDOS_FUN_018 | Normal_Others | FUNCTION | Check cross-component effect | B.1_Filter | Filter affects Highlight + All Kudos | Người dùng ở màn hình Sun*Kudos | 1. Chọn một hashtag filter<br>2. Quan sát section B (Highlight)<br>3. Quan sát section C (All Kudos) | Section B chỉ hiển thị kudos matching filter (TC_FUN_030)<br>Section C cũng chỉ hiển thị kudos matching filter (TC_FUN_033)<br>Filter label trên button cập nhật thành hashtag đã chọn | High |

---

### Function — Section B: Pagination & Navigation

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_KUDOS_FUN_019 | Normal_Others | FUNCTION | Check component interaction | B.5.1_prev button | Disabled at page 1 | Người dùng ở màn hình Sun*Kudos, carousel ở page 1 | 1. Quan sát B.5.1 (‹ prev button) khi đang ở card đầu tiên<br>2. Thử tap vào prev button | B.5.1 (‹) ở trạng thái disabled khi đang ở page 1<br>Tap không có tác dụng | High |
| TC_KUDOS_FUN_020 | Normal_Others | FUNCTION | Check component interaction | B.5.3_next button | Disabled at last page | Người dùng ở màn hình Sun*Kudos, carousel ở page cuối | 1. Swipe đến card cuối cùng (page 5 hoặc tổng số cards)<br>2. Quan sát B.5.3 (› next button) | B.5.3 (›) ở trạng thái disabled khi đang ở page cuối<br>Tap không có tác dụng | High |
| TC_KUDOS_FUN_021 | Normal_Others | FUNCTION | Check component interaction | B.5_slide | Tap next button navigates carousel | Người dùng ở màn hình Sun*Kudos, carousel ở page 1 | 1. Tap B.5.3 (›) next button<br>2. Quan sát carousel | Carousel chuyển sang card tiếp theo<br>Pagination text cập nhật "2/5" | High |
| TC_KUDOS_FUN_022 | Normal_Others | FUNCTION | Check data display | B.5.2_page text | Pagination text format | Người dùng ở màn hình Sun*Kudos, carousel có 5 cards | 1. Quan sát B.5.2 text khi ở từng page<br>2. Swipe qua tất cả 5 card | Text hiển thị đúng format `"N/5"`: "1/5", "2/5", "3/5", "4/5", "5/5" theo card hiện tại | Medium |

---

### Function — Section B: Spotlight Board

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_KUDOS_FUN_023 | Normal_Others | FUNCTION | Check component interaction | B.7.2_Pan zoom | Pan and zoom gesture | Người dùng ở màn hình Sun*Kudos, scroll đến Spotlight Board | 1. Thực hiện pinch gesture để zoom in trên B.7.2<br>2. Thực hiện pan (drag) để di chuyển<br>3. Pinch out để zoom out | Pan/zoom hoạt động đúng trên map board<br>Các text node tên Sunner có thể đọc sau khi zoom in | Medium |
| TC_KUDOS_FUN_024 | Normal_Others | FUNCTION | Check component interaction | B.7.3_Search | Search Sunner in board | Người dùng ở màn hình Sun*Kudos, Spotlight Board hiển thị | 1. Tap vào B.7.3 search input<br>2. Nhập tên Sunner<br>3. Quan sát kết quả trên board | Vị trí của Sunner tương ứng được highlight/focus trên board map<br>Tìm kiếm là realtime (phản hồi ngay khi gõ) | High |
| TC_KUDOS_FUN_025 | Normal_Others | FUNCTION | Check navigation | B.7.2_Sunner node | Tap Sunner → Profile | Người dùng ở màn hình Sun*Kudos, Spotlight Board hiển thị, đã zoom in | 1. Tap vào một text node tên Sunner trên board B.7.2<br>2. Quan sát điều hướng | Màn hình Profile của Sunner tương ứng mở thành công | High |

---

### Function — Section C: All Kudos Feed

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_KUDOS_FUN_026 | Normal_Others | FUNCTION | Check data loading | C_All kudos | Initial feed load | Người dùng đã đăng nhập, mở màn hình Sun*Kudos | 1. Scroll đến section C (All Kudos)<br>2. Quan sát danh sách kudo | Danh sách C.3 KudosPostCard hiển thị đúng với dữ liệu từ `GET /api/v1/kudos`<br>Mỗi card hiển thị: sender info, recipient info, thời gian, nội dung, hashtags, actions | High |
| TC_KUDOS_FUN_027 | Normal_Others | FUNCTION | Check business logic | C_Realtime | New kudo prepend via Realtime | Người dùng đang ở màn hình Sun*Kudos, section C đang hiển thị | 1. Người khác gửi một kudo mới trong khi màn hình đang mở<br>2. Quan sát section C | Kudo mới xuất hiện ở đầu danh sách C (prepend) qua Supabase Realtime subscription<br>Không cần pull-to-refresh | High |
| TC_KUDOS_FUN_028 | Normal_Others | FUNCTION | Check data loading | C_All kudos | Infinite scroll — load more | Người dùng ở màn hình Sun*Kudos, section C có >1 trang dữ liệu | 1. Scroll đến cuối danh sách C<br>2. Tiếp tục scroll<br>3. Quan sát hành vi load | Dữ liệu trang tiếp theo được tải (`GET /api/v1/kudos?page=N+1`)<br>Cards mới append vào cuối danh sách<br>Loading indicator hiển thị trong khi tải trang tiếp theo | High |
| TC_KUDOS_FUN_029 | Normal_Others | FUNCTION | Check state | C_All kudos | Empty state | Người dùng đã đăng nhập, không có kudo nào trong hệ thống (hoặc filter không có kết quả) | 1. Mở màn hình Sun*Kudos (không có kudo nào)<br>2. Quan sát section C | Section C hiển thị empty state "Chưa có Kudos nào"<br>Không có crash hay màn hình trắng | Medium |
| TC_KUDOS_FUN_030 | Abnormal_Others | FUNCTION | Check error handling | C_All kudos | API error | Người dùng ở màn hình Sun*Kudos, server trả về lỗi khi tải feed | 1. Mô phỏng lỗi API cho `GET /api/v1/kudos`<br>2. Quan sát màn hình | Lỗi hiển thị inline (không crash)<br>Raw exception **không** lộ ra UI (sealed `Result<T>`)<br>User có thể retry | High |
| TC_KUDOS_FUN_031 | Normal_Others | FUNCTION | Check data display | C.3.4_Post time | Timestamp format | Người dùng ở màn hình Sun*Kudos, All Kudos feed có dữ liệu | 1. Quan sát C.3.4 timestamp trên KudosPostCard<br>2. Kiểm tra format | Timestamp hiển thị đúng format `"HH:mm - DD/MM/YYYY"` (ví dụ: "10:00 - 30/10/2025")<br>Dùng local time của user | Medium |

---

### Function — Section D: Personal Stats

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_KUDOS_FUN_032 | Normal_Others | FUNCTION | Check data loading | D.1_Thống kê | Personal stats load | Người dùng đã đăng nhập, mở màn hình Sun*Kudos | 1. Scroll đến section D.1<br>2. Quan sát 5 stat items | Tất cả 5 stats hiển thị đúng giá trị từ `GET /api/v1/users/me`:<br>Kudos nhận = `kudos_received`, Kudos gửi = `kudos_sent`, Tim = `hearts_received`, Box mở = `secret_boxes_opened`, Box chưa mở = `secret_boxes_unopened` | High |
| TC_KUDOS_FUN_033 | Normal_Others | FUNCTION | Check data display | D.3_Gift recipients | Latest gift recipients list | Người dùng đã đăng nhập, có Sunner nhận quà gần nhất | 1. Scroll đến section D.3<br>2. Quan sát danh sách "NHẬN QUÀ MỚI NHẤT" | Danh sách D.3.2 GiftRecipientRow hiển thị đúng:<br>Avatar, tên (`users.full_name`), mô tả phần thưởng (`reward_name`)<br>Avatar fallback khi không có ảnh | Medium |
| TC_KUDOS_FUN_034 | Normal_Others | FUNCTION | Check navigation | D.3.2_GiftRecipientRow | Tap row → Profile | Người dùng ở màn hình Sun*Kudos, D.3 có dữ liệu | 1. Tap vào một D.3.2 GiftRecipientRow<br>2. Quan sát điều hướng | Màn hình Profile của Sunner nhận quà tương ứng mở thành công | High |

---

### Function — Section D: Secret Box

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_KUDOS_FUN_035 | Normal_Others | FUNCTION | Check component interaction | D.2_Secret Box | Button enabled — open flow | Người dùng đã đăng nhập, `secret_boxes_unopened > 0` | 1. Quan sát D.2 Button "Mở Secret Box 🎁"<br>2. Xác nhận button enabled<br>3. Tap vào button | Button enabled khi `secret_boxes_unopened > 0`<br>API flow khởi tạo: `GET /api/v1/users/me/secret-boxes/next` → `POST .../open`<br>Màn hình `[iOS] Open secret box` mở thành công | High |
| TC_KUDOS_FUN_036 | Normal_Others | FUNCTION | Check business logic | D.2_Secret Box | Stats update after open | Người dùng đã đăng nhập, `secret_boxes_unopened > 0`, đã mở 1 box | 1. Ghi lại giá trị D.1.6 (opened) và D.1.7 (unopened) trước khi mở<br>2. Tap D.2 và hoàn tất mở box<br>3. Quan sát D.1 stats | D.1.6 (Box đã mở) tăng 1<br>D.1.7 (Box chưa mở) giảm 1 (TC_FUN_024) | High |
| TC_KUDOS_FUN_037 | Normal_Others | FUNCTION | Check business logic | D.2_Secret Box | Double-tap prevention | Người dùng đã đăng nhập, `secret_boxes_unopened > 0` | 1. Tap D.2 Button "Mở Secret Box 🎁"<br>2. Ngay lập tức tap lại nút lần nữa<br>3. Quan sát số API call | Chỉ gửi **1 lần** API call dù tap nhiều lần (TC_FUN_025)<br>Không mở nhiều box cùng lúc | High |
| TC_KUDOS_FUN_038 | Normal_Others | FUNCTION | Check state | D.2_Secret Box | Button disabled — no box | Người dùng đã đăng nhập, `secret_boxes_unopened = 0` | 1. Quan sát D.2 Button<br>2. Thử tap vào button disabled<br>3. Quan sát hành vi | Button disabled, tap không có tác dụng (TC_FUN_039)<br>Không trigger API call nào | High |
| TC_KUDOS_FUN_039 | Abnormal_Others | FUNCTION | Check error handling | D.2_Secret Box | API error on open | Người dùng đã đăng nhập, `secret_boxes_unopened > 0`, server trả về lỗi | 1. Mô phỏng lỗi API cho `POST .../open`<br>2. Tap D.2 Button<br>3. Quan sát hành vi | Loading state kết thúc<br>Thông báo lỗi hiển thị (toast hoặc inline) — không crash<br>Stats **không thay đổi** (không tăng opened count)<br>Button trở lại enabled để retry | High |

---

## Notes

### Test Viewpoints Source
- Testviewpoint server không có screen "Sun*Kudos" — test cases sinh dựa trên spec `ios_sun_kudos.md` và design items JSON
- References từ spec: TC_FUN_001, TC_FUN_004–006, TC_FUN_019, TC_FUN_024–025, TC_FUN_029–030, TC_FUN_033, TC_FUN_038–039, TC_GUI_003–005, TC_GUI_008, TC_ACC_006–007

### Key Design Rules Validated
- B.3.6 / C.3.3 star badge: 10→1★, 20→2★, 50→3★
- B.5.2 pagination: text `"N/5"` (not dots)
- A.1 Button: always enabled, never disabled
- D.2 Button: disabled when `secret_boxes_unopened = 0`
- Carousel active/faded visual state (center = full opacity, sides = faded)
- Realtime: Supabase INSERT subscription for `kudos` table → prepend to C feed
- Filter AND logic: hashtag AND department both required to match
- Filter change resets carousel to page 1

### Localization
- All expected results in Vietnamese (default locale)
- Test cases cover locale key checks where applicable (timestamp format varies by locale)
