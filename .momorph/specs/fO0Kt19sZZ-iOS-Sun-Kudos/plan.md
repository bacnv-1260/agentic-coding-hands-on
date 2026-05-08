# Implementation Plan: [iOS] Sun*Kudos

**Screen ID**: `fO0Kt19sZZ`
**Frame**: `6885:9059 — [iOS] Sun*Kudos`
**Date**: 2026-05-06
**Spec**: `.momorph/contexts/screen_specs/ios_sun_kudos.md` ✅ reviewed
**Design Style**: `.momorph/contexts/screen_specs/ios_sun_kudos_design_style.md` ✅ reviewed

---

## Summary

Màn hình chính của tính năng Sun*Kudos — feed lời cảm ơn, gửi kudos mới, xem thống kê cá nhân, và mở Secret Box. Gồm 4 section cuộn dạng `LazyColumn`:

- **A — KV Banner**: Background keyvisual + "Write Kudos" button (sticky khi scroll)
- **B — Highlight Kudos**: Carousel 5 cards (top heart count) + filter + Spotlight Board tương tác
- **C — All Kudos**: Feed dạng list + view all link
- **D — Personal Stats**: Thống kê cá nhân + Secret Box CTA + danh sách người nhận quà

Approach: MVVM + Clean Architecture, dữ liệu từ Supabase PostgREST, Realtime subscribe bảng `kudos`.

---

## Technical Context

| Property | Value |
|----------|-------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose + Material3 |
| **Architecture** | MVVM + Clean Architecture (Data → Domain → Presentation) |
| **DI** | Hilt — `@HiltViewModel` |
| **Async** | Coroutines + StateFlow |
| **Data Source** | Supabase PostgREST (supabase-kt BOM 3.1.4) + Supabase Realtime |
| **Image Loading** | Coil 2.7.0 — `AsyncImage` |
| **Navigation** | `NavRoutes.Kudos` → screen đã có route |
| **Existing Routes** | `NavRoutes.Kudos`, `NavRoutes.WriteKudo`, `NavRoutes.Search` ✅ |

---

## Constitution Compliance Check

- [x] MVVM + Clean Architecture — Data → Domain → Presentation
- [x] Không dùng `!!` — dùng safe call `?.` và Elvis `?:`
- [x] Supabase DTO: tất cả field nullable (trừ primary key)
- [x] Không logic nghiệp vụ trong Composable
- [x] Chỉ dùng Material3
- [x] Màu sắc qua `MaterialTheme.colorScheme` (Background, ButtonPrimaryBg đã có trong `Color.kt`)
- [x] Xử lý lỗi: Sealed `Result<T>`, không để lộ exception ra UI
- [x] Logging: Timber only
- [x] Dependency: version trong `libs.versions.toml` (đã có đủ dependencies)

**Violations**: Không có.

---

## Architecture Decisions

### Presentation Layer
- `KudosScreen.kt` — root composable thin, delegate mọi thứ cho ViewModel
- `KudosViewModel.kt` — expose `StateFlow<KudosUiState>`, handle events
- `KudosUiState.kt` — sealed/data class cho UI state (loading, content, error)
- Components folder chia theo section: `kv/`, `highlight/`, `spotlight/`, `feed/`, `stats/`

### Domain Layer
- **Domain models mới**: `Kudos`, `UserStats`, `GiftRecipient`, `Hashtag`, `Department` — **không có `KudosHighlight` riêng** (highlight và all kudos dùng chung model `Kudos`)
- **Use cases**: `GetHighlightKudosUseCase`, `GetAllKudosUseCase`, `ToggleKudosLikeUseCase`, `OpenSecretBoxUseCase`, `GetUserStatsUseCase`, `GetGiftRecipientsUseCase`, `GetHashtagsUseCase`, `GetDepartmentsUseCase`
- **Repository interfaces mới**: `KudosRepository`, `UserStatsRepository`

