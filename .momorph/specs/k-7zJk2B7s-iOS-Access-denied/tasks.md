# Tasks: [iOS] Access Denied

**Frame**: `k-7zJk2B7s-iOS-Access-denied`
**Prerequisites**: spec.md ✅ | plan.md ✅ | design-style.md ⚠️ not present — UI specs embedded in spec.md (Figma nodes 6885:9522–9531)

---

## Task Format

```
- [ ] T### [P?] [Story?] Description | file/path.kt
```

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this belongs to (US1–US4)
- **|**: File path affected by this task

---

## Phase 1: Foundation — String Resources (Blocking Prerequisite)

**Purpose**: Cung cấp toàn bộ string keys cho tất cả user stories. Không có task nào sau đây có thể hoàn thành nếu thiếu phase này.

**⚠️ CRITICAL**: Phase 2–4 phụ thuộc hoàn toàn vào phase này.

- [x] T001 Add 4 VN string keys to default strings.xml: `access_denied_title`, `access_denied_description`, `access_denied_button`, `cd_back_arrow` | android/app/src/main/res/values/strings.xml
- [x] T002 [P] Add 4 EN string keys to values-en strings.xml: `access_denied_title`, `access_denied_description`, `access_denied_button`, `cd_back_arrow` | android/app/src/main/res/values-en/strings.xml

**String values**:
| Key | VN (`values/`) | EN (`values-en/`) |
|-----|----------------|-------------------|
| `access_denied_title` | `Truy cập bị từ chối` | `Access Denied` |
| `access_denied_description` | `The resource you're looking for doesn't exist or has been removed.` | `The resource you're looking for doesn't exist or has been removed.` |
| `access_denied_button` | `Quay về trang chủ` | `Go back to Home` |
| `cd_back_arrow` | `Quay lại` | `Go back` |

> T001 và T002 có thể chạy song song (khác file).

**Checkpoint**: Cả hai strings.xml đã có đủ 4 keys → Phase 2 có thể bắt đầu.

---

## Phase 2: User Story 1 — Hiển thị màn hình Access Denied (Priority: P1) 🎯 MVP

**Goal**: Khi `LoginViewModel` emit `isAccessDenied = true`, ứng dụng điều hướng đến màn hình hiển thị tiêu đề, mô tả lỗi, và hình minh họa.

**Independent Test**: Giả lập tài khoản `@gmail.com` (non sun-asterisk) đăng nhập → app phải hiển thị màn hình với title "Truy cập bị từ chối" / "Access Denied" và description text đúng ngôn ngữ.

### UI Implementation (US1)

- [x] T003 [US1] Create `AccessDeniedScreen.kt` with `Scaffold { Column { ... } }` skeleton, package `com.example.saa.presentation.ui.access_denied` | android/app/src/main/java/com/example/saa/presentation/ui/access_denied/AccessDeniedScreen.kt
- [x] T004 [US1] Implement `mms_Header` section (Node 6885:9523): `IconButton(back_arrow)` placeholder + `Text(access_denied_title)` + `HorizontalDivider` + `Text(access_denied_description)` | android/app/src/main/java/com/example/saa/presentation/ui/access_denied/AccessDeniedScreen.kt
- [x] T005 [US1] Implement `mms_2.1` image section (Node 6885:9529): `Image(painterResource(R.drawable.mm_media_icon), contentDescription = null)` with `// TODO: replace with mm_media_not_found when designer finalizes asset (Blocker B2)` | android/app/src/main/java/com/example/saa/presentation/ui/access_denied/AccessDeniedScreen.kt
- [x] T006 [US1] Update `SaaNavHost.kt` — replace TODO stub `composable(NavRoutes.AccessDenied.route) { Surface { Text(...) } }` with `AccessDeniedScreen(navController = navController)` | android/app/src/main/java/com/example/saa/SaaNavHost.kt

**Checkpoint**: Build `assembleDebug` passes. Navigating to `NavRoutes.AccessDenied` shows the screen with title + description + image.

---

## Phase 3: User Story 2 — "Go back to Home" Navigation (Priority: P1)

**Goal**: Nhấn nút "Go back to Home" điều hướng đến `NavRoutes.Home` và xóa Access Denied khỏi back stack.

**Independent Test**: Từ màn hình Access Denied, nhấn nút → `NavRoutes.Home` hiển thị → nhấn Back không quay lại Access Denied.

### UI Implementation (US2)

- [x] T007 [US2] Implement `mms_2.2_Button` (Node 6885:9531): `Button(onClick = { navController.navigate(NavRoutes.Home.route) { popUpTo(NavRoutes.AccessDenied.route) { inclusive = true } } }) { Text(stringResource(R.string.access_denied_button)) }` | android/app/src/main/java/com/example/saa/presentation/ui/access_denied/AccessDeniedScreen.kt

**Checkpoint**: Nhấn nút từ màn hình Access Denied → Home hiển thị → Back không quay lại Access Denied.

---

## Phase 4: User Story 4 — Back Arrow Exit App (Priority: P2)

