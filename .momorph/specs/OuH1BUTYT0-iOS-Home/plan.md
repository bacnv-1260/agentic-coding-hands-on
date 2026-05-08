# Development Plan: [iOS] Home

**Screen ID**: `OuH1BUTYT0`
**Frame**: `[iOS] Home` — `6885:8978`
**File Key**: `9ypp4enmFmdK3YAFJLIu6C`
**Spec**: `.momorph/specs/OuH1BUTYT0-iOS-Home/spec.md`
**Design Style**: `.momorph/specs/OuH1BUTYT0-iOS-Home/design-style.md`
**Created**: 2026-05-05
**Status**: Planning

---

## Objective

Implement the Home screen (`NavRoutes.Home`) as the post-login landing screen of the SAA 2025 app. Replace the current TODO placeholder in `SaaNavHost.kt` with a fully functional `HomeScreen` composable backed by a `HomeViewModel`. The screen presents 7 sections: sticky header, full-bleed keyvisual background, hero content with real-time countdown, theme description, awards horizontal list (API), Kudos intro section, FAB, and bottom navigation bar.

---

## Architecture Overview

```
Presentation
  HomeScreen.kt           — Composable (UI only, no logic)
  HomeViewModel.kt        — State holder: countdown coroutine, API calls, badge count, FAB cooldown
  HomeUiState.kt          — data class with nested sealed states

Domain
  model/Award.kt          — Domain entity
  usecase/GetAwardsUseCase.kt
  usecase/GetUnreadNotificationsUseCase.kt

Data
  remote/source/AwardDataSource.kt           — Supabase postgrest fetch (mirrors SupabaseAuthDataSource pattern)
  remote/source/NotificationDataSource.kt
  remote/dto/AwardDto.kt
  repository/AwardRepositoryImpl.kt
  repository/NotificationRepositoryImpl.kt

UI / Theme
  ui/theme/Type.kt        — homeCountdownNumber, homeSectionTitle, homeSectionLabel tokens
  res/drawable/           — mm_media_logo_homepage.png, mm_media_logo_rootfuther.png,
                            mm_media_kudos_background.png (download needed)
  res/values/strings.xml  — Home screen strings (VN + EN)
```

---

## Milestones

### M1 — Foundation (no UI yet)
Establish skeleton files, domain models, and theme tokens. Build must pass after this milestone.

### M2 — Static UI Shell
All 7 sections render with hardcoded/placeholder data. Layout, colors, typography exactly match design-style.md. No API, no real countdown.

### M3 — Live Countdown
`HomeViewModel` emits `TimeRemaining` every second via coroutine. Countdown units display real values. "Coming soon" / "Event Ended" states handled.

### M4 — Awards API
`GetAwardsUseCase` → ViewModel → Awards horizontal list with loading skeleton and empty/error states.

### M5 — Notifications Badge
`GetUnreadNotificationsUseCase` → ViewModel → Bell badge dot shown/hidden.

### M6 — Navigation Wiring
All tappable elements route to the correct screens. FAB double-tap prevention. Bottom nav active state synced.

### M7 — Polish & Accessibility
Content descriptions, Kudos feature flag hide behavior, asset final downloads, build verification.

---

## Task Breakdown

### M1 — Foundation

#### T001 — Add missing Gradle dependencies
- **Coil** (async image loading for award card thumbnails):
  - Add to `libs.versions.toml`:
    ```toml
    [versions]
    coil = "2.7.0"
    [libraries]
    coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }
    ```
  - Add to `app/build.gradle.kts` dependencies: `implementation(libs.coil.compose)`
- **Supabase postgrest** (required for `from("awards").select()` in T019–T023):
  - Add to `libs.versions.toml`:
    ```toml
    supabase-postgrest = { group = "io.github.jan-tennert.supabase", name = "postgrest-kt" }
    ```
  - Add to `app/build.gradle.kts`: `implementation(libs.supabase.postgrest)`
- **Files**: `android/gradle/libs.versions.toml`, `android/app/build.gradle.kts`
- **Acceptance**: `./gradlew assembleDebug` passes; `AsyncImage` and `supabase.from()` importable

