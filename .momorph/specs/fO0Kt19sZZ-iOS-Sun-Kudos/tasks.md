# Tasks: [iOS] Sun*Kudos

**Screen ID**: `fO0Kt19sZZ`
**Frame**: `6885:9059 — [iOS] Sun*Kudos`
**Prerequisites**: `ios_sun_kudos_plan.md` ✅ reviewed | `ios_sun_kudos.md` (spec) ✅ reviewed | `ios_sun_kudos_design_style.md` ✅ reviewed

---

## Task Format

```
- [ ] T### [P?] [US?] Description | file/path.kt
```

- **[P]**: Có thể chạy song song (file khác nhau, không có dependency)
- **[US]**: Thuộc User Story nào (US1–US5)
- **|**: File path bị ảnh hưởng (relative từ `android/app/src/main/java/com/example/saa/`)

---

## User Stories

| # | Story | Priority | Mô tả |
|---|-------|----------|--------|
| US1 | KV Banner + Write Kudos Button | 🔴 P1 — MVP | Section A: background + logo "KUDOS" + sticky Write button (A.1) |
| US2 | Highlight Kudos Carousel | 🔴 P1 — MVP | Section B: carousel 5 cards + filter hashtag/phòng ban + pagination "N/5" |
| US3 | Spotlight Board | 🟡 P2 | Section B.6/B.7: pan/zoom board + search sunner + tổng kudos |
| US4 | All Kudos Feed | 🔴 P1 — MVP | Section C: lazy list KudosPostCard + realtime + view all link |
| US5 | Personal Stats + Secret Box | 🔴 P1 — MVP | Section D: thống kê cá nhân D.1 + Secret Box CTA D.2 + gift recipients D.3 |

---

## Phase 1: Data & Domain Foundation *(blocking all UI)*

**Mục đích**: Toàn bộ data/domain layer phải xong trước khi code UI.

⚠️ **CRITICAL**: Không bắt đầu Phase 3–9 cho đến khi Phase 1 + 2 complete.

### DTOs

- [ ] T001 [P] Tạo `KudosDto.kt` — `@Serializable data class KudosDto`: fields nullable (trừ `id`): `senderId`, `recipientId`, `message`, `awardCategoryName`, `heartCount`, `createdAt`, `hashtags: List<HashtagDto>?`, `senderAvatarUrl`, `senderName`, `senderEmployeeCode`, `senderBadgeType`, `recipientAvatarUrl`, `recipientName`, `recipientHeroTier`, `shareUrl`, `isLiked`, `photoUrls: List<String>?`, `isAnonymous: Boolean?`, `anonymousNickname: String?`, `canLike: Boolean?`; thêm `fun KudosDto.toDomain(): Kudos` | `data/remote/dto/KudosDto.kt`
- [ ] T002 [P] Tạo `UserStatsDto.kt` — `@Serializable data class UserStatsDto`: `id`, `kudosReceived?`, `kudosSent?`, `heartsReceived?`, `secretBoxesOpened?`, `secretBoxesUnopened?`; thêm mapper `toDomain()` | `data/remote/dto/UserStatsDto.kt`
- [ ] T003 [P] Tạo `HashtagDto.kt` — `@Serializable data class HashtagDto`: `id`, `name?`; mapper `toDomain()` | `data/remote/dto/HashtagDto.kt`
- [ ] T004 [P] Tạo `DepartmentDto.kt` — `@Serializable data class DepartmentDto`: `id`, `name?`; mapper `toDomain()` | `data/remote/dto/DepartmentDto.kt`
- [ ] T005 [P] Tạo `GiftRecipientDto.kt` — `@Serializable data class GiftRecipientDto`: `id`, `fullName?`, `avatarUrl?`, `rewardName?`; mapper `toDomain()` | `data/remote/dto/GiftRecipientDto.kt`

### Domain Models

