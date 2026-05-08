# Tasks: [iOS] Award_Top Talent

**Screen ID**: `c-QM3_zjkG`
**Frame**: `6885:10259` — `[iOS] Award_Top talent`
**Plan**: [plan.md](./plan.md) | **Spec**: [spec.md](./spec.md) | **Design**: [design-style.md](./design-style.md)

---

## Task Format

```
- [ ] T### [P?] [Story?] Description | file/path.kt
```

- **[P]**: Can run in parallel (different files, no shared dependencies)
- **[Story]**: User story this task belongs to (US1–US4)
- **|**: Primary file affected

---

## Codebase State (đã kiểm tra)

| Thành phần | Trạng thái |
|-----------|-----------|
| `NavRoutes.AwardDetail` (`awards/{awardId}`) | ✅ Đã có — KHÔNG tạo lại |
| `SaaNavHost` — AwardDetail composable | ⚠️ Placeholder `Text("Award Detail")` — cần wire |
| `Award` domain model (`id, name, description, category, imageUrl?`) | ✅ Đã có |
| `AwardDto` | ✅ Đã có — **thiếu `quantity`, `quantityUnit`, `prizeValue`** |
| `awards` DB table | ⚠️ **Thiếu cột `quantity`, `quantity_unit`, `prize_value`** |
| `AwardRepository.getAwards()` | ✅ Đã có |
| `AwardRepository.getAwardById()` | ❌ Chưa có |
| `GetAwardsUseCase` | ✅ Đã có |
| `GetAwardByIdUseCase` | ❌ Chưa có |
| `R.drawable.img_keyvisual_bg` | ✅ Đã có — reuse |
| `HomeHeader`, `HomeNavBar` composables | ✅ Đã có — reuse |
| `GetUnreadNotificationsUseCase` | ✅ Đã có |
| `presentation/ui/award/` | ❌ Chưa tồn tại |

---

## Phase 1: DB Schema Extension

**Purpose**: Thêm các cột `quantity`, `quantity_unit`, `prize_value` vào bảng `awards` để hiển thị số lượng và giá trị giải thưởng.

- [ ] T001 [US1] Tạo migration mới `supabase/migrations/20260506000002_awards_add_quantity_prize.sql`: thêm `quantity integer`, `quantity_unit text`, `prize_value text` vào `public.awards` | supabase/migrations/20260506000002_awards_add_quantity_prize.sql
- [ ] T002 [US1] Cập nhật `supabase/seeds/dev/awards.sql` — thêm giá trị cho 3 awards hiện có: (10, 'Cá nhân', '7.000.000 VNĐ'), (2, 'Tập thể', '15.000.000 VNĐ'), (5, 'Cá nhân', '5.000.000 VNĐ') | supabase/seeds/dev/awards.sql
- [ ] T003 [US1] Cập nhật `AwardDto` — thêm 3 fields nullable: `@SerialName("quantity") val quantity: Int?`, `@SerialName("quantity_unit") val quantityUnit: String?`, `@SerialName("prize_value") val prizeValue: String?` | android/app/src/main/java/com/example/saa/data/remote/dto/AwardDto.kt
- [ ] T004 [US1] Cập nhật `Award` domain model — thêm `val quantity: Int?`, `val quantityUnit: String?`, `val prizeValue: String?`; cập nhật mapping trong `AwardRepositoryImpl` | android/app/src/main/java/com/example/saa/domain/model/Award.kt

**Checkpoint**: `supabase db push --local` thành công; 3 awards có đủ quantity/prize data

---

## Phase 2: Data Layer Extension

**Purpose**: Thêm `getAwardById()` vào data layer.

