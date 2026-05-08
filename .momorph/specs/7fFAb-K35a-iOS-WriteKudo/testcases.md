# Test Cases: [iOS] Sun*Kudos_Viết Kudo_default

## Screen Info

| Property | Value |
|----------|-------|
| **Screen ID** | `7fFAb-K35a` |
| **Figma File Key** | `9ypp4enmFmdK3YAFJLIu6C` |
| **Figma Node ID** | `6885:9271` |
| **Figma Link** | https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/7fFAb-K35a |
| **Spec File** | `.momorph/specs/7fFAb-K35a-iOS-WriteKudo/spec.md` |
| **Last Updated** | 2026-05-07 |

---

## Design Items Covered

| # | Item Name | Node ID | Type | Purpose |
|---|-----------|---------|------|---------|
| A | Header Text | `6885:9271` | Text | "Gửi lời cám ơn và ghi nhận đến đồng đội" — static label |
| B.1 | Label Người nhận | — | Text | Required field label with `*` |
| B.2 | Recipient Search Dropdown | `6885:9297` | Dropdown | Tìm kiếm và chọn người nhận; opens overlay |
| B.3 | Label Danh hiệu | — | Text | Required field label with `*` |
| B.4 | Danh hiệu (Title) Input | — | Text Field | Free-text title; max 100 chars |
| B.5 | Tiêu chuẩn cộng đồng | `6885:9303` | Link | Navigates to community standards screen |
| C | Formatting Toolbar | — | Toolbar | 6 buttons: Bold, Italic, Strikethrough, Numbered List, Link, Quote |
| D | Message Textarea | — | Rich Text Editor | Free-text message; max 1000 chars; supports @mention |
| D.1 | Mention Hint | — | Text | Hint below textarea: `"@ + tên"` |
| E.1 | Label Hashtag | — | Text | Required field label with `*` |
| E.2 | Tag Group / Hashtag Picker | `6885:9328` | Tag Group | Add/remove hashtag chips; max 5; opens picker overlay |
| F.1 | Label Image | — | Text | Optional field label |
| F.2–F.4 | Image Thumbnails | — | Image | Uploaded image previews with `×` remove button |
| F.5 | Add Image Button | — | Button | Opens file picker; hidden when 5 images attached |
| G | Anonymous Toggle | — | Checkbox | Gửi lời cám ơn và ghi nhận ẩn danh; default unchecked |
| H | Cancel Button | `6891:16834` | Button | "Hủy ✕" — always enabled; dirty-check on tap |
| I | Send Button | `6891:16833` | Button | "Gửi đi ▶" — disabled until all required fields filled |

---

## Test Case Summary

| Category | Count |
|----------|-------|
| Access Control & Security | 5 |
| User Interface (GUI) | 8 |
| Function — Data Validation | 17 |
| Function — Component Interaction | 22 |
| Function — State Transition | 8 |
| Function — Business Logic | 13 |
| Function — Navigation | 3 |
| **Total** | **78** |

---

## Test Cases

### Access Control & Security

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_VIETKUDO_ACC_001 | Access control and security | ACCESSING | Check access permission | Authenticated user | Access Viết Kudo screen | Người dùng đã đăng nhập vào Sun*Kudos | 1. Mở ứng dụng Sun*Kudos<br>2. Điều hướng đến màn hình Viết Kudo | Màn hình Viết Kudo hiển thị thành công | High |
| TC_VIETKUDO_ACC_002 | Access control and security | ACCESSING | Check access permission | Unauthenticated user | Redirect to login | Người dùng chưa đăng nhập | 1. Mở ứng dụng Sun*Kudos mà chưa đăng nhập<br>2. Cố gắng truy cập màn hình Viết Kudo | Người dùng bị chuyển hướng đến màn hình đăng nhập | High |
| TC_VIETKUDO_ACC_003 | Access control and security | ACCESSING | Check navigation path | From main screen | Via navigation menu | Người dùng đã đăng nhập, đang ở màn hình chính | 1. Mở màn hình chính Sun*Kudos<br>2. Nhấn vào menu/nút tạo Kudo | Màn hình Viết Kudo hiển thị | Medium |
| TC_VIETKUDO_ACC_004 | Access control and security | ACCESSING | Check navigation path | From Home screen | Via FAB button | Người dùng đã đăng nhập, đang ở màn hình Home | 1. Mở màn hình Home<br>2. Nhấn vào nút FAB (icon bút chì) | Màn hình Viết Kudo hiển thị với form trống (không có recipient được prefill) | Medium |
| TC_VIETKUDO_ACC_005 | Access control and security | ACCESSING | Check navigation path | From Search Sunner screen | Prefilled recipient | Người dùng đã đăng nhập, đang ở màn hình Search Sunner với kết quả tìm kiếm | 1. Mở màn hình Search Sunner<br>2. Tìm kiếm và nhấn vào một user trong kết quả | Màn hình Viết Kudo mở ra<br>Trường Người nhận (B.2) hiển thị tên của user đã chọn (không hiển thị placeholder "Tìm kiếm")<br>Các trường còn lại ở trạng thái trống | High |

