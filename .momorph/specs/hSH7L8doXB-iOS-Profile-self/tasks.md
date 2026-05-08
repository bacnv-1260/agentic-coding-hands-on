# Tasks: [iOS] Profile bản thân

**Screen ID**: `hSH7L8doXB`
**Plan**: `specs/hSH7L8doXB-iOS-Profile-self/plan.md`
**Date**: 2026-05-06
**Total Tasks**: 27

---

## Progress

| Phase | Done | Total |
|-------|------|-------|
| Phase 0 — Data Layer | 0 | 12 |
| Phase 1 — UiState + ViewModel | 0 | 2 |
| Phase 2 — Core UI | 0 | 4 |
| Phase 3 — Kudos Section | 0 | 2 |
| Phase 4 — Navigation | 0 | 1 |
| Phase 5 — Polish | 0 | 5 |
| Verification | 0 | 1 |
| **Total** | **0** | **27** |

---

## Phase 0 — Data Layer Foundation

> **Goal**: Toàn bộ data layer cho Profile screen hoạt động và compile sạch trước khi viết bất kỳ UI nào.

---

### T001 — Tạo `ProfileDto.kt`

- [ ] T001

**File**: `data/remote/dto/ProfileDto.kt`
**Depends on**: —

**Description**: DTO map tất cả cột của bảng `profiles` được dùng trong Profile screen. Tất cả field trừ `id` phải **nullable** (quy tắc Supabase DTO).

**Acceptance**:
- `@Serializable data class ProfileDto` với `@SerialName` tường minh cho từng field
- Fields: `id: String` (non-null, PK), `fullName: String?`, `employeeCode: String?`, `avatarUrl: String?`, `badgeType: String?`, `heroTier: Int?`, `departmentId: String?`
- Extension function `ProfileDto.toDomain(): Profile` (mapping `null` về `orEmpty()` / `?: 0`)
- Không có logic nào ngoài mapping

---

### T002 — Tạo `Profile` domain model

- [ ] T002

**File**: `domain/model/Profile.kt`
**Depends on**: —

**Description**: Domain model thuần Kotlin — không import bất kỳ thứ gì từ `data` layer hoặc Supabase.

**Acceptance**:
- `data class Profile(val id: String, val fullName: String, val employeeCode: String, val avatarUrl: String?, val badgeType: String, val heroTier: Int)`
- `avatarUrl` là nullable (có thể không có ảnh)
- Không có annotation, không có default constructor logic

---

### T003 — Tạo `ProfileRepository` interface

- [ ] T003

**File**: `domain/repository/ProfileRepository.kt`
**Depends on**: T002

**Description**: Interface trong domain layer — không biết về Supabase hay coroutines internals.

**Acceptance**:
- `interface ProfileRepository { suspend fun getMyProfile(): Result<Profile> }`
- Import chỉ dùng `com.example.saa.domain.model.Profile`

---

### T004 — Tạo `ProfileDataSource.kt`

- [ ] T004

**File**: `data/remote/source/ProfileDataSource.kt`
**Depends on**: T001

**Description**: Truy vấn Supabase `profiles` WHERE `id = auth.uid()`. Tham chiếu pattern của `KudosDataSource` — inject `SupabaseClient`, không inject Repository.

**Acceptance**:
- `class ProfileDataSource @Inject constructor(private val supabase: SupabaseClient)`
- Method: `suspend fun getMyProfile(): ProfileDto?`
- Query: `supabase.from("profiles").select { filter { eq("id", supabase.auth.currentUserOrNull()?.id ?: return@select) } }.decodeSingleOrNull<ProfileDto>()`
- Trả về `null` nếu `currentUserOrNull()` là null (không throw)
- Không dùng `!!` ở bất kỳ đâu

---

### T005 — Tạo `ProfileRepositoryImpl.kt`

- [ ] T005

**File**: `data/repository/ProfileRepositoryImpl.kt`
**Depends on**: T003, T004

**Description**: Impl của `ProfileRepository` — wrap DataSource call vào `Result`.

