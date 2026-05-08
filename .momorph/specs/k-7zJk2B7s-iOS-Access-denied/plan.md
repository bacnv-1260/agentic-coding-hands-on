# Implementation Plan: [iOS] Access Denied

**Frame**: `k-7zJk2B7s-iOS-Access-denied`
**Date**: 2026-05-05
**Spec**: `specs/k-7zJk2B7s-iOS-Access-denied/spec.md`

---

## Summary

Implement màn hình `AccessDeniedScreen` — một Composable thuần UI hiển thị khi Google OAuth thành công nhưng domain bị từ chối (403 / `AccessDeniedException`). Navigation event đã được `LoginViewModel` xử lý sẵn; công việc chính là: thêm string resources, thêm drawable asset, tạo Composable screen, và thay thế TODO stub trong `SaaNavHost`.

---

## Technical Context

**Language/Framework**: Kotlin / Jetpack Compose + Material3
**Primary Dependencies**: Hilt (DI), Navigation Compose, `collectAsStateWithLifecycle`
**Database**: N/A
**Testing**: JUnit 4, Kotlin Coroutines Test
**State Management**: `StateFlow<LoginUiState>` qua `LoginViewModel` (đã có)
**API Style**: N/A — màn hình stateless, không gọi API

---

## Constitution Compliance Check

*GATE: Must pass before implementation can begin*

- [x] MVVM + Clean Architecture — màn hình mỏng, delegate action về NavController
- [x] Không dùng `!!` — không có nullable risk trong màn hình này
- [x] Material3 only — dùng `MaterialTheme.*`, không hardcode màu/size
- [x] Một `NavHost` duy nhất (`SaaNavHost.kt`) — không tạo NavHost mới
- [x] Timber cho logging — không dùng `Log.*`
- [x] `stringResource()` cho tất cả text — không hardcode string
- [x] Không có logic nghiệp vụ trong Composable
- [x] TR-002: dùng `LocalOnBackPressedDispatcherOwner` thay vì `activity?.finish()` — không import `Activity` trong Composable

**Violations**: Không có.

---

## Architecture Decisions

### Composable structure

```
AccessDeniedScreen(navController: NavController)  ← thin screen
  └─ (no ViewModel needed — stateless UI)
```

Màn hình này **không cần ViewModel riêng**:
- Không có local state
- Không có API call
- Navigation được trigger từ `LoginViewModel` (đã xử lý trước khi đến màn hình này)
- Back arrow action là `navController.context.findActivity()?.finish()` hoặc `Activity.finish()`

### Back arrow — exit app pattern

`FR-006`: back arrow → exit app. `TR-002` cấm dùng `finish()` hay `Activity` trực tiếp trong Composable. Dùng `LocalOnBackPressedDispatcherOwner` để delegate system back behavior — khi back stack rỗng, dispatcher tự động exit app:
```kotlin
val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
// trong onClick: backDispatcher?.onBackPressed()
```
- Không dùng `activity?.finish()` — vi phạm TR-002.
- Không dùng `System.exit()` — unsafe, bỏ qua lifecycle.
- Không dùng `!!` — safe call `?.` cho `LocalOnBackPressedDispatcherOwner.current`.

> **Note**: `onBackPressed()` khi back stack rỗng → `NavController` xử lý → `Activity.onBackPressed()` → app exit. Behavior đồng nhất với FR-006.

### Navigation vào màn hình này

Đã có sẵn trong `LoginScreen.kt`:
```kotlin
LaunchedEffect(uiState.isAccessDenied) {
    if (uiState.isAccessDenied) {
        navController.navigate(NavRoutes.AccessDenied.route) {
            popUpTo(NavRoutes.Login.route) { inclusive = true }
        }
        viewModel.consumeNavigationEvent()
    }
}
```
→ **Không cần thay đổi** `LoginScreen.kt` hay `LoginViewModel.kt`.

### Navigation ra khỏi màn hình này (button)

```kotlin
navController.navigate(NavRoutes.Home.route) {
    popUpTo(NavRoutes.AccessDenied.route) { inclusive = true }
}
```

### Image asset

`mms_2.1_mm_media_Not Found` hiện là **draft** trong design items — sử dụng drawable asset đã có sẵn trong project cho tới khi designer finalize. Kiểm tra theo thứ tự ưu tiên:
1. `mm_media_not_found` — kiểm tra xem tồn tại không
2. Nếu không có → dùng `mm_media_icon` làm placeholder, để comment `// TODO: replace with finalized asset`

---

## Project Structure

### Documentation (this feature)

```
.momorph/specs/k-7zJk2B7s-iOS-Access-denied/
├── spec.md   ✅ done
├── plan.md   ← this file
```

### Source Code (affected areas)

```
android/app/src/main/
├── res/
│   ├── values/strings.xml              ← MODIFY: thêm 4 strings (VN): access_denied_title, access_denied_description, access_denied_button, cd_back_arrow
│   └── values-en/strings.xml           ← MODIFY: thêm 4 strings (EN): access_denied_title, access_denied_description, access_denied_button, cd_back_arrow
├── java/com/example/saa/
│   ├── presentation/ui/
│   │   └── access_denied/
│   │       └── AccessDeniedScreen.kt   ← NEW
│   └── SaaNavHost.kt                   ← MODIFY: thay TODO stub

android/app/src/test/java/com/example/saa/
└── presentation/ui/access_denied/
    └── AccessDeniedScreenNavigationTest.kt  ← NEW (optional)
```

