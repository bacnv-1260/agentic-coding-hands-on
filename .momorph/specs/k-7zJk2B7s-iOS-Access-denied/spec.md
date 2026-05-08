# Feature Specification: [iOS] Access Denied

**Frame ID**: `k-7zJk2B7s`
**Frame Name**: `[iOS] Access denied`
**File Key**: `9ypp4enmFmdK3YAFJLIu6C`
**Created**: 2026-05-05
**Status**: Done

---

## Overview

Màn hình lỗi hiển thị khi người dùng cố gắng đăng nhập bằng tài khoản Google không thuộc domain được phép (`@sun-asterisk.com`). Supabase Auth từ chối xác thực (403 / access denied), ứng dụng điều hướng sang màn hình này thay vì vào Home. Người dùng có hai hành động: nhấn "Go back to Home" để vào Home, hoặc nhấn back_arrow/system Back để exit app.

**Target users**: Người dùng đã vượt qua Google OAuth nhưng bị từ chối bởi RLS / business rule phía server.

**Business context**: Bảo vệ app khỏi truy cập trái phép — chỉ nhân viên Sun* mới được sử dụng SAA 2025.

---

## User Scenarios & Testing *(mandatory)*

### User Story 1 — Hiển thị màn hình Access Denied sau khi đăng nhập thất bại (Priority: P1)

Khi Supabase trả về lỗi 403 / `AccessDeniedException` trong quá trình đăng nhập Google, hệ thống điều hướng người dùng sang màn hình Access Denied, hiển thị thông báo lỗi rõ ràng.

**Why this priority**: Nếu màn hình này không hiển thị đúng, người dùng bị từ chối không nhận được phản hồi và app bị treo hoặc crash.

**Independent Test**: Giả lập Google OAuth với tài khoản `@gmail.com` (non sun-asterisk) → app phải hiển thị màn hình này.

**Acceptance Scenarios**:

1. **Given** người dùng đăng nhập bằng tài khoản không thuộc `@sun-asterisk.com`, **When** Supabase trả về 403, **Then** ứng dụng điều hướng đến màn hình Access Denied với tiêu đề "Access Denied" và mô tả lỗi.
2. **Given** màn hình Access Denied đang hiển thị, **When** ứng dụng khởi động lại, **Then** người dùng bắt đầu lại từ màn hình Login (không giữ session).
3. **Given** màn hình Access Denied đang hiển thị, **When** người dùng nhấn nút Back của hệ thống, **Then** ứng dụng không điều hướng về Login (back stack đã bị clear).

---

### User Story 2 — Điều hướng về Home bằng nút "Go back to Home" (Priority: P1)

Người dùng nhấn nút "Go back to Home" để thoát màn hình lỗi và quay về trang chủ.

**Why this priority**: Đây là lối thoát duy nhất để điều hướng đến Home; nếu không hoạt động, người dùng không thể vào app (back_arrow chỉ exit app, không vào Home).

**Independent Test**: Từ màn hình Access Denied, nhấn nút → phải điều hướng đến Home và không thể back về Access Denied.

**Acceptance Scenarios**:

1. **Given** màn hình Access Denied đang hiển thị, **When** người dùng nhấn "Go back to Home", **Then** ứng dụng điều hướng đến `NavRoutes.Home` và xóa Access Denied khỏi back stack.
2. **Given** người dùng đã nhấn "Go back to Home" và đang ở Home, **When** người dùng nhấn Back, **Then** ứng dụng không quay lại màn hình Access Denied.

---

### User Story 3 — Nội dung hỗ trợ đa ngôn ngữ (Priority: P2)

Tiêu đề, mô tả lỗi và nhãn nút đều hiển thị đúng ngôn ngữ đang được chọn (VN / EN).

**Why this priority**: Localization là yêu cầu toàn app; màn hình lỗi không phải ngoại lệ.

**Independent Test**: Chuyển ngôn ngữ sang EN → vào màn hình Access Denied → tất cả text hiển thị tiếng Anh.

**Acceptance Scenarios**:

1. **Given** ngôn ngữ được đặt là "VN", **When** màn hình Access Denied hiển thị, **Then** tiêu đề, mô tả và nhãn nút hiển thị tiếng Việt.
2. **Given** ngôn ngữ được đặt là "EN", **When** màn hình Access Denied hiển thị, **Then** tiêu đề, mô tả và nhãn nút hiển thị tiếng Anh.

---

### User Story 4 — Nút Back trên header thoát app (Priority: P2)

Biểu tượng mũi tên trái (back arrow) trên header khi được nhấn sẽ thoát app, vì Login đã bị xóa khỏi back stack (FR-002) — không có màn hình nào để pop về.

**Why this priority**: Cung cấp hành vi back rõ ràng; tránh trường hợp người dùng bị kẹt hoặc app bị crash khi back stack rỗng.

**Independent Test**: Từ màn hình Access Denied, nhấn back_arrow → app thoát (không navigate về Login vì Login đã bị cleared).

**Acceptance Scenarios**:

1. **Given** màn hình Access Denied đang hiển thị và back stack rỗng, **When** người dùng nhấn biểu tượng back_arrow trên header, **Then** ứng dụng exit (equivalent với system Back khi stack trống).
2. **Given** màn hình Access Denied đang hiển thị, **When** người dùng nhấn nút Back vật lý của hệ thống, **Then** ứng dụng exit — cùng hành vi với back_arrow.

---

### Edge Cases

- **Mất mạng trong lúc xử lý login**: Lỗi network không phải 403 → không hiển thị màn hình này, hiển thị snackbar lỗi tại Login thay vì điều hướng.
- **Back stack rỗng**: Nếu Access Denied là root của stack (deep-link hoặc cold start lỗi), nhấn Back không crash mà exit app.
- **Màn hình xoay (rotation)**: State không bị mất; màn hình tiếp tục hiển thị đúng.

---

## UI/UX Requirements *(from Figma)*

### Screen Components

| No. | Node ID | Component Name | Type | Description | Interactions |
|-----|---------|----------------|------|-------------|--------------|
| 1 | `6885:9523` | `mms_Header` | info_block | Header chứa: back_arrow (trái), tiêu đề "Access Denied", đường kẻ ngang, mô tả lỗi | back_arrow → exit app (back stack rỗng, không có màn hình để pop về) |
| 2 | `6885:9529` | `mms_2.1_mm_media_Not Found` | RECTANGLE | Hình minh họa lỗi truy cập (static image). ⚠️ **Status: draft** trong design items — asset chưa finalized, cần confirm với designer trước khi implement | Không có interaction |
| 3 | `6885:9531` | `mms_2.2_Button` | button | Nút "Go back to Home" (primary action) | on_click → `NavRoutes.Home`, clear back stack |
| 4 | `6885:9522` | `mms_2_Open secret box- chưa mở` *(Figma layer name — naming artifact; functional name: "Khung Chứa Lỗi Truy Cập")* | FRAME (card) | Container bọc toàn bộ nội dung lỗi | Không có interaction trực tiếp |

### Navigation Flow

- **From**: `NavRoutes.Login` (sau khi Google OAuth thành công nhưng domain bị từ chối)
- **To**: `NavRoutes.Home` (khi nhấn "Go back to Home")
- **Back stack behavior**: Login bị pop khi chuyển sang Access Denied → người dùng không thể back về Login từ màn hình này

### Visual Requirements

- Responsive: Portrait only (mobile)
- Animations/Transitions: Standard navigation transition (slide)
- Accessibility: back_arrow và button phải có `contentDescription` cho screen reader

---

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Hệ thống PHẢI điều hướng đến màn hình Access Denied khi `LoginViewModel` emit `isAccessDenied = true`.
- **FR-002**: Hệ thống PHẢI xóa `NavRoutes.Login` khỏi back stack khi điều hướng đến Access Denied (`popUpTo(Login) { inclusive = true }`).
- **FR-003**: Người dùng PHẢI có thể nhấn "Go back to Home" để điều hướng đến `NavRoutes.Home`.
- **FR-004**: Hệ thống PHẢI xóa Access Denied khỏi back stack khi điều hướng về Home (`popUpTo(AccessDenied) { inclusive = true }`).
- **FR-005**: Tất cả text (tiêu đề, mô tả, nhãn nút) PHẢI hỗ trợ localization (VN / EN) thông qua `stringResource`.
- **FR-006**: Back arrow trên header PHẢI exit app khi back stack rỗng (hành vi đồng nhất với system Back button).