#### T002 — Download missing assets
- Download from Figma (node IDs from design-style.md §7):
  - `I6885:9057;88:1827;65:1760` → `res/drawable/mm_media_logo_homepage.png`
  - `I6885:8984;65:1590` → `res/drawable/mm_media_logo_rootfuther.png`
  - `6885:9043` → `res/drawable/mm_media_kudos_background.png`
- **Files**: `android/app/src/main/res/drawable/`
- **Acceptance**: 3 PNG files present, build compiles without resource errors

#### T003 — Add typography tokens to Type.kt
- Add `MontserratLight` FontFamily (already exists as private — expose if needed)
- Add extension properties to `Typography`:
  - `homeSectionLabel`: MontserratRegular, 12sp, lineHeight 16sp, letterSpacing 0sp
  - `homeSectionTitle`: MontserratMedium, 22sp, lineHeight 28sp, letterSpacing 0sp
  - `homeCountdownNumber`: MontserratBold, 40sp (TBD — verify after querying node 6885:8988)
  - `homeCountdownLabel`: MontserratRegular, 12sp (TBD)
- **File**: `android/app/src/main/java/com/example/saa/ui/theme/Type.kt`
- **Acceptance**: No compile errors; tokens accessible from HomeScreen

#### T004 — Add Home screen strings
- Add to `res/values/strings.xml` (VN):
  ```
  home_coming_soon = "Coming soon"
  home_event_date = "Thời gian: 26/12/2025"
  home_event_venue = "Địa điểm: Âu Cơ Art Center"
  home_event_livestream = "Tường thuật trực tiếp tại Group Facebook Sun* Family"
  home_btn_about_award = "ABOUT AWARD"
  home_btn_about_kudos = "ABOUT KUDOS"
  home_kudos_badge = "ĐIỂM MỚI CỦA SAA 2025"
  home_kudos_description = "Sun* Kudos là hoạt động ghi nhận và tôn vinh những đóng góp tích cực của các Sunner trong tổ chức..." (full text from Figma node — verify exact text at implementation time)
  home_btn_kudos_detail = "Chi tiết"
  home_countdown_days = "DAYS"
  home_countdown_hours = "HOURS"
  home_countdown_minutes = "MINUTES"
  home_event_ended = "Event Ended"
  cd_home_language_switcher = "Ngôn ngữ: VN, nhấn để đổi"
  cd_home_search = "Tìm kiếm"
  cd_home_notifications = "Thông báo, %d chưa đọc"
  cd_home_fab_write_kudo = "Viết Kudo"
  cd_home_fab_kudos_feed = "Sun* Kudos"
  cd_award_detail = "Chi tiết về %s"
  ```
- Add same keys to `res/values-en/strings.xml` (EN translations)
- **Acceptance**: No missing resource errors

#### T005 — Create Award domain model
- `domain/model/Award.kt`:
  ```kotlin
  data class Award(
      val id: String,
      val category: String,
      val name: String,
      val description: String,
      val thumbnailUrl: String?,
  )
  ```
- **Acceptance**: Compiles; no `!!` usage

#### T006 — Create HomeUiState
- `presentation/ui/home/HomeUiState.kt`:
  ```kotlin
  data class HomeUiState(
      val timeRemaining: TimeRemaining = TimeRemaining.Loading,
      val awards: AwardsState = AwardsState.Loading,
      val unreadNotificationCount: Int = 0,
      val isKudosAvailable: Boolean = true,
      // TR-004: auth error flags — triggers navigation in HomeScreen
      val isUnauthenticated: Boolean = false,  // 401 → navigate to Login
      val isForbidden: Boolean = false,         // 403 → navigate to AccessDenied
  )
  sealed class TimeRemaining {
      data object Loading : TimeRemaining()
      data class Counting(val days: Long, val hours: Long, val minutes: Long) : TimeRemaining()
      data object EventEnded : TimeRemaining()
  }
  sealed class AwardsState {
      data object Loading : AwardsState()
      data class Success(val awards: List<Award>) : AwardsState()
      data object Empty : AwardsState()
      data class Error(val message: String) : AwardsState()
  }
  ```
