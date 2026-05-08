# Tasks: [iOS] Sun*Kudos_Tiêu chuẩn cộng đồng

**Frame**: `xms7csmDhD-iOS-Community-Standards`
**Prerequisites**: spec.md ✅ reviewed | design-style.md ✅ reviewed | plan.md ✅ reviewed

---

## Task Format

```
- [ ] T### [P?] [Story?] Description | file/path.kt
```

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this belongs to (US1–US3)
- **|**: File path affected by this task

---

## Phase 0: Unblock Figma Content ⚠️ (Pre-condition — Must Complete First)

**Purpose**: Xác minh số lượng section và nội dung text chính xác từ Figma trước khi điền string resources.

> ⚠️ **BLOCKER**: Phase 1 (String Resources) không thể hoàn thành cho đến khi Phase 0 xong. Các Phase 2–5 có thể bắt đầu song song với Phase 0 vì chúng không phụ thuộc vào nội dung text.

- [x] T000 Re-run `momorph_downloadFigmaImage` cho node `xms7csmDhD` (fileKey `9ypp4enmFmdK3YAFJLIu6C`) — xác minh số lượng section (spec liệt kê 5), tên section và body text | .momorph/specs/xms7csmDhD-iOS-Community-Standards/spec.md
- [x] T000b [P] Update TBD node IDs trong spec.md và design-style.md sau khi Figma unblock | .momorph/specs/xms7csmDhD-iOS-Community-Standards/spec.md, design-style.md

**Checkpoint**: Node IDs confirmed, section count và text content verified → Phase 1 có thể bắt đầu.

---

## Phase 1: String Resources (Blocking Prerequisite cho Phase 3)

**Purpose**: Cung cấp toàn bộ string keys cho `CommunityStandardsScreen`. Không có Composable nào compile được nếu thiếu keys.

**⚠️ DEPENDS ON**: Phase 0 (exact section content và section count).

> T001 và T002 có thể chạy song song (khác file).

- [x] T001 [US3] Add community_standards string keys (VN) to `values/strings.xml`: `community_standards_title` = `"Tiêu chuẩn cộng đồng"`, `community_standards_s1_title`, `community_standards_s1_body`, `community_standards_s2_title`, `community_standards_s2_body`, `community_standards_s3_title`, `community_standards_s3_body`, `community_standards_s4_title`, `community_standards_s4_body`, `community_standards_s5_title`, `community_standards_s5_body` (values confirmed từ Phase 0) | android/app/src/main/res/values/strings.xml
- [x] T002 [P] [US3] Add same 11 community_standards string keys (EN) to `values-en/strings.xml`: `community_standards_title` = `"Community Standards"`, s1–s5 title/body in English | android/app/src/main/res/values-en/strings.xml

> `cd_back_arrow` (`"Quay lại"` / `"Go back"`) đã tồn tại tại L18 trong cả hai files — **không thêm lại**.

**String key table** *(values to be filled after Phase 0)*:

| Key | VN (`values/`) | EN (`values-en/`) |
|-----|----------------|-------------------|
| `community_standards_title` | `Tiêu chuẩn cộng đồng` | `Community Standards` |
| `community_standards_s1_title` | *(verify từ Figma)* | *(verify từ Figma)* |
| `community_standards_s1_body` | *(verify từ Figma)* | *(verify từ Figma)* |
| `community_standards_s2_title` | *(verify từ Figma)* | *(verify từ Figma)* |
| `community_standards_s2_body` | *(verify từ Figma)* | *(verify từ Figma)* |
| `community_standards_s3_title` | *(verify từ Figma)* | *(verify từ Figma)* |
| `community_standards_s3_body` | *(verify từ Figma)* | *(verify từ Figma)* |
| `community_standards_s4_title` | *(verify từ Figma)* | *(verify từ Figma)* |
| `community_standards_s4_body` | *(verify từ Figma)* | *(verify từ Figma)* |
| `community_standards_s5_title` | *(verify từ Figma)* | *(verify từ Figma)* |
| `community_standards_s5_body` | *(verify từ Figma)* | *(verify từ Figma)* |

**Checkpoint**: `assembleDebug` passes với 11 community_standards keys có mặt trong cả hai strings.xml → Phase 3 có thể bắt đầu.

---

## Phase 2: Foundation — Route Declaration (Priority: P1) 🎯 MVP

**Purpose**: Khai báo `NavRoutes.CommunityStandards` — blocking prerequisite cho Phase 4 (SaaNavHost wiring) và Phase 5a (WriteKudoScreen wiring).

> Có thể bắt đầu **ngay lập tức**, song song với Phase 0 và Phase 1.