### Data Layer
- **DTOs mới**: `KudosDto`, `UserStatsDto`, `HashtagDto`, `DepartmentDto`, `GiftRecipientDto`
- **DataSources mới**: `KudosDataSource`, `UserStatsDataSource`
- **Repository impls**: `KudosRepositoryImpl`, `UserStatsRepositoryImpl`
- **Realtime**: subscribe trong ViewModel, emit qua `SharedFlow` → update `StateFlow`

### DI
- Module mới `KudosModule.kt` bind repository interfaces → impls

---

## Project Structure

```
android/app/src/main/java/com/example/saa/
│
├── data/
│   ├── remote/
│   │   ├── dto/
│   │   │   ├── KudosDto.kt               # NEW
│   │   │   ├── UserStatsDto.kt           # NEW
│   │   │   ├── HashtagDto.kt             # NEW
│   │   │   ├── DepartmentDto.kt          # NEW
│   │   │   └── GiftRecipientDto.kt       # NEW
│   │   └── source/
│   │       ├── KudosDataSource.kt        # NEW
│   │       └── UserStatsDataSource.kt    # NEW
│   └── repository/
│       ├── KudosRepositoryImpl.kt        # NEW
│       └── UserStatsRepositoryImpl.kt    # NEW
│
├── domain/
│   ├── model/
│   │   ├── Kudos.kt                      # NEW
│   │   ├── UserStats.kt                  # NEW
│   │   ├── Hashtag.kt                    # NEW
│   │   ├── Department.kt                 # NEW
│   │   └── GiftRecipient.kt              # NEW
│   ├── repository/
│   │   ├── KudosRepository.kt            # NEW
│   │   └── UserStatsRepository.kt        # NEW
│   └── usecase/
│       ├── GetHighlightKudosUseCase.kt   # NEW
│       ├── GetAllKudosUseCase.kt         # NEW
│       ├── ToggleKudosLikeUseCase.kt     # NEW
│       ├── OpenSecretBoxUseCase.kt       # NEW
│       ├── GetUserStatsUseCase.kt        # NEW
│       ├── GetGiftRecipientsUseCase.kt   # NEW
│       ├── GetHashtagsUseCase.kt         # NEW
│       └── GetDepartmentsUseCase.kt      # NEW
│
├── presentation/
│   └── ui/
│       └── kudos/
│           ├── KudosScreen.kt            # NEW
│           ├── KudosViewModel.kt         # NEW
│           ├── KudosUiState.kt           # NEW
│           └── components/
│               ├── KudosKVBanner.kt      # NEW (Section A)
│               ├── WriteKudosButton.kt   # NEW (A.1)
│               ├── KudosFilterRow.kt     # NEW (B.1)
│               ├── KudosHighlightCarousel.kt  # NEW (B.2 + B.3)
│               ├── KudosHighlightCard.kt      # NEW (B.3)
│               ├── KudosPagination.kt         # NEW (B.5)
│               ├── SpotlightBoardSection.kt   # NEW (B.6 + B.7)
│               ├── KudosPostCard.kt           # NEW (C.3)
│               ├── PersonalStatsGrid.kt       # NEW (D.1)
│               ├── OpenSecretBoxButton.kt     # NEW (D.2)
│               └── GiftRecipientsList.kt      # NEW (D.3)
│
└── di/
    └── KudosModule.kt                    # NEW
```

---

## User Stories

| # | Story | Priority | Mô tả |
|---|-------|----------|--------|
| US1 | KV Banner + Write Kudos Button | 🔴 P1 — MVP | Section A: background, logo "KUDOS", sticky Write button |
| US2 | Highlight Kudos Carousel | 🔴 P1 — MVP | Section B: carousel 5 cards + filter hashtag/phòng ban + pagination "N/5" |
| US3 | Spotlight Board | 🟡 P2 | Section B.7: pan/zoom board + search sunner + tổng kudos |
| US4 | All Kudos Feed | 🔴 P1 — MVP | Section C: lazy list KudosPostCard + realtime insert + view all link |
| US5 | Personal Stats + Secret Box | 🔴 P1 — MVP | Section D: thống kê cá nhân D.1, D.2 CTA, D.3 gift recipients |

---

## Phase Breakdown

