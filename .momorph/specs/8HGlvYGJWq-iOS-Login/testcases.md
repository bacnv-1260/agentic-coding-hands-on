# Test Cases: [iOS] Login

## Screen Info

| Property | Value |
|----------|-------|
| **Screen ID** | `8HGlvYGJWq` |
| **Figma File Key** | `9ypp4enmFmdK3YAFJLIu6C` |
| **Figma Node ID** | `6885:8963` |
| **Figma Link** | https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/8HGlvYGJWq |
| **Spec File** | `.momorph/contexts/screen_specs/ios_login.md` |
| **Server Group ID** | `714` |
| **Last Updated** | 2026-05-05 (tasks created; spec diffs applied — EN/VN exact strings added) |

---

## Design Items Covered

| # | Item Name | Node ID | Type | Purpose |
|---|-----------|---------|------|---------|
| 1 | `mms_2_mm_media_logo` | `6885:8977` | Image | Sun* Annual Awards logo — static display |
| 2 | `mms_2.1_language` | `6885:8976` | Dropdown | Language selector (VN / EN / JA) |
| 3 | `mms_3_MM_MEDIA_Logo/RootFuther` | `6885:8967` | Image | "ROOT FURTHER" branding text — static |
| 4 | `mms_4_content` | `6885:8968` | Text | Localized description text |
| 5 | `mms_5_Button` | `6885:8969` | Button (icon_text) | "LOGIN With Google" — Google OAuth entry point |
| 6 | `mms_6_Copyright` | `6885:8971` | Text | Copyright text — static, localized |

---

## Test Case Summary

| Category | Count |
|----------|-------|
| Access Control & Security | 3 |
| User Interface (GUI) | 8 |
| Function — Language Selector | 9 |
| Function — Login (Normal) | 12 |
| Function — Login (Abnormal) | 5 |
| **Total** | **37** |

---

## Test Cases

### Access Control & Security

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_LOGIN_ACC_001 | Access control and security | ACCESSING | Check access permission | Unauthenticated user | Access login screen | Người dùng chưa đăng nhập, ứng dụng đã cài đặt | 1. Mở ứng dụng SAA 2025<br>2. Quan sát màn hình hiển thị | Màn hình Login hiển thị thành công | High |
| TC_LOGIN_ACC_002 | Access control and security | ACCESSING | Check access permission | Authenticated user | Redirect from login screen | Người dùng đã đăng nhập thành công trước đó, token còn hiệu lực | 1. Mở ứng dụng SAA 2025 khi đã đăng nhập<br>2. Quan sát màn hình hiển thị | Người dùng được chuyển hướng đến Home screen | High |
| TC_LOGIN_ACC_003 | Access control and security | ACCESSING | Check authentication | Google OAuth | Authentication required | Người dùng chưa đăng nhập | 1. Mở ứng dụng<br>2. Quan sát màn hình Login<br>3. Xác minh chỉ có phương thức đăng nhập Google | Chỉ hiển thị nút Login with Google, không có cách truy cập khác vào ứng dụng | High |

---