- [x] T003 [US1] Add `data object CommunityStandards : NavRoutes("community_standards")` vào `sealed class NavRoutes`, đặt sau `data object Search` | android/app/src/main/java/com/example/saa/NavRoutes.kt

**Checkpoint**: `NavRoutes.CommunityStandards.route` compile được → Phase 4 và 5a có thể bắt đầu.

---

## Phase 3: User Story 1 — Màn hình CommunityStandardsScreen (Priority: P1) 🎯 MVP

**Goal**: Tạo `CommunityStandardsScreen.kt` — Composable thuần UI, không ViewModel, hiển thị đầy đủ header + scrollable content với N sections.

**Independent Test**: Giả lập navigate trực tiếp đến `NavRoutes.CommunityStandards` → màn hình hiển thị đúng header (title vàng, back arrow), nền `#00101A`, các section có thể scroll.

**⚠️ DEPENDS ON**: Phase 1 (string keys phải tồn tại để compile).

### UI Implementation (US1 + US2)

- [x] T004 [US1] Create new file `CommunityStandardsScreen.kt`, khai báo package `com.example.saa.presentation.ui.community_standards`, thêm 3 top-level private color constants: `CsScreenBg = Color(0xFF00101A)`, `CsGoldText = Color(0xFFFFEA9E)`, `CsBodyText = Color.White`, `CsDivider = Color(0xFF2E3940)` | android/app/src/main/java/com/example/saa/presentation/ui/community_standards/CommunityStandardsScreen.kt
- [x] T005 [US1] Implement `CommunityStandardsScreen(onNavigateBack: () -> Unit)` Composable: `Scaffold(containerColor = CsScreenBg)` + `TopAppBar` với `containerColor = CsScreenBg`, `scrolledContainerColor = CsScreenBg` | android/app/src/main/java/com/example/saa/presentation/ui/community_standards/CommunityStandardsScreen.kt
- [x] T006 [US1] Implement `mms_Header` trong `TopAppBar`: `navigationIcon = IconButton(onClick = onNavigateBack, Modifier.semantics { contentDescription = backArrowDesc })` — `backArrowDesc` capture từ `stringResource(R.string.cd_back_arrow)` ở Composable scope (không capture bên trong `semantics {}` lambda); title `Text(stringResource(R.string.community_standards_title), color = CsGoldText, fontWeight = Bold, textAlign = Center)` | android/app/src/main/java/com/example/saa/presentation/ui/community_standards/CommunityStandardsScreen.kt
- [x] T007 [US1] Implement scrollable content: `Column(Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp))` chứa `HorizontalDivider(color = CsDivider)` → N × `CommunityStandardsSection(title, body)` → `Spacer(24.dp)` | android/app/src/main/java/com/example/saa/presentation/ui/community_standards/CommunityStandardsScreen.kt
- [x] T008 [US1] Implement private helper `CommunityStandardsSection(title: String, body: String)`: `Text(title, titleMedium, CsGoldText, SemiBold)` + `Spacer(8.dp)` + `Text(body, bodyMedium, CsBodyText)` + `Spacer(16.dp)` + `HorizontalDivider(CsDivider)` + `Spacer(16.dp)` | android/app/src/main/java/com/example/saa/presentation/ui/community_standards/CommunityStandardsScreen.kt
- [x] T009 [P] [US1] Add `@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)` cho `CommunityStandardsScreen` — dùng placeholder strings nếu Phase 1 chưa xong | android/app/src/main/java/com/example/saa/presentation/ui/community_standards/CommunityStandardsScreen.kt

**Checkpoint**: `assembleDebug` passes. Layout preview hiển thị đúng nền tối, title vàng, scroll content.

---

## Phase 4: Foundation — SaaNavHost Wiring (Priority: P1) 🎯 MVP

**Purpose**: Đăng ký `CommunityStandardsScreen` trong NavHost để route `community_standards` hoạt động.

**⚠️ DEPENDS ON**: Phase 2 (`NavRoutes.CommunityStandards`) + Phase 3 (`CommunityStandardsScreen`).

- [x] T010 [US1] Add `composable(NavRoutes.CommunityStandards.route) { CommunityStandardsScreen(onNavigateBack = { navController.navigateUp() }) }` trong `NavHost` block, sau `composable(NavRoutes.WriteKudo.route)` entry | android/app/src/main/java/com/example/saa/SaaNavHost.kt
- [x] T011 [P] [US1] Add import `com.example.saa.presentation.ui.community_standards.CommunityStandardsScreen` | android/app/src/main/java/com/example/saa/SaaNavHost.kt

**Checkpoint**: `NavRoutes.CommunityStandards` có entry trong NavHost. Build passes.

---