### Phase 1: Data & Domain Foundation *(blocking all UI)*

**Mục đích**: Toàn bộ data/domain layer phải xong trước khi code UI.

#### Data Layer — DTOs

- [ ] T001 [P] Tạo `KudosDto.kt` — `@Serializable data class KudosDto`: fields nullable (trừ `id`): `senderId`, `recipientId`, `message`, `awardCategoryName`, `heartCount`, `createdAt`, `hashtags: List<HashtagDto>?`, `senderAvatarUrl`, `senderName`, `senderEmployeeCode`, `senderBadgeType`, `recipientAvatarUrl`, `recipientName`, `recipientHeroTier`, `shareUrl`, `isLiked`, `photoUrls: List<String>?`, **`isAnonymous: Boolean?`**, **`anonymousNickname: String?`**, **`canLike: Boolean?`**; thêm `fun KudosDto.toDomain(): Kudos` | `data/remote/dto/KudosDto.kt`
- [ ] T002 [P] Tạo `UserStatsDto.kt` — `@Serializable data class UserStatsDto`: `id`, `kudosReceived?`, `kudosSent?`, `heartsReceived?`, `secretBoxesOpened?`, `secretBoxesUnopened?`; thêm mapper `toDomain()` | `data/remote/dto/UserStatsDto.kt`
- [ ] T003 [P] Tạo `HashtagDto.kt` — `@Serializable data class HashtagDto`: `id`, `name?`; mapper `toDomain()` | `data/remote/dto/HashtagDto.kt`
- [ ] T004 [P] Tạo `DepartmentDto.kt` — `@Serializable data class DepartmentDto`: `id`, `name?`; mapper `toDomain()` | `data/remote/dto/DepartmentDto.kt`
- [ ] T005 [P] Tạo `GiftRecipientDto.kt` — `@Serializable data class GiftRecipientDto`: `id`, `fullName?`, `avatarUrl?`, `rewardName?`; mapper `toDomain()` | `data/remote/dto/GiftRecipientDto.kt`

#### Domain Models

- [ ] T006 [P] Tạo `Kudos.kt` domain model — non-nullable fields sau mapping; bao gồm `id`, `senderId`, `recipientId`, `message`, `awardCategoryName`, `heartCount`, `createdAt`, `hashtags: List<String>`, `senderName`, `senderAvatarUrl`, `senderEmployeeCode`, `senderBadgeType`, `recipientName`, `recipientAvatarUrl`, `recipientHeroTier`, `shareUrl`, `isLiked: Boolean`, `photoUrls: List<String>`, **`isAnonymous: Boolean = false`**, **`anonymousNickname: String = ""`**, **`canLike: Boolean = true`** | `domain/model/Kudos.kt`
- [ ] T007 [P] Tạo `UserStats.kt` domain model — `kudosReceived`, `kudosSent`, `heartsReceived`, `secretBoxesOpened`, `secretBoxesUnopened` (tất cả `Int`, default 0) | `domain/model/UserStats.kt`
- [ ] T008 [P] Tạo `Hashtag.kt` domain model — `id: String`, `name: String` | `domain/model/Hashtag.kt`
- [ ] T009 [P] Tạo `Department.kt` domain model — `id: String`, `name: String` | `domain/model/Department.kt`
- [ ] T010 [P] Tạo `GiftRecipient.kt` domain model — `id`, `fullName`, `avatarUrl`, `rewardName` | `domain/model/GiftRecipient.kt`

#### Domain Repository Interfaces

- [ ] T011 [P] Tạo `KudosRepository.kt` interface — `getHighlightKudos(hashtagId?, departmentId?): Result<List<Kudos>>`; `getAllKudos(page, limit, hashtagId?, departmentId?): Result<List<Kudos>>`; `toggleLike(kudosId): Result<Unit>`; `observeNewKudos(): Flow<Kudos>` (Realtime) | `domain/repository/KudosRepository.kt`
  > Depends on: T006
