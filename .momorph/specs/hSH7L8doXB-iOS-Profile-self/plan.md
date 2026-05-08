# Implementation Plan: [iOS] Profile bản thân

**Frame**: `hSH7L8doXB-iOS-Profile-self`
**Date**: 2026-05-06
**Spec**: `specs/hSH7L8doXB-iOS-Profile-self/spec.md`

---

## Summary

Xây dựng màn hình Profile cá nhân cho người dùng đang đăng nhập. Màn hình gồm 5 phần chính: (1) Profile info (avatar, tên, mã nhân viên, badge danh hiệu), (2) Icon collection row (badge đã earn), (3) Stats container + "Mở Secret Box" CTA, (4) Kudos section với filter dropdown, (5) Scrollable kudos card list.

Approach: tạo `ProfileScreen` / `ProfileViewModel` / `ProfileUiState` theo chuẩn MVVM + Clean Architecture của dự án. Tái sử dụng `HomeHeader`, `HomeNavBar`, `KudosHighlightCard`. Data lấy từ `profiles` + `user_stats` + `kudos_view` (Supabase) với các use-case đã có (`GetCurrentUserUseCase`, `GetUserStatsUseCase`) và use-case mới (`GetProfileKudosUseCase`).

---

## Technical Context

| | |
|--|--|
| **Language/Framework** | Kotlin / Jetpack Compose + Material3 |
| **Architecture** | MVVM + Clean Architecture (Data → Domain → Presentation) |
| **DI** | Hilt |
| **Navigation** | Jetpack Navigation Compose — `NavRoutes.Profile` đã có |
| **Async** | Kotlin Coroutines + `StateFlow` |
| **Backend** | Supabase (supabase-kt 3.1.4) — `profiles`, `user_stats`, `kudos_view` |
| **Image loading** | Coil 2.7.0 — `AsyncImage` |
| **State** | `ProfileUiState` data class, `StateFlow` từ `ProfileViewModel` |

---

## Constitution Compliance Check

*GATE: Must pass before implementation can begin*

- [x] MVVM + Clean Architecture — Screen/ViewModel/UiState/UseCase/Repository/DataSource tách biệt
- [x] Không dùng `!!` — safe call `?.` và Elvis `?:` throughout
- [x] Supabase DTO fields nullable ngoại trừ PK
- [x] Không có logic nghiệp vụ trong Composable — uỷ quyền cho ViewModel
- [x] Chỉ dùng Material3
- [x] Token trong `EncryptedSharedPreferences` (đã có `EncryptedSessionManager`)
- [x] Logging qua Timber — không log PII
- [x] Sealed `Result<T>` cho error handling
- [x] Màu sắc từ `MaterialTheme` / token constants — không hard-code
- [x] Navigation qua `NavRoutes.Profile` đã có sẵn trong `NavRoutes.kt`

**Violations**: Không có

---

## Architecture Decisions

### Presentation Layer
- **Component pattern**: 1 Screen Composable + 1 ViewModel + 1 UiState (chuẩn dự án)
- **State**: `ProfileUiState` — single state object, ViewModel emit qua `StateFlow`
- **Composable decomposition**:
  - `ProfileScreen.kt` — root composable, subscribe UiState, forward events
  - `components/ProfileInfoSection.kt` — Avatar + Name + Badge
  - `components/IconCollectionSection.kt` — Icon badge row + label
  - `components/ProfileStatsContainer.kt` — Stats grid + Secret Box button
  - `components/KudosFilterDropdown.kt` — Filter pill + overlay
  - Tái sử dụng: `HomeHeader`, `HomeNavBar`, `KudosHighlightCard`

### Data Layer
- **Profile data**: `UserStatsRepository.getUserStats()` — đã có; cần thêm `ProfileRepository.getProfile()` từ `profiles` table
- **Kudos list**: Tái sử dụng `KudosDataSource` nhưng filter theo `sender_id = me` hoặc `recipient_id = me` → tạo `GetProfileKudosUseCase`
- **User stats**: `GetUserStatsUseCase` đã có → tái sử dụng trực tiếp
- **Current user**: `GetCurrentUserUseCase` đã có → cần extend `User` model với profile fields

### Quyết định kiến trúc quan trọng

