# Development Plan: [iOS] Award_Top Talent

**Screen ID**: `c-QM3_zjkG`
**Spec**: [spec.md](./spec.md) | **Design**: [design-style.md](./design-style.md)
**Created**: 2026-05-06

---

## Tổng Quan

Màn hình chi tiết giải thưởng SAA 2025 — hiển thị huy hiệu, tiêu chí, số lượng và giá trị giải thưởng theo hạng mục được chọn từ dropdown. Có thêm block cross-promo Sun\*Kudos và bottom nav với tab "Awards" active.

---

## Hiện Trạng Codebase

| Thành phần | Trạng thái | Ghi chú |
|-----------|-----------|---------|
| `NavRoutes.AwardDetail` | ✅ Tồn tại | `awards/{awardId}` đã có |
| `SaaNavHost` — AwardDetail composable | ⚠️ Placeholder | `Surface { Text("Award Detail") }` — cần thay bằng màn hình thật |
| `Award` domain model | ✅ Tồn tại | `id, name, description, category, imageUrl?` |
| `AwardRepository` interface | ⚠️ Thiếu | Chỉ có `getAwards()` — cần thêm `getAwardById(id)` |
| `GetAwardsUseCase` | ✅ Tồn tại | Dùng để load danh sách dropdown |
| `AwardDto` | ✅ Tồn tại | Đủ fields |
| `AwardDataSource` | ⚠️ Thiếu | Cần thêm `getAwardById()` |
| Keyvisual BG drawable | ✅ Tồn tại | `R.drawable.img_keyvisual_bg` đã dùng ở `HomeScreen` — reuse |
| `GetUnreadNotificationsUseCase` | ✅ Tồn tại | Cần inject vào `AwardDetailViewModel` để load badge count |
| `HomeHeader` composable | ✅ Tồn tại | Reusable — có inline language dropdown |
| `HomeNavBar` composable | ✅ Tồn tại | Reusable — `selected: HomeNavTab` |
| `presentation/ui/award/` | ❌ Chưa tồn tại | Cần tạo mới hoàn toàn |

---

## Phạm Vi Implement

### Không cần tạo mới (reuse):
- `HomeHeader` — dùng lại từ `home/components/`
- `HomeNavBar` — dùng lại với `selected = HomeNavTab.AWARDS`
- `GetAwardsUseCase` — dùng cho dropdown list
- `Award` domain model, `AwardDto`

### Cần tạo mới / sửa:
- `AwardRepository` + `AwardRepositoryImpl` + `AwardDataSource` — thêm `getAwardById()`
- `GetAwardByIdUseCase`
- `AwardDetailUiState`, `AwardDetailViewModel`, `AwardDetailScreen`
- 4 components: `KudosKVBanner`, `AwardHighlightBlock`, `AwardInfoBlock`, `KudosBannerBlock`
- Strings resources + drawable SVGs

---

## Milestones

### Milestone 1 — Data Layer Extension (1–2 tasks)
Mở rộng data layer để hỗ trợ load award theo ID.

**Deliverable**: `getAwardById(id)` hoạt động, trả `Result<Award>`

---

### Milestone 2 — Domain Layer (1 task)
Use case mới cho single award lookup.

**Deliverable**: `GetAwardByIdUseCase` inject được vào ViewModel

---

### Milestone 3 — Presentation Layer Foundation (2 tasks)
ViewModel + UiState cho màn hình.

**Deliverable**: ViewModel load award + awards list, quản lý dropdown state, expose StateFlow

---

### Milestone 4 — Screen Skeleton + Layout (1 task)
`AwardDetailScreen` với Box layout: sticky Header overlay + LazyColumn + sticky NavBar.

**Deliverable**: Màn hình compile được, scroll hoạt động, Header/NavBar sticky

---

### Milestone 5 — UI Components (4 tasks, có thể song song)
Implement 4 components độc lập.

**Deliverable**: Tất cả components render đúng theo design-style.md

---

### Milestone 6 — Wire Everything + Nav (2 tasks)
Gắn components vào Screen, wire SaaNavHost, test navigation từ Home.

**Deliverable**: E2E flow Home → AwardDetail → dropdown → Kudos nav hoạt động