**Acceptance**:
- `class ProfileRepositoryImpl @Inject constructor(private val dataSource: ProfileDataSource) : ProfileRepository`
- `override suspend fun getMyProfile(): Result<Profile>` — dùng `runCatching { dataSource.getMyProfile()?.toDomain() ?: error("Profile not found") }`
- Không log PII (không log profile data, chỉ log lỗi)
- Timber error logging khi `Result.isFailure`

---

### T006 — Tạo `GetMyProfileUseCase.kt`

- [ ] T006

**File**: `domain/usecase/GetMyProfileUseCase.kt`
**Depends on**: T003

**Description**: Thin use-case — không chứa logic nghiệp vụ, chỉ delegate.

**Acceptance**:
- `class GetMyProfileUseCase @Inject constructor(private val repository: ProfileRepository)`
- `suspend operator fun invoke(): Result<Profile> = repository.getMyProfile()`
- File chỉ 5–8 dòng thực chất

---

### T007 — Tạo `KudosFilter.kt` enum

- [ ] T007

**File**: `domain/model/KudosFilter.kt`
**Depends on**: —

**Description**: Shared enum giữa `ProfileViewModel` và `GetProfileKudosUseCase`. Đặt trong domain layer để cả Presentation lẫn Data có thể sử dụng.

**Acceptance**:
- `enum class KudosFilter { SENT, RECEIVED }`
- Package: `com.example.saa.domain.model`
- Không import thêm gì

---

### T008 — Thêm `getProfileKudos()` vào `KudosDataSource.kt`

- [ ] T008

**File**: `data/remote/source/KudosDataSource.kt`
**Depends on**: T007

**Description**: Thêm method mới để query `kudos_view` lọc theo sender hoặc recipient = current user. `userId` lấy từ `auth.currentUserOrNull()?.id` bên trong DataSource — **không nhận userId qua parameter** (cùng pattern với `toggleLike`).

**Acceptance**:
- Method signature: `suspend fun getProfileKudos(filter: KudosFilter): List<KudosDto>`
- Lấy `userId = supabase.auth.currentUserOrNull()?.id ?: return emptyList()`
- SENT: `filter { eq("sender_id", userId) }`
- RECEIVED: `filter { eq("recipient_id", userId) }`
- Query table: `"kudos_view"` (không phải `"kudos"`)
- Order: `order("created_at", Order.DESCENDING)`, `limit(20)`
- Không thay đổi bất kỳ method nào đang có

---

### T009 — Thêm `getProfileKudos()` vào `KudosRepository` interface

- [ ] T009

**File**: `domain/repository/KudosRepository.kt`
**Depends on**: T007

**Description**: Bổ sung method vào interface.

**Acceptance**:
- `suspend fun getProfileKudos(filter: KudosFilter): Result<List<Kudos>>`
- Import `com.example.saa.domain.model.KudosFilter`

---

### T010 — Implement `getProfileKudos()` trong `KudosRepositoryImpl.kt`

- [ ] T010

**File**: `data/repository/KudosRepositoryImpl.kt`
**Depends on**: T008, T009

**Description**: Delegate xuống DataSource, wrap kết quả trong `Result`, map DTO → domain.

**Acceptance**:
- `override suspend fun getProfileKudos(filter: KudosFilter): Result<List<Kudos>>` = `runCatching { dataSource.getProfileKudos(filter).map { it.toDomain() } }`
- Timber error logging khi failure

---

### T011 — Tạo `GetProfileKudosUseCase.kt`

- [ ] T011

**File**: `domain/usecase/GetProfileKudosUseCase.kt`
**Depends on**: T009

**Description**: Thin use-case cho kudos list trên Profile screen.

**Acceptance**:
- `class GetProfileKudosUseCase @Inject constructor(private val repository: KudosRepository)`
- `suspend operator fun invoke(filter: KudosFilter): Result<List<Kudos>> = repository.getProfileKudos(filter)`

---

### T012 — Đăng ký `ProfileRepository` binding trong `KudosModule.kt`