### User Interface (GUI)

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_LOGIN_GUI_001 | User interface | GUI | Check layout | Screen-wide layout | Overall structure | Người dùng chưa đăng nhập, mở ứng dụng | 1. Mở ứng dụng SAA 2025<br>2. Quan sát bố cục tổng thể màn hình Login<br>3. Kiểm tra vị trí và khả năng hiển thị từng thành phần | Logo Sun* Annual Awards hiển thị phía trên cùng<br>Text "ROOT FURTHER" hiển thị dưới logo<br>Language Selection (cờ + mã ngôn ngữ + mũi tên dropdown) hiển thị góc trên màn hình<br>Description text hiển thị ở giữa màn hình<br>Nút "LOGIN With Google" với icon Google hiển thị phía dưới<br>Copyright text hiển thị ở cuối màn hình | High |
| TC_LOGIN_GUI_002 | User interface | GUI | Initialize | Language Selection | Default value/state | Người dùng chưa đăng nhập, mở ứng dụng lần đầu | 1. Mở ứng dụng SAA 2025<br>2. Quan sát Language Selection<br>3. Kiểm tra giá trị mặc định | Language Selection hiển thị cờ Việt Nam và mã "VN" được chọn sẵn | Medium |
| TC_LOGIN_GUI_003 | User interface | GUI | Check display | mms_2_mm_media_logo | Logo visibility | Người dùng chưa đăng nhập, mở ứng dụng | 1. Mở ứng dụng SAA 2025<br>2. Quan sát vùng logo phía trên cùng | Logo Sun* Annual Awards hiển thị rõ ràng, đúng tỷ lệ, không bị cắt hoặc méo | High |
| TC_LOGIN_GUI_004 | User interface | GUI | Check display | mms_3_MM_MEDIA_Logo/RootFuther | ROOT FURTHER branding | Người dùng chưa đăng nhập, mở ứng dụng | 1. Mở ứng dụng SAA 2025<br>2. Quan sát text/logo "ROOT FURTHER" | Text "ROOT FURTHER" hiển thị đúng font, kích thước và vị trí theo spec design<br>Không bị ảnh hưởng bởi thay đổi ngôn ngữ (không localize) | Medium |
| TC_LOGIN_GUI_005 | User interface | GUI | Check display | mms_6_Copyright | Copyright text | Người dùng chưa đăng nhập, mở ứng dụng | 1. Mở ứng dụng SAA 2025<br>2. Quan sát copyright text ở cuối màn hình | Copyright text hiển thị đúng nội dung `"Bản quyền thuộc về Sun* © 2025"`, đúng vị trí ở bottom của màn hình | Medium |
| TC_LOGIN_GUI_006 | User interface | GUI | Check display | mms_5_Button | Button initial state | Người dùng chưa đăng nhập, mở ứng dụng | 1. Mở ứng dụng SAA 2025<br>2. Quan sát nút "LOGIN With Google"<br>3. Kiểm tra icon Google và label text | Nút hiển thị icon Google và text "LOGIN With Google"<br>Nút ở trạng thái enabled, có thể tương tác | High |
| TC_LOGIN_GUI_007 | User interface | GUI | Check display | mms_4_content | Description text — Vietnamese | Người dùng trên màn hình Login, ngôn ngữ đang là VN | 1. Xác nhận Language Selection đang là "VN"<br>2. Quan sát nội dung description text | Description text hiển thị đúng tiếng Việt: `"Bắt đầu hành trình của bạn cùng SAA 2025. Đăng nhập để khám phá!"` | High |
| TC_LOGIN_GUI_008 | User interface | GUI | Check display | mms_2.1_language | Language selector appearance | Người dùng chưa đăng nhập, mở ứng dụng | 1. Mở ứng dụng SAA 2025<br>2. Quan sát Language Selection component<br>3. Kiểm tra icon cờ và mã ngôn ngữ | Language Selection hiển thị icon cờ quốc gia và mã ngôn ngữ viết tắt (ví dụ: VN)<br>Component được đặt đúng vị trí theo design | Medium |

---

