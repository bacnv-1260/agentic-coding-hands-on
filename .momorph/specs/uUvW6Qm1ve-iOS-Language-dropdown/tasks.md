# Tasks: [iOS] Language Dropdown

**Screen ID**: `uUvW6Qm1ve`
**Frame**: `6891:15508 — [iOS] Language dropdown`
**Prerequisites**: `plan.md` ✅ | `spec.md` ✅ (2 review passes)
**Refactors**: Login tasks T026 (LanguageSelectorBottomSheet) + T027 (LoginScreen language trigger)

---

## Task Format

```
- [ ] T### [P?] [US?] Description | file/path.kt
```

- **[P]**: Có thể chạy song song (file khác nhau, không có dependency)
- **[US]**: Thuộc User Story nào (US1, US2)
- **|**: File path bị ảnh hưởng (relative từ `android/app/src/main/`)

---

## User Stories

| # | Story | Priority | Mô tả |
|---|-------|----------|--------|
| US1 | Language Dropdown UI | 🔴 P1 — MVP | Tap trigger → `DropdownMenu` popup góc trên phải → chọn VN/EN → UI cập nhật ngay, dropdown đóng |
| US2 | Persist Language | 🟡 P2 | Chọn ngôn ngữ → lưu DataStore → khôi phục khi mở lại app; I/O error fallback `"VN"` |

---

## Phase 1: Foundation — Hardening Prerequisites

**Purpose**: Đảm bảo data layer + ViewModel vững trước khi làm UI. Cả hai task có thể chạy song song.

⚠️ **CRITICAL**: T001 phải complete trước T004. T002 độc lập, chạy song song với T001.

- [x] T001 [US2] Thêm DataStore I/O error fallback vào `LanguagePreferenceDataSource.getLanguage()`:
  - Thêm `.catch { cause -> Timber.e(cause, "LanguagePreferenceDataSource") ; emit(emptyPreferences()) }` trước `.map`
  - Import `androidx.datastore.preferences.core.emptyPreferences`
  - **Không** log giá trị ngôn ngữ (TR-004/TR-006)
  | `java/com/example/saa/data/local/preference/LanguagePreferenceDataSource.kt`