- [ ] T012 [P] Tạo `UserStatsRepository.kt` interface — `getUserStats(): Result<UserStats>`; `getGiftRecipients(): Result<List<GiftRecipient>>`; `getHashtags(): Result<List<Hashtag>>`; `getDepartments(): Result<List<Department>>`; **`getNextSecretBox(): Result<String>`** (trả box ID); **`openSecretBox(boxId: String): Result<Unit>`** (mở box) | `domain/repository/UserStatsRepository.kt`
  > Depends on: T007, T008, T009, T010

#### Data Sources

- [ ] T013 Tạo `KudosDataSource.kt` — inject `SupabaseClient`; method `getHighlightKudos(hashtagId?, departmentId?)`, `getAllKudos(page, limit, hashtagId?, departmentId?)`, `toggleLike(kudosId)`, Realtime subscribe via Supabase Realtime channel cho bảng `kudos` INSERT; return DTOs | `data/remote/source/KudosDataSource.kt`
  > Depends on: T001, T003, T004
- [ ] T014 Tạo `UserStatsDataSource.kt` — inject `SupabaseClient`; method `getUserStats(): UserStatsDto`, `getGiftRecipients(): List<GiftRecipientDto>`, `getHashtags(): List<HashtagDto>`, `getDepartments(): List<DepartmentDto>`, **`getNextSecretBox(): String`** (query `secret_boxes` where `user_id = me AND opened = false LIMIT 1`), **`openSecretBox(boxId: String)`** (`POST` hoặc update record) | `data/remote/source/UserStatsDataSource.kt`
  > Depends on: T002, T003, T004, T005

#### Repository Implementations

- [ ] T015 Tạo `KudosRepositoryImpl.kt` — implement `KudosRepository`, inject `KudosDataSource`, map DTO → domain, wrap mọi call trong `runCatching`, expose Realtime qua `Flow` | `data/repository/KudosRepositoryImpl.kt`
  > Depends on: T011, T013
- [ ] T016 Tạo `UserStatsRepositoryImpl.kt` — implement `UserStatsRepository`, inject `UserStatsDataSource`, map DTO → domain | `data/repository/UserStatsRepositoryImpl.kt`
  > Depends on: T012, T014

#### Use Cases

- [ ] T017 [P] Tạo `GetHighlightKudosUseCase.kt` — gọi `KudosRepository.getHighlightKudos(hashtagId?, departmentId?)`: `Result<List<Kudos>>` | `domain/usecase/GetHighlightKudosUseCase.kt`
  > Depends on: T011
- [ ] T018 [P] Tạo `GetAllKudosUseCase.kt` — gọi `KudosRepository.getAllKudos(...)`: `Result<List<Kudos>>` | `domain/usecase/GetAllKudosUseCase.kt`
  > Depends on: T011
- [ ] T019 [P] Tạo `ToggleKudosLikeUseCase.kt` — gọi `KudosRepository.toggleLike(kudosId)`: `Result<Unit>` | `domain/usecase/ToggleKudosLikeUseCase.kt`
  > Depends on: T011
- [ ] T020 [P] Tạo `GetUserStatsUseCase.kt` — gọi `UserStatsRepository.getUserStats()`: `Result<UserStats>` | `domain/usecase/GetUserStatsUseCase.kt`
  > Depends on: T012
- [ ] T021 [P] Tạo `GetGiftRecipientsUseCase.kt` — gọi `UserStatsRepository.getGiftRecipients()`: `Result<List<GiftRecipient>>` | `domain/usecase/GetGiftRecipientsUseCase.kt`
  > Depends on: T012
- [ ] T022 [P] Tạo `GetHashtagsUseCase.kt` — gọi `UserStatsRepository.getHashtags()`: `Result<List<Hashtag>>` | `domain/usecase/GetHashtagsUseCase.kt`
  > Depends on: T012
- [ ] T023 [P] Tạo `GetDepartmentsUseCase.kt` — gọi `UserStatsRepository.getDepartments()`: `Result<List<Department>>` | `domain/usecase/GetDepartmentsUseCase.kt`
  > Depends on: T012