- [ ] T012

**File**: `di/KudosModule.kt`
**Depends on**: T003, T005

**Description**: Thêm `@Binds` cho `ProfileRepository → ProfileRepositoryImpl` vào module hiện tại (không tạo module mới).

**Acceptance**:
- Thêm import: `ProfileRepository`, `ProfileRepositoryImpl`
- Thêm abstract fun: `@Binds @Singleton abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository`
- Không thay đổi các binding đang có

---

## Phase 1 — UiState + ViewModel

> **Goal**: `ProfileViewModel` compile sạch và có thể unit test độc lập.

---

### T013 — Tạo `ProfileUiState.kt`

- [ ] T013

**File**: `presentation/ui/profile/ProfileUiState.kt`
**Depends on**: T002, T007

**Description**: Single state object cho toàn bộ Profile screen. Copy pattern từ `KudosUiState`.

**Acceptance**:
- `data class ProfileUiState(...)` với các fields:

```kotlin
data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val stats: UserStats? = null,
    val kudosList: List<Kudos> = emptyList(),
    val kudosFilter: KudosFilter = KudosFilter.SENT,
    val isDropdownOpen: Boolean = false,
    val error: String? = null,
    val isUnauthenticated: Boolean = false,
    // HomeHeader shared state (same pattern as KudosUiState)
    val unreadNotificationCount: Int = 0,
    val selectedLanguage: String = "VN",
    val showLanguageSelector: Boolean = false,
)
```

- Không có logic trong data class

---

### T014 — Tạo `ProfileViewModel.kt`

- [ ] T014

**File**: `presentation/ui/profile/ProfileViewModel.kt`
**Depends on**: T006, T011, T013

**Description**: ViewModel load data parallel, handle events, expose `StateFlow<ProfileUiState>`. Pattern theo `KudosViewModel`.

**Acceptance**:
- `@HiltViewModel class ProfileViewModel @Inject constructor(...)` với injections:
  - `GetMyProfileUseCase`
  - `GetUserStatsUseCase`
  - `GetProfileKudosUseCase`
  - `GetUnreadNotificationsUseCase`
  - `LanguageRepository`
- `init { loadData(); loadNotificationCount(); collectLanguage() }`
- `loadData()`: parallel `async` cho profile + stats + kudos (filter = `kudosFilter`)
- `onFilterChange(filter: KudosFilter)`: update `kudosFilter` → reload kudos list
- `onDropdownToggle()` / `onDropdownDismiss()`: toggle `isDropdownOpen`
- `consumeError()` / `consumeAuthError()` để clear transient state
- Timber logging — không log user data
- Không dùng `!!`, không expose exception thô ra UI

---

## Phase 2 — Core UI (US1 + US2)

> **Goal**: First-fold render đúng, match Figma pixel-perfect.

---

### T015 — Tạo `ProfileInfoSection.kt`

- [ ] T015

**File**: `presentation/ui/profile/components/ProfileInfoSection.kt`
**Depends on**: T002, T013

**Description**: Avatar tròn + tên + employee code + badge chip. Design tokens từ `design-style.md`.

**Acceptance**:
- `AsyncImage` 72×72dp với `clip(CircleShape)`, `contentScale = ContentScale.Crop`, `fallback`/`error` painter = placeholder icon tròn
- Tên: `Montserrat Bold 18sp`, màu `#FFEA9E` (gold — `--color-profile-name`)
- Employee code: `Montserrat Regular 14sp`, màu `#FFFFFF` (white — `--color-profile-info`)
- Badge chip: `Montserrat Bold ~8sp`, màu `#FFFFFF`, bg tint `#E73928` (`--color-badge-legend-bg`), border-radius full, padding `2dp 8dp`
- Tên `maxLines = 1, overflow = TextOverflow.Ellipsis`
- Gap avatar ↔ name frame: `24dp` (`--spacing-profile-section-gap`)
- Screen horizontal padding: `20dp` (`--spacing-screen-h-padding`)
- Không có logic — chỉ nhận `profile: Profile?` và render