---

### Milestone 7 — Assets + Strings (2 tasks, parallel)
SVG drawables và string resources.

**Deliverable**: Icons/strings đầy đủ, không hardcode

---

### Milestone 8 — Build Verification (1 task)
`./gradlew assembleDebug` pass.

---

## Task Breakdown

### Phase 1 — Data Layer

**T01** · Thêm `getAwardById(id: String): Result<Award>` vào `AwardRepository` interface
→ `domain/repository/AwardRepository.kt`

**T02** · Thêm `getAwardById(id: String)` vào `AwardDataSource` (Supabase `.select().eq("id", id).single()`)
→ `data/remote/source/AwardDataSource.kt`

**T03** · Implement `getAwardById()` trong `AwardRepositoryImpl` — map `AwardDto → Award`, wrap `Result`
→ `data/repository/AwardRepositoryImpl.kt`

---

### Phase 2 — Domain Layer

**T04** · Tạo `GetAwardByIdUseCase` — inject `AwardRepository`, gọi `getAwardById(id)`
→ `domain/usecase/GetAwardByIdUseCase.kt`

---

### Phase 3 — Presentation Foundation

**T05** · Tạo `AwardDetailUiState`:
```kotlin
data class AwardDetailUiState(
    val isLoading: Boolean = false,
    val award: Award? = null,           // selected award
    val allAwards: List<Award> = emptyList(),  // for dropdown
    val showDropdown: Boolean = false,
    val error: String? = null,
    val selectedLanguage: String = "VN",
    val showLanguageSelector: Boolean = false,
    val unreadNotificationCount: Int = 0,
)
```
→ `presentation/ui/award/AwardDetailUiState.kt`

**T06** · Tạo `AwardDetailViewModel` — `@HiltViewModel`, inject:
- `savedStateHandle: SavedStateHandle` — Hilt tự inject từ nav argument `awardId`
- `GetAwardByIdUseCase`, `GetAwardsUseCase`, `LanguageRepository`, `GetUnreadNotificationsUseCase`

Functions:
- `init`: load award by ID từ `savedStateHandle["awardId"]` + load all awards cho dropdown + load notification count
- `toggleDropdown()` / `dismissDropdown()`
- `selectAward(award: Award)` — cập nhật selected + đóng dropdown
- `showLanguageSelector()` / `dismissLanguageSelector()` / `setLanguage(code)`
- `retry()` — re-trigger load award từ `savedStateHandle["awardId"]`
- Xử lý `Result.failure` → `uiState.error` (dùng `runCatching` / `onFailure` — không dùng `!!`)
→ `presentation/ui/award/AwardDetailViewModel.kt`

---

### Phase 4 — Screen Skeleton

**T07** · Tạo `AwardDetailScreen` với layout:
```
Box(fillMaxSize, background #00101A) {
    // Keyvisual BG image (top portion) — reuse R.drawable.img_keyvisual_bg
    Image(keyvisual, Modifier.fillMaxWidth().height(723.dp).align(TopEnd))

    // Scrollable content
    LazyColumn(
        contentPadding = PaddingValues(
            top = headerHeight,
            bottom = navBarHeight + navigationBarInsets
        )
    ) { ... }

    // Sticky header overlay
    HomeHeader(..., modifier = Modifier.align(TopStart))

    // Sticky nav bar overlay
    HomeNavBar(selected = HomeNavTab.AWARDS, ..., modifier = Modifier.align(BottomStart))
}
```
→ `presentation/ui/award/AwardDetailScreen.kt`

---

### Phase 5 — UI Components (có thể implement song song)

**T08** · `KudosKVBanner` — banner đầu trang:
- Column: subtitle "Hệ thống ghi nhận và cảm ơn" (12sp, TextSecondary) + Row(flame icon + "KUDOS" text 28–32sp Bold gold)
- Padding horizontal 20dp, top 20dp
→ `presentation/ui/award/components/KudosKVBanner.kt`