- [ ] T023B [P] Tạo `OpenSecretBoxUseCase.kt` — gọi `UserStatsRepository.getNextSecretBox(): Result<String>` (box ID) rồi `UserStatsRepository.openSecretBox(boxId): Result<Unit>`; thực hiện double-tap prevention bằng flag trong use case; return `Result<Unit>` | `domain/usecase/OpenSecretBoxUseCase.kt`
  > Depends on: T012

#### DI Module

- [ ] T024 Tạo `KudosModule.kt` — `@Module @InstallIn(SingletonComponent)`, `@Binds` `KudosRepository → KudosRepositoryImpl`, `UserStatsRepository → UserStatsRepositoryImpl` | `di/KudosModule.kt`
  > Depends on: T015, T016

**Checkpoint Phase 1**: Build xanh, domain models + use cases có thể unit test với mock repos.

---

### Phase 2: ViewModel + UiState *(blocking all UI)*

- [ ] T025 Tạo `KudosUiState.kt` — `data class KudosUiState`: `isLoading: Boolean`, `highlightKudos: List<Kudos>`, `allKudos: List<Kudos>`, `hashtags: List<Hashtag>`, `departments: List<Department>`, `userStats: UserStats?`, `giftRecipients: List<GiftRecipient>`, `selectedHashtagId: String?`, `selectedDepartmentId: String?`, `currentCarouselPage: Int`, `error: String?`, `isLoadingMore: Boolean`, `hasMoreKudos: Boolean` | `presentation/ui/kudos/KudosUiState.kt`
  > Depends on: T006, T007, T008, T009, T010
- [ ] T026 Tạo `KudosViewModel.kt` — `@HiltViewModel`, inject tất cả **8 use cases** (bao gồm `OpenSecretBoxUseCase`); expose `StateFlow<KudosUiState>`; implement: `loadInitialData()` (parallel launch highlight + allKudos + hashtags + departments + userStats + giftRecipients), `onFilterHashtag(id?)`, `onFilterDepartment(id?)`, `onCarouselPageChange(page)`, `onToggleLike(kudosId)`, `loadMoreKudos()`, `observeRealtime()` (subscribe Realtime → prepend mới lên `allKudos`), `onOpenSecretBox()` (gọi `OpenSecretBoxUseCase`); optimistic update cho like | `presentation/ui/kudos/KudosViewModel.kt`
  > Depends on: T017–T023, T023B, T025

**Checkpoint Phase 2**: ViewModel có thể được test với fake use cases; `collectAsStateWithLifecycle` hoạt động.

---

### Phase 3: US1 — KV Banner + Write Kudos Button *(Section A)*

- [ ] T027 [P] Tạo `KudosKVBanner.kt` — Composable: background `mm_media_bg` (BoxWithConstraints + AsyncImage full-width); text "Hệ thống ghi nhận và cảm ơn" (Montserrat 12px Regular, gold @ 60%); text logo "KUDOS" (Montserrat 48px ExtraBold, `#FFEA9E`) | `presentation/ui/kudos/components/KudosKVBanner.kt`
- [ ] T028 [P] Tạo `WriteKudosButton.kt` — Composable: `Box` rounded 8dp, border `rgba(255,255,255,0.20)`, background `rgba(255,255,255,0.08)`, `Image(ic_kudos_pen)` icon + `Text` placeholder (Montserrat 14px Regular, white @ 60%); onClick lambda; đây là `A.1` default (node `6885:9083`); **sticky variant `6891:21267`**: dùng `LazyListState.firstVisibleItemIndex` — khi index > 0 (đã scroll qua banner A) hiển thị instance thứ 2 bằng `Box` fixed-top overlay bên trong `Scaffold`, không dùng `FloatingActionButton` | `presentation/ui/kudos/components/WriteKudosButton.kt`
- [ ] T029 Tạo `KudosScreen.kt` (skeleton) — `@Composable`, `@HiltViewModel`, `collectAsStateWithLifecycle`; `Box` với background `#00101A`; `LazyColumn` với HomeHeader overlay top + HomeNavBar overlay bottom; `item { KudosKVBanner() }`; `item { WriteKudosButton(onClick = onNavigateToWriteKudo) }` | `presentation/ui/kudos/KudosScreen.kt`