| Vấn đề | Quyết định | Lý do |
|--------|-----------|-------|
| `User` model thiếu `avatarUrl`, `employeeCode`, `badgeType`, `heroTier` | Tạo `ProfileRepository` + `ProfileDataSource` riêng, return `Profile` domain model | Tách biệt auth user (minimal) với profile data (rich) |
| Icon collection badges | Dùng `badge_type` / `hero_tier` từ `profiles` — hiển thị placeholder badge icons theo tier | Không có bảng `user_badges` riêng trong schema hiện tại |
| Kudos list filter | Thêm `getProfileKudos(filter: KudosFilter)` vào `KudosDataSource` — `userId` lấy từ `auth.currentUserOrNull()?.id` bên trong DataSource (cùng pattern với `toggleLike`) | Không expose userId qua use-case chain; tránh duplicate |
| Dropdown overlay dismiss | `Box` với `clickable` trên backdrop bên ngoài overlay | Không dùng `Dialog` — cần control positioning chính xác theo Figma |
| Keyvisual background | Tái sử dụng pattern từ `KudosScreen` / `HomeScreen` (`bgOffset` từ `LazyListState`) | Consistent với toàn app |

---

## Project Structure

### Documentation

```text
.momorph/specs/hSH7L8doXB-iOS-Profile-self/
├── spec.md              ✅ reviewed
├── design-style.md      ✅ reviewed
├── plan.md              ← này
└── tasks.md             (next step)
```

### New Files

| File | Purpose |
|------|---------|
| `data/remote/dto/ProfileDto.kt` | DTO mapping `profiles` table (full_name, employee_code, avatar_url, badge_type, hero_tier) |
| `data/remote/source/ProfileDataSource.kt` | Supabase query `profiles WHERE id = auth.uid()` |
| `data/repository/ProfileRepositoryImpl.kt` | Implements `ProfileRepository` |
| `domain/model/Profile.kt` | Domain model: id, fullName, employeeCode, avatarUrl, badgeType, heroTier |
| `domain/repository/ProfileRepository.kt` | Interface: `getMyProfile(): Result<Profile>` |
| `domain/usecase/GetMyProfileUseCase.kt` | Thin use-case wrapping `ProfileRepository.getMyProfile()` |
| `domain/usecase/GetProfileKudosUseCase.kt` | `operator fun invoke(filter: KudosFilter): Result<List<Kudos>>` — không nhận `userId` param, DataSource tự lấy từ `auth.uid()` |
| `domain/model/KudosFilter.kt` | `enum class KudosFilter { SENT, RECEIVED }` — dùng chung cho ProfileViewModel và GetProfileKudosUseCase |
| `presentation/ui/profile/ProfileScreen.kt` | Root screen composable |
| `presentation/ui/profile/ProfileViewModel.kt` | ViewModel với UiState + events |
| `presentation/ui/profile/ProfileUiState.kt` | Data class cho toàn bộ UI state của màn hình |
| `presentation/ui/profile/components/ProfileInfoSection.kt` | Avatar + tên + employee code + badge chip |
| `presentation/ui/profile/components/IconCollectionSection.kt` | Row icon badges + "Bộ sưu tập icon của tôi" label |
| `presentation/ui/profile/components/ProfileStatsContainer.kt` | 5 stat rows + divider + "Mở Secret Box" button |
| `presentation/ui/profile/components/KudosFilterDropdown.kt` | Filter pill + overlay (2 options) |
| *(none — see Modified Files)* | `ProfileRepository` binding được thêm vào module hiện có |

### Modified Files

| File | Changes |
|------|---------|
| `SaaNavHost.kt` | Thay placeholder `Surface { Text("Profile") }` bằng `ProfileScreen(navController, onNavigateToTab, onNavigateToLogin, ...)` — xem callback signature bên dưới |
| `data/remote/source/KudosDataSource.kt` | Thêm `getProfileKudos(filter: KudosFilter): List<KudosDto>` — `userId` lấy từ `auth.currentUserOrNull()?.id ?: return emptyList()` trong DataSource (cùng pattern với `toggleLike`); query `kudos_view` với filter `sender_id.eq.$userId` (SENT) hoặc `recipient_id.eq.$userId` (RECEIVED) |
| `domain/repository/KudosRepository.kt` | Thêm `getProfileKudos(filter: KudosFilter): Result<List<Kudos>>` |
| `data/repository/KudosRepositoryImpl.kt` | Implement `getProfileKudos(filter)` bằng cách delegate xuống `dataSource.getProfileKudos(filter)` |
| `di/KudosModule.kt` | Thêm `@Binds` cho `ProfileRepository` → `ProfileRepositoryImpl` (tránh tạo module mới không cần thiết) |
| `NavRoutes.kt` | Không cần thay đổi — `Profile` route đã có |