---

### T016 — Tạo `IconCollectionSection.kt`

- [ ] T016

**File**: `presentation/ui/profile/components/IconCollectionSection.kt`
**Depends on**: T002

**Description**: Row 6 badge slot (circular, dark tinted) + label bên dưới. Ẩn section nếu không có badge.

**Acceptance**:
- Component nhận `profile: Profile?` — hiển thị section chỉ khi có `heroTier > 0` hoặc có badge data
- Row: 6 `Box` 36dp circular với background tối, spacing đều nhau (tạm thời dùng `heroTier` để xác định số slot active)
- Label "Bộ sưu tập icon của tôi": `Montserrat Regular 12sp`, màu `#FFFFFF`, centered (`--text-collection-label`)
- Gap icon row ↔ label: `12dp` (`--spacing-collection-gap`)
- Nếu `heroTier == 0 && badgeType.isBlank()`: không render `IconCollectionSection` (cha phải check)
- Screen horizontal padding: `20dp`

---

### T017 — Tạo `ProfileStatsContainer.kt`

- [ ] T017

**File**: `presentation/ui/profile/components/ProfileStatsContainer.kt`
**Depends on**: T013

**Description**: Container stats 5 dòng + divider + "Mở Secret Box" button. Design theo node `6885:10358`.

**Acceptance**:
- Container: bg `#00070C`, border `0.794dp solid #998C5F`, border-radius `8dp`, padding `12dp`, gap rows `8dp`
- 5 `StatRow` composable: label (Montserrat Light 300, 14sp, white) + value (Montserrat Bold 700, 14sp, `#FFEA9E`)
- Thứ tự rows: (1) Số Kudos bạn nhận được, (2) Số Kudos bạn đã gửi, (3) Số tim bạn nhận được → **`HorizontalDivider` màu `#2E3940`** → (4) Số Secret Box bạn đã mở, (5) Số Secret Box chưa mở
- Button "Mở Secret Box 🎁": height `40dp`, Montserrat Medium 14sp, label `#00101A`, bg `#FFEA9E`, width `fillMaxWidth`
- Button enabled khi `stats?.secretBoxesUnopened ?: 0 > 0`; disabled khi `= 0` (Compose `enabled = ...`)
- Button nhận `onOpenSecretBox: () -> Unit` callback — **không** navigate trực tiếp
- Loading skeleton shimmer khi `isLoading = true` (đơn giản: `Box` với shimmer background)
- Screen horizontal padding: `20dp`

---

### T018 — Tạo `ProfileScreen.kt` scaffold

- [ ] T018

**File**: `presentation/ui/profile/ProfileScreen.kt`
**Depends on**: T014, T015, T016, T017

**Description**: Root screen composable — scaffold với keyvisual bg, fixed header, fixed nav bar, `LazyColumn` nội dung. **Chưa** có Kudos section (T019 thêm vào sau).

**Acceptance**:
- Signature đúng với plan:
```kotlin
@Composable
fun ProfileScreen(
    onNavigateToTab: (HomeNavTab) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToKudoDetail: (kudosId: String) -> Unit,
    onNavigateToSecretBox: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
)
```
- `val uiState by viewModel.uiState.collectAsStateWithLifecycle()`
- `LazyListState` cho `bgOffset` parallax (cùng pattern `KudosScreen`)
- `Box` layout: bg keyvisual (full screen) + `LazyColumn` + fixed `HomeHeader` (top) + fixed `HomeNavBar` (bottom, `activeTab = HomeNavTab.PROFILE`)
- `LazyColumn` items: `ProfileInfoSection` → `IconCollectionSection` (nếu có data) → `ProfileStatsContainer` → *(placeholder cho Kudos section)*
- `LaunchedEffect(uiState.isUnauthenticated)`: nếu true → `onNavigateToLogin()` + `viewModel.consumeAuthError()`
- `LaunchedEffect(uiState.error)`: show snackbar + `viewModel.consumeError()`
- `HomeHeader` nhận `unreadCount`, `onNotificationClick = onNavigateToNotifications`, `onSearchClick = onNavigateToSearch`, `onLanguageClick = { viewModel.onLanguageToggle() }`
- `HomeNavBar` với `activeTab = HomeNavTab.PROFILE`, `onTabSelected = onNavigateToTab`
- Không dùng `!!`, không có business logic trong Composable