- [ ] T006 [P] Tạo `Kudos.kt` domain model — non-nullable fields sau mapping: `id`, `senderId`, `recipientId`, `message`, `awardCategoryName`, `heartCount`, `createdAt`, `hashtags: List<String>`, `senderName`, `senderAvatarUrl`, `senderEmployeeCode`, `senderBadgeType`, `recipientName`, `recipientAvatarUrl`, `recipientHeroTier`, `shareUrl`, `isLiked: Boolean`, `photoUrls: List<String>`, `isAnonymous: Boolean = false`, `anonymousNickname: String = ""`, `canLike: Boolean = true` | `domain/model/Kudos.kt`
- [ ] T007 [P] Tạo `UserStats.kt` domain model — `kudosReceived`, `kudosSent`, `heartsReceived`, `secretBoxesOpened`, `secretBoxesUnopened` (tất cả `Int`, default 0) | `domain/model/UserStats.kt`
- [ ] T008 [P] Tạo `Hashtag.kt` domain model — `id: String`, `name: String` | `domain/model/Hashtag.kt`
- [ ] T009 [P] Tạo `Department.kt` domain model — `id: String`, `name: String` | `domain/model/Department.kt`
- [ ] T010 [P] Tạo `GiftRecipient.kt` domain model — `id`, `fullName`, `avatarUrl`, `rewardName` | `domain/model/GiftRecipient.kt`

### Repository Interfaces

- [ ] T011 [P] Tạo `KudosRepository.kt` interface — `getHighlightKudos(hashtagId?, departmentId?): Result<List<Kudos>>`; `getAllKudos(page, limit, hashtagId?, departmentId?): Result<List<Kudos>>`; `toggleLike(kudosId): Result<Unit>`; `observeNewKudos(): Flow<Kudos>` | `domain/repository/KudosRepository.kt`
  > Depends on: T006
- [ ] T012 [P] Tạo `UserStatsRepository.kt` interface — `getUserStats(): Result<UserStats>`; `getGiftRecipients(): Result<List<GiftRecipient>>`; `getHashtags(): Result<List<Hashtag>>`; `getDepartments(): Result<List<Department>>`; `getNextSecretBox(): Result<String>`; `openSecretBox(boxId: String): Result<Unit>` | `domain/repository/UserStatsRepository.kt`
  > Depends on: T007, T008, T009, T010

### Data Sources

- [ ] T013 Tạo `KudosDataSource.kt` — inject `SupabaseClient`; method `getHighlightKudos(hashtagId?, departmentId?)`, `getAllKudos(page, limit, hashtagId?, departmentId?)`, `toggleLike(kudosId)`, Realtime subscribe via Supabase Realtime channel cho bảng `kudos` INSERT | `data/remote/source/KudosDataSource.kt`
  > Depends on: T001, T003, T004
- [ ] T014 Tạo `UserStatsDataSource.kt` — inject `SupabaseClient`; method `getUserStats()`, `getGiftRecipients()`, `getHashtags()`, `getDepartments()`, `getNextSecretBox()` (query `secret_boxes` where `user_id = me AND opened = false LIMIT 1`), `openSecretBox(boxId)` | `data/remote/source/UserStatsDataSource.kt`
  > Depends on: T002, T003, T004, T005

### Repository Implementations

- [ ] T015 Tạo `KudosRepositoryImpl.kt` — implement `KudosRepository`, inject `KudosDataSource`, map DTO → domain, wrap trong `runCatching`, expose Realtime qua `Flow` | `data/repository/KudosRepositoryImpl.kt`
  > Depends on: T011, T013
- [ ] T016 Tạo `UserStatsRepositoryImpl.kt` — implement `UserStatsRepository`, inject `UserStatsDataSource`, map DTO → domain | `data/repository/UserStatsRepositoryImpl.kt`
  > Depends on: T012, T014

### Use Cases

- [ ] T017 [P] Tạo `GetHighlightKudosUseCase.kt` — gọi `KudosRepository.getHighlightKudos(hashtagId?, departmentId?): Result<List<Kudos>>` | `domain/usecase/GetHighlightKudosUseCase.kt`
  > Depends on: T011
- [ ] T018 [P] Tạo `GetAllKudosUseCase.kt` — gọi `KudosRepository.getAllKudos(...): Result<List<Kudos>>` | `domain/usecase/GetAllKudosUseCase.kt`
  > Depends on: T011
- [ ] T019 [P] Tạo `ToggleKudosLikeUseCase.kt` — gọi `KudosRepository.toggleLike(kudosId): Result<Unit>` | `domain/usecase/ToggleKudosLikeUseCase.kt`
  > Depends on: T011
- [ ] T020 [P] Tạo `GetUserStatsUseCase.kt` — gọi `UserStatsRepository.getUserStats(): Result<UserStats>` | `domain/usecase/GetUserStatsUseCase.kt`
  > Depends on: T012
- [ ] T021 [P] Tạo `GetGiftRecipientsUseCase.kt` — gọi `UserStatsRepository.getGiftRecipients(): Result<List<GiftRecipient>>` | `domain/usecase/GetGiftRecipientsUseCase.kt`
  > Depends on: T012