**`ProfileScreen` callback signature** (để `SaaNavHost.kt` wire đúng):
```kotlin
@Composable
fun ProfileScreen(
    onNavigateToTab: (HomeNavTab) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToKudoDetail: (kudosId: String) -> Unit,
    onNavigateToSecretBox: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSearch: () -> Unit,
)
```

### Dependencies
Không cần thêm dependency mới — toàn bộ dùng thư viện đã có.

---

## Implementation Strategy

### Phase 0: Data Layer Foundation

**Mục tiêu**: Có đủ data để render màn hình trước khi viết UI.

1. Tạo `ProfileDto.kt` — map `profiles` table columns
2. Tạo `ProfileDataSource.kt` — query `profiles WHERE id = auth.uid()`
3. Tạo `Profile` domain model + `ProfileRepository` interface
4. Tạo `ProfileRepositoryImpl.kt`
5. Tạo `GetMyProfileUseCase.kt`
6. Thêm `getProfileKudos(filter)` vào `KudosDataSource` + repository chain
7. Tạo `GetProfileKudosUseCase.kt`
8. Thêm `@Binds` cho `ProfileRepository → ProfileRepositoryImpl` vào `di/KudosModule.kt`

### Phase 1: UiState + ViewModel

**Mục tiêu**: ViewModel hoạt động đúng, có thể test độc lập.

```kotlin
// ProfileUiState.kt
data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val stats: UserStats? = null,
    val kudosList: List<Kudos> = emptyList(),
    val kudosFilter: KudosFilter = KudosFilter.SENT,
    val isDropdownOpen: Boolean = false,
    val error: String? = null,
    // HomeHeader shared state (same pattern as KudosUiState)
    val unreadNotificationCount: Int = 0,
    val selectedLanguage: String = "VN",
    val showLanguageSelector: Boolean = false,
)
// KudosFilter defined in domain/model/KudosFilter.kt
```

- `ProfileViewModel` injects: `GetMyProfileUseCase`, `GetUserStatsUseCase`, `GetProfileKudosUseCase`, `GetUnreadNotificationsUseCase`, `LanguageRepository` (same pattern as `KudosViewModel`)
- `ProfileViewModel` load parallel: `getMyProfile()` + `getUserStats()` + `getProfileKudos(userId, SENT)` + `getUnreadNotifications()`
- Event: `onFilterChange(KudosFilter)` → reload kudos list
- Event: `onDropdownToggle()` / `onDropdownDismiss()`
- Event: `onOpenSecretBox()` — không cần async, `ProfileStatsContainer` nhận `onOpenSecretBox: () -> Unit` callback, `ProfileScreen` forward `onNavigateToSecretBox` callback trực tiếp xuống. Không dùng Channel (không có async work trước navigation)

### Phase 2: Core UI — Profile Info + Stats (US1 + US2)

**Mục tiêu**: First-fold render đúng, match Figma pixel-perfect.

1. `ProfileScreen.kt` — scaffold với `Box`, keyvisual bg, `LazyColumn`, fixed header + nav
2. `ProfileInfoSection.kt`:
   - `AsyncImage` 72×72dp circle với fallback placeholder
   - `Text` tên: `Montserrat Bold 18sp #FFEA9E`
   - Row: employee code `14sp white` + dot + badge chip `8sp bold white`
3. `IconCollectionSection.kt`:
   - Row 6 badge slots (circular, 36dp, dark tinted)
   - Label "Bộ sưu tập icon của tôi" 12sp white centered
   - Empty state: ẩn section nếu không có badges
4. `ProfileStatsContainer.kt`:
   - Container: bg `#00070C`, border `0.794dp solid #998C5F`, radius 8dp, padding 12dp
   - 5 `StatRow` composables + `HorizontalDivider` giữa row 3 và 4
   - "Mở Secret Box" `Button`: enabled/disabled dựa vào `stats.secretBoxesUnopened > 0`
   - Loading skeleton shimmer khi `isLoading = true`

### Phase 3: Kudos Section — Filter + List (US3 + US4)

**Mục tiêu**: Filter dropdown hoạt động đúng, card list render chính xác.

1. `SectionHeader` (tái sử dụng từ `presentation/ui/home/components/SectionHeader.kt`) — gọi `SectionHeader(label = "Sun* Annual Awards 2025", title = "KUDOS")`
2. `KudosFilterDropdown.kt`:
   - Pill button: `rgba(FFEA9E, 0.10)` bg, `#998C5F` border 1dp, 40dp height, radius 4dp
   - Overlay `Box` với `Popup` hoặc `DropdownMenu`: 118dp wide, 2 options
   - Dismiss on outside tap