### Dependencies

Không cần thêm dependency mới — tất cả đã có: Compose, Navigation, Material3, Hilt.

---

## Implementation Strategy

### Phase 1 — String Resources (P1, US3)

**Mục tiêu**: Tất cả text từ `stringResource()` — không hardcode.

**`values/strings.xml`** — thêm:
```xml
<!-- Access Denied screen -->
<string name="access_denied_title">Truy cập bị từ chối</string>
<string name="access_denied_description">The resource you&#8217;re looking for doesn&#8217;t exist or has been removed.</string>
<string name="access_denied_button">Quay về trang chủ</string>
<string name="cd_back_arrow">Quay lại</string>
```

**`values-en/strings.xml`** — thêm:
```xml
<string name="access_denied_title">Access Denied</string>
<string name="access_denied_description">The resource you&#8217;re looking for doesn&#8217;t exist or has been removed.</string>
<string name="access_denied_button">Go back to Home</string>
<string name="cd_back_arrow">Go back</string>
```

> **VN translations confirmed** (từ lựa chọn của PM).

---

### Phase 2 — AccessDeniedScreen Composable (P1, US1 + US2 + US4)

**File**: `presentation/ui/access_denied/AccessDeniedScreen.kt`

**Cấu trúc Composable**:

```kotlin
@Composable
fun AccessDeniedScreen(navController: NavController) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Scaffold { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // mms_Header — Node 6885:9523
            //   back_arrow: Icon(back) → backDispatcher?.onBackPressed()  ← TR-002 compliant
            //   Text("Access Denied") ← stringResource(R.string.access_denied_title)
            //   HorizontalDivider
            //   Text(description) ← stringResource(R.string.access_denied_description)

            // mms_2.1 — Node 6885:9529 (draft ⚠️)
            //   Image(painter = painterResource(R.drawable.mm_media_icon), contentDescription = null)
            //   TODO: replace with finalized asset (mm_media_not_found)

            // mms_2.2_Button — Node 6885:9531
            //   Button("Go back to Home") → navController.navigate(Home) { popUpTo(AccessDenied) { inclusive=true } }
        }
    }
}
```

**Accessibility** (FR-005, FR-006):
- `back_arrow` → `Modifier.semantics { contentDescription = stringResource(R.string.cd_back_arrow) }` — dùng key `cd_back_arrow` (đã thêm vào strings.xml ở Phase 1)
- Button → nhãn nút đã là text rõ ràng, không cần thêm contentDescription
- `Image` minh họa → `contentDescription = null` (decorative)

---

### Phase 3 — SaaNavHost update (P1, US1)

**File**: `SaaNavHost.kt` — thay TODO stub:

```kotlin
// TRƯỚC:
composable(NavRoutes.AccessDenied.route) {
    Surface { Text("Access Denied", ...) }
}

// SAU:
composable(NavRoutes.AccessDenied.route) {
    AccessDeniedScreen(navController = navController)
}
```

---

### Phase 4 — Unit Test (optional, US2)

**File**: `AccessDeniedScreenNavigationTest.kt`

Test scope nhỏ — chỉ verify navigation logic không cần Compose test runner:
- `navController.navigate(Home) { popUpTo(AccessDenied) { inclusive = true } }` → back stack đúng

---

## Risk Assessment

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| `mms_2.1` asset chưa finalized (draft) | High | Low | Dùng `mm_media_icon` làm placeholder + TODO comment |
| VN translation chưa có | ~~Medium~~ **Resolved** | Low | ✅ Translations confirmed: title=Truy cập bị từ chối, button=Quay về trang chủ |
| `LocalOnBackPressedDispatcherOwner.current` trả về null ngoài activity context | Low | Low | Safe call `?.`, nếu null thì skip — back_arrow không phản hồi nhưng system Back vẫn hoạt động |
| Figma layer name `mms_2_Open secret box- chưa mở` nhầm → confuse developer | Low | Low | Đã ghi chú trong spec; functional name rõ ràng trong plan |

---

## Blockers

| ID | Mô tả | Owner | Unblock by |
|----|-------|-------|------------|
| ~~B1~~ | ~~VN translation~~ | — | ✅ Resolved — translations confirmed |
| B2 | Asset finalized cho `mms_2.1_mm_media_Not Found` | Designer | Thay placeholder sau khi asset được export |

---

## Integration Testing Strategy

| Category | Applicable? | Key Scenarios |
|----------|-------------|---------------|
| UI ↔ Navigation | Yes | Nhấn "Go back to Home" → Home, không back được về AccessDenied |
| UI ↔ Localization | Yes | Đổi language → text đổi theo |
| Back arrow → Activity.finish() | Yes | Nhấn back_arrow → app exit |
| UI ↔ External API | No | Màn hình không gọi API |
| App ↔ Data Layer | No | Không có DataStore/DB interaction |

---

## Estimated Complexity

- **Frontend (Composable)**: Low — ~50 LOC, không state, không API
- **Backend**: N/A
- **Testing**: Low — navigation smoke test

---

## Checklist trước khi implement

- [ ] Confirm VN text với PM/designer (Blocker B1) — hoặc proceed với EN placeholder
- [ ] Kiểm tra drawable `mm_media_not_found` có trong `res/drawable/` không
- [ ] Đọc `NavRoutes.kt` confirm tên route `NavRoutes.AccessDenied`