## Phase 5: User Story 1 — WriteKudo Trigger Wiring (Priority: P1) 🎯 MVP

**Goal**: Thay thế `/* TODO */` trong `onCommunityStandardsClick` để tap vào "Tiêu chuẩn cộng đồng" trên toolbar thực sự điều hướng đến màn hình.

**⚠️ DEPENDS ON**: Phase 2 (`NavRoutes.CommunityStandards`) + Phase 4 (SaaNavHost entry).

### 5a — WriteKudoScreen.kt parameter update

- [x] T012 [US1] Add `onNavigateToCommunityStandards: () -> Unit` parameter vào `WriteKudoScreen` function signature (sau `onNavigateBack: () -> Unit`, trước `viewModel`) | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoScreen.kt
- [x] T013 [US1] Replace `onCommunityStandardsClick = { /* TODO: navigate to community standards */ }` với `onCommunityStandardsClick = onNavigateToCommunityStandards` trong `FormattingToolbar(...)` call | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoScreen.kt

### 5b — SaaNavHost.kt WriteKudo call update

- [x] T014 [US1] Update `WriteKudoScreen(...)` call trong `SaaNavHost.kt`: thêm argument `onNavigateToCommunityStandards = { navController.navigate(NavRoutes.CommunityStandards.route) }` | android/app/src/main/java/com/example/saa/SaaNavHost.kt

### 5c — FormattingToolbar.kt color fix (independent)

- [x] T015 [P] Fix `CommunityLinkRed` color deviation: `Color(0xFFE46060)` → `Color(0xFFE73928)` (Figma token `--color-heart-active`) | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/FormattingToolbar.kt

**Checkpoint**: Từ màn hình Viết Kudo, tap "Tiêu chuẩn cộng đồng" trên toolbar → màn hình `CommunityStandardsScreen` hiển thị (SC-001 ✅). "Tiêu chuẩn cộng đồng" text hiển thị màu `#E73928`.

---

## Phase 6: User Story 2 — Xác minh Back Navigation (Priority: P1)

**Goal**: Back arrow và system back đều quay lại WriteKudo với form data không mất.

**⚠️ DEPENDS ON**: Phase 3 + 4 + 5 hoàn thành.

- [ ] T016 [US2] Manual test: điền form WriteKudo (title, recipient, message) → tap "Tiêu chuẩn cộng đồng" → màn hình CS hiển thị → tap back arrow → WriteKudo hiển thị, tất cả fields giữ nguyên. Nếu không pass: kiểm tra `navController.navigateUp()` trong SaaNavHost (không phải `popBackStack`) | android/app/src/main/java/com/example/saa/SaaNavHost.kt
- [ ] T017 [P] [US2] Manual test: system back button từ CS screen → same result as T016 | -

**Checkpoint**: SC-003 pass: back → WriteKudo, form intact.

---

## Phase 7: User Story 3 — Localization Verification (Priority: P2)

**Goal**: Tất cả text hiển thị đúng ngôn ngữ (VN/EN).

**⚠️ DEPENDS ON**: Phase 1 (both strings.xml complete) + Phase 3 + 4 + 5.

- [ ] T018 [US3] Manual test VN: ngôn ngữ app = VN → open CS screen → title "Tiêu chuẩn cộng đồng", tất cả section titles và bodies bằng tiếng Việt | -
- [ ] T019 [P] [US3] Manual test EN: chuyển ngôn ngữ sang EN → open CS screen → title "Community Standards", tất cả content bằng tiếng Anh | -

**Checkpoint**: SC-004 pass.

---

## Completion Checklist

| SC | Task(s) | Status |
|----|---------|--------|
| SC-001: Tất cả sections hiển thị | T007, T008, T013, T014 | ⬜ |
| SC-002: Scroll hoạt động | T007 (`verticalScroll`) | ⬜ |
| SC-003: Back → WriteKudo, form intact | T010, T016, T017 | ⬜ |
| SC-004: Nội dung đúng ngôn ngữ | T001, T002, T018, T019 | ⬜ |
| SC-005: Không có API call | T004–T009 (code review) | ⬜ |

---

## Dependency Graph

```
Phase 0 (Figma unblock)
    └──→ Phase 1 (strings) ──→ Phase 3 (screen) ──→ Phase 4 (NavHost) ──→ Phase 5a/b (wiring) ──→ Phase 6 (back nav test)
                                                                                                  └──→ Phase 7 (l10n test)
Phase 2 (NavRoutes) ──────────────────────────────→ Phase 4 (NavHost) ──→ Phase 5a/b
Phase 5c (color fix): independent, no dependencies
```

> Phases 0, 2 có thể bắt đầu ngay lập tức và song song với nhau.