- [ ] T022 [P] Tạo `GetHashtagsUseCase.kt` — gọi `UserStatsRepository.getHashtags(): Result<List<Hashtag>>` | `domain/usecase/GetHashtagsUseCase.kt`
  > Depends on: T012
- [ ] T023 [P] Tạo `GetDepartmentsUseCase.kt` — gọi `UserStatsRepository.getDepartments(): Result<List<Department>>` | `domain/usecase/GetDepartmentsUseCase.kt`
  > Depends on: T012
- [ ] T023B [P] Tạo `OpenSecretBoxUseCase.kt` — gọi `getNextSecretBox()` lấy box ID rồi `openSecretBox(boxId)`; double-tap prevention bằng `AtomicBoolean` flag; return `Result<Unit>` | `domain/usecase/OpenSecretBoxUseCase.kt`
  > Depends on: T012

### DI Module

- [ ] T024 Tạo `KudosModule.kt` — `@Module @InstallIn(SingletonComponent)`, `@Binds` `KudosRepository → KudosRepositoryImpl`, `UserStatsRepository → UserStatsRepositoryImpl` | `di/KudosModule.kt`
  > Depends on: T015, T016

**Checkpoint Phase 1**: Build xanh, domain models + use cases có thể unit test với mock repos.

---

## Phase 2: ViewModel + UiState *(blocking all UI)*

- [ ] T025 Tạo `KudosUiState.kt` — `data class KudosUiState`: `isLoading: Boolean`, `highlightKudos: List<Kudos>`, `allKudos: List<Kudos>`, `hashtags: List<Hashtag>`, `departments: List<Department>`, `userStats: UserStats?`, `giftRecipients: List<GiftRecipient>`, `selectedHashtagId: String?`, `selectedDepartmentId: String?`, `currentCarouselPage: Int`, `error: String?`, `isLoadingMore: Boolean`, `hasMoreKudos: Boolean` | `presentation/ui/kudos/KudosUiState.kt`
  > Depends on: T006, T007, T008, T009, T010
- [ ] T026 Tạo `KudosViewModel.kt` — `@HiltViewModel`, inject 8 use cases (T017–T023, T023B); expose `StateFlow<KudosUiState>`; implement `loadInitialData()` (parallel coroutines), `onFilterHashtag(id?)`, `onFilterDepartment(id?)`, `onCarouselPageChange(page)`, `onToggleLike(kudosId)`, `loadMoreKudos()`, `observeRealtime()`, `onOpenSecretBox()`; optimistic update cho like | `presentation/ui/kudos/KudosViewModel.kt`
  > Depends on: T017–T023, T023B, T025

**Checkpoint Phase 2**: ViewModel testable với fake use cases; `collectAsStateWithLifecycle` hoạt động.

---

## Phase 3: US1 — KV Banner + Write Kudos Button *(Section A)*

- [ ] T027 [P] [US1] Tạo `KudosKVBanner.kt` — Composable: `AsyncImage` full-width background `mm_media_bg`; text "Hệ thống ghi nhận và cảm ơn" (Montserrat 12sp Regular, gold @ 60%); text "KUDOS" (Montserrat 48sp ExtraBold, `#FFEA9E`) | `presentation/ui/kudos/components/KudosKVBanner.kt`
- [ ] T028 [P] [US1] Tạo `WriteKudosButton.kt` — Composable: `Box` rounded 8dp, border `rgba(255,255,255,0.20)`, bg `rgba(255,255,255,0.08)`, icon `ic_kudos_pen` + placeholder text (Montserrat 14sp, white 60%); onClick lambda; sticky variant `6891:21267` dùng `LazyListState.firstVisibleItemIndex` → `Box` fixed-top overlay khi `index > 0`, không dùng FAB | `presentation/ui/kudos/components/WriteKudosButton.kt`
- [ ] T029 [US1] Tạo `KudosScreen.kt` (skeleton) — `@Composable` + `@HiltViewModel` + `collectAsStateWithLifecycle`; `Box` bg `#00101A`; `LazyColumn`; `item { KudosKVBanner() }`; `item { WriteKudosButton(onClick = onNavigateToWriteKudo) }` | `presentation/ui/kudos/KudosScreen.kt`

**Checkpoint Phase 3**: Banner + button hiển thị đúng, sticky overlay hoạt động khi scroll.

