# Test Cases: [iOS] Award_Top talent

## Screen Info

| Property | Value |
|----------|-------|
| **Screen ID** | `c-QM3_zjkG` |
| **Figma File Key** | `9ypp4enmFmdK3YAFJLIu6C` |
| **Figma Node ID** | `6885:10259` |
| **Figma Link** | https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/c-QM3_zjkG |
| **Spec File** | `.momorph/specs/c-QM3_zjkG-iOS-Award-Top-talent/spec.md` |
| **Last Updated** | 2026 |

---

## Design Items Covered

| # | Item Name | Node ID | Type | Purpose |
|---|-----------|---------|------|---------|
| 1 | `mms_1_header` | `6885:10264` | Sticky Header | SAA logo + language/search/bell actions |
| 2 | `mms_1.1_logo` | `6885:10265` | Image | Sun* Annual Awards 2025 logo |
| 3 | `mms_1.2_language` | `6885:10272` | Inline Dropdown | Language selector (VN / EN) |
| 4 | `mms_1.3_search` | `6885:10275` | Icon Button | Search action |
| 5 | `mms_1.4_notification` | `6885:10278` | Icon Button | Notification bell + badge |
| 6 | `mms_A_KV Kudos` | `6885:10283` | Banner | Keyvisual Kudos: subtitle + KUDOS logo |
| 7 | `mms_B_Highlight` | `6885:10288` | Container | Award highlight: sub-label + title + dropdown |
| 8 | `mms_B.1_award_category` | `6885:10291` | Dropdown | Category selector — list of all awards from API |
| 9 | `mms_2.3_award` | `6885:10295` | Section | Award info block: image + title + desc + qty + prize |
| 10 | `mms_C2.1_award_image` | `6885:10296` | AsyncImage | Award badge image (dynamic from API) |
| 11 | `mms_C2.1.3_award_image_alt` | `6885:10314` | Placeholder | Fallback when image_url is null |
| 12 | `mms_C2.2_award_name` | `6885:10299` | Text | Award name row (icon + name) |
| 13 | `mms_C2.3_award_description` | `6885:10302` | Text | Award description |
| 14 | `mms_C2.4_award_quantity` | `6885:10305` | Text | Quantity section (label + value + unit) |
| 15 | `mms_C2.5_award_prize` | `6885:10308` | Text | Prize section (label + value + per-award note) |
| 16 | `mms_2.4_kudos` | `6885:10311` | Section | Sun*Kudos block |
| 17 | `mms_D_kudos_chi_tiet` | `6885:10312` | Button (Filled) | "Chi tiết" — filled gold #FFEA9E, text #0A1929 |
| 18 | `mms_3_nav bar` | `6885:10318` | Sticky NavBar | Bottom Nav: SAA / Awards (active) / Kudos / Profile |

---

## Test Case Summary

| Category | Count |
|----------|-------|
| Access Control & Security (ACCESSING) | 2 |
| User Interface (GUI) | 11 |
| Function — Data Display | 6 |
| Function — Category Dropdown | 5 |
| Function — Scrolling | 2 |
| Function — Navigation | 7 |
| Function — Language Selector | 2 |
| **Total** | **35** |

> Note: 34 test cases uploaded to MoMorph (ACC_001–002, GUI_001–011, FUN_001–021).

---

## Test Cases

### Access Control & Security (ACCESSING)

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_IOS_AWARD_DETAIL_ACC_001 | Access control and security | ACCESSING | Check access permission | Authenticated user | Navigate from Home screen | Người dùng đã đăng nhập thành công, đang ở Home screen, có ít nhất 1 award card hiển thị | 1. Mở ứng dụng SAA 2025 và đăng nhập thành công<br>2. Trên Home screen, tìm section danh sách giải thưởng<br>3. Tap nút "Chi tiết" trên một award card bất kỳ<br>4. Quan sát màn hình hiển thị | Màn hình Award Detail mở thành công với thông tin đầy đủ của giải thưởng được chọn | High |
| TC_IOS_AWARD_DETAIL_ACC_002 | Access control and security | ACCESSING | Check access permission | Unauthenticated user | Direct access without auth | Người dùng chưa đăng nhập, session đã hết hạn | 1. Xóa session hoặc đăng xuất<br>2. Cố gắng điều hướng trực tiếp đến màn hình Award Detail<br>3. Quan sát màn hình hiển thị | Người dùng được chuyển hướng đến màn hình Login — không hiển thị Award Detail | High |