**Goal**: Nhấn `back_arrow` trên header exit app (không điều hướng về Login vì Login đã bị pop).

**Independent Test**: Từ màn hình Access Denied, nhấn back_arrow → app thoát hoàn toàn (confirmed bằng Android Profiler hoặc logcat lifecycle).

### UI Implementation (US4)

- [x] T008 [US4] Implement `back_arrow` `onClick` trong `mms_Header`: lấy `val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher`, gọi `backDispatcher?.onBackPressed()` khi click. Không dùng `activity?.finish()` (vi phạm TR-002). | android/app/src/main/java/com/example/saa/presentation/ui/access_denied/AccessDeniedScreen.kt
- [x] T009 [US4] Add `Modifier.semantics { contentDescription = stringResource(R.string.cd_back_arrow) }` to `back_arrow` `IconButton` for TalkBack accessibility (FR-005) | android/app/src/main/java/com/example/saa/presentation/ui/access_denied/AccessDeniedScreen.kt

**Checkpoint**: Nhấn back_arrow trên Access Denied screen → app exit (Activity `onDestroy` logged).

---

## Phase 5: User Story 3 — Localization (Priority: P2)

**Goal**: Tất cả text hiển thị đúng ngôn ngữ đang được chọn (VN / EN).

**Independent Test**: Từ Login screen, đổi language sang EN → trigger access denied flow → tất cả text (title, description, button) hiển thị tiếng Anh.

### Verification (US3)

- [x] T010 [US3] Verify `AccessDeniedScreen.kt` — không có hardcoded string nào; tất cả text đi qua `stringResource(R.string.*)`. Confirm: `access_denied_title`, `access_denied_description`, `access_denied_button`, `cd_back_arrow` đều sử dụng keys từ T001/T002. | android/app/src/main/java/com/example/saa/presentation/ui/access_denied/AccessDeniedScreen.kt

**Checkpoint**: Đổi ngôn ngữ → Access Denied text thay đổi tương ứng mà không cần restart app.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Preview, code quality, build validation.

- [x] T011 [P] Add `@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)` and `@Preview(showBackground = true)` annotations to `AccessDeniedScreen.kt` for design verification | android/app/src/main/java/com/example/saa/presentation/ui/access_denied/AccessDeniedScreen.kt
- [x] T012 [P] Run `./gradlew assembleDebug` — BUILD SUCCESSFUL ✅ and confirm BUILD SUCCESSFUL with zero errors | (terminal)
- [x] T013 [P] Run `./gradlew testDebugUnitTest` — BUILD SUCCESSFUL ✅ and confirm existing tests still pass (regression check) | (terminal)

---

## Dependencies & Execution Order

### Phase Dependencies

```
Phase 1 (Foundation)
  ├─ T001 (values/strings.xml)
  └─ T002 [P] (values-en/strings.xml)
        ↓ (both must complete)
Phase 2 (US1 — display screen)
  ├─ T003 → T004 → T005 (sequential, same file)
  └─ T006 (SaaNavHost — can start after T003)
        ↓
Phase 3 (US2 — button) → T007
        ↓
Phase 4 (US4 — back arrow) → T008 → T009
        ↓
Phase 5 (US3 — localization verify) → T010
        ↓
Phase 6 (Polish) → T011 [P] T012 [P] T013 [P]
```

### Parallel Opportunities

| Group | Tasks | Condition |
|-------|-------|-----------|
| Foundation strings | T001, T002 | Can start in parallel immediately |
| NavHost update | T006 | Can start once T003 creates the file |
| Polish | T011, T012, T013 | All independent after T010 |

### Within Each User Story

- T003 must complete before T004 (file must exist)
- T004 must complete before T008 (back_arrow onClick added to existing IconButton placeholder)
- T007 adds Button to existing Column — depends on T005 (image section created first)

---

## Implementation Strategy

### MVP First (Recommended)

1. Complete **Phase 1** (T001 + T002) — ~5 min
2. Complete **Phase 2** (T003 → T006) — ~30 min
3. **STOP and validate**: `assembleDebug` passes, screen renders
4. Complete **Phase 3** (T007) — ~10 min
5. **STOP and validate**: button navigation works
6. Complete Phase 4 → 5 → 6 incrementally

### Single Session

T001 → T002 → T003 → T004 → T005 → T006 → T007 → T008 → T009 → T010 → T011 → T012 → T013

---

## Blockers

| ID | Task(s) Affected | Description | Action |
|----|------------------|-------------|--------|
| B2 | T005 | `mm_media_not_found` asset not finalized by designer | Use `mm_media_icon` + `// TODO` comment; replace when asset exported |

---

## Notes

- Commit after Phase 2 checkpoint and Phase 3 checkpoint.
- Mark tasks `[x]` as you complete them.
- If `LocalOnBackPressedDispatcherOwner.current` returns null in tests, skip gracefully — system Back still functions.
- `access_denied_description` is identical in VN and EN (Figma source is EN; VN translation not yet provided for description — only title and button were confirmed).