---

### Phase 4: US2 — Highlight Kudos Carousel *(Section B)*

- [ ] T030 [P] Tạo `KudosFilterRow.kt` — Composable: Row 2 filter `Button` (Hashtag ▼, Phòng ban ▼); style: background `rgba(255,255,255,0.08)`, border `rgba(255,255,255,0.20)`, radius 8dp; selected state: gold tint background + `#FFEA9E` border/text; onClick lambda `(String?) -> Unit` | `presentation/ui/kudos/components/KudosFilterRow.kt`
- [ ] T031 [P] Tạo `KudosHighlightCard.kt` — Composable: `Card` border `rgba(255,255,255,0.20)` radius 12dp; Header row: 2 avatar `AsyncImage` 40dp circular + tên/badge/employee code + arrow icon; Content: timestamp (Montserrat 11px) + category UPPERCASE (13px Bold) + message (14px Regular, max 3 lines) + hashtag chips (click chip → `onHashtagClick(hashtagId)`) + Action row (heart toggle disabled khi `!kudos.canLike` + Copy Link + Xem chi tiết); lambdas: `onSenderClick(userId)` (B.3.1), `onRecipientClick(userId)` (B.3.5/B.3.6), `onLikeToggle(kudosId)`, `onCopyLink(shareUrl)`, `onViewDetail(kudosId)`, `onHashtagClick(hashtagId)` (B.4.3 — filters cả Highlight + All Kudos) | `presentation/ui/kudos/components/KudosHighlightCard.kt`
  > Depends on: T006
- [ ] T032 Tạo `KudosPagination.kt` — Composable: Row `‹ "N/5" ›`; prev/next `IconButton` circle 32dp, `Text` "N/5" Montserrat 13px SemiBold white @ 80%; disabled state opacity 0.3; nhận `currentPage: Int`, `total: Int`, `onPrev`, `onNext` lambdas | `presentation/ui/kudos/components/KudosPagination.kt`
- [ ] T033 Tạo `KudosHighlightCarousel.kt` — Composable: `HorizontalPager` (Compose Foundation) `pageSize = Fixed(311.dp)`, `beyondBoundsPageCount = 1`; side card opacity 0.4 scale 0.95; nav overlay `‹/›` (B.2.1/B.2.2) floating via `Box` trên carousel — **lưu ý: trong MoMorph `B.2.1` được gọi `"Nút Tiến (Next)"` nhưng description là "card trước" (prev), và `B.2.2` gọi `"Nút lùi"` nhưng description là "item tiếp theo" (next) — tên trong MoMorph bị swap, implement theo description: B.2.1 = prev (‹), B.2.2 = next (›)**; tiêu đề section "HIGHLIGHT KUDOS" (Montserrat 20px Bold gold); nhận `List<Kudos>`, `currentPage`, pager callbacks, event lambdas | `presentation/ui/kudos/components/KudosHighlightCarousel.kt`
  > Depends on: T031
- [ ] T034 Cập nhật `KudosScreen.kt` — thêm section B vào LazyColumn: `item { KudosFilterRow(...) }`; `item { KudosHighlightCarousel(...) }`; `item { KudosPagination(...) }` | `presentation/ui/kudos/KudosScreen.kt`
  > Depends on: T030, T032, T033

---

### Phase 5: US3 — Spotlight Board *(Section B.6/B.7)*

- [ ] T035 Tạo `SpotlightBoardSection.kt` — Composable: Section header "Sun* Annual Awards 2025" + "SPOTLIGHT BOARD" (Montserrat 20px Bold gold); `Box` chứa B.7 SpotlightBoard bên dưới | `presentation/ui/kudos/components/SpotlightBoardSection.kt`
- [ ] T036 Tạo `SpotlightBoard.kt` — Composable: `Canvas` composable với `detectTransformGestures` (pan + zoom); background `AsyncImage` (B.7.2); text overlay `"N KUDOS"` (B.7.1, Montserrat 16px Bold); Sunner text nodes vị trí từ data; `OutlinedTextField` search (B.7.3, `mms_B.7.3`) → filter + highlight node; hiển thị tên Sunner từ API (không hardcode) | `presentation/ui/kudos/components/SpotlightBoard.kt`
- [ ] T037 Cập nhật `KudosScreen.kt` — thêm `item { SpotlightBoardSection(...) }` sau pagination | `presentation/ui/kudos/KudosScreen.kt`
  > Depends on: T035, T036

