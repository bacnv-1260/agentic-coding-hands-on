# Screen: [iOS] Login

## Screen Info

| Property | Value |
|----------|-------|
| **Screen ID** | `8HGlvYGJWq` |
| **Figma Node ID** | `6885:8963` |
| **Figma Link** | https://momorph.ai/files/9ypp4enmFmdK3YAFJLIu6C/screens/8HGlvYGJWq |
| **Screen Image** | ![Login](https://momorph.ai/api/images/9ypp4enmFmdK3YAFJLIu6C/6885:8963/59e51999392a4363c206f36cdac92fe4.png) |
| **Screen Group** | Authentication |
| **Spec Status** | ✅ done |
| **Platform** | iOS / Android |
| **Discovered At** | 2026-05-05 |
| **Last Updated** | 2026-05-05 |

---

## Description

Màn hình đăng nhập duy nhất của ứng dụng SAA 2025. Sunner dùng tài khoản Google (thuộc domain `@sun-asterisk.com`) để xác thực qua OAuth. Hỗ trợ chọn ngôn ngữ hiển thị (VN / EN / JA) trước khi đăng nhập.

---

## Navigation Analysis

### Incoming Navigations (From)

| Source Screen | Trigger | Condition |
|---------------|---------|-----------|
| App Launch | Tự động | Chưa có session hợp lệ |
| Mọi màn hình main | Redirect tự động | Token hết hạn (401) |

### Outgoing Navigations (To)

| Target Screen | Trigger Element | Node ID | Confidence | Notes |
|---------------|-----------------|---------|------------|-------|
| [iOS] Home | Button `mms_5_Button` — "LOGIN With Google" | `6885:8969` | high | Google OAuth success → navigate to Home |
| Language Overlay (in-screen) | Instance `mms_2.1_language` | `6885:8976` | high | Click flag → mở language selector overlay (BottomSheet/Popup); **không phải NavDestination** |
| [iOS] Access denied | Button `mms_5_Button` | `6885:8969` | high | OAuth success nhưng email không thuộc domain `@sun-asterisk.com` |

### Navigation Rules
- **Back behavior**: Không có back (entry point của app)
- **Deep link**: Không hỗ trợ
- **Auth required**: Không
- **Language selector**: Mở overlay (BottomSheet/Popup) trực tiếp trên màn hình Login — **không** điều hướng sang screen mới, không cần entry trong `NavRoutes.kt`
- **Access Denied**: App điều hướng sang `[iOS] Access Denied` sau khi OAuth thành công với email không thuộc domain `@sun-asterisk.com`; không có navigation ngược về Login

---

## Component Schema

### Layout Structure

```
┌──────────────────────────────────────┐
│  bg (MM_MEDIA_Keyvisual BG)          │
├──────────────────────────────────────┤
│  header                              │
│    [StatusBar]  [Logo]  [VN ▼]      │  ← mms_2_mm_media_logo + mms_2.1_language
├──────────────────────────────────────┤
│                                      │
│  mms_3_MM_MEDIA_Logo/RootFuther     │  ← "ROOT FURTHER" brand image
│                                      │
│  mms_4_content                       │  ← "Bắt đầu hành trình..."
│                                      │
│  mms_5_Button                        │  ← [ LOGIN With Google  🔵 ]
│                                      │
├──────────────────────────────────────┤
│  footer                              │
│    "Bản quyền thuộc về Sun* © 2025" │  ← mms_6
└──────────────────────────────────────┘
```

### Component Hierarchy

```
[iOS] Login (6885:8963)
├── bg
│   └── MM_MEDIA_Keyvisual BG (background image)
├── header (6885:8972)
│   ├── iOS/Component/StatusBar
│   ├── mms_2_mm_media_logo (SAA logo) — #1
│   └── mms_2.1_language (Language selector) — #2
├── mms_3_MM_MEDIA_Logo/RootFuther — #3
├── mms_4_content (Description text) — #4
├── mms_5_Button (Login with Google) — #5
└── footer (6885:8970)
    └── mms_6_Copyright text — #6
```

### Main Components

| # | Component | Node ID | Type | Mô tả | Reusable |
|---|-----------|---------|------|-------|----------|
| 1 | `mms_2_mm_media_logo` | `6885:8977` | Image | Logo SAA 2025 (static) | Yes |
| 2 | `mms_2.1_language` | `6885:8976` | Dropdown trigger | Language selector: flag + mã ngôn ngữ + dropdown arrow | Yes (Header) |
| 3 | `mms_3_MM_MEDIA_Logo/RootFuther` | `6885:8967` | Image | Brand tagline "ROOT FURTHER" (static) | Yes |
| 4 | `mms_4_content` | `6885:8968` | Text | Mô tả giới thiệu, cập nhật theo ngôn ngữ | No |
| 5 | `mms_5_Button` | `6885:8969` | Button (icon_text) | CTA: "LOGIN With Google" + Google icon | Yes |
| 6 | `mms_6_Bản quyền...` | `6885:8971` | Text | Copyright footer (static) | Yes (Footer) |

---

## Design Items Detail

### #1 — mms_2_mm_media_logo (Logo)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:8977` |
| **Type** | Image |
| **Interaction** | None (static) |
| **State** | Always visible |

**Hiển thị:** Hình ảnh logo Sun* Annual Awards — static asset.  
**Logic:** Không có tương tác. Luôn hiển thị, không có trạng thái loading hay error.

---

### #2 — mms_2.1_language (Language Selection)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:8976` |
| **Type** | Dropdown trigger |
| **Default Value** | `VN` |
| **Format** | Language code (`VN`, `EN`, `JA`) |
| **Storage** | `DataStore<Preferences>` — key: `"selected_language"`, type: `String`, default: `"VN"` |
| **Interaction** | `on_click` → mở Language dropdown |

**Hiển thị:**
- Icon cờ quốc gia (mặc định: cờ Việt Nam)
- Label mã ngôn ngữ (mặc định: "VN")
- Icon dropdown arrow

**Logic:**
- Click → mở Language selector overlay (BottomSheet/Popup) trên cùng màn hình Login (không phải screen riêng)
- Sau khi chọn ngôn ngữ mới: cập nhật flag icon + label, re-render toàn bộ UI text
- Lưu giá trị vào `DataStore<Preferences>` để persist qua các lần mở app
- Mặc định: "VN" (Vietnamese)
- Luôn enabled trên màn hình Login

**Validation:**
- Giá trị phải nằm trong danh sách ngôn ngữ hỗ trợ (`VN`, `EN`, `JA`)
- Error nếu giá trị không hợp lệ: `"Invalid language selection."`

---

### #3 — mms_3_MM_MEDIA_Logo/RootFuther (Root Further Logo Text)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:8967` |
| **Type** | Image (static branding) |
| **Interaction** | None |

**Hiển thị:** Text branding "ROOT FURTHER" dạng decorative typography.  
**Logic:** Nội dung cố định, không thay đổi theo ngôn ngữ.

---

### #4 — mms_4_content (Description Text)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:8968` |
| **Type** | Text label |
| **Localized** | Yes |

**Hiển thị:**
- VI: `"Bắt đầu hành trình của bạn cùng SAA 2025. Đăng nhập để khám phá!"`
- EN: `"Start your journey with SAA 2025. Log in to explore!"`
- JA: `[NEEDS TRANSLATION — yêu cầu từ Localization Team trước khi dev]`

**Logic:** Static display. Nội dung cập nhật khi ngôn ngữ thay đổi.

---

### #5 — mms_5_Button (Login with Google Button)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:8969` |
| **Type** | Button (`icon_text`) |
| **Label** | `"LOGIN With Google"` |
| **Icon** | Google logo (bên phải label) |
| **Interaction** | `on_click` → Google OAuth flow |

**Hiển thị:**
- Label: `"LOGIN With Google"`
- Icon: Google logo icon (bên phải)

**Logic:**
- Click → khởi động Google OAuth consent flow
- **On success:** Điều hướng đến `[iOS] Home`
- **On success nhưng domain sai:** Điều hướng đến `[iOS] Access denied`
- **On failure (auth error / network):** Hiển thị error message
- **Double-click prevention:** Cần implement để tránh duplicate auth request
- **Loading state:** Hiển thị `CircularProgressIndicator` inline trong nút và disable interaction trong khi đang xác thực
- **Button label string resource:** `"LOGIN With Google"` — không có trailing space (Figma node có trailing space cần trim)

---

### #6 — mms_6_Bản quyền thuộc về Sun* © 2025 (Copyright Text)

| Property | Value |
|----------|-------|
| **Node ID** | `6885:8971` |
| **Type** | Text label |
| **Localized** | Yes |

**Hiển thị:**
- VI: `"Bản quyền thuộc về Sun* © 2025"`
- EN: `"Copyright belongs to Sun* © 2025"`
- JA: `[NEEDS TRANSLATION — yêu cầu từ Localization Team trước khi dev]`

**Logic:** Static display. Không có tương tác.

---

## API Mapping

### On User Action

| Action | API | Method | Request Body | Response |
|--------|-----|--------|--------------|----------|
| Click "LOGIN With Google" | Supabase Auth Google OAuth | OAuth2 | `{google_token}` | `{access_token, refresh_token, user}` |

### Error Handling

| Scenario | Xử lý UI |
|----------|----------|
| OAuth thất bại (user cancel) | Giữ màn hình Login, không hiển thị error |
| Network error | Toast: `"Lỗi kết nối. Vui lòng thử lại."` |
| Email không thuộc domain Sun* | Redirect → `[iOS] Access denied` |
| Auth server error (5xx) | Toast: `"Đã có lỗi xảy ra. Vui lòng thử lại."` |

---

## State Management

### Local State

| State | Type | Initial | Mục đích |
|-------|------|---------|---------|
| `selectedLanguage` | String | `"VN"` | Ngôn ngữ hiển thị hiện tại |
| `isLoading` | Boolean | `false` | Trạng thái đang xử lý OAuth |
| `errorMessage` | String? | `null` | Thông báo lỗi xác thực |

### Global State

| State | Store | Read/Write | Mục đích |
|-------|-------|------------|---------|
| `user` | AuthStore | Write | Lưu user sau login thành công |
| `accessToken` | AuthStore | Write | Lưu JWT (EncryptedSharedPreferences) |
| `refreshToken` | AuthStore | Write | Lưu refresh token |
| `language` | AppStore | Read/Write | Đồng bộ ngôn ngữ toàn app |

---

## UI States

### Default State
- Nút "LOGIN With Google" enabled
- Language selector hiển thị "VN" + cờ Việt Nam

### Loading State (Đang xác thực)
- Nút "LOGIN With Google" hiển thị `CircularProgressIndicator` inline và disabled (không thể tap)
- Ngăn double-tap (pointer events disabled trong suốt OAuth flow)
- Language selector vẫn enabled

### Error State
- Toast hiển thị message lỗi (bottom of screen)
- Nút trở lại enabled

### Success State
- Tự động navigate sang `[iOS] Home`
- Không hiển thị animation đặc biệt

---

## Accessibility

| Requirement | Implementation |
|-------------|----------------|
| Screen reader | ARIA/TalkBack label cho logo, language selector, login button |
| Touch target | Tối thiểu 44×44 pt cho button và language selector |
| Color contrast | WCAG AA — text trên nền Keyvisual |
| Loading announcement | Announce "Đang đăng nhập..." khi OAuth bắt đầu |
| Error announcement | Live region cho toast error |

---

## Notes

- Language selector trên màn hình Login cũng xuất hiện trên Header của mọi màn hình sau đăng nhập — đây là component dùng chung.
- Token sau khi nhận được từ Supabase Auth phải được lưu bằng `EncryptedSharedPreferences` (Android) hoặc `Keychain` (iOS) — **không** lưu trong `SharedPreferences` thông thường.
- Không log access token hay user info vào Timber.