---

### User Interface (GUI)

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_VIETKUDO_GUI_001 | User interface | GUI | Check layout | Screen-wide layout | Overall structure | Người dùng đã đăng nhập, mở màn hình Viết Kudo | 1. Mở màn hình Viết Kudo<br>2. Quan sát bố cục tổng thể<br>3. Cuộn xuống kiểm tra toàn bộ form | Header text "Gửi lời cám ơn và ghi nhận đến đồng đội" hiển thị ở đầu<br>Label "Người nhận \*" và dropdown hiển thị<br>Label "Danh hiệu \*" và input hiển thị<br>Link "Tiêu chuẩn cộng đồng" hiển thị<br>Formatting toolbar với 6 nút (Bold, Italic, Strikethrough, Numbered List, Link, Quote) hiển thị<br>Message textarea hiển thị<br>Hint mention hiển thị bên dưới textarea<br>Label "Hashtag \*" và tag group hiển thị<br>Image section với label và nút thêm hiển thị<br>Anonymous checkbox hiển thị<br>Nút "Hủy" và "Gửi đi" hiển thị | High |
| TC_VIETKUDO_GUI_002 | User interface | GUI | Initialize | Recipient Search Dropdown | Placeholder text | Người dùng mở màn hình Viết Kudo | 1. Mở màn hình Viết Kudo<br>2. Quan sát dropdown Người nhận | Placeholder "Tìm kiếm" hiển thị<br>Icon chevron down hiển thị | Medium |
| TC_VIETKUDO_GUI_003 | User interface | GUI | Initialize | Danh hiệu Input | Placeholder text | Người dùng mở màn hình Viết Kudo | 1. Mở màn hình Viết Kudo<br>2. Quan sát input Danh hiệu | Placeholder "Danh tặng một danh hiệu cho..." hiển thị | Medium |
| TC_VIETKUDO_GUI_004 | User interface | GUI | Initialize | Message Textarea | Placeholder text | Người dùng mở màn hình Viết Kudo | 1. Mở màn hình Viết Kudo<br>2. Quan sát textarea Message | Placeholder "Hãy gửi gắm lời cám ơn và ghi nhận đến đồng đội tại đây nhé!" hiển thị<br>Hint "Bạn có thể \"@ + tên\" để nhắc tới đồng nghiệp khác" hiển thị bên dưới | Medium |
| TC_VIETKUDO_GUI_005 | User interface | GUI | Initialize | Anonymous Toggle | Default value/state | Người dùng mở màn hình Viết Kudo | 1. Mở màn hình Viết Kudo<br>2. Quan sát checkbox Anonymous | Checkbox ở trạng thái unchecked (tắt) mặc định<br>Label "Gửi lời cám ơn và ghi nhận ẩn danh" hiển thị | Medium |
| TC_VIETKUDO_GUI_006 | User interface | GUI | Initialize | Send Button | Default disabled state | Người dùng mở màn hình Viết Kudo mới (chưa có dữ liệu) | 1. Mở màn hình Viết Kudo<br>2. Quan sát trạng thái nút "Gửi đi" (I) | Nút "Gửi đi" ở trạng thái disabled<br>Visually dimmed/greyed out, không thể nhấn | High |
| TC_VIETKUDO_GUI_007 | User interface | GUI | Initialize | Message Textarea | Character counter initial display | Người dùng mở màn hình Viết Kudo | 1. Mở màn hình Viết Kudo<br>2. Quan sát vùng bên cạnh textarea Message | Character counter hiển thị "0/1000" | Medium |
| TC_VIETKUDO_GUI_008 | User interface | GUI | Check layout | Bottom Action Bar | Always visible | Người dùng mở màn hình Viết Kudo với form có thể cuộn | 1. Mở màn hình Viết Kudo<br>2. Cuộn form card lên và xuống | Nút "Hủy" (H) và "Gửi đi" (I) luôn hiển thị cố định ở dưới cùng màn hình khi cuộn | High |