- [x] T002 [P] [US1] Thêm rapid-tap guard vào `LoginViewModel.setLanguage()`:
  - Thêm `if (code == _uiState.value.selectedLanguage) return` làm dòng đầu tiên của function — trước `viewModelScope.launch`
  - Edge case: chọn lại option đang active → DataStore không bị ghi lại (spec Edge Case #1)
  | `java/com/example/saa/presentation/ui/login/LoginViewModel.kt`

**Checkpoint**: Build thành công; DataStore fallback + ViewModel guard sẵn sàng

---

## Phase 2: User Story 1 — Language Dropdown UI (Priority: P1) 🎯

**Goal**: Thay thế `ModalBottomSheet` bằng `DropdownMenu` anchor góc trên phải — đúng Figma `kind: "dropdown"`.

**Independent Test**: Tap Language Selector → `DropdownMenu` xuất hiện với VN (highlighted) + EN → chọn EN → dropdown đóng, trigger cập nhật cờ + code → không có JA option.

### String Resources (US1)

- [x] T003 [P] [US1] Thêm string keys accessibility vào `values/strings.xml` (VN) + `values-en/strings.xml` (EN):
  - `language_selector_description` = `"Chọn ngôn ngữ, hiện tại: %s"` / `"Select language, current: %s"`
  - `language_option_vn` = `"Tiếng Việt"` / `"Vietnamese"`
  - `language_option_en` = `"Tiếng Anh"` / `"English"`
  | `res/values/strings.xml` + `res/values-en/strings.xml`

### Component (US1)

- [x] T004 [US1] Tạo `LanguageDropdownMenu.kt` — composable mới thay thế `LanguageSelectorBottomSheet`:

  ```kotlin
  private data class LanguageOption(val code: String, val flag: String, val contentDescRes: Int)
  private val SUPPORTED_LANGUAGES = listOf(
      LanguageOption("VN", "🇻🇳", R.string.language_option_vn),
      LanguageOption("EN", "🇬🇧", R.string.language_option_en),
  )
  ```

  - `DropdownMenu(expanded, onDismissRequest = onDismiss)` — FR-005: click outside đóng tự động
  - Mỗi `DropdownMenuItem`: flag emoji + Text(code, white) + `Modifier.semantics { contentDescription = stringResource(lang.contentDescRes) }`
  - **Selected state**: `DropdownMenuItem` đang active thêm `Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.20f))` — FR-002
  - **Pressed state**: ripple tự động từ Material3 — không cần custom code
  - Signature: `fun LanguageDropdownMenu(expanded: Boolean, selectedLanguage: String, onLanguageSelected: (String) -> Unit, onDismiss: () -> Unit)`
  | `java/com/example/saa/presentation/ui/login/components/LanguageDropdownMenu.kt`
  > Depends on: T001, T003

### LoginScreen Integration (US1)

- [x] T005 [US1] Cập nhật `LoginScreen.kt` — 3 thay đổi cấu trúc:

  **1. Xóa JA khỏi `flagMap`** (dòng trong header row):
  ```kotlin
  // Trước: mapOf("VN" to "🇻🇳", "EN" to "🇬🇧", "JA" to "🇯🇵")
  // Sau:
  val flagMap = mapOf("VN" to "🇻🇳", "EN" to "🇬🇧")
  ```

  **2. Wrap trigger Row trong `Box` + thêm `LanguageDropdownMenu`** (thay thế trigger Row hiện tại):
  ```kotlin
  Box {
      Row(
          modifier = Modifier
              .size(width = 90.dp, height = 32.dp)
              .clip(RoundedCornerShape(4.dp))
              .clickable { viewModel.showLanguageSelector() }
              .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
              .semantics {
                  contentDescription = context.getString(
                      R.string.language_selector_description,
                      uiState.selectedLanguage
                  )
              },
          // ... nội dung giữ nguyên
      ) { /* giữ nguyên Text + Icon */ }
      LanguageDropdownMenu(
          expanded = uiState.showLanguageSelector,
          selectedLanguage = uiState.selectedLanguage,
          onLanguageSelected = { viewModel.setLanguage(it) },
          onDismiss = { viewModel.dismissLanguageSelector() },
      )
  }
  ```

  **3. Xóa root-level overlay block** ở cuối root `Box`:
  ```kotlin
  // XÓA toàn bộ block này:
  // if (uiState.showLanguageSelector) {
  //     LanguageSelectorBottomSheet(...)
  // }
  ```

  Cập nhật import: thêm `LanguageDropdownMenu`, xóa `LanguageSelectorBottomSheet`
  | `java/com/example/saa/presentation/ui/login/LoginScreen.kt`
  > Depends on: T004

- [x] T006 [US1] Xóa `LanguageSelectorBottomSheet.kt` — không còn được import hay dùng sau T005
  | `java/com/example/saa/presentation/ui/login/components/LanguageSelectorBottomSheet.kt`
  > Depends on: T005

**Checkpoint**: App build thành công; Tap trigger → `DropdownMenu` popup đúng vị trí; VN option highlighted; chọn EN → trigger cập nhật; không còn JA option; không còn `ModalBottomSheet`

---

## Phase 3: Testing

**Purpose**: Unit tests cho 2 hardening changes trong Phase 1 + optional Compose UI test.

- [x] T007 [P] Unit test `LanguagePreferenceDataSource.getLanguage()` — DataStore throw `IOException` → `catch` block emit `emptyPreferences()` → `.map` trả về `"VN"` (fallback) | `test/java/com/example/saa/data/local/preference/LanguagePreferenceDataSourceTest.kt`
  > Depends on: T001

- [x] T008 [P] Unit test `LoginViewModel.setLanguage()` — rapid-tap guard: gọi `setLanguage("EN")` khi `uiState.selectedLanguage` đã là `"EN"` → không launch coroutine, `LanguageRepository.setLanguage()` không bị gọi | `test/java/com/example/saa/presentation/ui/login/LoginViewModelLanguageTest.kt`
  > Depends on: T002

- [ ] T009 [P] (Optional) Composable test `LoginScreen` — render với `showLanguageSelector = true` → `DropdownMenu` visible; `LanguageSelectorBottomSheet` không còn trong tree | `androidTest/java/com/example/saa/presentation/ui/login/LoginScreenLanguageTest.kt`
  > Depends on: T005

**Checkpoint**: T007 + T008 pass; TR-006 (DataStore fallback) + Edge Case #1 (rapid tap) được verify tự động

---

## Dependency Map

```
T001 (DataSource fallback)  ──────────────────────────────┐
T002 [P] (ViewModel guard)  ──────────────────────────┐   │
T003 [P] (string resources) ──────────────────────┐   │   │
                                                   ↓   ↓   ↓
                                             T004 (LanguageDropdownMenu.kt)
                                                   │
                                                   ↓
                                             T005 (LoginScreen.kt update)
                                                   │
                                                   ↓
                                             T006 (Delete BottomSheet.kt)

T001 → T007 (unit test fallback)
T002 → T008 (unit test guard)
T005 → T009 (optional composable test)
```

### Parallel Execution Strategy

```
[Thread A]  T001 → T004 → T005 → T006
[Thread B]  T002
[Thread C]  T003
            (T003 và T002 có thể merge vào T001 nếu 1 dev làm một mình)
```

---

## Acceptance Criteria Traceability

| Task | Spec AC | Figma Item |
|------|---------|------------|
| T001 | Edge Case: DataStore read failure → fallback "VN" | — |
| T002 | Edge Case: Rapid tap → chỉ lần chọn cuối có hiệu lực | — |
| T004 | FR-002 (selected highlight), FR-004 (đóng sau chọn), FR-005 (click outside đóng), US1 AC1–AC6 | A, A.1, A.2 |
| T004 | A.1 selected state: nền highlight; A.2 default state | A.1, A.2 |
| T005 | FR-003 (cập nhật UI ngay), FR-007 (default VN); trigger `contentDescription` | A |
| T006 | TR-001 (không phải NavDestination; không dùng BottomSheet) | — |
| T007 | TR-006 (DataStore I/O error fallback không crash) | — |
| T008 | Edge Case: chọn option đang active → DataStore không ghi | — |