**T09** · `AwardHighlightBlock` — tiêu đề + dropdown:
- Column: sub-label 12sp + title 24–28sp Bold white + `AwardCategoryDropdown`
- `AwardCategoryDropdown`: trigger button (width 140dp, height 40dp, border 1dp gold, radius 4dp, bg `#0A1929`) + `DropdownMenu` listing all awards, selected item highlighted
- Params: `awards: List<Award>`, `selectedAward: Award?`, `expanded: Boolean`, `onToggle`, `onSelect`, `onDismiss`
→ `presentation/ui/award/components/AwardHighlightBlock.kt`

**T10** · `AwardInfoBlock` — chi tiết giải:
- Award badge: `AsyncImage(imageUrl)` trong Container 240×240dp, centered, gold glow border 1dp `#FFEA9E30`, radius 16dp; fallback placeholder icon khi imageUrl null
- Divider `#FFFFFF20`
- Title row: icon (20dp) + award name (18–20sp Bold gold), spacing 8dp
- Description: 14sp Regular white, lineHeight 1.5×
- Divider
- Quantity section: icon + label 14sp SemiBold white + value row (quantity Bold white + unit Regular TextSecondary)
- Divider
- Prize section: icon + label 14sp SemiBold white + value row (amount Bold gold + note Regular TextSecondary)
- Params: `award: Award`
→ `presentation/ui/award/components/AwardInfoBlock.kt`

**T11** · `KudosBannerBlock` — Sun\*Kudos cross-promo:
- Column: label "Phong trào ghi nhận" 12sp TextSecondary + title "Sun\* Kudos" 22–24sp Bold gold
- Banner images (2 PNGs fill width, height ~120dp, radius 8dp)
- Badge text "ĐIỂM MỚI CỦA SAA 2025" uppercase 12sp Bold
- Description 14sp Regular TextSecondary
- **"Chi tiết" button**: width 120dp, height 40dp, radius 4dp, bg `#FFEA9E`, text "Chi tiết" 14–16sp SemiBold color `#0A1929` + arrow icon
- Padding horizontal 20dp, vertical 32dp
→ `presentation/ui/award/components/KudosBannerBlock.kt`

---

### Phase 6 — Wire + Navigation

**T12** · Lắp tất cả components vào `AwardDetailScreen` + wire ViewModel state:
- Loading state: `CircularProgressIndicator` centered
- Error state: Text + Retry button → `viewModel.retry()` (xem T06)
- Header: wire `selectedLanguage`, `showLanguageSelector`, language callbacks
- NavBar: `onTabSelected` → navigate by tab (SAA→Home, KUDOS→Kudos, PROFILE→Profile)
- KudosBannerBlock "Chi tiết" → `navController.navigate(NavRoutes.Kudos.route)` (BR-005: tạm dùng Kudos route cho đến khi designer xác nhận)
→ `presentation/ui/award/AwardDetailScreen.kt`

**T13** · Cập nhật `SaaNavHost.kt` — thay placeholder `Surface { Text("Award Detail") }` bằng:
```kotlin
composable(
    route = NavRoutes.AwardDetail.route,
    arguments = listOf(navArgument("awardId") { type = NavType.StringType }),
) {
    // awardId được đọc từ SavedStateHandle bên trong AwardDetailViewModel — không cần extract ở đây
    AwardDetailScreen(navController = navController)
}
```
> ⚠️ Không truyền `awardId` thủ công vào Screen — `@HiltViewModel` với `SavedStateHandle` tự nhận nav argument từ Hilt Navigation.
→ `SaaNavHost.kt`

---

### Phase 7 — Assets & Strings (parallel)

**T14** · Download SVG icons từ Figma và thêm vào `res/drawable/` (keyvisual BG đã tồn tại — không cần download lại):
- `ic_award_title.xml` (node `6885:10296`)
- `ic_award_quantity.xml` (node `6885:10302`)
- `ic_award_prize.xml` (node `6885:10310`)
- `ic_kudos_detail_arrow.xml` (node `6885:10321`)
→ `res/drawable/`