---

## Phase 3 — Kudos Section (US3 + US4)

> **Goal**: Filter dropdown hoạt động đúng, card list render và filter chính xác.

---

### T019 — Tạo `KudosFilterDropdown.kt`

- [ ] T019

**File**: `presentation/ui/profile/components/KudosFilterDropdown.kt`
**Depends on**: T007, T013

**Description**: Filter pill + overlay dropdown. Overlay dùng `DropdownMenu` của Compose Material3 hoặc `Popup` nếu cần position chính xác.

**Acceptance**:
- Pill button: bg `rgba(255,234,158, 0.10)` (`--color-filter-pill-bg`), border `1dp solid #998C5F`, height `40dp`, border-radius `4dp`, padding `8dp`
- Pill text: filter label với count, ví dụ "Đã gửi (5)" — Montserrat Regular 14sp white
- Caret icon (expand_more) bên phải pill
- Khi `isOpen = true`: overlay hiển thị ngay dưới pill
- Overlay: bg `#00070C`, border `1dp solid #998C5F`, border-radius `8dp`, padding `6dp`, width `118dp`
- **Đúng 2 options**: "Đã nhận (n)" và "Đã gửi (n)" — Montserrat Medium 14sp white
- Active option: text-shadow glow `#FAE287`
- Tap option → `onFilterChange(filter)` + đóng overlay
- Tap ngoài overlay → `onDismiss()` → overlay đóng, filter không đổi
- Component nhận: `selectedFilter: KudosFilter`, `sentCount: Int`, `receivedCount: Int`, `isOpen: Boolean`, `onToggle: () -> Unit`, `onDismiss: () -> Unit`, `onFilterChange: (KudosFilter) -> Unit`

---

### T020 — Tích hợp Kudos section vào `ProfileScreen.kt`

- [ ] T020

**File**: `presentation/ui/profile/ProfileScreen.kt`
**Depends on**: T018, T019

**Description**: Thêm `SectionHeader`, `KudosFilterDropdown`, `KudosHighlightCard` list vào `LazyColumn` trong `ProfileScreen`.

**Acceptance**:
- `SectionHeader(label = "Sun* Annual Awards 2025", title = "KUDOS")` — tái sử dụng từ `presentation/ui/home/components/SectionHeader.kt`
- `KudosFilterDropdown(...)` đặt ngay sau `SectionHeader` item
- `KudosHighlightCard` cho từng item trong `uiState.kudosList`:
  - `onViewDetail = { id -> onNavigateToKudoDetail(id) }` — **không** gọi navController trực tiếp
  - `onCopyLink = { url -> /* copy to clipboard */ }`
  - `onLikeToggle = { id -> viewModel.toggleLike(id) }` (nếu Profile hỗ trợ like) hoặc `{}`
  - `onSenderClick = {}` — no-op
  - `onRecipientClick = {}` — no-op
  - `onHashtagClick = { tag -> onNavigateToSearch(tag) }`
- Empty state item khi `uiState.kudosList.isEmpty() && !uiState.isLoading`: Text "Chưa có Kudos" centered, white, 14sp
- Spacing giữa các card: `24dp` (`--spacing-kudos-list-gap`)
- Screen horizontal padding: `20dp`

---

## Phase 4 — Navigation (US5)

> **Goal**: Tab bar active đúng, điều hướng qua tất cả tabs và deep links hoạt động.

---

### T021 — Wire `ProfileScreen` vào `SaaNavHost.kt`

- [ ] T021

**File**: `SaaNavHost.kt`
**Depends on**: T018, T020

**Description**: Thay `Surface { Text("Profile") }` placeholder bằng `ProfileScreen(...)` với đầy đủ callbacks.