---

### Phase 6: US4 — All Kudos Feed *(Section C)*

- [ ] T038 [P] Tạo `KudosPostCard.kt` — Composable: card `rgba(255,255,255,0.06)` border `rgba(255,255,255,0.15)` radius 12dp; sender row (avatar 32dp + name + `→` + recipient name/badge); **khi `kudos.isAnonymous = true`: ẩn sender avatar/name, hiện `kudos.anonymousNickname` thay thế** (C.3.1); timestamp (C.3.4); CATEGORY (B.4.2) + message (max 5 lines, C.3.5) + hashtags (C.3.5); ❤ disabled khi `!kudos.canLike` + Copy Link actions; lambdas: `onSenderClick(userId)`, `onRecipientClick(userId)`, `onLikeToggle(kudosId)`, `onCopyLink(shareUrl)` | `presentation/ui/kudos/components/KudosPostCard.kt`
  > Depends on: T006
- [ ] T039 Cập nhật `KudosScreen.kt` — thêm section C: `item { AllKudosSectionHeader() }` (text "ALL KUDOS"); `items(allKudos) { KudosPostCard(...) }`; `item { ViewAllKudosLink(onClick) }`; `item { CircularProgressIndicator }` khi `isLoadingMore`; LaunchedEffect `loadMoreKudos()` khi scroll đến gần cuối (NestedScrollConnection hoặc `LazyListState.isScrolledToEnd()`) | `presentation/ui/kudos/KudosScreen.kt`
  > Depends on: T038

---

### Phase 7: US5 — Personal Stats + Secret Box *(Section D)*

- [ ] T040 [P] Tạo `PersonalStatsGrid.kt` — Composable: Row 3 stat items (kudos received, kudos sent, hearts) Montserrat 28px Bold; Divider `rgba(255,255,255,0.20)`; Row 2 stat items (secret box opened, unopened); nhận `UserStats` | `presentation/ui/kudos/components/PersonalStatsGrid.kt`
  > Depends on: T007
- [ ] T041 [P] Tạo `OpenSecretBoxButton.kt` — Composable: `Button` full-width 52dp height, background `#FFEA9E`, text `"Mở Secret Box 🎁"` Montserrat 14px SemiBold `#00101A`; disabled state `rgba(255,234,158,0.30)`; enabled = `userStats.secretBoxesUnopened > 0`; onClick lambda → gọi `viewModel.onOpenSecretBox()` | `presentation/ui/kudos/components/OpenSecretBoxButton.kt`
  > Depends on: T007
- [ ] T042 [P] Tạo `GiftRecipientsList.kt` — Composable: title "NHẬN QUÀ MỚI NHẤT"; `Column` của `GiftRecipientRow` (avatar 44dp circular + name 14px SemiBold + reward text 12px Regular + divider); nhận `List<GiftRecipient>` + onClickRow lambda | `presentation/ui/kudos/components/GiftRecipientsList.kt`
  > Depends on: T010
- [ ] T043 Cập nhật `KudosScreen.kt` — thêm section D vào LazyColumn: `item { PersonalStatsGrid(userStats) }`; `item { OpenSecretBoxButton(...) }`; `item { GiftRecipientsList(...) }` | `presentation/ui/kudos/KudosScreen.kt`
  > Depends on: T040, T041, T042

---

### Phase 8: Realtime + Navigation Integration