- [ ] T005 [US1] Thêm `suspend fun getAwardById(id: String): Result<Award>` vào `AwardRepository` interface | android/app/src/main/java/com/example/saa/domain/repository/AwardRepository.kt
- [ ] T006 [US1] Thêm `suspend fun getAwardById(id: String): AwardDto` vào `AwardDataSource` — dùng `supabase.from("awards").select().eq("id", id).decodeSingle<AwardDto>()` | android/app/src/main/java/com/example/saa/data/remote/source/AwardDataSource.kt
- [ ] T007 [US1] Implement `getAwardById()` trong `AwardRepositoryImpl` — gọi `dataSource.getAwardById(id)`, map `AwardDto → Award`, wrap `runCatching` | android/app/src/main/java/com/example/saa/data/repository/AwardRepositoryImpl.kt

---

## Phase 3: Domain Layer

- [ ] T008 [US1] Tạo `GetAwardByIdUseCase` — inject `AwardRepository`, `suspend operator fun invoke(id: String): Result<Award> = repository.getAwardById(id)` | android/app/src/main/java/com/example/saa/domain/usecase/GetAwardByIdUseCase.kt

---

## Phase 4: String Resources

> ⚠️ Phải thực hiện TRƯỚC Phase 6 (components). Components dùng `stringResource()` — thiếu key sẽ gây compile error.
>
> Các string sau ĐÃ tồn tại và có thể reuse trực tiếp: `home_kudos_section_label`, `home_kudos_section_title`, `home_kudos_new_badge`, `home_kudos_description`, `home_kudos_chi_tiet`, `home_awards_section_label`, `home_awards_retry`, `home_awards_error`

- [ ] T009 [P] Thêm strings mới vào `res/values/strings.xml` (VN):
  ```xml
  <string name="award_detail_system_title">Hệ thống giải thưởng SAA 2025</string>
  <string name="award_detail_quantity_label">Số lượng giải thưởng</string>
  <string name="award_detail_prize_label">Giá trị giải thưởng</string>
  <string name="award_detail_prize_note">cho mỗi giải thưởng</string>
  <string name="award_detail_kudos_intro">Hệ thống ghi nhận và cảm ơn</string>
  <string name="award_detail_image_cd">Huy hiệu giải thưởng %s</string>
  <string name="award_detail_loading">Đang tải giải thưởng…</string>
  <string name="award_detail_error">Không thể tải giải thưởng. Thử lại?</string>
  ```
  | android/app/src/main/res/values/strings.xml
- [ ] T010 [P] Tạo `res/values-en/strings.xml` nếu chưa có; thêm bản EN cho 8 strings trên | android/app/src/main/res/values-en/strings.xml

---

## Phase 5: Typography Tokens

> Các token sau CHƯA có trong `Type.kt` và cần thêm cho các text styles đặc thù của màn hình này.

- [ ] T011 [P] Thêm typography extension vào `Type.kt`:
  - `awardDetailTitle` — 18sp Bold (tên giải thưởng, màu gold)
  - `awardDetailKudosTitle` — 30sp ExtraBold (text "KUDOS" banner, màu gold)
  - `awardDetailSectionTitle` — 24sp Bold (tiêu đề block lớn)
  - `awardDetailPrizeValue` — 22sp Bold (số tiền giải, màu gold)
  - `awardDetailQuantityValue` — 22sp Bold (số lượng giải, màu white)
  - `awardDetailSectionLabel` — 14sp SemiBold (label icon+text sections)
  - `awardDetailBody` — 14sp Regular, lineHeight 21sp (mô tả, 1.5× line height)
  | android/app/src/main/java/com/example/saa/ui/theme/Type.kt

---

## Phase 6: Presentation Foundation

- [ ] T012 [US1,US2,US3,US4] Tạo `AwardDetailUiState`:
  ```kotlin
  data class AwardDetailUiState(
      val isLoading: Boolean = false,
      val award: Award? = null,
      val allAwards: List<Award> = emptyList(),
      val showDropdown: Boolean = false,
      val error: String? = null,
      val selectedLanguage: String = "VN",
      val showLanguageSelector: Boolean = false,
      val unreadNotificationCount: Int = 0,
  )
  ```
  | android/app/src/main/java/com/example/saa/presentation/ui/award/AwardDetailUiState.kt