---

### Function — Data Validation

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_VIETKUDO_FUN_001 | Abnormal_Others | FUNCTION | Check data validation | Recipient Search Dropdown | Required | Người dùng mở form Viết Kudo, đã điền Title, Message, Hashtag hợp lệ | 1. Mở form Viết Kudo<br>2. Điền đầy đủ Title, Message, Hashtag<br>3. Không chọn Người nhận<br>4. Nhấn "Gửi đi" | Hiển thị lỗi "Please select a recipient." | High |
| TC_VIETKUDO_FUN_002 | Abnormal_Others | FUNCTION | Check data validation | Recipient Search Dropdown | Allowed values | Người dùng đăng nhập với tài khoản A | 1. Mở form Viết Kudo<br>2. Mở dropdown Người nhận<br>3. Tìm và chọn chính mình | Hiển thị lỗi "You cannot send a kudo to yourself." | High |
| TC_VIETKUDO_FUN_003 | Abnormal_Others | FUNCTION | Check data validation | Danh hiệu Input | Required | Người dùng mở form, đã điền Recipient, Message, Hashtag | 1. Mở form Viết Kudo<br>2. Điền Recipient, Message, Hashtag<br>3. Để trống Danh hiệu<br>4. Nhấn "Gửi đi" | Hiển thị lỗi "Please enter a title for this recognition." | High |
| TC_VIETKUDO_FUN_004 | Abnormal_Others | FUNCTION | Check data validation | Danh hiệu Input | Max length | Người dùng mở form Viết Kudo | 1. Mở form Viết Kudo<br>2. Nhập Danh hiệu với 101 ký tự<br>3. Nhấn "Gửi đi" | Hiển thị lỗi "Title is too long (max 100 characters)." | High |
| TC_VIETKUDO_FUN_005 | Normal_Others | FUNCTION | Check data validation | Danh hiệu Input | Max length | Người dùng mở form Viết Kudo | 1. Mở form Viết Kudo<br>2. Nhập Danh hiệu với đúng 100 ký tự | Giá trị được chấp nhận, không hiển thị lỗi | Medium |
| TC_VIETKUDO_FUN_006 | Abnormal_Others | FUNCTION | Check data validation | Message Textarea | Required | Người dùng mở form, đã điền Recipient, Title, Hashtag | 1. Mở form Viết Kudo<br>2. Điền Recipient, Title, Hashtag<br>3. Để trống Message<br>4. Nhấn "Gửi đi" | Hiển thị lỗi "Please write your recognition message." | High |
| TC_VIETKUDO_FUN_007 | Abnormal_Others | FUNCTION | Check data validation | Message Textarea | Max length | Người dùng mở form Viết Kudo | 1. Mở form Viết Kudo<br>2. Nhập Message với 1001 ký tự | Hiển thị lỗi "Character limit reached. Please shorten your message." | High |
| TC_VIETKUDO_FUN_008 | Normal_Others | FUNCTION | Check data validation | Message Textarea | Max length | Người dùng mở form Viết Kudo | 1. Mở form Viết Kudo<br>2. Nhập Message với đúng 1000 ký tự | Giá trị được chấp nhận, không hiển thị lỗi | Medium |
| TC_VIETKUDO_FUN_009 | Abnormal_Others | FUNCTION | Check data validation | Message Textarea | Nullable/Empty handling | Người dùng mở form Viết Kudo | 1. Mở form Viết Kudo<br>2. Nhập Message chỉ với khoảng trắng<br>3. Nhấn "Gửi đi" | Hiển thị lỗi "Message cannot be empty." | High |
| TC_VIETKUDO_FUN_010 | Abnormal_Others | FUNCTION | Check data validation | Hashtag Section | Required | Người dùng mở form, đã điền Recipient, Title, Message | 1. Mở form Viết Kudo<br>2. Điền Recipient, Title, Message<br>3. Không thêm hashtag<br>4. Nhấn "Gửi đi" | Hiển thị lỗi "Please add at least one hashtag." | High |
| TC_VIETKUDO_FUN_011 | Abnormal_Others | FUNCTION | Check data validation | Hashtag Section | Max length | Người dùng mở form Viết Kudo | 1. Mở form Viết Kudo<br>2. Thêm 5 hashtag<br>3. Cố gắng thêm hashtag thứ 6 | Hiển thị lỗi "Maximum 5 hashtags allowed." hoặc nút thêm bị disabled | High |
| TC_VIETKUDO_FUN_012 | Abnormal_Others | FUNCTION | Check data validation | Link Insert | Format | Người dùng đang soạn tin nhắn | 1. Chọn text trong textarea<br>2. Nhấn nút Link (C.5)<br>3. Nhập URL không hợp lệ<br>4. Xác nhận | URL không hợp lệ bị từ chối hoặc hiển thị lỗi format | Medium |
| TC_VIETKUDO_FUN_049 | Abnormal_Others | FUNCTION | Check data validation | Image Section | File type | Người dùng mở form Viết Kudo, chưa có ảnh nào | 1. Nhấn nút "+ Image"<br>2. Chọn file PDF hoặc file không phải ảnh jpg/png/webp từ picker | File bị từ chối<br>Hiển thị thông báo lỗi định dạng file không hỗ trợ<br>Thumbnail không được thêm | High |
| TC_VIETKUDO_FUN_050 | Abnormal_Others | FUNCTION | Check data validation | Image Section | File size | Người dùng mở form Viết Kudo, chưa có ảnh nào | 1. Nhấn nút "+ Image"<br>2. Chọn file ảnh có kích thước vượt quá 10 MB | File bị từ chối<br>Hiển thị thông báo lỗi file quá lớn (max 10 MB)<br>Thumbnail không được thêm | High |
| TC_VIETKUDO_FUN_064 | Normal_Others | FUNCTION | Check data validation | Danh hiệu Input | Min length | Người dùng mở form Viết Kudo | 1. Nhấn vào input Danh hiệu<br>2. Nhập đúng 1 ký tự<br>3. Điền các trường còn lại<br>4. Nhấn "Gửi đi" | Giá trị 1 ký tự được chấp nhận<br>Không hiển thị lỗi validation cho trường Danh hiệu | Medium |
| TC_VIETKUDO_FUN_065 | Normal_Others | FUNCTION | Check data validation | Message Textarea | Min length | Người dùng mở form Viết Kudo | 1. Nhấn vào textarea Message<br>2. Nhập đúng 1 ký tự không phải khoảng trắng<br>3. Điền các trường còn lại<br>4. Nhấn "Gửi đi" | Giá trị 1 ký tự được chấp nhận<br>Không hiển thị lỗi validation cho trường Message | Medium |