- [ ] T044A Cập nhật `NavRoutes.kt` — thêm `data object AllKudos : NavRoutes("all_kudos")`; `data object KudoDetail : NavRoutes("kudo/{kudosId}") { fun createRoute(id: String) = "kudo/$id" }` (nếu chưa có) | `NavRoutes.kt`
- [ ] T044 Cập nhật `KudosViewModel.kt` — gọi `observeRealtime()` trong `init {}` block; collect `Flow<Kudos>` từ `KudosRepository.observeNewKudos()` → prepend kudo mới vào đầu `allKudos` list trong UiState; **hủy Realtime channel trong `onCleared()`** để tránh leak | `presentation/ui/kudos/KudosViewModel.kt`
  > Depends on: T026
- [ ] T045 Cập nhật `SaaNavHost.kt` — thêm `composable(NavRoutes.Kudos.route) { KudosScreen(...) }` với navigation lambdas: `onNavigateToWriteKudo`, `onNavigateToAllKudos`, `onNavigateToKudoDetail`, `onNavigateToProfile` | `SaaNavHost.kt`
- [ ] T046 Cập nhật `HomeScreen.kt` / `HomeNavBar.kt` — đảm bảo tab "Kudos" navigate đúng sang `NavRoutes.Kudos` (thay placeholder nếu có) | `presentation/ui/home/components/HomeNavBar.kt`

---

### Phase 9: Polish & Error States

- [ ] T047 [P] Thêm Skeleton loading vào `KudosScreen.kt` — khi `isLoading = true`: `ShimmerBox` placeholder cho carousel (1 card width=311dp height=200dp) + 3 `ShimmerBox` cho All Kudos list cards | `presentation/ui/kudos/KudosScreen.kt`
- [ ] T048 [P] Thêm Empty state — section C khi `allKudos.isEmpty() && !isLoading`: `Text("Chưa có Kudos nào")` centered với secondary color | `presentation/ui/kudos/KudosScreen.kt`
- [ ] T049 [P] Thêm Error state — `uiState.error != null`: `Snackbar` hoặc inline error `Text` với retry button | `presentation/ui/kudos/KudosScreen.kt`
- [ ] T050 [P] Thêm `@Preview` cho tất cả components chính: `KudosHighlightCard`, `KudosPostCard`, `PersonalStatsGrid`, `GiftRecipientsList`, `OpenSecretBoxButton` với `uiMode = UI_MODE_NIGHT_YES` | các component files tương ứng

---

## Risk Assessment

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| Supabase Realtime subscribe leak | Medium | Medium | Cancel channel trong `onCleared()` của ViewModel |
| HorizontalPager nested scroll conflict với LazyColumn | High | Medium | Dùng `nestedScroll(rememberNestedScrollInteractionSource())` hoặc `userScrollEnabled = false` trên outer LazyColumn tại vùng carousel |
| Canvas SpotlightBoard transform gesture conflict | Medium | Low | Dùng `pointerInput(Unit) { detectTransformGestures }` với `consumeEachGesture` |
| API endpoint chưa tồn tại (mock-first) | High | High | Implement với fake data trước, swap DataSource khi API ready |
| `mms_S_Group 435` layout phức tạp (D.1 grouping) | Low | Low | Dùng `Row` + `Spacer` thông thường, không cần replicate chính xác Figma group |

---

## Task Summary

| Phase | Tasks | Blocking |
|-------|-------|---------|
| 1 — Data & Domain | T001–T024, T023B (25 tasks) | All UI |
| 2 — ViewModel | T025–T026 (2 tasks) | All UI |
| 3 — US1 KV Banner | T027–T029 (3 tasks) | - |
| 4 — US2 Highlight Carousel | T030–T034 (5 tasks) | - |
| 5 — US3 Spotlight Board | T035–T037 (3 tasks) | - |
| 6 — US4 All Kudos Feed | T038–T039 (2 tasks) | - |
| 7 — US5 Personal Stats | T040–T043 (4 tasks) | - |
| 8 — Realtime + Nav | T044A, T044–T046 (4 tasks) | - |
| 9 — Polish | T047–T050 (4 tasks) | - |
| **Total** | **52 tasks** | |

> Phases 3–9 có thể chạy song song với nhau sau khi Phase 1 + 2 hoàn thành.