---

## Phase 4: US2 — Highlight Kudos Carousel *(Section B)*

- [ ] T030 [P] [US2] Tạo `KudosFilterRow.kt` — Row 2 filter `Button` (Hashtag ▼, Phòng ban ▼); bg `rgba(255,255,255,0.08)`, border `rgba(255,255,255,0.20)`, radius 8dp; selected state: gold bg + `#FFEA9E` border/text; opens BottomSheet với list từ API; onSelect lambda `(String?) -> Unit` | `presentation/ui/kudos/components/KudosFilterRow.kt`
- [ ] T031 [P] [US2] Tạo `KudosHighlightCard.kt` — `Card` border `rgba(255,255,255,0.20)` radius 12dp; Header: sender avatar 40dp + tên/badge/employeeCode + arrow icon + receiver avatar 40dp; Content: timestamp (11sp) + category UPPERCASE (13sp Bold) + message (14sp, max 3 lines) + hashtag chips; Action row: heart toggle (disabled khi `!kudos.canLike`) + Copy Link + Xem chi tiết; lambdas: `onSenderClick(userId)`, `onRecipientClick(userId)`, `onLikeToggle(kudosId)`, `onCopyLink(shareUrl)`, `onViewDetail(kudosId)`, `onHashtagClick(hashtagId)` | `presentation/ui/kudos/components/KudosHighlightCard.kt`
  > Depends on: T006
- [ ] T032 [P] [US2] Tạo `KudosPagination.kt` — Row `‹ "N/5" ›`; prev/next `IconButton` circle 32dp; `Text` "N/5" 13sp SemiBold white 80%; disabled state opacity 0.3; nhận `currentPage: Int`, `total: Int`, `onPrev`, `onNext` | `presentation/ui/kudos/components/KudosPagination.kt`
- [ ] T033 [US2] Tạo `KudosHighlightCarousel.kt` — `HorizontalPager` `pageSize = Fixed(311.dp)`, `beyondBoundsPageCount = 1`; side card opacity 0.4 scale 0.95; nav buttons B.2.1 (prev ‹) + B.2.2 (next ›) floating via `Box` — **lưu ý B.2.1/B.2.2 tên trong MoMorph bị swap, implement theo description**; tiêu đề "HIGHLIGHT KUDOS" (20sp Bold gold) | `presentation/ui/kudos/components/KudosHighlightCarousel.kt`
  > Depends on: T031
- [ ] T034 [US2] Cập nhật `KudosScreen.kt` — thêm section B: `item { KudosFilterRow(...) }`; `item { KudosHighlightCarousel(...) }`; `item { KudosPagination(...) }` | `presentation/ui/kudos/KudosScreen.kt`
  > Depends on: T030, T032, T033

**Checkpoint Phase 4**: Carousel hiển thị 5 cards, swipe + nav buttons hoạt động, filter reset page về 1.

---

## Phase 5: US3 — Spotlight Board *(Section B.6/B.7)*

- [ ] T035 [P] [US3] Tạo `SpotlightBoardSection.kt` — Composable header: subtitle "Sun* Annual Awards 2025" + title "SPOTLIGHT BOARD" (20sp Bold gold); `Box` container cho SpotlightBoard | `presentation/ui/kudos/components/SpotlightBoardSection.kt`
- [ ] T036 [US3] Tạo `SpotlightBoard.kt` — `Canvas` composable với `detectTransformGestures` (pan + zoom); background `AsyncImage` (B.7.2); text overlay "N KUDOS" (B.7.1, 16sp Bold); Sunner text nodes từ API data; `OutlinedTextField` search (B.7.3) → filter + highlight; không hardcode tên Sunner | `presentation/ui/kudos/components/SpotlightBoard.kt`
- [ ] T037 [US3] Cập nhật `KudosScreen.kt` — thêm `item { SpotlightBoardSection(...) }` sau carousel section | `presentation/ui/kudos/KudosScreen.kt`
  > Depends on: T035, T036

**Checkpoint Phase 5**: Pan/zoom board render, search filter highlight node.

---

## Phase 6: US4 — All Kudos Feed *(Section C)*