**Acceptance**:
- Thêm import `com.example.saa.presentation.ui.profile.ProfileScreen`
- Replace composable block `NavRoutes.Profile.route`:
```kotlin
composable(NavRoutes.Profile.route) {
    ProfileScreen(
        onNavigateToTab = { tab ->
            when (tab) {
                HomeNavTab.PROFILE -> Unit // already on Profile
                HomeNavTab.SAA -> navController.navigate(NavRoutes.Home.route) {
                    popUpTo(NavRoutes.Profile.route) { inclusive = true }
                }
                HomeNavTab.AWARDS -> navController.navigate(NavRoutes.Awards.route) {
                    popUpTo(NavRoutes.Profile.route) { inclusive = true }
                }
                HomeNavTab.KUDOS -> navController.navigate(NavRoutes.Kudos.route) {
                    popUpTo(NavRoutes.Profile.route) { inclusive = true }
                }
            }
        },
        onNavigateToLogin = {
            navController.navigate(NavRoutes.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        },
        onNavigateToKudoDetail = { kudosId ->
            navController.navigate(NavRoutes.KudoDetail.createRoute(kudosId))
        },
        onNavigateToSecretBox = {
            navController.navigate(NavRoutes.WriteKudo.route) // placeholder until SecretBox screen exists
        },
        onNavigateToNotifications = {
            navController.navigate(NavRoutes.Notifications.route)
        },
        onNavigateToSearch = {
            navController.navigate(NavRoutes.Search.route)
        },
    )
}
```

---

## Phase 5 — Polish + Edge Cases

> **Goal**: Không có crash, graceful fallback cho mọi edge case.

---

### T022 — Avatar error fallback

- [ ] T022

**File**: `presentation/ui/profile/components/ProfileInfoSection.kt`
**Depends on**: T015

**Description**: `AsyncImage` phải hiển thị placeholder tròn khi URL null hoặc load thất bại.

**Acceptance**:
- `AsyncImage(model = profile?.avatarUrl, fallback = painterResource(R.drawable.ic_avatar_placeholder), error = painterResource(R.drawable.ic_avatar_placeholder), ...)`
- Nếu chưa có `ic_avatar_placeholder`: dùng `Icons.Default.Person` trong `Box(modifier = Modifier.background(Color.Gray, CircleShape))`
- Fallback kích thước đúng 72×72dp

---

### T023 — Long name truncation

- [ ] T023

**File**: `presentation/ui/profile/components/ProfileInfoSection.kt`
**Depends on**: T015

**Description**: Tên quá dài phải bị cắt với ellipsis.

**Acceptance**:
- Name `Text` có `maxLines = 1`, `overflow = TextOverflow.Ellipsis`
- Employee code cũng `maxLines = 1` nếu cần

---

### T024 — Empty icon collection state

- [ ] T024

**File**: `presentation/ui/profile/ProfileScreen.kt`
**Depends on**: T016, T018

**Description**: Ẩn `IconCollectionSection` khi user không có badge.

**Acceptance**:
- Trong `LazyColumn` của `ProfileScreen`: `if (profile?.heroTier != null && profile.heroTier > 0 || profile?.badgeType?.isNotBlank() == true)` → hiển thị `IconCollectionSection`
- Nếu ẩn: không có khoảng trống thừa (spacer conditional)

---

### T025 — Stats error state + retry

- [ ] T025

**File**: `presentation/ui/profile/components/ProfileStatsContainer.kt`
**Depends on**: T017

**Description**: Khi API stats trả về lỗi, container hiển thị error message và nút retry.

**Acceptance**:
- Component nhận thêm param `onRetry: () -> Unit`
- Khi `stats == null && !isLoading`: hiển thị Text "Không thể tải dữ liệu" + `TextButton("Thử lại", onClick = onRetry)`
- Khi `isLoading = true`: hiển thị shimmer skeleton (3 dòng placeholder)
- Khi `stats != null`: hiển thị normal state

---