### Technical Requirements

- **TR-001**: Màn hình là `Composable` mỏng — không có logic nghiệp vụ, không gọi API, chỉ hiển thị UI và delegate action về ViewModel hoặc NavController.
- **TR-002**: Sử dụng `NavController.navigate` với `popUpTo` để quản lý back stack — không dùng `finish()` hay Activity.
- **TR-003**: Image minh họa (`mms_2.1`) được load từ drawable resource (`mm_media_not_found` hoặc tương đương) — không load từ network.
- **TR-004**: Không có API call khi màn hình này được hiển thị — đây là màn hình thuần UI.

### Key Entities

- **AccessDeniedException** (`domain/exception/AccessDeniedException.kt`): Exception type để phân biệt lỗi 403 với các lỗi khác trong `LoginViewModel`.
- **LoginUiState.isAccessDenied** (`Boolean`): Flag trigger điều hướng sang màn hình này.

---

## API Dependencies

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| _(none)_ | — | Màn hình không gọi API | — |

> Việc xác định người dùng bị từ chối đã được xử lý ở `LoginViewModel` / `LoginWithGoogleUseCase` trước khi điều hướng đến màn hình này.

---

## State Management

| State | Scope | Description |
|-------|-------|-------------|
| `isAccessDenied` | `LoginUiState` (ViewModel) | Set `true` khi nhận `AccessDeniedException`; consumed ngay sau khi navigate bằng `viewModel.consumeNavigationEvent()` (reset cả `isAccessDenied` và `isLoginSuccess` về `false`) |
| Language | `AppViewModel.selectedLanguage` (app-level) | Điều khiển locale context → `stringResource()` tự động dùng đúng ngôn ngữ |

- **Local state**: Không có — màn hình là stateless (chỉ nhận navigation event từ LoginViewModel).
- **Cache/invalidation**: Không áp dụng.
- **Optimistic update**: Không áp dụng.

---

## Success Criteria *(mandatory)*

- **SC-001**: Màn hình Access Denied hiển thị đúng trong 100% trường hợp Google OAuth trả về domain không được phép.
- **SC-002**: Nhấn "Go back to Home" luôn điều hướng đến Home và không để lại màn hình lỗi trong back stack.
- **SC-003**: Tất cả text hiển thị đúng ngôn ngữ được chọn (VN / EN).
- **SC-004**: Không có API call thừa khi màn hình được hiển thị.

---

## Out of Scope

- Phân biệt các loại lỗi 4xx khác nhau (404, 401) — màn hình này chỉ dành cho 403 / access denied.
- Cơ chế "Request Access" / "Contact Admin" — không có trong Figma design.
- Animation đặc biệt cho hình minh họa.

---

## Dependencies

- [`AccessDeniedException`](../../android/app/src/main/java/com/example/saa/domain/exception/AccessDeniedException.kt) — đã có
- [`LoginViewModel.isAccessDenied`](../../android/app/src/main/java/com/example/saa/presentation/ui/login/LoginViewModel.kt) — đã có
- [`SaaNavHost`](../../android/app/src/main/java/com/example/saa/SaaNavHost.kt) — route `NavRoutes.AccessDenied` đã khai báo (TODO stub)
- Image asset `mm_media_not_found` — cần kiểm tra drawable đã có chưa (hiện chỉ có `mm_media_keyvisual_bg`, `mm_media_logo_*`, `mm_media_icon`)
- String resources: cần thêm `access_denied_title`, `access_denied_description`, `access_denied_button` vào `values/strings.xml` và `values-en/strings.xml`

---

## Related Screens

| Screen | Screen ID | Relationship |
|--------|-----------|--------------|
| [iOS] Login | `8HGlvYGJWq` | Entry point — điều hướng đến Access Denied khi 403 |
| [iOS] Home | `OuH1BUTYT0` | Destination — điều hướng đến khi nhấn "Go back to Home" |
| [iOS] Not Found | `sn2mdavs1a` | Màn hình lỗi tương tự (404) — chia sẻ pattern UI |