---

### User Interface (GUI)

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_IOS_AWARD_DETAIL_GUI_001 | User interface | GUI | Check layout | Overall screen structure | All design elements visible | Người dùng đã đăng nhập, điều hướng đến Award Detail giải Top Talent | 1. Điều hướng đến màn hình Award Detail<br>2. Quan sát bố cục tổng thể<br>3. Kiểm tra từng khu vực: Header, KV Kudos banner, Highlight block, Award info block, Kudos block, Bottom Nav | Màn hình hiển thị đầy đủ 6 khu vực:<br>- Header sticky ở trên cùng: logo (góc trái) + actions (góc phải)<br>- KV Kudos banner: subtitle + KUDOS logo<br>- Highlight block: sub-label + title + dropdown<br>- Award info block: badge image + title row + mô tả + quantity + prize<br>- Sun*Kudos block: label + title + banner + badge + mô tả + nút Chi tiết<br>- Bottom Nav sticky ở dưới cùng: 4 tab, Awards tab active (gold) | High |
| TC_IOS_AWARD_DETAIL_GUI_002 | User interface | GUI | Check display | mms_1_header | Header components visible | Người dùng đang ở màn hình Award Detail, có 3 thông báo chưa đọc | 1. Mở màn hình Award Detail<br>2. Quan sát Header ở trên cùng<br>3. Kiểm tra SAA logo góc trái<br>4. Kiểm tra cụm action góc phải: ngôn ngữ + tìm kiếm + chuông<br>5. Kiểm tra badge đỏ trên icon chuông | - Logo 'Sun* Annual Awards 2025' hiển thị rõ ràng góc trái<br>- Nút ngôn ngữ: icon cờ + mã ngôn ngữ + mũi tên dropdown<br>- Icon tìm kiếm: 24×24dp, màu trắng<br>- Icon chuông: 24×24dp + badge chấm đỏ khi có thông báo chưa đọc | High |
| TC_IOS_AWARD_DETAIL_GUI_003 | User interface | GUI | Check display | mms_1_header | Notification badge — no unread | Người dùng đang ở màn hình Award Detail, không có thông báo chưa đọc | 1. Mở màn hình Award Detail<br>2. Quan sát icon chuông ở Header<br>3. Kiểm tra badge đỏ | Icon chuông hiển thị không có badge đỏ kèm theo | Medium |
| TC_IOS_AWARD_DETAIL_GUI_004 | User interface | GUI | Check display | mms_A_KV Kudos | KV Kudos banner content | Người dùng đang ở màn hình Award Detail | 1. Mở màn hình Award Detail<br>2. Quan sát banner Kudos ở đầu trang<br>3. Kiểm tra text subtitle và logo KUDOS | - Text phụ 'Hệ thống ghi nhận và cảm ơn' hiển thị 12sp, màu secondary<br>- Flame icon + text 'KUDOS' hiển thị 28–32sp Bold, màu vàng gold #FFEA9E | Medium |
| TC_IOS_AWARD_DETAIL_GUI_005 | User interface | GUI | Check display | mms_B_Highlight | Highlight block — sub-label, title, dropdown trigger | Người dùng đang ở màn hình Award Detail, giải đang xem là 'Top Talent' | 1. Mở màn hình Award Detail<br>2. Quan sát khối highlight bên dưới KV banner<br>3. Kiểm tra sub-label 'Sun* Annual Awards 2025'<br>4. Kiểm tra title 'Hệ thống giải thưởng SAA 2025'<br>5. Kiểm tra dropdown trigger hiển thị tên giải đang xem + mũi tên xuống | - Sub-label 'Sun* Annual Awards 2025' — 12sp, màu secondary<br>- Title 'Hệ thống giải thưởng SAA 2025' — 24–28sp Bold, màu trắng<br>- Dropdown trigger width ~140dp, height 40dp, border gold, radius 4dp, hiển thị tên giải 'Top Talent' + icon mũi tên xuống | High |
| TC_IOS_AWARD_DETAIL_GUI_006 | User interface | GUI | Check display | mms_2.3_award | Award badge image — image_url hợp lệ | Người dùng đang ở màn hình Award Detail, award có image_url hợp lệ | 1. Mở màn hình Award Detail với award có image_url<br>2. Quan sát vùng hình ảnh huy hiệu ở giữa màn hình<br>3. Chờ ảnh tải xong<br>4. Kiểm tra kích thước, viền và vị trí | - Ảnh huy hiệu hiển thị 240×240dp, căn giữa horizontally<br>- Có viền gold glow (1dp, #FFEA9E 30% alpha), radius 16dp<br>- Ảnh không bị méo hoặc cắt | High |
| TC_IOS_AWARD_DETAIL_GUI_007 | User interface | GUI | Check display | mms_2.3_award | Award badge image — image_url null | Người dùng đang ở màn hình Award Detail, award có image_url = null | 1. Điều hướng đến Award Detail của giải có image_url = null<br>2. Quan sát vùng hình ảnh huy hiệu<br>3. Kiểm tra placeholder | - Placeholder icon/image hiển thị trong container 240×240dp<br>- Container vẫn giữ kích thước 240×240dp và viền gold<br>- Không hiển thị lỗi hoặc UI bị vỡ | High |
| TC_IOS_AWARD_DETAIL_GUI_008 | User interface | GUI | Check display | mms_2.3_award | Award info text sections — all populated | Người dùng đang ở màn hình Award Detail giải Top Talent | 1. Mở màn hình Award Detail<br>2. Quan sát title row (icon + tên giải)<br>3. Quan sát description text<br>4. Quan sát section 'Số lượng giải thưởng'<br>5. Quan sát section 'Giá trị giải thưởng'<br>6. Kiểm tra dividers giữa các section | - Title row: icon 20dp + 'Top Talent' 18–20sp Bold màu vàng gold<br>- Description: 14sp Regular màu trắng, lineHeight 1.5×<br>- Divider: 1dp màu #FFFFFF20 giữa các section<br>- Quantity: icon + 'Số lượng giải thưởng' (14sp SemiBold trắng) + '10' (20–24sp Bold trắng) + 'Cá nhân' (14sp Regular secondary)<br>- Prize: icon + 'Giá trị giải thưởng' (14sp SemiBold trắng) + '7.000.000 VNĐ' (20–24sp Bold gold) + 'cho mỗi giải thưởng' (12sp Regular secondary) | High |
| TC_IOS_AWARD_DETAIL_GUI_009 | User interface | GUI | Check display | mms_2.4_kudos | Sun*Kudos block full content | Người dùng đã cuộn xuống cuối màn hình Award Detail | 1. Mở màn hình Award Detail<br>2. Cuộn xuống hết trang để thấy Kudos block<br>3. Kiểm tra từng thành phần: label, title, banner image, badge text, description, nút Chi tiết | - Label 'Phong trào ghi nhận' — 12sp Regular secondary<br>- Title 'Sun* Kudos' — 22–24sp Bold gold<br>- Banner image Kudos — fill width, height ~120dp, radius 8dp<br>- Badge 'ĐIỂM MỚI CỦA SAA 2025' — 12sp Bold uppercase<br>- Description text — 14sp Regular secondary<br>- Nút 'Chi tiết': width 120dp, height 40dp, radius 4dp, **nền vàng #FFEA9E (FILLED)**, text 14–16sp SemiBold màu #0A1929 + arrow icon | High |
| TC_IOS_AWARD_DETAIL_GUI_010 | User interface | GUI | Check display | mms_3_nav bar | Bottom Nav — Awards tab active gold | Người dùng đang ở màn hình Award Detail | 1. Mở màn hình Award Detail<br>2. Quan sát Bottom Navigation Bar ở cuối màn hình<br>3. Kiểm tra 4 tabs: SAA 2025, Awards, Kudos, Profile<br>4. Kiểm tra tab Awards highlight màu vàng | - Bottom Nav hiển thị đủ 4 tabs: SAA 2025, Awards, Kudos, Profile<br>- Tab 'Awards' active: icon + text màu #FFEA9E (gold)<br>- Các tab khác: màu inactive #6B7280<br>- Bottom Nav background #0A1929 | High |
| TC_IOS_AWARD_DETAIL_GUI_011 | User interface | GUI | Check display | Background | Dark background + keyvisual artwork | Người dùng đang ở màn hình Award Detail | 1. Mở màn hình Award Detail<br>2. Quan sát màu nền tổng thể<br>3. Kiểm tra artwork keyvisual ở phần trên màn hình | - Nền màn hình màu #00101A (dark navy)<br>- Keyvisual artwork hiển thị ở phần trên (fill width, height 723dp, align TopEnd) | Medium |

---

### Function — Data Display

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_IOS_AWARD_DETAIL_FUN_001 | Normal_Function | FUNCTION | Check data display | Award detail load — normal case | Data loads from API correctly | Người dùng đã đăng nhập, API /awards/:id trả về dữ liệu hợp lệ | 1. Từ Home screen, tap 'Chi tiết' trên award card<br>2. Chờ màn hình Award Detail load<br>3. Xác nhận tên giải hiển thị đúng<br>4. Xác nhận mô tả hiển thị đúng<br>5. Xác nhận số lượng (quantity + unit) hiển thị đúng<br>6. Xác nhận giá trị giải thưởng hiển thị đúng | - Tên giải: lấy từ API, không hardcode<br>- Mô tả: nội dung từ API hiển thị đầy đủ<br>- Số lượng: hiển thị đúng giá trị và đơn vị từ API<br>- Giá trị: hiển thị đúng prize_value từ API, màu gold | High |
| TC_IOS_AWARD_DETAIL_FUN_002 | Normal_Function | FUNCTION | Check data display | Loading state | Loading indicator while fetching | Người dùng tap Chi tiết từ Home screen, mạng chậm (throttled) | 1. Throttle kết nối mạng về Slow 3G<br>2. Từ Home screen, tap 'Chi tiết' trên award card bất kỳ<br>3. Quan sát màn hình Award Detail ngay khi mở<br>4. Chờ dữ liệu tải xong | - Trong khi đang tải: loading indicator (spinner hoặc skeleton) hiển thị ở giữa màn hình<br>- Sau khi tải xong: loading indicator biến mất, nội dung giải thưởng hiển thị đầy đủ | High |
| TC_IOS_AWARD_DETAIL_FUN_003 | Abnormal_Function | FUNCTION | Check error handling | Error state | API call fails — retry | Người dùng đang ở màn hình Award Detail, API /awards/:id trả về lỗi | 1. Tắt kết nối mạng<br>2. Từ Home screen, tap 'Chi tiết' trên award card<br>3. Quan sát error state<br>4. Tap nút 'Thử lại'<br>5. Bật lại mạng<br>6. Tap nút 'Thử lại' lần nữa | - Error state hiển thị: text thông báo lỗi thân thiện (không lộ exception thô) + nút 'Thử lại'<br>- Sau khi tap Retry và có mạng: API được gọi lại, loading state hiển thị, sau đó hiển thị nội dung giải thưởng | High |
| TC_IOS_AWARD_DETAIL_FUN_004 | Abnormal_Function | FUNCTION | Check data display | Award detail — edge case | image_url null — placeholder fallback | Có award trong DB với image_url = null | 1. Điều hướng đến Award Detail của giải có image_url = null<br>2. Quan sát vùng badge image<br>3. Kiểm tra ứng dụng không crash<br>4. Kiểm tra placeholder hiển thị | - Ứng dụng không crash<br>- Placeholder icon/image hiển thị trong container 240×240dp<br>- Các thành phần khác (title, description, quantity, prize) vẫn hiển thị bình thường | High |
| TC_IOS_AWARD_DETAIL_FUN_020 | Abnormal_Function | FUNCTION | Check data display | Award detail — edge case | quantity and prize_value null — fallback | Có award trong DB với quantity = null và prize_value = null | 1. Điều hướng đến Award Detail của giải có quantity và prize_value null<br>2. Quan sát section 'Số lượng giải thưởng' và 'Giá trị giải thưởng'<br>3. Kiểm tra ứng dụng không crash | - Ứng dụng không crash<br>- Section số lượng hiển thị '—' hoặc placeholder thay cho null<br>- Section giá trị hiển thị '—' hoặc placeholder thay cho null<br>- Layout không bị vỡ | Medium |
| TC_IOS_AWARD_DETAIL_FUN_021 | Abnormal_Function | FUNCTION | Check data display | Kudos banner image | Banner image load failure — placeholder | Người dùng ở Award Detail, ảnh banner Kudos không tải được | 1. Tắt mạng<br>2. Mở màn hình Award Detail<br>3. Cuộn xuống đến Kudos block<br>4. Quan sát vùng banner image | - Placeholder hiển thị thay thế banner image<br>- Ứng dụng không crash<br>- Các thành phần khác trong Kudos block vẫn hiển thị bình thường | Medium |

---

### Function — Category Dropdown

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_IOS_AWARD_DETAIL_FUN_005 | Normal_Function | FUNCTION | Check component interaction | Category dropdown | Open dropdown — show all awards from API | Người dùng đang ở màn hình Award Detail, API /awards trả về danh sách đầy đủ | 1. Mở màn hình Award Detail<br>2. Quan sát dropdown trigger hiển thị tên giải hiện tại<br>3. Tap vào dropdown trigger<br>4. Quan sát danh sách dropdown | - Dropdown mở ra hiển thị danh sách tất cả hạng mục giải thưởng từ API<br>- Danh sách không hardcode — tất cả awards từ API đều xuất hiện<br>- Hạng mục đang xem được highlight (màu gold hoặc selected indicator) | High |
| TC_IOS_AWARD_DETAIL_FUN_006 | Normal_Function | FUNCTION | Check component interaction | Category dropdown | Select another award — content updates | Người dùng đang xem Award Detail giải Top Talent, dropdown đang hiển thị danh sách | 1. Mở màn hình Award Detail giải Top Talent<br>2. Tap dropdown trigger để mở danh sách<br>3. Chọn hạng mục khác (ví dụ: Top Project)<br>4. Quan sát nội dung màn hình | - Dropdown đóng lại sau khi chọn<br>- Dropdown trigger cập nhật hiển thị tên 'Top Project'<br>- Badge image thay đổi sang ảnh của 'Top Project'<br>- Title, description, quantity, prize_value cập nhật theo dữ liệu của 'Top Project'<br>- Header và Bottom Nav không thay đổi | High |
| TC_IOS_AWARD_DETAIL_FUN_007 | Normal_Function | FUNCTION | Check component interaction | Category dropdown | Dismiss dropdown without selection | Người dùng đang ở Award Detail, dropdown đang mở | 1. Mở màn hình Award Detail đang xem 'Top Talent'<br>2. Tap dropdown trigger để mở danh sách<br>3. Tap ra ngoài vùng dropdown (dismiss)<br>4. Quan sát dropdown và nội dung màn hình | - Dropdown đóng lại<br>- Dropdown trigger vẫn hiển thị 'Top Talent' (không thay đổi)<br>- Nội dung màn hình không thay đổi | Medium |
| TC_IOS_AWARD_DETAIL_FUN_008 | Normal_Function | FUNCTION | Check component interaction | Category dropdown | Select first item in list | Người dùng đang ở Award Detail, dropdown đang mở với ít nhất 3 items | 1. Mở dropdown danh sách hạng mục<br>2. Chọn item đầu tiên trong danh sách<br>3. Quan sát nội dung màn hình cập nhật | - Dropdown đóng<br>- Nội dung màn hình cập nhật theo thông tin của item đầu tiên<br>- Dropdown trigger hiển thị tên item đầu tiên | Medium |
| TC_IOS_AWARD_DETAIL_FUN_009 | Normal_Function | FUNCTION | Check component interaction | Category dropdown | Select last item in list | Người dùng đang ở Award Detail, dropdown đang mở | 1. Mở dropdown danh sách hạng mục<br>2. Scroll xuống nếu list dài<br>3. Chọn item cuối cùng trong danh sách<br>4. Quan sát nội dung màn hình | - Dropdown đóng<br>- Nội dung màn hình cập nhật theo thông tin item cuối<br>- Dropdown trigger hiển thị tên item cuối | Medium |

---

### Function — Scrolling

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_IOS_AWARD_DETAIL_FUN_010 | Normal_Function | FUNCTION | Check scrolling | Scroll content | Header sticky on scroll down | Người dùng đang ở màn hình Award Detail | 1. Mở màn hình Award Detail<br>2. Cuộn nội dung từ từ xuống dưới<br>3. Quan sát vị trí Header khi cuộn<br>4. Cuộn xuống hết trang<br>5. Quan sát Header | - Header luôn hiển thị ở trên cùng màn hình trong suốt quá trình cuộn<br>- Header không bị cuộn theo nội dung<br>- Logo và actions vẫn accessible khi cuộn | High |
| TC_IOS_AWARD_DETAIL_FUN_011 | Normal_Function | FUNCTION | Check scrolling | Scroll content | NavBar sticky on scroll | Người dùng đang ở màn hình Award Detail | 1. Mở màn hình Award Detail<br>2. Cuộn nội dung xuống dưới<br>3. Quan sát vị trí Bottom Nav khi cuộn<br>4. Cuộn lên trên<br>5. Quan sát Bottom Nav | - Bottom Nav luôn hiển thị ở dưới cùng màn hình trong suốt quá trình cuộn<br>- Bottom Nav không bị cuộn theo nội dung<br>- 4 tabs luôn accessible | High |

---

### Function — Navigation

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_IOS_AWARD_DETAIL_FUN_012 | Normal_Function | FUNCTION | Check navigation | Sun*Kudos block | Tap Chi tiết button — navigate to Kudos | Người dùng đã cuộn xuống thấy Kudos block | 1. Mở màn hình Award Detail<br>2. Cuộn xuống đến Kudos block<br>3. Tap nút 'Chi tiết' trong Kudos block<br>4. Quan sát màn hình điều hướng đến | - Ứng dụng điều hướng sang màn hình Sun*Kudos<br>- Màn hình Kudos mở thành công<br>⚠️ Note: linkedFrameId chưa xác nhận trong Figma — cần verify screen đích với designer (BR-005) | High |
| TC_IOS_AWARD_DETAIL_FUN_013 | Normal_Function | FUNCTION | Check navigation | Bottom Nav | Tap SAA 2025 tab | Người dùng đang ở màn hình Award Detail | 1. Mở màn hình Award Detail<br>2. Tap tab 'SAA 2025' ở Bottom Nav<br>3. Quan sát màn hình điều hướng đến | Điều hướng về Home screen (SAA 2025 main screen) | High |
| TC_IOS_AWARD_DETAIL_FUN_014 | Normal_Function | FUNCTION | Check navigation | Bottom Nav | Tap Kudos tab | Người dùng đang ở màn hình Award Detail | 1. Mở màn hình Award Detail<br>2. Tap tab 'Kudos' ở Bottom Nav<br>3. Quan sát màn hình điều hướng đến | Điều hướng đến màn hình Sun*Kudos | High |
| TC_IOS_AWARD_DETAIL_FUN_015 | Normal_Function | FUNCTION | Check navigation | Bottom Nav | Tap Profile tab | Người dùng đang ở màn hình Award Detail | 1. Mở màn hình Award Detail<br>2. Tap tab 'Profile' ở Bottom Nav<br>3. Quan sát màn hình điều hướng đến | Điều hướng đến màn hình Profile bản thân | High |
| TC_IOS_AWARD_DETAIL_FUN_016 | Normal_Function | FUNCTION | Check navigation | Header | Tap notification bell | Người dùng đang ở màn hình Award Detail | 1. Mở màn hình Award Detail<br>2. Tap icon chuông ở góc phải Header<br>3. Quan sát màn hình điều hướng đến | Điều hướng đến màn hình Notifications | High |
| TC_IOS_AWARD_DETAIL_FUN_017 | Normal_Function | FUNCTION | Check navigation | Header | Tap search icon | Người dùng đang ở màn hình Award Detail | 1. Mở màn hình Award Detail<br>2. Tap icon kính lúp ở Header<br>3. Quan sát màn hình điều hướng đến | Điều hướng đến màn hình Search | Medium |

---

### Function — Language Selector

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_IOS_AWARD_DETAIL_FUN_018 | Normal_Function | FUNCTION | Check component interaction | Header — Language selector | Open inline dropdown | Người dùng đang ở màn hình Award Detail | 1. Mở màn hình Award Detail<br>2. Tap nút ngôn ngữ (icon cờ + mã ngôn ngữ) ở Header<br>3. Quan sát hành vi | - Dropdown chọn ngôn ngữ mở ra inline trên màn hình Award Detail<br>- **Không** điều hướng sang màn hình mới<br>- Danh sách ngôn ngữ (VN, EN) hiển thị | High |
| TC_IOS_AWARD_DETAIL_FUN_019 | Normal_Function | FUNCTION | Check component interaction | Header — Language selector | Switch language VN to EN — UI re-render | Người dùng đang ở màn hình Award Detail, ngôn ngữ đang là VN | 1. Mở màn hình Award Detail (ngôn ngữ VN)<br>2. Tap nút ngôn ngữ ở Header<br>3. Chọn 'EN' từ dropdown<br>4. Quan sát toàn bộ text trên màn hình | - Nút ngôn ngữ cập nhật: icon cờ Anh + text 'EN'<br>- Tất cả strings localized trên màn hình re-render sang tiếng Anh<br>- Dữ liệu từ API (tên giải, mô tả, số lượng, giá trị) không thay đổi | High |

---

## Business Rules Coverage

| Rule | ID | Covered by |
|------|-----|------------|
| Tất cả nội dung giải thưởng lấy từ API | BR-001 | FUN_001, FUN_002, FUN_003 |
| Dropdown list awards lấy từ API, không hardcode | BR-002 | FUN_005 |
| Tab Awards luôn active trên màn hình này | BR-003 | GUI_010 |
| Fallback khi image_url null | BR-004 | GUI_007, FUN_004 |
| Navigation từ Kudos "Chi tiết" (⚠️ pending confirm) | BR-005 | FUN_012 |
| Header sticky | BR-006 | FUN_010 |
| NavBar sticky | BR-007 | FUN_011 |