### T026 — Scroll-to-top khi tap Profile tab từ Profile screen

- [ ] T026

**File**: `presentation/ui/profile/ProfileScreen.kt`
**Depends on**: T018, T021

**Description**: Khi user đang ở Profile screen và tap tab Profile lại, scroll về đầu trang.

**Acceptance**:
- `HomeNavBar` được pass `onTabSelected` callback
- Trong `onNavigateToTab` của `ProfileScreen`, khi `tab == HomeNavTab.PROFILE`: gọi `coroutineScope.launch { listState.animateScrollToItem(0) }` thay vì navigate

---

### T027 — Build verification

- [ ] T027

**File**: N/A
**Depends on**: T001–T026

**Description**: Chạy `./gradlew :app:compileDebugKotlin` để đảm bảo không có compile error nào trước khi merge.

**Acceptance**:
- Output: `BUILD SUCCESSFUL`
- Zero Kotlin compile errors (`e:` lines)
- Hilt code generation thành công (không có `@InstallIn` or `@Binds` errors)

**Command**:
```bash
cd android && ./gradlew :app:compileDebugKotlin --no-daemon 2>&1 | grep -E "^e:|error:|BUILD|FAILED|SUCCESSFUL" | head -20
```

---

## Quick Reference

### Files to Create (15 files)

| File | Task |
|------|------|
| `data/remote/dto/ProfileDto.kt` | T001 |
| `domain/model/Profile.kt` | T002 |
| `domain/repository/ProfileRepository.kt` | T003 |
| `data/remote/source/ProfileDataSource.kt` | T004 |
| `data/repository/ProfileRepositoryImpl.kt` | T005 |
| `domain/usecase/GetMyProfileUseCase.kt` | T006 |
| `domain/model/KudosFilter.kt` | T007 |
| `domain/usecase/GetProfileKudosUseCase.kt` | T011 |
| `presentation/ui/profile/ProfileUiState.kt` | T013 |
| `presentation/ui/profile/ProfileViewModel.kt` | T014 |
| `presentation/ui/profile/components/ProfileInfoSection.kt` | T015 |
| `presentation/ui/profile/components/IconCollectionSection.kt` | T016 |
| `presentation/ui/profile/components/ProfileStatsContainer.kt` | T017 |
| `presentation/ui/profile/ProfileScreen.kt` | T018, T020 |
| `presentation/ui/profile/components/KudosFilterDropdown.kt` | T019 |

### Files to Modify (5 files)

| File | Task | Change |
|------|------|--------|
| `data/remote/source/KudosDataSource.kt` | T008 | Add `getProfileKudos(filter)` |
| `domain/repository/KudosRepository.kt` | T009 | Add `getProfileKudos(filter)` |
| `data/repository/KudosRepositoryImpl.kt` | T010 | Implement `getProfileKudos()` |
| `di/KudosModule.kt` | T012 | Add `@Binds ProfileRepository` |
| `SaaNavHost.kt` | T021 | Replace `Surface { Text("Profile") }` placeholder |

### Key Design Tokens (quick ref)

| Token | Value | Usage |
|-------|-------|-------|
| `--color-profile-name` | `#FFEA9E` | Tên user (gold) |
| `--color-profile-info` | `#FFFFFF` | Employee code, labels |
| `--color-stats-container-bg` | `#00070C` | Stats container bg |
| `--color-stats-container-border` | `#998C5F` | Stats container border (0.794dp) |
| `--color-divider` | `#2E3940` | Divider giữa stats groups |
| `--color-button-primary-bg` | `#FFEA9E` | "Mở Secret Box" button bg |
| `--color-button-primary-text` | `#00101A` | "Mở Secret Box" button label |
| `--color-filter-pill-bg` | `#FFEA9E` @ 10% | Filter pill bg |
| `--color-card-hashtag` | `#D4271D` | Hashtag text — đỏ (không phải gold!) |
| `--spacing-screen-h-padding` | `20dp` | Screen horizontal padding |
| `--spacing-avatar-size` | `72dp` | Profile avatar |