- [ ] T013 [US1,US2,US3,US4] Tạo `AwardDetailViewModel` — `@HiltViewModel`, inject `savedStateHandle: SavedStateHandle`, `GetAwardByIdUseCase`, `GetAwardsUseCase`, `LanguageRepository`, `GetUnreadNotificationsUseCase`:
  - `init`: load award by `savedStateHandle["awardId"]` + load all awards + load notification count + collect language
  - `toggleDropdown()` / `dismissDropdown()`
  - `selectAward(award: Award)` — update `uiState.award` + dismiss dropdown
  - `showLanguageSelector()` / `dismissLanguageSelector()` / `setLanguage(code: String)`
  - `retry()` — re-trigger award load từ `savedStateHandle["awardId"]`
  - Không dùng `!!` — dùng `?.` và `?:` cho tất cả nullable access
  | android/app/src/main/java/com/example/saa/presentation/ui/award/AwardDetailViewModel.kt

---

## Phase 7: Screen Skeleton

- [ ] T014 [US4] Tạo `AwardDetailScreen` skeleton với layout:
  ```
  Box(Modifier.fillMaxSize().background(Background)) {
      Image(R.drawable.img_keyvisual_bg, fillMaxWidth().height(723.dp).align(TopEnd))
      LazyColumn(contentPadding = PaddingValues(top = 144.dp, bottom = 120.dp)) { /* items */ }
      HomeHeader(..., modifier = Modifier.align(TopStart))
      HomeNavBar(selected = HomeNavTab.AWARDS, ..., modifier = Modifier.align(BottomStart))
  }
  ```
  Wire: `uiState` từ `viewModel.uiState.collectAsStateWithLifecycle()`, loading + error states
  | android/app/src/main/java/com/example/saa/presentation/ui/award/AwardDetailScreen.kt

---

## Phase 8: UI Components

> T015–T018 độc lập với nhau, có thể implement song song.

- [ ] T015 [P][US1] Tạo `KudosKVBanner` composable:
  - Column(padding horizontal=20dp, top=20dp): Text "Hệ thống ghi nhận và cảm ơn" 12sp TextSecondary → Row: flame SVG icon (24dp) + Text "KUDOS" style `awardDetailKudosTitle` màu `ButtonPrimaryBg`
  - Params: `modifier: Modifier = Modifier`
  | android/app/src/main/java/com/example/saa/presentation/ui/award/components/KudosKVBanner.kt

- [ ] T016 [P][US2] Tạo `AwardHighlightBlock` composable:
  - Column(padding horizontal=20dp, vertical=24dp, gap=12dp): sub-label 12sp TextSecondary → title style `awardDetailSectionTitle` white → `AwardCategoryDropdown`
  - `AwardCategoryDropdown` (internal composable): trigger Button(width=140dp, height=40dp, border=1dp `ButtonPrimaryBg`, radius=4dp, bg=`#0A1929`) hiển thị `selectedAward?.name` + `KeyboardArrowDown`; `DropdownMenu` list all awards; selected item highlighted gold
  - Params: `allAwards: List<Award>`, `selectedAward: Award?`, `expanded: Boolean`, `onToggle: () -> Unit`, `onSelect: (Award) -> Unit`, `onDismiss: () -> Unit`
  | android/app/src/main/java/com/example/saa/presentation/ui/award/components/AwardHighlightBlock.kt

- [ ] T017 [P][US1] Tạo `AwardInfoBlock` composable:
  - `AsyncImage(award.imageUrl)` trong Box(size=240dp, centered, border=1dp `#FFEA9E` 30% alpha, radius=16dp); fallback: `Icon(Icons.Default.EmojiEvents)` nếu `imageUrl` null
  - `HorizontalDivider(color = Color(0x33FFFFFF))`
  - Row: `Icon(ic_award_title, 20dp)` + Text `award.name` style `awardDetailTitle` màu `ButtonPrimaryBg`
  - Text `award.description` style `awardDetailBody` màu `TextOnDark`
  - Divider → Quantity section: Row(icon `ic_award_quantity` + label) + Row(Text `award.quantity` style `awardDetailQuantityValue` + Text `award.quantityUnit` Regular TextSecondary)
  - Divider → Prize section: Row(icon `ic_award_prize` + label) + Row(Text `award.prizeValue` style `awardDetailPrizeValue` màu `ButtonPrimaryBg` + Text `prize_note` TextSecondary)
  - Hiển thị "—" nếu quantity/prizeValue null
  - Params: `award: Award`
  | android/app/src/main/java/com/example/saa/presentation/ui/award/components/AwardInfoBlock.kt

