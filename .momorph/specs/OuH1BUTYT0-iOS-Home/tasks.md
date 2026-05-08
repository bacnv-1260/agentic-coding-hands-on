# Tasks: [iOS] Home

**Screen ID**: `OuH1BUTYT0`
**Frame**: `6885:8978` — `[iOS] Home`
**Prerequisites**: plan.md ✅ | spec.md ✅ | design-style.md ✅

---

## Task Format

```
- [x] T### [P?] [Story?] Description | file/path.kt
```

- **[P]**: Can run in parallel (different files, no shared dependencies)
- **[Story]**: User story this task belongs to (US1–US5)
- **|**: Primary file affected

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Add missing dependencies and download assets so all subsequent phases can build.

- [x] T001 Add Coil and Supabase postgrest-kt to libs.versions.toml and build.gradle.kts | android/gradle/libs.versions.toml + android/app/build.gradle.kts
- [x] T002 Download missing assets from Figma (mm_media_logo_homepage.png, mm_media_logo_rootfuther.png, mm_media_kudos_background.png) | android/app/src/main/res/drawable/

**Checkpoint**: `./gradlew assembleDebug` passes — AsyncImage and supabase.from() importable

---

## Phase 2: Foundation (Blocking Prerequisites)

**Purpose**: Core skeleton — UiState, ViewModel, domain model, theme tokens, strings — required by ALL user stories before any UI can render.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete.

- [x] T003 [P] Add 4 typography tokens (homeSectionLabel, homeSectionTitle, homeCountdownNumber, homeCountdownLabel) | android/app/src/main/java/com/example/saa/ui/theme/Type.kt
- [x] T004 [P] Add all Home screen strings to strings.xml (VN) and values-en/strings.xml (EN) | android/app/src/main/res/values/strings.xml + values-en/strings.xml
- [x] T005 [P] Create Award domain model | android/app/src/main/java/com/example/saa/domain/model/Award.kt
- [x] T006 Create HomeUiState (TimeRemaining, AwardsState sealed classes + isUnauthenticated/isForbidden flags) | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeUiState.kt
- [x] T007 Create HomeViewModel skeleton with @HiltViewModel, StateFlow, handleApiError(), consumeAuthError() | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeViewModel.kt
- [x] T008 Create HomeScreen skeleton with collectAsStateWithLifecycle() + TR-004 auth LaunchedEffect redirects; wire into SaaNavHost.kt | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt + SaaNavHost.kt

**Checkpoint**: Foundation complete — `./gradlew assembleDebug` passes, Home route reachable without crash

---

## Phase 3: User Story 1 — Event Info & Countdown (Priority: P1) 🎯 MVP

**Goal**: Hero section renders with all static event info; real-time countdown updates every second; "Coming soon"/"Event Ended" states handled.

**Independent Test**: Launch app → log in → observe Home screen → DAYS/HOURS/MINUTES update every 60s; "Thời gian: **26/12/2025**" date is bold gold; Actions Row shows both buttons.

### Static UI Shell (US1)

- [x] T009 [US1] Implement HomeBackground() composable — full-bleed keyvisual + shadow left + shadow bottom layers | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt
- [x] T010 [US1] Implement HomeHeader() — sticky header with gradient bg, SAA logo, Language Switcher, Search icon, Bell icon + badge dot | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt
- [x] T011 [US1] Implement mms_2_content Hero section — ROOT FURTHER logo, "Coming soon" label, static countdown placeholders (00/00/00), AnnotatedString date with bold gold value, venue/livestream rows, ABOUT AWARD primary button + ABOUT KUDOS secondary OutlinedButton; LazyColumn with contentPadding bottom=120dp | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt

### Live Countdown (US1)

- [x] T012 [US1] Implement countdown coroutine in HomeViewModel — tick every 1s, compute DAYS/HOURS/MINUTES diff to 2025-12-26T00:00 GMT+7, emit TimeRemaining.Counting / EventEnded; add TODO for server time (TR-001) | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeViewModel.kt
- [x] T013 [US1] Wire countdown states to HomeScreen — Counting shows real values; EventEnded replaces row with "Event Ended" text and hides "Coming soon" label; Loading shows "---" placeholder | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt

**Checkpoint**: User Story 1 complete — countdown ticks live; hero layout matches design-style.md §4.2

---

## Phase 4: User Story 2 — Awards Section (Priority: P1)

**Goal**: Awards horizontal card list loads from Supabase, shows loading skeleton, empty state, and error state with retry.

**Independent Test**: Launch → log in → scroll to Awards section → ≥1 AwardCard with thumbnail + name + truncated description + "Chi tiết" link; swipe horizontally; description truncates with ellipsis.

### Static UI Shell (US2)

- [x] T014 [US2] Implement SectionHeader(eventLabel, sectionTitle) shared composable — Column/gap=4dp, event label (homeSectionLabel), HorizontalDivider, section title (homeSectionTitle / gold) — reused for Awards 4.1 and Kudos 5.1 | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt
- [x] T015 [US2] Implement mms_4_awards static shell — SectionHeader + LazyRow with 3 hardcoded placeholder AwardCards (thumbnail Image, name, description maxLines=2 ellipsis, gold "Chi tiết" TextButton) | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt

### Data Layer (US2)

- [x] T016 [P] [US2] Create AwardDto (@Serializable, all fields nullable except id) + toDomain() mapper + AwardDataSource (supabase.from("awards").select()) | android/app/src/main/java/com/example/saa/data/remote/dto/AwardDto.kt + data/remote/source/AwardDataSource.kt
- [x] T017 [P] [US2] Create AwardRepository interface + AwardRepositoryImpl (injects AwardDataSource, maps DTOs) + Hilt binding in di/ | android/app/src/main/java/com/example/saa/domain/repository/AwardRepository.kt + data/repository/AwardRepositoryImpl.kt
- [x] T018 [US2] Create GetAwardsUseCase delegating to AwardRepository.getAwards() | android/app/src/main/java/com/example/saa/domain/usecase/GetAwardsUseCase.kt

### ViewModel + UI Integration (US2)

- [x] T019 [US2] Inject GetAwardsUseCase into HomeViewModel; load awards in init{} — Loading → Success/Empty/Error; call handleApiError() on failure; add TODO for Paging3 | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeViewModel.kt
- [x] T020 [US2] Wire uiState.awards to HomeScreen LazyRow — Loading: shimmer skeleton (3 cards animated alpha); Success: real AwardCards with AsyncImage (Coil, placeholder+error drawable, contentDescription); Empty: "Chưa có giải thưởng"; Error: message + retry Button | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt

**Checkpoint**: User Stories 1 & 2 complete — awards load from local Supabase; all states render

---

## Phase 5: User Story 3 — Sun*Kudos Section (Priority: P1)

**Goal**: Kudos section renders with banner, badge, description, and "Chi tiết" button. Entire section hidden when `isKudosAvailable = false`.

**Independent Test**: Launch → log in → scroll to Kudos section → banner image renders with rounded corners; "ĐIỂM MỚI CỦA SAA 2025" badge visible; set isKudosAvailable=false in preview → section disappears entirely (no placeholder).

### Static UI Shell (US3)