- **Note**: `isEventStarted` from spec is derivable as `timeRemaining is TimeRemaining.EventEnded` in the Composable — no separate field needed
- **Acceptance**: Compiles; no `!!`

#### T007 — Create HomeViewModel skeleton
- `presentation/ui/home/HomeViewModel.kt`:
  - `@HiltViewModel` + `@Inject constructor`
  - `private val _uiState = MutableStateFlow(HomeUiState())`
  - `val uiState: StateFlow<HomeUiState>` exposed via `asStateFlow()`
  - Empty `init {}` placeholder for M3/M4/M5
  - Add **auth error helper** for TR-004 (mirrors LoginViewModel pattern):
    ```kotlin
    // Maps HTTP 401/403 from Supabase exceptions to uiState flags
    private fun handleApiError(e: Exception) {
        val msg = e.message ?: ""
        when {
            msg.contains("401") || msg.contains("JWT") || msg.contains("unauthorized", ignoreCase = true) ->
                _uiState.update { it.copy(isUnauthenticated = true) }
            msg.contains("403") || msg.contains("forbidden", ignoreCase = true) ->
                _uiState.update { it.copy(isForbidden = true) }
            else -> { /* propagated as AwardsState.Error or similar */ }
        }
    }
    ```
  - Add `fun consumeAuthError() { _uiState.update { it.copy(isUnauthenticated = false, isForbidden = false) } }`
- **File**: `presentation/ui/home/HomeViewModel.kt`
- **Acceptance**: Hilt injection compiles; `handleApiError` callable from M4/M5 coroutines

#### T008 — Create HomeScreen skeleton + wire into NavHost
- `presentation/ui/home/HomeScreen.kt`: Composable `fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel())`
- Collect `uiState` via `collectAsStateWithLifecycle()`
- Add **TR-004 auth redirect side-effects** (mirrors LoginScreen pattern):
  ```kotlin
  LaunchedEffect(uiState.isUnauthenticated) {
      if (uiState.isUnauthenticated) {
          navController.navigate(NavRoutes.Login.route) { popUpTo(0) { inclusive = true } }
          viewModel.consumeAuthError()
      }
  }
  LaunchedEffect(uiState.isForbidden) {
      if (uiState.isForbidden) {
          navController.navigate(NavRoutes.AccessDenied.route) { popUpTo(0) { inclusive = true } }
          viewModel.consumeAuthError()
      }
  }
  ```
- `SaaNavHost.kt`: Replace the `Surface { Text("Home Screen") }` placeholder with `HomeScreen(navController)`
- `NavRoutes.kt`: Confirm `Home` route exists (already present — no change needed)
- **Do NOT change `startDestination`** — keep it as `NavRoutes.Login.route`
- **Acceptance**: App launches at Login; Home route reachable; auth errors redirect correctly

---

### M2 — Static UI Shell

#### T009 — Implement background layer (mm_media_bg)
- `Box(Modifier.fillMaxSize())` as outermost container (z=0)
- Layer 1: `Image(painterResource(mm_media_keyvisual_bg), ContentScale.Crop, Alignment.CenterEnd)`
- Layer 2: `Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(0.0007f to #00101A, 0.1861f to #10181F, 0.772f to transparent)))`
- Layer 3 (Shadow Bottom): `Box(Modifier.fillMaxSize().background(Brush.verticalGradient(from transparent to #00101A, startY=bottomY)))` — adapt from `linear-gradient(90deg, …)` rotated −90deg
- **File**: `HomeScreen.kt` → private composable `HomeBackground()`
- **Acceptance**: Keyvisual + left shadow + bottom shadow visible on device/emulator

#### T010 — Implement mms_1_header (sticky header)
- Fixed `Box` at top of screen (z-top via `Scaffold` or manual `Box` with z-index)
- `Brush.verticalGradient` for 7-stop gradient background per design-style.md §4.1
- Logo `Image` (48×44dp, start=20dp, top=52dp)
- Actions Row (end-aligned): Language Switcher (flag + "VN" + chevron), Search icon, Bell icon + badge dot
- Badge dot: `Box(8×8dp, CircleShape, #D4271D)` positioned top-end of Bell icon via `Box` overlay
- Content descriptions from strings.xml
- **File**: `HomeScreen.kt` → private `HomeHeader()`
- **Acceptance**: Header shows correct layout; badge dot visible; all content descriptions set