3. `LazyColumn` danh sách `KudosHighlightCard` (tái sử dụng):
   - `onViewDetail = { id -> onNavigateToKudoDetail(id) }` — dùng callback từ `ProfileScreen` signature, **không** gọi `navController` trực tiếp trong Composable (vi phạm Constitution rule 4)
   - `onCopyLink` → clipboard toast (giữ nguyên)
   - `onLikeToggle` → `viewModel.toggleLike(id)` hoặc `{}` nếu Profile không hỗ trợ like
   - `onSenderClick = {}` / `onRecipientClick = {}` — no-op (Profile screen không navigate đến profile người khác trong scope này)
   - `onHashtagClick = {}` — no-op hoặc `{ tag -> onNavigateToSearch(tag) }` nếu muốn filter theo hashtag
4. Empty state "Chưa có Kudos" khi list rỗng

### Phase 4: Navigation + Bottom Nav (US5)

**Mục tiêu**: Tab bar active state đúng, điều hướng qua tất cả tabs hoạt động.

1. Cập nhật `SaaNavHost.kt` — mount `ProfileScreen` tại `NavRoutes.Profile.route`
2. `HomeNavBar` trong `ProfileScreen` với `activeTab = HomeNavTab.PROFILE`
3. Wire `onNavigateToTab` callbacks giống `KudosScreen`

### Phase 5: Polish + Edge Cases

1. Avatar error fallback (placeholder icon)
2. Long name truncation `maxLines = 1, overflow = TextOverflow.Ellipsis`
3. Empty icon collection → ẩn `IconCollectionSection`
4. Stats = 0 → hiển thị "0" (không ẩn row)
5. API error state → retry button trong Stats Container
6. Scroll-to-top khi tap Profile tab từ Profile screen

---

## Risk Assessment

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| `profiles` thiếu `employee_code` / `badge_type` cho user test | Medium | Medium | Seed data trong `supabase/seeds/dev/` có các giá trị này; fallback graceful nếu null |
| `icon collection` không có bảng `user_badges` | High | Low | Dùng `hero_tier` từ `profiles` để hiển thị placeholder badge icons theo tier level |
| Overlay dropdown bị clip bởi `LazyColumn` | Medium | Medium | Dùng `Popup` với `windowSoftInputMode` thay vì `DropdownMenu` trong column |
| `ProfileViewModel` cần userId để query `getProfileKudos()` — nhưng `GetMyProfileUseCase` là async | Low | Low | Load profile trước, dùng `profile.id` trong coroutine chain; hoặc lấy `auth.currentUserOrNull()?.id` trực tiếp trong DataSource (không cần pass qua ViewModel) |
| `kudos_view` chưa có filter `WHERE sender_id = me OR recipient_id = me` | Low | High | `getProfileKudos()` thêm filter Supabase: `.or("sender_id.eq.$userId,recipient_id.eq.$userId")` với thêm điều kiện theo filter SENT/RECEIVED |

---

## Integration Testing Strategy

### Test Scope

- [x] `ProfileViewModel` — load data, filter change, error revert
- [x] `ProfileDataSource` — Supabase query trả về đúng row cho `auth.uid()`
- [x] `KudosDataSource.getProfileKudos()` — filter SENT vs RECEIVED đúng
- [x] `ProfileStatsContainer` — enabled/disabled state của button

### Test Categories

| Category | Scenario | Tool |
|----------|----------|------|
| Unit | `ProfileViewModel.onFilterChange()` → kudos list reload | JUnit + Turbine |
| Unit | `ProfileViewModel` khi profile null → `error` state | JUnit |
| Unit | Secret Box button enabled khi `secretBoxesUnopened > 0` | JUnit |
| Integration | `ProfileDataSource.getMyProfile()` trả đúng user data | Supabase local |
| Manual | Dropdown overlay dismiss khi tap outside | Device/emulator |
| Manual | Long name truncation với ellipsis | Device/emulator |

---

## Open Questions

- [ ] Icon collection: Figma hiển thị 6 icon badge slots — source data từ đâu? Schema hiện tại không có `user_badges` table. → Tạm thời dùng `hero_tier` để render placeholder icons, hoặc thêm migration mới?
- [ ] "Mở Secret Box" button → navigate đến Secret Box screen hay mở Bottom Sheet? Screen chưa được implement (`NavRoutes.WriteKudo` placeholder). → Plan hiện tại: emit navigation event, wire-up khi Secret Box screen được build.
- [ ] Kudos list trong Profile có hỗ trợ pagination (infinite scroll) không, hay fixed 20 items? → Default: load 20 items, thêm pagination sau nếu cần.