**T15** · Thêm string resources vào `res/values/strings.xml` (VN) và `res/values-en/strings.xml` (EN):
```xml
<string name="award_detail_quantity_label">Số lượng giải thưởng</string>
<string name="award_detail_prize_label">Giá trị giải thưởng</string>
<string name="award_detail_prize_note">cho mỗi giải thưởng</string>
<string name="award_detail_kudos_label">Phong trào ghi nhận</string>
<string name="award_detail_kudos_title">Sun* Kudos</string>
<string name="award_detail_kudos_new_badge">ĐIỂM MỚI CỦA SAA 2025</string>
<string name="award_detail_kudos_detail_btn">Chi tiết</string>
<string name="award_detail_quantity_unit">Cá nhân</string>
<string name="award_saa_label">Sun* Annual Awards 2025</string>
<string name="award_system_title">Hệ thống giải thưởng SAA 2025</string>
```
→ `res/values/strings.xml` + `res/values-en/strings.xml`

---

### Phase 8 — Build Verification

**T16** · Chạy `./gradlew assembleDebug` — BUILD SUCCESSFUL, 0 errors

---

## File Structure Sẽ Tạo

```
android/app/src/main/java/com/example/saa/
├── domain/
│   ├── repository/
│   │   └── AwardRepository.kt          ← MODIFIED: +getAwardById()
│   └── usecase/
│       └── GetAwardByIdUseCase.kt       ← NEW
├── data/
│   ├── remote/source/
│   │   └── AwardDataSource.kt          ← MODIFIED: +getAwardById()
│   └── repository/
│       └── AwardRepositoryImpl.kt      ← MODIFIED: +getAwardById()
└── presentation/ui/award/              ← NEW package
    ├── AwardDetailUiState.kt
    ├── AwardDetailViewModel.kt
    ├── AwardDetailScreen.kt
    └── components/
        ├── KudosKVBanner.kt
        ├── AwardHighlightBlock.kt
        ├── AwardInfoBlock.kt
        └── KudosBannerBlock.kt

android/app/src/main/res/
├── drawable/
│   ├── ic_award_title.xml              ← NEW
│   ├── ic_award_quantity.xml           ← NEW
│   ├── ic_award_prize.xml              ← NEW
│   └── ic_kudos_detail_arrow.xml       ← NEW
└── values/
    └── strings.xml                     ← MODIFIED: +10 strings
```

---

## Dependencies Check

| Dependency | Version | Status |
|-----------|---------|--------|
| Coil `AsyncImage` | 2.7.0 | ✅ Đã có (dùng trong `AwardCard.kt`) |
| Hilt | 2.59.2 | ✅ Đã có |
| Navigation Compose | 2.9.0 | ✅ Đã có |
| Material3 | BOM 2026.02.01 | ✅ Đã có |
| `supabase-postgrest` | 3.1.4 | ✅ Đã có |

Không cần thêm dependency mới.

---

## Rủi Ro & Lưu Ý

| Rủi ro | Mức độ | Cách xử lý |
|--------|--------|-----------|
| BR-005: Kudos "Chi tiết" linkedFrameId trống | Medium | Tạm navigate đến `NavRoutes.Kudos`, cần xác nhận với designer |
| `AwardRepository.getAwardById()` không có trong hiện tại | Low | Extend interface + impl trước khi làm ViewModel |
| Keyvisual BG image | Low | `R.drawable.img_keyvisual_bg` ĐÃ TỒN TẠI — reuse trực tiếp, không cần download |
| `HomeNavBar` hiện không có `navigationBarInsets` | Low | Kiểm tra và wrap với `WindowInsets.navigationBars` padding |

---

## Thứ Tự Implement Được Khuyến Nghị

```
T01 → T02 → T03   (data layer, sequential)
              ↓
             T04   (use case)
              ↓
        T05 → T06   (UiState → ViewModel)
              ↓
        T15 (strings TRƯỚC components — tránh compile error từ stringResource())
              ↓
             T07   (screen skeleton)
              ↓
  T08, T09, T10, T11   (components — parallel, dùng strings đã có)
              ↓
        T12 → T13   (wire + nav)
              ↓
             T14   (SVG assets)
              ↓
             T16   (build check)
```

> ⚠️ T15 (strings) phải được thực hiện TRƯỚC các components (T08–T11) vì components dùng `stringResource()`. Nếu không, build sẽ lỗi `Unresolved reference`.

**Ước tính**: 16 tasks — 1 ngày dev full focus.