#### T011 — Implement mms_2_content (Hero section)
- `Column(gap=32dp, padding start=20dp)` beginning at top=144dp (below header)
- `Image` ROOT FURTHER logo (247×109dp)
- Countdown section (gap=8dp): "Coming soon" label + countdown Row(gap=16dp)
  - Each unit: `Column(width=72dp, height=84dp, gap=4dp, CenterHorizontally)`
  - Number tile: dark bg, white number (homeCountdownNumber token, TBD size)
  - Unit label (homeSectionLabel token, white)
- Event info column:
  - **"Thời gian: ..."**: use `AnnotatedString` — prefix "Thời gian: " in Light 300 white, date value "26/12/2025" in `SpanStyle(fontWeight = FontWeight.Bold, color = ButtonPrimaryBg)` (#FFEA9E)
  - "Địa điểm: Âu Cơ Art Center" — Light 300, 14sp, white
  - Livestream text — Light 300, 14sp, white
- Actions Row (gap=16dp):
  - mms_2.2 primary Button (160×40dp, solid #FFEA9E, RoundedCornerShape(4dp), padding=12dp)
  - mms_2.3 secondary OutlinedButton (159×40dp, 10% #FFEA9E bg, border=1dp #998C5F, padding=10dp, white label)
- All values hardcoded for now (countdown shows static 00/00/00 or placeholder)
- **LazyColumn content padding**: outermost scrollable container must have `contentPadding = PaddingValues(bottom = 120.dp)` (72dp NavBar + 48dp FAB) so last content section is not hidden under fixed overlays
- **Acceptance**: Hero section layout matches design; bold gold date visible; bottom content not obscured

#### T012 — Implement mms_3_note (theme description)
- `Text` with hardcoded full EN/VN string from strings.xml
- Font: MontserratLight, 14sp, lineHeight 20sp, letterSpacing 0.25px, white
- Width 335dp, padding start=20dp
- **Acceptance**: Paragraph text displays correctly; no truncation

#### T013 — Implement mms_4 Awards section header (mms_4.1)
- Shared component `SectionHeader(eventLabel, sectionTitle)` composable:
  - `Column(gap=4dp, width=335dp, height=53dp)`
  - Event label: homeSectionLabel token, white
  - `HorizontalDivider(color = outlineVariant)` (1dp)
  - Section title: homeSectionTitle token, ButtonPrimaryBg (#FFEA9E)
- Reused for both Awards (4.1) and Kudos (5.1) sections
- **Acceptance**: Header matches design; used for both sections

#### T014 — Implement mms_4.2 Awards list (static placeholder)
- `LazyRow` with hardcoded 3 placeholder cards (use same component as final, with dummy data)
- Each card: thumbnail Image + name Text + description Text (maxLines=2, overflow=Ellipsis) + "Chi tiết" TextButton (gold color)
- Scrollable, content width ~1040dp effective
- **Acceptance**: 3 cards visible; description truncates; horizontal scroll works

#### T015 — Implement mms_5 Kudos section
- `Column(gap=24dp, width=335dp, padding start=20dp)` — visibility controlled by `isKudosAvailable` flag
- `SectionHeader("Phong trào ghi nhận", "Sun* Kudos")` using T012 component
- Kudos Banner (335×145dp): `Box` with `#0F0F0F` bg + `Image(mm_media_kudos_background)`, `RoundedCornerShape(5.dp)`, logo overlay `Image(118×21dp)` end-right
- Note block: badge text "ĐIỂM MỚI CỦA SAA 2025" (Light 300 14sp) + description paragraph
- mms_5.3_Button: primary style (160×40dp, solid #FFEA9E, 12dp padding, arrow icon)
- When `isKudosAvailable = false`: entire Column hidden (`if (uiState.isKudosAvailable)`)
- **Acceptance**: Kudos section visible by default; hidden when flag false; banner renders correctly

#### T016 — Implement mms_6 FAB
- Fixed `Box` positioned `bottomEnd`, `end=20dp`, above nav bar
- `Box(89×48dp, RoundedCornerShape(100dp), ButtonPrimaryBg)`
- Inner `Row(gap=8dp, padding=8dp)`: Pen icon (24dp) + "/" Text (Regular 400, 24sp) + S/Kudos icon (24dp)
- Shadow: `Modifier.shadow(4.dp) + graphicsLayer` for outer glow `#FAE287` (approx via `drawBehind`)
- Content descriptions: FAB pencil + FAB Kudos
- **Acceptance**: FAB renders at bottom-right; glow shadow visible; correct icons

#### T017 — Implement mms_7 Bottom NavBar
- `NavigationBar` or custom `Row` at bottom of `Scaffold`
- `Box(fillMaxWidth, height=72dp)` with `clip(topStart=20dp, topEnd=20dp)` + `background(rgba(FFEA9E, 0.15))`
- Blur: `Modifier.blur()` requires **API 31+** (`minSdk = 24`). Use conditional:
  ```kotlin
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
      Modifier.blur(20.dp)
  else
      Modifier // no blur fallback — semi-transparent bg still visible
  ```
- 4 tabs: SAA 2025, Awards, Kudos, Profile
- Active (Home): icon + label in ButtonPrimaryBg (#FFEA9E); inactive: white reduced opacity
- **Acceptance**: Nav bar renders correctly on API 24 (no blur) and API 31+ (with blur); "SAA 2025" tab highlighted

---

### M3 — Live Countdown

#### T018 — Implement countdown coroutine in HomeViewModel
- `private val eventTarget = LocalDateTime.of(2025, 12, 26, 0, 0, 0).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant()`
- `viewModelScope.launch { while(true) { delay(1000); updateCountdown() } }`
- `updateCountdown()`: compute diff from `Clock.systemUTC().instant()` to target
  - If diff ≤ 0 → `TimeRemaining.EventEnded`
  - Else → `TimeRemaining.Counting(days, hours, minutes)`
- Note: **TR-001** requires server time — add `TODO("Replace with server time call")` comment for now; use device clock initially, flag as tech debt
- **Acceptance**: DAYS/HOURS/MINUTES update every second; "Event Ended" state shows when manually setting target to past date

#### T019 — Wire countdown to HomeScreen
- `HomeScreen` collects `uiState.timeRemaining` via `collectAsStateWithLifecycle()`
- When `Counting`: display values in countdown units
- When `EventEnded`: replace countdown row with `Text("Event Ended")`, hide "Coming soon" label
- When `Loading`: show placeholder `---`
- **Acceptance**: Real countdown values display; no crashes on state transitions

---

### M4 — Awards API

#### T020 — Create AwardDto + AwardDataSource (remote)
- `data/remote/dto/AwardDto.kt`:
  ```kotlin
  @Serializable
  data class AwardDto(
      val id: String,
      val category: String?,
      val name: String?,
      val description: String?,
      @SerialName("thumbnail_url") val thumbnailUrl: String?,
  )
  fun AwardDto.toDomain() = Award(id = id, category = category ?: "", name = name ?: "", description = description ?: "", thumbnailUrl = thumbnailUrl)
  ```
- `data/remote/source/AwardDataSource.kt` (mirrors `SupabaseAuthDataSource` pattern):
  ```kotlin
  @Singleton
  class AwardDataSource @Inject constructor(private val supabase: SupabaseClient) {
      suspend fun fetchAwards(): Result<List<AwardDto>> = try {
          val result = supabase.from("awards").select().decodeList<AwardDto>()
          Result.success(result)
      } catch (e: CancellationException) { throw e
      } catch (e: Exception) { Timber.e("fetchAwards: ${e.message}"); Result.failure(e) }
  }
  ```
- All fields except `id` nullable (constitution: Supabase DTOs nullable except PK)
- **Acceptance**: No `!!` usage; Serializable annotation compiles; requires T001 (postgrest dependency)

#### T021 — Create AwardRepository + impl
- `domain/repository/AwardRepository.kt`: `suspend fun getAwards(): Result<List<Award>>`
- `data/repository/AwardRepositoryImpl.kt`: injects `AwardDataSource`, calls `fetchAwards()`, maps DTOs via `toDomain()`
- Hilt binding in `di/` module
- **Acceptance**: Repository injectable; `Result.failure` propagated on network error; no exception leak to UI

#### T022 — Create GetAwardsUseCase
- `domain/usecase/GetAwardsUseCase.kt`: delegates to `AwardRepository.getAwards()`
- Simple delegate, no extra logic
- **Acceptance**: UseCase injectable; returns `Result<List<Award>>`

#### T023 — Wire awards into HomeViewModel
- Inject `GetAwardsUseCase`; call in `init {}`:
  ```kotlin
  viewModelScope.launch {
      _uiState.update { it.copy(awards = AwardsState.Loading) }
      getAwardsUseCase().fold(
          onSuccess = { list -> _uiState.update { it.copy(awards = if (list.isEmpty()) AwardsState.Empty else AwardsState.Success(list)) } },
          onFailure = { e -> handleApiError(e); _uiState.update { it.copy(awards = AwardsState.Error(e.message ?: "")) } },
      )
  }
  ```
- **TR-002 paging decision**: Initial implementation fetches all awards in one request (`from("awards").select()`). Add a `// TODO: Add Paging3 when award count exceeds ~50` comment. Paging implementation is deferred — not in scope for this milestone.
- **Acceptance**: UiState reflects Loading → Success/Empty/Error transitions; auth errors redirect via flags

#### T024 — Wire awards to HomeScreen
- Replace placeholder cards with `LazyRow` driven by `uiState.awards`:
  - `Loading` → shimmer skeleton (3 placeholder cards with animated alpha)
  - `Success(list)` → real AwardCard composables
  - `Empty` → `Text("Chưa có giải thưởng")` centered
  - `Error` → `Text(message) + Button("Thử lại")` → triggers retry in ViewModel
- `AwardCard` composable: `AsyncImage` (Coil) for `thumbnailUrl`, name, description (2-line ellipsis), gold "Chi tiết" link
  - Coil params: `placeholder = painterResource(R.drawable.ic_launcher_background)` (or a dedicated placeholder drawable), `error = painterResource(R.drawable.ic_launcher_background)` — ensures FR-013 fallback
  - `contentDescription = stringResource(R.string.cd_award_detail, award.name)`
- **Acceptance**: Real awards display; loading/empty/error states all render correctly

---

### M5 — Notifications Badge

#### T025 — Create NotificationRepository + usecase
- `GetUnreadNotificationsUseCase`: returns `Result<Int>` (unread count)
- Calls `GET /notifications?unread=true` (or Supabase table count query)
- Pattern mirrors T020/T021
- **Acceptance**: Returns count; handles 0 gracefully

#### T026 — Wire badge to HomeViewModel + HomeScreen
- In HomeViewModel `init {}`: call `GetUnreadNotificationsUseCase`, update `unreadNotificationCount`
- In HomeScreen `HomeHeader`: badge dot shown when `unreadCount > 0`
- Bell contentDescription uses `String.format(cd_home_notifications, count)`
- **Acceptance**: Badge dot appears when count > 0; hides when count = 0

---

### M6 — Navigation Wiring

#### T027 — Add new NavRoutes for Home sub-destinations
- `NavRoutes.kt`: add routes for screens linked from Home:
  ```kotlin
  data object Awards : NavRoutes("awards")
  data object Kudos : NavRoutes("kudos")
  data object Profile : NavRoutes("profile")
  data object WriteKudo : NavRoutes("write_kudo")
  data object Notifications : NavRoutes("notifications")
  data object LanguageSelector : NavRoutes("language_selector")
  data object Search : NavRoutes("search")
  ```
- Add stub `composable(route) { Surface { Text("...") } }` for each in `SaaNavHost.kt`
- ⚠️ Destination screen IDs from spec are unverified — use route strings only; do NOT hardcode Figma screen IDs
- **Acceptance**: All routes navigable without crash

#### T028 — Wire header actions (Language, Search, Bell)
- Language Switcher tap → `navController.navigate(NavRoutes.LanguageSelector.route)`
- Search icon tap → `navController.navigate(NavRoutes.Search.route)`
- Bell tap → `navController.navigate(NavRoutes.Notifications.route)`
- **Acceptance**: Each tap navigates without crash

#### T029 — Wire Hero buttons (ABOUT AWARD, ABOUT KUDOS)
- mms_2.2 Button: `navController.navigate(NavRoutes.Awards.route)`
- mms_2.3 Button: `navController.navigate(NavRoutes.Kudos.route)`
- **Acceptance**: Both navigate correctly

#### T030 — Wire award card "Chi tiết" navigation
- `AwardCard` receives `onDetailClick: (Award) -> Unit` callback
- In `HomeScreen`: `navController.navigate("${NavRoutes.Awards.route}/${award.id}")`
- Add parameterized route `awards/{awardId}` to NavHost
- **Acceptance**: Tapping any award card navigates with correct ID in route

#### T031 — Wire Kudos section "Chi tiết" button
- mms_5.3 Button: `navController.navigate(NavRoutes.Kudos.route)`
- **Acceptance**: Navigates to Kudos stub

#### T032 — Wire FAB actions with double-tap prevention
- Double-tap prevention belongs in **ViewModel**, not Composable (constitution Rule 4):
  ```kotlin
  // HomeViewModel.kt
  private var lastFabClickTime = 0L
  fun onFabPencilClick(navigate: () -> Unit) {
      val now = System.currentTimeMillis()
      if (now - lastFabClickTime > 500L) { lastFabClickTime = now; navigate() }
  }
  fun onFabKudosClick(navigate: () -> Unit) {
      val now = System.currentTimeMillis()
      if (now - lastFabClickTime > 500L) { lastFabClickTime = now; navigate() }
  }
  ```
- In `HomeScreen.kt` FAB: `onClick = { viewModel.onFabPencilClick { navController.navigate(NavRoutes.WriteKudo.route) } }`
- S/Kudos icon: `onClick = { viewModel.onFabKudosClick { navController.navigate(NavRoutes.Kudos.route) } }`
- **Acceptance**: Single tap navigates; rapid double-tap fires only once; no business logic in Composable

#### T033 — Wire Bottom NavBar tab selection
- Each tab calls `navController.navigate(route) { launchSingleTop = true; restoreState = true }`
- "SAA 2025" tab: when already on Home, do nothing (launchSingleTop prevents re-navigation)
- Active state: track current back-stack destination via `navController.currentBackStackEntryAsState()`
- **Acceptance**: Tapping each tab navigates; active tab highlighted; no duplicate stack entries

---

### M7 — Polish & Accessibility

#### T034 — ~~Restore startDestination to Login~~ (N/A — removed)
- `startDestination` was never changed (see T007 fix). No action required.
- Verify Login → Home navigation still works end-to-end on a real/emulated device.
- **Acceptance**: App starts at Login; successful login lands on Home

#### T035 — Countdown font size TBD resolution
- Query Figma node `6885:8988` via MoMorph to get exact font sizes for countdown numbers and unit labels
- Update `homeCountdownNumber` and `homeCountdownLabel` tokens in `Type.kt`
- **Acceptance**: Countdown tiles match Figma pixel-for-pixel

#### T036 — Full accessibility pass
- Verify all `contentDescription` values match strings.xml entries
- Bell: dynamic `String.format(cd_home_notifications, unreadCount)`
- Award card: `cd_award_detail.format(award.name)`
- Countdown: `semantics { liveRegion = LiveRegionMode.Polite }` on countdown Row
- Decorative images: `contentDescription = null`
- **Acceptance**: TalkBack narration covers all interactive elements

#### T037 — Kudos feature flag test
- Add `isKudosAvailable = false` test path in ViewModel (via unit test or preview)
- Verify entire `mms_5_kudos` section disappears (no placeholder)
- **Acceptance**: Section hidden when flag false; no layout shift

#### T038 — Build verification
- Run `./gradlew assembleDebug --no-daemon`
- Confirm `BUILD SUCCESSFUL`, no warnings about missing resources, no `!!` in new files
- **Acceptance**: Clean build

---

## Dependencies & Blockers

| ID | Dependency | Status | Blocking Tasks |
|----|-----------|--------|---------------|
| D1 | Countdown exact font sizes (Figma node 6885:8988) | ⚠️ TBD | T034 |
| D2 | Navigation screen IDs verified (linkedFrameId = null) | ⚠️ Unverified | T026–T032 |
| D3 | `GET /awards` endpoint confirmed (Supabase table name) | ⚠️ Predicted | T019–T023 |
| D4 | `GET /notifications` endpoint / unread count mechanism | ⚠️ Predicted | T024–T025 |
| D5 | Server time API for countdown (TR-001) | ⚠️ Not defined | T017 (tech debt) |
| D6 | KudosFeatureFlag source (remote config vs. hardcoded) | ⚠️ Not defined | T014, T036 |
| D7 | Coil `coil-compose` dependency | ✅ Resolved — add in T001 | T024 |
| D8 | Supabase `postgrest-kt` dependency | ✅ Resolved — add in T001 | T020–T024 |

---

## File Checklist

| File | Action | Milestone |
|------|--------|-----------|
| `android/gradle/libs.versions.toml` | Edit — add coil + supabase-postgrest | M1/T001 |
| `android/app/build.gradle.kts` | Edit — add coil + supabase-postgrest impl | M1/T001 |
| `res/drawable/mm_media_logo_homepage.png` | Create (download) | M1/T002 |
| `res/drawable/mm_media_logo_rootfuther.png` | Create (download) | M1/T002 |
| `res/drawable/mm_media_kudos_background.png` | Create (download) | M1/T002 |
| `ui/theme/Type.kt` | Edit — add 4 tokens | M1/T003 |
| `res/values/strings.xml` | Edit — add Home strings | M1/T004 |
| `res/values-en/strings.xml` | Edit — add EN strings | M1/T004 |
| `domain/model/Award.kt` | Create | M1/T005 |
| `presentation/ui/home/HomeUiState.kt` | Create | M1/T006 |
| `presentation/ui/home/HomeViewModel.kt` | Create | M1/T007 |
| `presentation/ui/home/HomeScreen.kt` | Create | M1/T008 |
| `SaaNavHost.kt` | Edit — wire HomeScreen | M1/T008 |
| `data/remote/dto/AwardDto.kt` | Create | M4/T020 |
| `data/remote/source/AwardDataSource.kt` | Create | M4/T020 |
| `domain/repository/AwardRepository.kt` | Create | M4/T021 |
| `data/repository/AwardRepositoryImpl.kt` | Create | M4/T021 |
| `domain/usecase/GetAwardsUseCase.kt` | Create | M4/T022 |
| `data/remote/source/NotificationDataSource.kt` | Create | M5/T025 |
| `domain/repository/NotificationRepository.kt` | Create (interface) | M5/T025 |
| `data/repository/NotificationRepositoryImpl.kt` | Create | M5/T025 |
| `domain/usecase/GetUnreadNotificationsUseCase.kt` | Create | M5/T025 |
| `NavRoutes.kt` | Edit — add 7 routes (incl. Search) | M6/T027 |

---

## Open Questions (confirm before T027+)

1. **Navigation destinations**: All `linkedFrameId` values are `null` in MoMorph. Confirm with designer which route each Home CTA targets before wiring T027–T032.
2. **Countdown event-ended state**: What replaces countdown after 26/12/2025? "Event Ended" text or hide section entirely?
3. **Server time**: Is there a Supabase function / REST endpoint for authoritative server time to satisfy TR-001?
4. **Kudos feature flag**: Is this a Supabase remote config flag, a local boolean, or based on a user role?
5. **Award Detail route**: Does award detail use a parameterized route (`awards/{id}`) or navigates to a fixed screen? Confirm with current NavRoutes design.