- [ ] T038 [P] [US4] Tạo `KudosPostCard.kt` — card bg `rgba(255,255,255,0.06)` border `rgba(255,255,255,0.15)` radius 12dp; sender row (avatar 32dp + name + `→` + recipient name/badge); **khi `isAnonymous = true`: ẩn sender info, hiện `anonymousNickname`** (C.3.1); timestamp (C.3.4); CATEGORY + message (max 5 lines) + hashtags; ❤ disabled khi `!canLike` + Copy Link; lambdas: `onSenderClick`, `onRecipientClick`, `onLikeToggle`, `onCopyLink` | `presentation/ui/kudos/components/KudosPostCard.kt`
  > Depends on: T006
- [ ] T039 [US4] Cập nhật `KudosScreen.kt` — thêm section C: `item { C1Header() }` ("ALL KUDOS"); `items(allKudos) { KudosPostCard(...) }`; `item { ViewAllKudosLink(onClick) }` (navigates to AllKudos screen); `item { CircularProgressIndicator }` khi `isLoadingMore`; load more khi gần cuối list | `presentation/ui/kudos/KudosScreen.kt`
  > Depends on: T038

**Checkpoint Phase 6**: Feed render đúng, anonymous kudos mask sender, scroll load more.

---

## Phase 7: US5 — Personal Stats + Secret Box *(Section D)*

- [ ] T040 [P] [US5] Tạo `PersonalStatsGrid.kt` — Row 3 stat items (kudos received, sent, hearts) 28sp Bold; Divider `rgba(255,255,255,0.20)`; Row 2 stat items (secret box opened, unopened); nhận `UserStats` | `presentation/ui/kudos/components/PersonalStatsGrid.kt`
  > Depends on: T007
- [ ] T041 [P] [US5] Tạo `OpenSecretBoxButton.kt` — `Button` full-width 52dp height, bg `#FFEA9E`, text "Mở Secret Box 🎁" 14sp SemiBold `#00101A`; disabled state `rgba(255,234,158,0.30)`; enabled khi `userStats.secretBoxesUnopened > 0`; onClick → `viewModel.onOpenSecretBox()` | `presentation/ui/kudos/components/OpenSecretBoxButton.kt`
  > Depends on: T007
- [ ] T042 [P] [US5] Tạo `GiftRecipientsList.kt` — title "NHẬN QUÀ MỚI NHẤT"; `Column` của `GiftRecipientRow` (avatar 44dp circular + name 14sp SemiBold + reward text 12sp Regular + divider); onClick → profile; nhận `List<GiftRecipient>` + `onClickRow(userId)` | `presentation/ui/kudos/components/GiftRecipientsList.kt`
  > Depends on: T010
- [ ] T043 [US5] Cập nhật `KudosScreen.kt` — thêm section D: `item { PersonalStatsGrid(userStats) }`; `item { OpenSecretBoxButton(...) }`; `item { GiftRecipientsList(...) }` | `presentation/ui/kudos/KudosScreen.kt`
  > Depends on: T040, T041, T042

**Checkpoint Phase 7**: Stats grid đúng số liệu, secret box button disabled/enabled theo data, recipients list render.

---

## Phase 8: Realtime + Navigation Integration

- [ ] T044A Cập nhật `NavRoutes.kt` — thêm `data object AllKudos : NavRoutes("all_kudos")`; `data object KudoDetail : NavRoutes("kudo/{kudosId}") { fun createRoute(id: String) = "kudo/$id" }` (nếu chưa có) | `NavRoutes.kt`
- [ ] T044 Cập nhật `KudosViewModel.kt` — gọi `observeRealtime()` trong `init {}`; collect `Flow<Kudos>` → prepend vào `allKudos`; **hủy Realtime channel trong `onCleared()`** để tránh leak | `presentation/ui/kudos/KudosViewModel.kt`
  > Depends on: T026
- [ ] T045 Cập nhật `SaaNavHost.kt` — thêm `composable(NavRoutes.Kudos.route) { KudosScreen(...) }` với lambdas: `onNavigateToWriteKudo`, `onNavigateToAllKudos`, `onNavigateToKudoDetail`, `onNavigateToProfile` | `SaaNavHost.kt`
- [ ] T046 Cập nhật `HomeNavBar.kt` — đảm bảo tab "Kudos" navigate đúng sang `NavRoutes.Kudos` (thay placeholder nếu có) | `presentation/ui/home/components/HomeNavBar.kt`

**Checkpoint Phase 8**: Realtime nhận kudo mới → xuất hiện đầu feed, NavHost route đăng ký đúng.

---

## Phase 9: Polish & Error States