- [x] T021 [US3] Implement mms_5_kudos section — SectionHeader (from T014), Kudos Banner (335×145dp Box: #0F0F0F bg + mm_media_kudos_background image, RoundedCornerShape(5.dp), 118×21dp logo overlay end-right), badge text + description paragraph (MontserratLight 14sp), mms_5.3_Button primary style (160×40dp #FFEA9E 12dp padding); entire Column hidden when !uiState.isKudosAvailable | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt

### US3 Supporting UI

- [x] T022 [US3] Implement mms_3_note theme description (Text from strings.xml, MontserratLight 14sp lineHeight 20sp letterSpacing 0.25px, white, width 335dp padding start=20dp) | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt

**Checkpoint**: User Stories 1, 2 & 3 complete — full screen renders statically; Kudos hide/show works

---

## Phase 6: User Story 4 — Navigation (Priority: P1)

**Goal**: All tappable elements on Home navigate to correct destinations. FAB double-tap prevented. Bottom nav active state synced.

**Independent Test**: Tap "Awards" tab → navigates; tap "Kudos" tab → navigates; tap FAB pencil → opens WriteKudo; rapid double-tap FAB → fires only once; tap Bell → navigates to Notifications.

### FAB & Fixed Elements (US4)

- [x] T023 [US4] Implement mms_6 FAB — fixed Box bottomEnd end=20dp, 89×48dp pill RoundedCornerShape(100dp) #FFEA9E, Pen icon + "/" (24sp Regular) + Kudos icon, shadow 4dp + drawBehind glow #FAE287 | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt
- [x] T024 [US4] Implement mms_7 Bottom NavBar — Box fillMaxWidth height=72dp clip(topStart=20dp,topEnd=20dp) background rgba(FFEA9E,0.15), Modifier.blur(20.dp) conditional on API>=31, 4 tabs (SAA 2025 active/gold, others white reduced opacity) | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt

### Route Setup (US4)

- [x] T025 [US4] Add 7 new NavRoutes (Awards, Kudos, Profile, WriteKudo, Notifications, LanguageSelector, Search) + stub composables in SaaNavHost.kt | android/app/src/main/java/com/example/saa/NavRoutes.kt + SaaNavHost.kt

### Navigation Wiring (US4)

- [x] T026 [US4] Wire Header actions — Language Switcher → NavRoutes.LanguageSelector; Search → NavRoutes.Search; Bell → NavRoutes.Notifications | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt
- [x] T027 [US4] Wire Hero buttons — mms_2.2 ABOUT AWARD → NavRoutes.Awards; mms_2.3 ABOUT KUDOS → NavRoutes.Kudos | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt
- [x] T028 [US4] Wire AwardCard "Chi tiết" — add onDetailClick callback; NavHost adds parameterized route awards/{awardId}; navigate with award.id | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt + SaaNavHost.kt
- [x] T029 [US4] Wire Kudos "Chi tiết" button → NavRoutes.Kudos | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt
- [x] T030 [US4] Add onFabPencilClick() / onFabKudosClick() with 500ms debounce to HomeViewModel; wire FAB onClick lambdas in HomeScreen | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeViewModel.kt + HomeScreen.kt
- [x] T031 [US4] Wire Bottom NavBar — each tab navigates with launchSingleTop=true restoreState=true; active state via currentBackStackEntryAsState() | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt

**Checkpoint**: User Stories 1–4 complete — all navigation paths functional; double-tap protected

---

## Phase 7: User Story 5 — Header Notifications Badge (Priority: P2)

**Goal**: Bell icon shows/hides red badge dot based on unread notification count from API.

**Independent Test**: Seed unread notifications in local Supabase → launch → badge dot (8×8dp #D4271D) appears on Bell; read all notifications → dot disappears.

### Data Layer (US5)

- [x] T032 [P] [US5] Create NotificationDataSource (Supabase count query for unread notifications) + NotificationRepository interface + NotificationRepositoryImpl + Hilt binding + GetUnreadNotificationsUseCase | android/app/src/main/java/com/example/saa/data/remote/source/NotificationDataSource.kt + domain/repository/NotificationRepository.kt + data/repository/NotificationRepositoryImpl.kt + domain/usecase/GetUnreadNotificationsUseCase.kt

### ViewModel + UI Integration (US5)

- [x] T033 [US5] Inject GetUnreadNotificationsUseCase into HomeViewModel; load in init{}; update unreadNotificationCount | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeViewModel.kt
- [x] T034 [US5] Wire unreadNotificationCount to HomeHeader badge dot — show when count > 0; dynamic contentDescription using cd_home_notifications format string | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt

**Checkpoint**: All 5 user stories complete

---

## Phase 8: Polish & Cross-Cutting Concerns

**Purpose**: Accessibility, final asset verification, font size TBD resolution, and build sign-off.

- [x] T035 Query Figma node 6885:8988 via MoMorph to get exact countdown font sizes; update homeCountdownNumber and homeCountdownLabel tokens | android/app/src/main/java/com/example/saa/ui/theme/Type.kt
- [x] T036 [P] Full accessibility pass — verify all contentDescriptions match strings.xml; add semantics { liveRegion = LiveRegionMode.Polite } on countdown Row; set contentDescription=null on decorative images | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt
- [x] T037 [P] Add Kudos feature flag @Preview — set isKudosAvailable=false; verify mms_5_kudos section disappears with no layout shift | android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt
- [x] T038 Build verification — run ./gradlew assembleDebug --no-daemon; confirm BUILD SUCCESSFUL, no missing resource warnings, no !! in new files | android/

---

## Dependencies & Execution Order

### Phase Dependencies

```
Phase 1 (Setup)       — no deps — start immediately
Phase 2 (Foundation)  — depends on Phase 1 — BLOCKS all phases
Phase 3 (US1)         — depends on Phase 2
Phase 4 (US2)         — depends on Phase 2 (T014 also depends on Phase 3 for SectionHeader)
Phase 5 (US3)         — depends on T014 from Phase 4 (SectionHeader reuse)
Phase 6 (US4)         — depends on Phase 3 (T011 must exist), Phase 4 (T020 AwardCard), Phase 5 (T021 Kudos button)
Phase 7 (US5)         — depends on Phase 2 (Foundation) only — can run in parallel with Phase 3–5
Phase 8 (Polish)      — depends on all prior phases
```

### Within Each Phase

- Data layer tasks [P] within a story can run in parallel (different files)
- ViewModel injection tasks depend on UseCase tasks
- UI wiring tasks depend on ViewModel injection tasks

### Parallel Opportunities

| Parallel Group | Tasks | Can start after |
|----------------|-------|----------------|
| Foundation assets | T003, T004, T005 | Phase 1 complete |
| Awards data layer | T016, T017 | Phase 2 complete |
| Notifications data layer | T032 | Phase 2 complete (independent of Phase 3–5) |
| Polish tasks | T036, T037 | All stories complete |

---

## Implementation Strategy

### MVP Scope (Phases 1–3)

1. Complete Phase 1 + 2
2. Complete Phase 3 (US1: countdown + hero layout)
3. **STOP and VALIDATE**: App launches, countdown ticks, hero layout matches design
4. Continue to Phase 4+ incrementally

### Incremental Delivery

1. Setup + Foundation → build passes
2. Phase 3 (US1) → hero with live countdown
3. Phase 4 (US2) → awards load from API
4. Phase 5 (US3) → Kudos section functional
5. Phase 6 (US4) → all navigation wired
6. Phase 7 (US5) → notification badge live
7. Phase 8 → polish and final build

---

## Open Questions (block T025–T031 navigation wiring)

1. **Navigation destinations**: All `linkedFrameId` null — confirm which route each CTA targets before T026–T031.
2. **Countdown event-ended state**: "Event Ended" text or hide section entirely after 26/12/2025?
3. **Server time endpoint**: Any Supabase function for TR-001 authoritative time?
4. **Kudos feature flag**: Remote config, local boolean, or user role?
5. **Award Detail route**: Parameterized `awards/{id}` or fixed screen? (T028 assumes parameterized)