---

### Function — Component Interaction

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_VIETKUDO_FUN_013 | Normal_Others | FUNCTION | Check component interaction | Recipient Search Dropdown | Open | Người dùng mở form Viết Kudo | 1. Nhấn vào dropdown Người nhận | Dropdown mở ra hiển thị danh sách user | High |
| TC_VIETKUDO_FUN_014 | Normal_Others | FUNCTION | Check component interaction | Recipient Search Dropdown | Search | Dropdown Người nhận đang mở | 1. Mở dropdown Người nhận<br>2. Nhập từ khóa tìm kiếm (tên hoặc phòng ban) | Danh sách được lọc theo từ khóa tìm kiếm | High |
| TC_VIETKUDO_FUN_015 | Normal_Others | FUNCTION | Check component interaction | Recipient Search Dropdown | Select | Dropdown Người nhận đang mở với danh sách | 1. Mở dropdown Người nhận<br>2. Chọn một người từ danh sách | Người nhận được chọn hiển thị trong dropdown | High |
| TC_VIETKUDO_FUN_016 | Normal_Others | FUNCTION | Check component interaction | Danh hiệu Input | Input | Người dùng mở form Viết Kudo | 1. Nhấn vào input Danh hiệu<br>2. Nhập text | Cursor và bàn phím hiển thị<br>Text được nhập thành công | Medium |
| TC_VIETKUDO_FUN_017 | Normal_Others | FUNCTION | Check component interaction | Awards Information Navigation Link | Navigate | Người dùng mở form Viết Kudo | 1. Nhấn vào link "Tiêu chuẩn cộng đồng" | Trang tiêu chuẩn cộng đồng/awards info mở ra | Medium |
| TC_VIETKUDO_FUN_018 | Normal_Others | FUNCTION | Check component interaction | Formatting Toolbar - Bold | Toggle | Người dùng đang soạn tin nhắn, đã chọn text | 1. Chọn text trong textarea<br>2. Nhấn nút Bold (B) | Text được chọn hiển thị in đậm<br>Icon Bold ở trạng thái active | Medium |
| TC_VIETKUDO_FUN_019 | Normal_Others | FUNCTION | Check component interaction | Formatting Toolbar - Italic | Toggle | Người dùng đang soạn tin nhắn, đã chọn text | 1. Chọn text trong textarea<br>2. Nhấn nút Italic (I) | Text được chọn hiển thị in nghiêng<br>Icon Italic ở trạng thái active | Medium |
| TC_VIETKUDO_FUN_020 | Normal_Others | FUNCTION | Check component interaction | Formatting Toolbar - Strikethrough | Toggle | Người dùng đang soạn tin nhắn, đã chọn text | 1. Chọn text trong textarea<br>2. Nhấn nút Strikethrough | Text được chọn hiển thị gạch ngang<br>Icon Strikethrough ở trạng thái active | Medium |
| TC_VIETKUDO_FUN_021 | Normal_Others | FUNCTION | Check component interaction | Formatting Toolbar - Numbered List | Toggle | Người dùng đang soạn tin nhắn | 1. Đặt cursor vào textarea<br>2. Nhấn nút Numbered List | Text chuyển sang định dạng danh sách đánh số<br>Icon Numbered List ở trạng thái active | Medium |
| TC_VIETKUDO_FUN_022 | Normal_Others | FUNCTION | Check component interaction | Formatting Toolbar - Link | Open | Người dùng đang soạn tin nhắn | 1. Chọn text trong textarea<br>2. Nhấn nút Link<br>3. Nhập URL hợp lệ vào dialog<br>4. Xác nhận | Dialog/inline input hiển thị<br>Link được chèn vào text thành công | Medium |
| TC_VIETKUDO_FUN_023 | Normal_Others | FUNCTION | Check component interaction | Formatting Toolbar - Quote | Toggle | Người dùng đang soạn tin nhắn | 1. Đặt cursor vào đoạn văn<br>2. Nhấn nút Quote | Đoạn văn hiển thị dạng blockquote<br>Icon Quote ở trạng thái active | Medium |
| TC_VIETKUDO_FUN_024 | Normal_Others | FUNCTION | Check component interaction | Message Textarea | Input | Người dùng mở form Viết Kudo | 1. Nhấn vào textarea Message<br>2. Nhập nội dung tin nhắn | Cursor và bàn phím hiển thị<br>Text được nhập thành công | High |
| TC_VIETKUDO_FUN_025 | Normal_Others | FUNCTION | Check component interaction | Message Textarea | @mention | Người dùng đang soạn tin nhắn | 1. Nhấn vào textarea<br>2. Nhập "@" theo sau bởi tên đồng nghiệp | Gợi ý @mention hiển thị danh sách đồng nghiệp phù hợp | High |
| TC_VIETKUDO_FUN_026 | Normal_Others | FUNCTION | Check component interaction | Tag Group | Add tag | Người dùng mở form Viết Kudo | 1. Nhấn nút "+ Hashtag"<br>2. Nhập/chọn hashtag | Hashtag được thêm dạng chip/pill với nút 'x' | High |
| TC_VIETKUDO_FUN_027 | Normal_Others | FUNCTION | Check component interaction | Tag Group | Remove tag | Đã có ít nhất 1 hashtag | 1. Nhấn nút 'x' trên chip hashtag | Hashtag bị xóa khỏi danh sách | High |
| TC_VIETKUDO_FUN_028 | Normal_Others | FUNCTION | Check component interaction | Image Section | Upload | Người dùng mở form Viết Kudo | 1. Nhấn nút "+ Image"<br>2. Chọn ảnh từ file picker | Ảnh được upload, thumbnail hiển thị với nút 'x' | Medium |
| TC_VIETKUDO_FUN_029 | Normal_Others | FUNCTION | Check component interaction | Image Thumbnail | Remove | Đã upload ít nhất 1 ảnh | 1. Nhấn nút 'x' trên thumbnail ảnh | Ảnh bị xóa khỏi danh sách đính kèm | Medium |
| TC_VIETKUDO_FUN_030 | Normal_Others | FUNCTION | Check component interaction | Anonymous Toggle | Toggle | Người dùng mở form Viết Kudo | 1. Nhấn vào checkbox ẩn danh<br>2. Xác nhận trạng thái checked<br>3. Nhấn lại để tắt<br>4. Xác nhận trạng thái unchecked | Nhấn lần 1: checkbox checked (bật ẩn danh)<br>Nhấn lần 2: checkbox unchecked (tắt ẩn danh) | Medium |
| TC_VIETKUDO_FUN_031 | Normal_Others | FUNCTION | Check component interaction | Cancel Button | Cancel | Người dùng mở form Viết Kudo, chưa nhập gì | 1. Mở form Viết Kudo<br>2. Nhấn nút "Hủy" | Quay về màn hình trước không cần xác nhận | High |
| TC_VIETKUDO_FUN_032 | Normal_Others | FUNCTION | Check component interaction | Send Button | Submit | Người dùng mở form Viết Kudo | 1. Chọn Người nhận<br>2. Nhập Danh hiệu<br>3. Nhập Message<br>4. Thêm Hashtag<br>5. Nhấn "Gửi đi" | Loading spinner hiển thị<br>Submit thành công<br>Chuyển đến màn hình thành công | High |
| TC_VIETKUDO_FUN_055 | Normal_Others | FUNCTION | Check component interaction | Message Textarea | @mention select | @mention suggestion list đang hiển thị sau khi nhập "@Nguyễn" | 1. Nhấn vào textarea<br>2. Nhập "@Nguyễn"<br>3. Chờ danh sách gợi ý xuất hiện<br>4. Nhấn vào "Nguyễn Văn A" trong danh sách | Mention "@Nguyễn Văn A" được chèn vào vị trí cursor trong textarea<br>Danh sách gợi ý đóng lại<br>Cursor đặt sau mention đã chèn | High |
| TC_VIETKUDO_FUN_056 | Abnormal_Others | FUNCTION | Check component interaction | Message Textarea | @mention no results | Textarea đang active | 1. Nhấn vào textarea<br>2. Nhập "@xyznotexist" (tên không tồn tại) | Overlay gợi ý @mention hiển thị trạng thái trống (không có kết quả)<br>Giá trị textarea không thay đổi | Medium |
| TC_VIETKUDO_FUN_057 | Abnormal_Others | FUNCTION | Check component interaction | Recipient Search Dropdown | No results | Dropdown Người nhận đang mở | 1. Nhấn vào dropdown Người nhận<br>2. Nhập từ khóa không khớp bất kỳ user nào | Overlay hiển thị trạng thái trống (empty state message)<br>Giá trị trường Người nhận không thay đổi | Medium |
| TC_VIETKUDO_FUN_058 | Normal_Others | FUNCTION | Check component interaction | Tag Group | Close picker without selecting | Người dùng mở form Viết Kudo, chưa có hashtag nào | 1. Nhấn nút "+ Hashtag"<br>2. Overlay picker hashtag mở ra<br>3. Đóng overlay mà không chọn hashtag nào (back hoặc swipe down) | Overlay đóng lại<br>Danh sách hashtag không thay đổi (vẫn trống)<br>Nút "+ Hashtag" vẫn ở trạng thái enabled | Medium |
| TC_VIETKUDO_FUN_059 | Normal_Others | FUNCTION | Check component interaction | Message Textarea | Character counter update | Người dùng mở form Viết Kudo | 1. Nhấn vào textarea Message<br>2. Nhập 10 ký tự<br>3. Quan sát character counter | Character counter cập nhật từ "0/1000" thành "10/1000" ngay lập tức sau khi nhập | Medium |