- [ ] T047 [P] Thêm Skeleton loading — khi `isLoading = true`: `ShimmerBox` placeholder carousel (311dp × 200dp) + 3 `ShimmerBox` cho All Kudos cards | `presentation/ui/kudos/KudosScreen.kt`
- [ ] T048 [P] Thêm Empty state — section C khi `allKudos.isEmpty() && !isLoading`: `Text("Chưa có Kudos nào")` centered, secondary color | `presentation/ui/kudos/KudosScreen.kt`
- [ ] T049 [P] Thêm Error state — `uiState.error != null`: `Snackbar` với retry button; `consumeError()` sau khi show | `presentation/ui/kudos/KudosScreen.kt`
- [ ] T050 [P] Thêm `@Preview` — `KudosHighlightCard`, `KudosPostCard`, `PersonalStatsGrid`, `GiftRecipientsList`, `OpenSecretBoxButton` với `uiMode = UI_MODE_NIGHT_YES` | component files tương ứng

**Checkpoint Phase 9**: App đủ polish cho demo; skeleton/empty/error states cover mọi scenario.

---

## Dependency Map

```
T001–T005 [P]  ← DTOs (parallel)
T006–T010 [P]  ← Domain models (parallel)
T011 ← T006
T012 ← T007, T008, T009, T010
T013 ← T001, T003, T004
T014 ← T002, T003, T004, T005
T015 ← T011, T013
T016 ← T012, T014
T017 ← T011  T018 ← T011  T019 ← T011   ← Use cases (parallel per group)
T020 ← T012  T021 ← T012  T022 ← T012  T023 ← T012  T023B ← T012
T024 ← T015, T016

T025 ← T006–T010
T026 ← T017–T023, T023B, T025

T027 [P]  T028 [P]  ← Section A (parallel)
T029 ← T026 (KudosScreen skeleton)

T030 [P]  T031 ← T006  T032 [P]   ← Section B components (T030, T032 parallel)
T033 ← T031
T034 ← T030, T032, T033

T035 [P]
T036 ← (SpotlightBoard data from ViewModel)
T037 ← T035, T036

T038 ← T006
T039 ← T038

T040 ← T007  T041 ← T007  T042 ← T010   ← Section D (parallel)
T043 ← T040, T041, T042

T044A (NavRoutes — no deps)
T044 ← T026
T045 ← T029, T039, T043
T046 (HomeNavBar — no deps on Kudos code)

T047 [P]  T048 [P]  T049 [P]  T050 [P]  ← Polish (all parallel)
```

---

## Blockers Summary

| # | Blocker | Impact | Owner |
|---|---------|--------|-------|
| B1 | API endpoints `/api/v1/kudos/highlight`, `/api/v1/kudos`, `/api/v1/users/me/secret-boxes/*` chưa tồn tại | T013, T014 phải dùng mock data; swap DataSource khi API ready | Backend Team |
| B2 | Asset `mm_media_bg` (KV background image) chưa có trong `res/drawable/` | T027 blocked | Designer |
| B3 | Asset `ic_kudos_pen` (Write button icon) chưa có trong `res/drawable/` | T028 blocked | Designer |
| B4 | Spotlight Board data API (Sunner positions trên board) chưa có spec | T036 chỉ render static layout | Backend / PM |
| B5 | Secret Box animation screen chưa có spec | T041 onClick chỉ trigger use case, không navigate đến animation | PM / Designer |

---

## Estimated Effort

| Phase | Tasks | Estimate |
|-------|-------|----------|
| Phase 1 — Data & Domain | T001–T024, T023B (25 tasks) | 4–6h |
| Phase 2 — ViewModel | T025–T026 (2 tasks) | 2–3h |
| Phase 3 — US1 KV Banner | T027–T029 (3 tasks) | 2–3h |
| Phase 4 — US2 Carousel | T030–T034 (5 tasks) | 4–6h |
| Phase 5 — US3 Spotlight | T035–T037 (3 tasks) | 3–5h |
| Phase 6 — US4 Feed | T038–T039 (2 tasks) | 2–3h |
| Phase 7 — US5 Stats | T040–T043 (4 tasks) | 2–3h |
| Phase 8 — Realtime + Nav | T044A, T044–T046 (4 tasks) | 2–3h |
| Phase 9 — Polish | T047–T050 (4 tasks) | 1–2h |
| **Total** | **52 tasks** | **~22–34h** |

> Phase 3–9 có thể chạy song song với nhau sau khi Phase 1 + 2 hoàn thành.
> Thời gian chưa bao gồm thời gian chờ blockers (B1–B5).