### Function — Language Selector

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_LOGIN_FUN_001 | Normal_Others | FUNCTION | Check data validation | Language Selection | Allowed values | Người dùng trên màn hình Login | 1. Mở ứng dụng<br>2. Tap vào Language Selection<br>3. Quan sát danh sách ngôn ngữ | Danh sách dropdown chỉ hiển thị các ngôn ngữ hỗ trợ (VN, EN, JA) | Medium |
| TC_LOGIN_FUN_002 | Normal_Others | FUNCTION | Check component interaction | Language Selection | Open | Người dùng trên màn hình Login | 1. Mở ứng dụng<br>2. Tap vào Language Selection | Language selector overlay mở ra và hiển thị danh sách ngôn ngữ có sẵn (VN, EN, JA) trực tiếp trên màn hình Login — **không** điều hướng sang màn hình mới | Medium |
| TC_LOGIN_FUN_003 | Normal_Others | FUNCTION | Check component interaction | Language Selection | Select EN | Người dùng trên màn hình Login, language selector overlay đang mở | 1. Tap vào Language Selection<br>2. Chọn "EN" từ dropdown<br>3. Quan sát Language Selection sau khi chọn | Label mã ngôn ngữ cập nhật thành "EN"<br>Icon cờ cập nhật tương ứng (cờ Anh/Mỹ) | High |
| TC_LOGIN_FUN_004 | Normal_Others | FUNCTION | Check cross-component effect | Language Selection change | UI text re-render — VN→EN | Người dùng trên màn hình Login | 1. Xác nhận ngôn ngữ đang là VN<br>2. Tap Language Selection<br>3. Chọn "EN"<br>4. Quan sát Description Text và Copyright | Description Text hiển thị bằng tiếng Anh: `"Start your journey with SAA 2025. Log in to explore!"`<br>Copyright Text hiển thị bằng tiếng Anh: `"Copyright belongs to Sun* © 2025"` | High |
| TC_LOGIN_FUN_016 | Normal_Others | FUNCTION | Check component interaction | Language Selection | Select JA | Người dùng trên màn hình Login, language selector overlay đang mở | 1. Tap vào Language Selection<br>2. Chọn "JA" từ dropdown<br>3. Quan sát Language Selection sau khi chọn | Label mã ngôn ngữ cập nhật thành "JA"<br>Icon cờ cập nhật tương ứng (cờ Nhật Bản) | High |
| TC_LOGIN_FUN_017 | Normal_Others | FUNCTION | Check cross-component effect | Language Selection change | UI text re-render — VN→JA | Người dùng trên màn hình Login | 1. Xác nhận ngôn ngữ đang là VN<br>2. Tap Language Selection<br>3. Chọn "JA"<br>4. Quan sát Description Text và Copyright | Language selector cập nhật thành "JA" + cờ Nhật Bản<br>Description Text và Copyright re-render sang tiếng Nhật (**⚠️ JA strings pending từ Localization Team — cần cập nhật expected text khi có bản dịch**) | High |
| TC_LOGIN_FUN_018 | Normal_Others | FUNCTION | Check component interaction | Language Selection | Dismiss without selection | Người dùng trên màn hình Login, language selector overlay đang mở | 1. Tap vào Language Selection<br>2. Tap ra ngoài overlay (dismiss)<br>3. Quan sát trạng thái Language Selection | Overlay đóng lại<br>Ngôn ngữ không thay đổi (giữ nguyên giá trị trước khi mở) | Medium |
| TC_LOGIN_FUN_019 | Normal_Others | FUNCTION | Check cross-component effect | Language Selection change | Switch back to VN | Người dùng trên màn hình Login, đang hiển thị ngôn ngữ EN | 1. Tap Language Selection<br>2. Chọn "VN"<br>3. Quan sát toàn bộ text trên màn hình | Tất cả text localize quay lại tiếng Việt<br>Icon cờ Việt Nam hiển thị trong Language Selection<br>Description Text hiển thị: `"Bắt đầu hành trình của bạn cùng SAA 2025. Đăng nhập để khám phá!"`<br>Copyright Text hiển thị: `"Bản quyền thuộc về Sun* © 2025"` | Medium |
| TC_LOGIN_FUN_026 | Normal_Others | FUNCTION | Check business logic | Language Selection | Persist selection via DataStore | Người dùng trên màn hình Login | 1. Tap Language Selection<br>2. Chọn "EN"<br>3. Xác nhận UI hiển thị tiếng Anh<br>4. Đóng ứng dụng (không force-kill)<br>5. Mở lại ứng dụng<br>6. Quan sát Language Selection và nội dung màn hình | Language Selection hiển thị "EN" + cờ tương ứng<br>Description Text và Copyright vẫn hiển thị bằng tiếng Anh<br>Lựa chọn ngôn ngữ được lưu trong `DataStore<Preferences>` và được phục hồi sau khi restart app | Medium |

---