---

### Function — State Transition

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_VIETKUDO_FUN_033 | Normal_Others | FUNCTION | Check state transition | Send Button | Disabled -> Enabled | Người dùng mở form Viết Kudo mới (trống) | 1. Mở form Viết Kudo (nút Gửi disabled)<br>2. Điền Recipient<br>3. Điền Title<br>4. Điền Message<br>5. Thêm Hashtag | Nút Gửi đi chuyển sang enabled khi tất cả trường bắt buộc đã được điền | High |
| TC_VIETKUDO_FUN_034 | Normal_Others | FUNCTION | Check state transition | Tag Group - Add button | Enabled -> Disabled | Người dùng đã thêm 4 hashtag | 1. Thêm hashtag thứ 5<br>2. Quan sát nút "+ Hashtag" | Nút "+ Hashtag" bị disabled sau khi đạt 5 tags | High |
| TC_VIETKUDO_FUN_035 | Normal_Others | FUNCTION | Check state transition | Image Section - Add button | Shown -> Hidden | Người dùng đã upload 4 ảnh | 1. Upload ảnh thứ 5<br>2. Quan sát nút "+ Image" | Nút "+ Image" bị ẩn sau khi đạt 5 ảnh | High |
| TC_VIETKUDO_FUN_036 | Abnormal_Others | FUNCTION | Check state transition | Recipient Search Dropdown | Enabled -> Disabled | Danh sách user không khả dụng | 1. Mở form Viết Kudo khi danh sách user không load được<br>2. Quan sát dropdown Người nhận | Dropdown ở trạng thái disabled | Medium |
| TC_VIETKUDO_FUN_037 | Normal_Others | FUNCTION | Check state transition | Formatting Toolbar buttons | Inactive -> Active | Người dùng đang soạn tin nhắn, chọn text | 1. Chọn text đã có bold formatting<br>2. Quan sát nút Bold trên toolbar | Nút Bold hiển thị ở trạng thái active | Medium |
| TC_VIETKUDO_FUN_060 | Normal_Others | FUNCTION | Check state transition | Form Submission | Loading - inputs disabled | Form đã điền đầy đủ hợp lệ | 1. Điền đầy đủ form<br>2. Nhấn "Gửi đi"<br>3. Trong khi loading spinner hiển thị, thử tương tác với các trường form | Nút "Gửi đi" hiển thị loading spinner<br>Các trường input bị disabled trong quá trình submit<br>Nút "Hủy" vẫn enabled | High |
| TC_VIETKUDO_FUN_063 | Normal_Others | FUNCTION | Check state transition | Send Button | Enabled -> Disabled after removing last hashtag | Form đã điền đầy đủ tất cả trường bắt buộc, nút Gửi đang enabled | 1. Điền đầy đủ form (Recipient, Title, Message, 1 Hashtag)<br>2. Xác nhận nút "Gửi đi" enabled<br>3. Nhấn 'x' để xóa hashtag duy nhất | Hashtag bị xóa<br>Nút "Gửi đi" chuyển về trạng thái disabled | High |
| TC_VIETKUDO_FUN_038 | Abnormal_Others | FUNCTION | Check business logic | Form Submission | Validate all required fields | Người dùng mở form Viết Kudo mới | 1. Mở form Viết Kudo<br>2. Không điền gì<br>3. Kiểm tra trạng thái nút Gửi | Nút Gửi disabled hoặc hiển thị lỗi cho các trường bắt buộc | High |