- [ ] T018 [P][US3] Tạo `KudosBannerBlock` composable:
  - Column(padding horizontal=20dp, vertical=32dp, gap=12dp):
    - Text `home_kudos_section_label` 12sp TextSecondary
    - Text `home_kudos_section_title` style `awardDetailSectionTitle` màu `ButtonPrimaryBg`
    - AsyncImage banner 1 (`kudos_banner_1`) fill width, height=120dp, radius=8dp (dùng `R.drawable` hoặc URL từ assets)
    - Text `home_kudos_new_badge` 12sp Bold uppercase TextOnDark
    - Text `home_kudos_description` style `awardDetailBody` TextSecondary
    - Button(width=120dp, height=40dp, radius=4dp, **bg=`ButtonPrimaryBg` (FILLED — không phải outlined**), onClick=onKudosDetailClick):
      Row: Text "Chi tiết" style 14sp SemiBold màu `#0A1929` + Icon(`ic_kudos_detail_arrow`, tint=`#0A1929`)
  - Params: `onKudosDetailClick: () -> Unit`
  | android/app/src/main/java/com/example/saa/presentation/ui/award/components/KudosBannerBlock.kt

---

## Phase 9: Wire Screen + Navigation

- [ ] T019 [US1,US2,US3,US4] Lắp components vào `AwardDetailScreen`:
  - LazyColumn items: `KudosKVBanner` → `AwardHighlightBlock` (wire dropdown state) → `AwardInfoBlock` (award từ uiState) → `KudosBannerBlock` (onKudosDetailClick → navigate Kudos)
  - Loading: `CircularProgressIndicator()` trong `Box(Modifier.fillMaxSize())`
  - Error: Text + `Button("Thử lại") { viewModel.retry() }`
  - Header: wire `selectedLanguage`, `showLanguageSelector`, `onLanguageClick=viewModel::showLanguageSelector`, `onLanguageSelected=viewModel::setLanguage`, `onLanguageDismiss=viewModel::dismissLanguageSelector`
  - NavBar `onTabSelected`: `AWARDS` → no-op (đang ở đây); `SAA` → navigate Home; `KUDOS` → navigate Kudos; `PROFILE` → navigate Profile
  - `LaunchedEffect(uiState.error)`: log error với Timber (không show raw exception ra UI)
  | android/app/src/main/java/com/example/saa/presentation/ui/award/AwardDetailScreen.kt

- [ ] T020 [US1] Cập nhật `SaaNavHost.kt` — thay `Surface { Text("Award Detail") }` bằng `AwardDetailScreen(navController = navController)`:
  > ⚠️ Không cần extract `awardId` từ `backStackEntry` — `@HiltViewModel` với `SavedStateHandle` tự nhận nav argument từ Hilt Navigation
  | android/app/src/main/java/com/example/saa/SaaNavHost.kt

---

## Phase 10: SVG Assets

- [ ] T021 [P] Download SVG icons từ Figma (dùng `momorph_downloadFigmaImage`) và thêm vào `res/drawable/`:
  - `ic_award_title.xml` — node `6885:10296` (award name icon)
  - `ic_award_quantity.xml` — node `6885:10302` (quantity icon)
  - `ic_award_prize.xml` — node `6885:10310` (prize icon)
  - `ic_kudos_detail_arrow.xml` — node `6885:10321` (arrow cho "Chi tiết" button)
  | android/app/src/main/res/drawable/

---

## Phase 11: Build Verification

- [ ] T022 Chạy `./gradlew assembleDebug --no-daemon` từ `/android` — BUILD SUCCESSFUL, 0 compile errors | —