### Function — Login (Normal)

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_LOGIN_FUN_005 | Normal_Login | FUNCTION | Check component interaction | Login with Google Button | Submit — initiate OAuth | Người dùng trên màn hình Login | 1. Tap nút "LOGIN With Google"<br>2. Quan sát luồng xác thực | Luồng Google OAuth consent/authentication được khởi tạo<br>Màn hình Google account picker / login xuất hiện | High |
| TC_LOGIN_FUN_006 | Normal_Login | FUNCTION | Check state transition | Login with Google Button | Enabled → Loading | Người dùng trên màn hình Login | 1. Tap nút "LOGIN With Google"<br>2. Quan sát trạng thái nút trong khi xác thực đang xử lý | Nút hiển thị `CircularProgressIndicator` inline và chuyển sang disabled state trong khi xác thực<br>Không thể tap lại nút | High |
| TC_LOGIN_FUN_007 | Normal_Login | FUNCTION | Check navigation behavior | Login with Google Button | Success → Home screen | Người dùng trên màn hình Login | 1. Tap nút "LOGIN With Google"<br>2. Hoàn tất xác thực Google thành công với tài khoản @sun-asterisk.com<br>3. Quan sát màn hình sau đăng nhập | Chuyển hướng đến Home screen (main dashboard)<br>Không hiển thị màn hình Login nữa | High |
| TC_LOGIN_FUN_008 | Normal_Login | FUNCTION | Check business logic | Login with Google Button | Double-click prevention | Người dùng trên màn hình Login | 1. Tap nút "LOGIN With Google"<br>2. Ngay lập tức tap lại nút lần nữa<br>3. Quan sát hành vi | Chỉ gửi một yêu cầu xác thực, không gửi trùng lặp | High |
| TC_LOGIN_FUN_009 | Normal_Login | FUNCTION | Check business logic | Google OAuth | Authentication success — registered account | Người dùng trên màn hình Login, tài khoản Google @sun-asterisk.com đã đăng ký | 1. Tap nút "LOGIN With Google"<br>2. Chọn tài khoản Google @sun-asterisk.com đã đăng ký<br>3. Hoàn tất xác thực | Đăng nhập thành công, chuyển đến Home screen | High |
| TC_LOGIN_FUN_010 | Normal_Login | FUNCTION | Check business logic | Google OAuth | First time login — no Google account on device | Người dùng chưa từng đăng nhập, chưa có tài khoản Google nào trên thiết bị | 1. Mở ứng dụng lần đầu<br>2. Tap nút "LOGIN With Google"<br>3. Nhập tài khoản Google @sun-asterisk.com<br>4. Hoàn tất xác thực | Hiển thị màn hình đăng nhập Google<br>Đăng nhập thành công sau khi nhập tài khoản @sun-asterisk.com | High |
| TC_LOGIN_FUN_011 | Normal_Login | FUNCTION | Check business logic | Google OAuth | First time login — registered Google already on device | Người dùng chưa từng đăng nhập, tài khoản @sun-asterisk.com đã có sẵn trên thiết bị | 1. Mở ứng dụng lần đầu<br>2. Tap nút "LOGIN With Google"<br>3. Quan sát account picker | Tài khoản @sun-asterisk.com hiển thị trong account picker<br>Có thể đăng nhập ngay mà không cần nhập password | High |
| TC_LOGIN_FUN_012 | Normal_Login | FUNCTION | Check business logic | Google OAuth | Next time login — valid token | Người dùng đã đăng nhập trước đó, token còn hiệu lực | 1. Đóng ứng dụng (không logout)<br>2. Mở lại ứng dụng<br>3. Quan sát hành vi | Auto-login thành công, chuyển đến Home screen<br>Không hiển thị màn hình Login | High |
| TC_LOGIN_FUN_013 | Normal_Login | FUNCTION | Check business logic | Google OAuth | Next time login — expired token | Người dùng đã đăng nhập trước đó, token đã hết hạn | 1. Chờ token hết hạn<br>2. Mở lại ứng dụng<br>3. Quan sát màn hình | Không auto-login<br>Hiển thị màn hình Login<br>Đăng nhập lại thành công với tài khoản đã đăng ký | High |
| TC_LOGIN_FUN_014 | Normal_Login | FUNCTION | Check business logic | Google OAuth | Login after logout | Người dùng đã logout | 1. Thực hiện logout<br>2. Quan sát màn hình hiển thị<br>3. Tap "LOGIN With Google"<br>4. Hoàn tất xác thực | Không auto-login<br>Hiển thị màn hình Login<br>Đăng nhập lại thành công | High |
| TC_LOGIN_FUN_020 | Normal_Login | FUNCTION | Check business logic | Google OAuth | First time login — non-registered Google on device | Người dùng chưa đăng nhập, thiết bị có tài khoản Google không đăng ký trong hệ thống | 1. Tap nút "LOGIN With Google"<br>2. Chọn tài khoản Google không phải @sun-asterisk.com từ account picker<br>3. Hoàn tất xác thực | Luồng OAuth hoàn tất nhưng access bị từ chối<br>Chuyển đến màn hình Access Denied<br>Không vào được Home screen | High |
| TC_LOGIN_FUN_021 | Normal_Login | FUNCTION | Check navigation behavior | Login with Google Button | Login → Access Denied screen | Người dùng trên màn hình Login | 1. Tap nút "LOGIN With Google"<br>2. Xác thực bằng tài khoản Google không thuộc domain @sun-asterisk.com<br>3. Quan sát kết quả | Không đăng nhập được vào ứng dụng<br>Hiển thị màn hình / thông báo Access Denied | High |

---

### Function — Login (Abnormal)