---

### Function — Business Logic

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_VIETKUDO_FUN_039 | Normal_Others | FUNCTION | Check business logic | Form Submission | Success flow | Form đã điền đầy đủ và hợp lệ | 1. Điền đầy đủ form với ảnh và bật ẩn danh<br>2. Nhấn "Gửi đi" | Loading spinner hiển thị<br>Kudo gửi thành công<br>Chuyển đến màn hình thành công | High |
| TC_VIETKUDO_FUN_040 | Abnormal_Others | FUNCTION | Check business logic | Form Submission | Error flow | Form hợp lệ nhưng server trả lỗi | 1. Điền đầy đủ form hợp lệ<br>2. Nhấn "Gửi đi" (server trả lỗi) | Hiển thị error message<br>Form giữ nguyên dữ liệu để retry | High |
| TC_VIETKUDO_FUN_041 | Abnormal_Others | FUNCTION | Check business logic | Self-send Prevention | Recipient is current user | Người dùng đăng nhập | 1. Mở dropdown Người nhận<br>2. Tìm và chọn chính mình | Hiển thị lỗi "You cannot send a kudo to yourself." | High |
| TC_VIETKUDO_FUN_042 | Normal_Others | FUNCTION | Check business logic | Cancel with Unsaved Content | Show confirmation dialog | Người dùng đã nhập một số dữ liệu vào form | 1. Nhập một số dữ liệu vào form<br>2. Nhấn nút "Hủy" | Dialog xác nhận hiển thị hỏi có muốn hủy bỏ | High |
| TC_VIETKUDO_FUN_043 | Normal_Others | FUNCTION | Check business logic | Cancel with Empty Form | Navigate back directly | Form Viết Kudo trống | 1. Mở form Viết Kudo<br>2. Không nhập gì<br>3. Nhấn nút "Hủy" | Quay về màn hình trước không hiển thị dialog xác nhận | Medium |
| TC_VIETKUDO_FUN_044 | Normal_Others | FUNCTION | Check business logic | Anonymous Mode | Checked - anonymous | Form đã điền đầy đủ | 1. Điền đầy đủ form<br>2. Bật checkbox ẩn danh<br>3. Nhấn "Gửi đi" | Kudo gửi thành công ở chế độ ẩn danh<br>Tên người gửi không hiển thị | High |
| TC_VIETKUDO_FUN_045 | Normal_Others | FUNCTION | Check business logic | Anonymous Mode | Unchecked - visible sender | Form đã điền đầy đủ | 1. Điền đầy đủ form<br>2. Giữ checkbox ẩn danh ở trạng thái mặc định<br>3. Nhấn "Gửi đi" | Kudo gửi thành công<br>Tên người gửi hiển thị | High |
| TC_VIETKUDO_FUN_051 | Normal_Others | FUNCTION | Check business logic | Cancel with Empty Form | System back button - navigate back directly | Form Viết Kudo trống, chưa có dữ liệu nào được nhập | 1. Mở form Viết Kudo<br>2. Không nhập bất kỳ dữ liệu nào<br>3. Nhấn nút back của hệ thống (system back button) | Ứng dụng quay về màn hình trước mà không hiển thị dialog xác nhận | High |
| TC_VIETKUDO_FUN_052 | Normal_Others | FUNCTION | Check business logic | Cancel with Unsaved Content | System back button - show confirmation dialog | Người dùng đã nhập ít nhất một trường trong form | 1. Nhập ít nhất một trường trong form<br>2. Nhấn nút back của hệ thống | Dialog xác nhận hiển thị: "Bạn có chắc muốn hủy không? Dữ liệu đã nhập sẽ bị mất."<br>Form không bị đóng cho đến khi người dùng xác nhận | High |
| TC_VIETKUDO_FUN_053 | Normal_Others | FUNCTION | Check business logic | Cancel with Unsaved Content | Confirm cancel - navigate back | Dialog xác nhận hủy đang hiển thị | 1. Nhập dữ liệu vào form<br>2. Nhấn "Hủy" để mở dialog<br>3. Nhấn nút Confirm (Xác nhận) trong dialog | Dialog đóng lại<br>Form bị xóa<br>Ứng dụng quay về màn hình trước | High |
| TC_VIETKUDO_FUN_054 | Normal_Others | FUNCTION | Check business logic | Cancel with Unsaved Content | Dismiss cancel - stay on form | Dialog xác nhận hủy đang hiển thị | 1. Nhập dữ liệu vào form<br>2. Nhấn "Hủy" để mở dialog<br>3. Nhấn nút Dismiss (Hủy bỏ) hoặc nhấn ngoài dialog để đóng | Dialog đóng lại<br>Form vẫn hiển thị với dữ liệu đã nhập còn nguyên vẹn<br>Người dùng tiếp tục ở màn hình Viết Kudo | High |
| TC_VIETKUDO_FUN_061 | Abnormal_Others | FUNCTION | Check business logic | Form Submission | Session expired - redirect to login | Token người dùng đã hết hạn, form đã điền đầy đủ | 1. Điền đầy đủ form hợp lệ<br>2. Nhấn "Gửi đi"<br>3. Server trả về lỗi 401 (token hết hạn) | Session bị xóa<br>Người dùng bị chuyển hướng đến màn hình đăng nhập | High |
| TC_VIETKUDO_FUN_062 | Normal_Others | FUNCTION | Check business logic | Form Submission | Prefill recipient - partial form state | Navigate đến Viết Kudo từ Search Sunner với recipient được prefill | 1. Từ màn hình Search Sunner, nhấn vào user Nguyễn Văn A<br>2. Màn hình Viết Kudo mở ra với recipient đã được điền<br>3. Quan sát trạng thái nút "Gửi đi" | Trường Người nhận hiển thị "Nguyễn Văn A"<br>Nút "Gửi đi" vẫn disabled (vì Title, Message, Hashtag chưa được điền)<br>FormDirty = false tại thời điểm mở màn hình (prefill không kích hoạt dirty check) | High |

---

### Function — Navigation

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_VIETKUDO_FUN_046 | Normal_Others | FUNCTION | Check navigation behavior | Awards Information Link | Community standards page | Người dùng mở form Viết Kudo | 1. Nhấn link "Tiêu chuẩn cộng đồng" | Trang tiêu chuẩn cộng đồng hiển thị | Medium |
| TC_VIETKUDO_FUN_047 | Normal_Others | FUNCTION | Check navigation behavior | Cancel Button | Previous screen | Người dùng mở form Viết Kudo | 1. Nhấn nút "Hủy"<br>2. Xác nhận hủy (nếu có dialog) | Quay về màn hình trước đó | High |
| TC_VIETKUDO_FUN_048 | Normal_Others | FUNCTION | Check navigation behavior | Send Button | Success screen | Form đã điền đầy đủ và hợp lệ | 1. Điền đầy đủ form<br>2. Nhấn "Gửi đi"<br>3. Chờ submit thành công | Chuyển đến màn hình thành công/xác nhận | High |