| ID | tc_type | test_area | category | sub_category | sub_sub_category | pre_condition | step | expected_result | priority |
|----|---------|-----------|----------|--------------|------------------|---------------|------|-----------------|----------|
| TC_LOGIN_FUN_015 | Abnormal_Login | FUNCTION | Check business logic | Google OAuth | Special account — deleted/inactive/blocked | Người dùng trên màn hình Login | 1. Tap nút "LOGIN With Google"<br>2. Xác thực bằng tài khoản bị xóa / vô hiệu hóa / bị khóa<br>3. Quan sát kết quả | Không thể đăng nhập<br>Hiển thị thông báo lỗi tương ứng<br>Người dùng ở lại màn hình Login | High |
| TC_LOGIN_FUN_022 | Abnormal_Login | FUNCTION | Check business logic | Google OAuth | Cancel OAuth | Người dùng trên màn hình Login | 1. Tap nút "LOGIN With Google"<br>2. Hủy xác thực Google (nhấn Back / Cancel)<br>3. Quan sát trạng thái màn hình | Không đăng nhập<br>Người dùng quay lại màn hình Login<br>Nút "LOGIN With Google" trở lại trạng thái enabled | High |
| TC_LOGIN_FUN_023 | Abnormal_Login | FUNCTION | Check business logic | Login with Google Button | No internet connection | Thiết bị không có kết nối mạng, người dùng trên màn hình Login | 1. Tắt Wi-Fi và mobile data<br>2. Tap nút "LOGIN With Google"<br>3. Quan sát thông báo | Hiển thị thông báo lỗi kết nối mạng (toast hoặc dialog)<br>Người dùng ở lại màn hình Login | High |
| TC_LOGIN_FUN_024 | Abnormal_Login | FUNCTION | Check business logic | Login with Google Button | Server / Supabase error | Người dùng trên màn hình Login, có kết nối mạng | 1. Mô phỏng lỗi server (Supabase unavailable)<br>2. Tap nút "LOGIN With Google"<br>3. Hoàn tất Google OAuth<br>4. Quan sát kết quả khi server trả về lỗi | Hiển thị thông báo lỗi server (toast)<br>Người dùng ở lại màn hình Login<br>Không crash ứng dụng | High |
| TC_LOGIN_FUN_025 | Abnormal_Login | FUNCTION | Check business logic | Login with Google Button | Timeout | Người dùng trên màn hình Login, kết nối mạng yếu / chậm | 1. Tap nút "LOGIN With Google"<br>2. Chờ timeout xảy ra trong quá trình OAuth / API call<br>3. Quan sát hành vi | Hiển thị thông báo lỗi timeout<br>Loading state kết thúc<br>Nút trở lại trạng thái enabled<br>Người dùng ở lại màn hình Login | Medium |

---

## Notes

### Test Viewpoints Source
- `mcp_testviewpoint_get_test_viewpoints(screen="Login", item_type="Login by Google account")` — 11 viewpoints
- `mcp_testviewpoint_get_test_viewpoints(screen="Login", item_type="Design of Login screen")` — 1 viewpoint
- `mcp_testviewpoint_get_test_viewpoints(screen="Login", item_type="Other cases")` — 5 viewpoints

### Coverage Map

| Design Item | Covered By TCs |
|-------------|----------------|
| `mms_2_mm_media_logo` (Image) | TC_LOGIN_GUI_001, TC_LOGIN_GUI_003 |
| `mms_2.1_language` (Dropdown overlay) | TC_LOGIN_GUI_002, TC_LOGIN_GUI_008, TC_LOGIN_FUN_001~004, TC_LOGIN_FUN_016~019, TC_LOGIN_FUN_026 |
| `mms_3_MM_MEDIA_Logo/RootFuther` (Image) | TC_LOGIN_GUI_001, TC_LOGIN_GUI_004 |
| `mms_4_content` (Text — localized) | TC_LOGIN_GUI_007, TC_LOGIN_FUN_004, TC_LOGIN_FUN_017 |
| `mms_5_Button` (Login Button) | TC_LOGIN_GUI_006, TC_LOGIN_FUN_005~015, TC_LOGIN_FUN_020~025 |
| `mms_6_Copyright` (Text — localized) | TC_LOGIN_GUI_005, TC_LOGIN_FUN_004, TC_LOGIN_FUN_017 |

### Access Control Logic
- Only `@sun-asterisk.com` Google accounts are allowed
- Non-Sun* domain → redirect to Access Denied screen (not a Login error)
- Expired token → show Login screen (no auto-login)

### Test Execution Order (Suggested)
1. GUI display tests (no interaction needed)
2. Language selector tests (non-destructive)
3. Normal login flow (requires test Google account)
4. Abnormal login / error cases
