# Implementation Plan: [iOS] Language Dropdown

**Screen ID**: `uUvW6Qm1ve`
**Frame**: `6891:15508 — [iOS] Language dropdown`
**Date**: 2026-05-05
**Spec**: `.momorph/specs/uUvW6Qm1ve-iOS-Language-dropdown/spec.md`

---

## Summary

Thay thế `LanguageSelectorBottomSheet` (full-width `ModalBottomSheet`) hiện tại bằng `DropdownMenu` nhỏ gọn anchor tại góc trên phải — đúng với Figma design. Đồng thời:
- Thêm **selected state** highlight cho option đang chọn (hiện tại chỉ đổi màu text, chưa có nền)
- Thêm **validation + DataStore error fallback** trong `LanguagePreferenceDataSource`
- Thêm **content description** cho accessibility (TalkBack)
- Loại bỏ JA option — Figma design items (A.1=VN, A.2=EN) xác nhận chỉ có 2 ngôn ngữ, không có JA

**Scope**: UI overlay + minor ViewModel/DataSource tweak. Không có API call. Không thay đổi `NavRoutes.kt`.

---

## Technical Context

| Property | Value |
|----------|-------|
| Language/Framework | Kotlin / Jetpack Compose + Material3 |
| Primary Dependencies | `DropdownMenu` (Material3), `DataStore<Preferences>` |
| Database | Không có — client-side only |
| Testing | JUnit + Hilt test, optional Compose UI test |
| State Management | `StateFlow<LoginUiState>` trong `LoginViewModel` |
| API | Không có |

---

## Constitution Compliance Check

*GATE: Must pass before implementation can begin*

- [x] Follows project coding conventions (Kotlin, PascalCase, 4-space indent)
- [x] Uses approved libraries: Material3 `DropdownMenu`, `DataStore<Preferences>` đã có trong `libs.versions.toml`
- [x] Adheres to folder structure: component trong `presentation/ui/login/components/`
- [x] Security: không log ngôn ngữ đã chọn; không có PII
- [x] No logic in Composable — mọi state update qua `LoginViewModel`

**Violations: Không có**

---

## Architecture Decisions

### Overlay Pattern: `DropdownMenu` thay cho `ModalBottomSheet`

**Xác nhận từ Figma**: Design item A có `kind: "dropdown"` (không phải `sheet`). Frame image cho thấy popup nhỏ anchor tại góc trên phải — đây là **quyết định đã được xác nhận từ Figma**, không phải team decision.

`DropdownMenu` của Material3 hỗ trợ anchor-based positioning, dismiss on outside click tự động — đúng spec FR-005.

**Alternative bị loại**: Giữ `ModalBottomSheet` — UX khác Figma, chiếm toàn màn hình, không đúng kích thước thiết kế.

### Selected State: `DropdownMenuItem` + `background modifier`

Option đang active dùng `Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.20f))` với shape `RoundedCornerShape(2.dp)` — tương ứng với behavior từ `list_design_items`: "selected có nền khác".

### DataStore Error Handling

`LanguagePreferenceDataSource.getLanguage()` hiện chưa handle `IOException` từ DataStore. Bổ sung `.catch { emit(emptyPreferences()) }` trong Flow để đảm bảo TR-006 (fallback mà không crash).

> **Lưu ý**: `.catch { emit("VN") }` là **sai kiểu** — `dataStore.data` emit `Preferences`, không phải `String`. Phải dùng `emit(emptyPreferences())` rồi để `.map { prefs -> prefs[key] ?: "VN" }` xử lý fallback.

### Rapid Tap Guard

`setLanguage()` trong ViewModel gọi `viewModelScope.launch` — nếu tap nhanh, nhiều coroutine có thể chạy đồng thời. Giải pháp: check `if (code == uiState.value.selectedLanguage) return` trước khi launch để tránh duplicate write.

---

## Project Structure

### Documentation

```text
.momorph/specs/uUvW6Qm1ve-iOS-Language-dropdown/
├── spec.md       ✅ hoàn chỉnh (sau 2 lần review)
├── plan.md       ← file này
└── tasks.md      (bước tiếp theo — momorph.tasks)
```

### Source Code — Files bị ảnh hưởng

```text
android/app/src/main/java/com/example/saa/
├── presentation/
│   └── ui/
│       └── login/
│           ├── components/
│           │   ├── LanguageSelectorBottomSheet.kt   ← XÓA (thay hoàn toàn bằng file mới)
│           │   └── LanguageDropdownMenu.kt           ← TẠO MỚI
│           ├── LoginScreen.kt                        ← Cập nhật import, cấu trúc anchor, xóa JA trong flagMap
│           ├── LoginViewModel.kt                     ← Thêm rapid-tap guard trong setLanguage()
│           └── LoginUiState.kt                       ← Không thay đổi
├── data/
│   └── local/
│       └── preference/
│           └── LanguagePreferenceDataSource.kt       ← Thêm .catch fallback
└── domain/
    └── repository/
        └── LanguageRepository.kt                     ← Không thay đổi (interface đã đủ)
```

**Files KHÔNG thay đổi**: `NavRoutes.kt`, `SaaNavHost.kt`, `AppModule.kt`, `LanguageRepositoryImpl.kt`

---

## Implementation Strategy

### Phase Breakdown

#### Phase 1 — DataSource Hardening (Foundation)
Bổ sung error fallback vào `LanguagePreferenceDataSource.getLanguage()` để đảm bảo TR-006 trước khi làm UI.

#### Phase 2 — ViewModel Rapid-Tap Guard
Thêm guard `if (code == current) return` trong `LoginViewModel.setLanguage()` để tránh duplicate DataStore write (edge case spec).

#### Phase 3 — Tạo `LanguageDropdownMenu.kt` (Core)
Tạo file mới `LanguageDropdownMenu.kt`, xóa `LanguageSelectorBottomSheet.kt`:
- `DropdownMenu` anchor tại Language Selector trigger
- 2 options: VN (cờ + label) + EN (cờ + label) — theo thứ tự Figma
- Selected state highlight cho option đang chọn
- Content descriptions cho TalkBack

#### Phase 4 — Cập nhật `LoginScreen.kt`
Yêu cầu 3 thay đổi cấu trúc:

1. **Xóa JA khỏi `flagMap`**: `mapOf("VN" to "🇻🇳", "EN" to "🇬🇧", "JA" to "🇯🇵")` → chỉ còn `mapOf("VN" to "🇻🇳", "EN" to "🇬🇧")` — Figma chỉ có A.1(VN) + A.2(EN).

2. **Anchor `DropdownMenu` đúng vị trí**: Wrap trigger `Row` (language selector 90×32dp) trong một `Box` để `DropdownMenu` có thể anchor vào đó. `DropdownMenu` trong Compose phải được đặt TRONG cùng `Box` với trigger:
   ```kotlin
   Box {
       // trigger Row — giữ nguyên, thêm contentDescription
       Row(modifier = Modifier.clickable { viewModel.showLanguageSelector() }) { ... }
       // dropdown anchors tự động tại đây
       LanguageDropdownMenu(
           expanded = uiState.showLanguageSelector,
           selectedLanguage = uiState.selectedLanguage,
           onLanguageSelected = { viewModel.setLanguage(it) },
           onDismiss = { viewModel.dismissLanguageSelector() },
       )
   }
   ```

3. **Xóa root-level overlay block**: Loại bỏ `if (uiState.showLanguageSelector) { LanguageSelectorBottomSheet(...) }` ở cuối root `Box` — dropdown đã được render trong trigger `Box` ở bước trên.

#### Phase 5 — Unit Tests (Optional)
Unit test cho `LanguagePreferenceDataSource` fallback + `LoginViewModel.setLanguage()` guard.

### Risk Assessment

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| `DropdownMenu` anchor position lệch trên một số device | Low | Medium | Test trên emulator API 24 + API 36; dùng `Modifier.wrapContentSize(Alignment.TopEnd)` |
| DataStore `.catch` operator không được test đủ | Low | Medium | Viết unit test cho `getLanguage()` với mock DataStore throw IOException |

### Estimated Complexity

- **UI (Phase 3-4)**: Low — chỉ đổi `ModalBottomSheet` → `DropdownMenu`, không thay đổi business logic
- **DataSource (Phase 1-2)**: Low — 1-2 dòng thay đổi mỗi file
- **Testing (Phase 5)**: Low — pure unit test, không cần device

---

## Detailed Component Design

### `LanguageDropdownMenu` (thay thế `LanguageSelectorBottomSheet`)

```kotlin
@Composable
fun LanguageDropdownMenu(
    expanded: Boolean,           // = uiState.showLanguageSelector
    selectedLanguage: String,    // = uiState.selectedLanguage
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,   // FR-005: click outside tự động đóng
    ) {
        SUPPORTED_LANGUAGES.forEach { lang ->
            DropdownMenuItem(
                text = { /* flag icon + language code */ },
                onClick = {
                    onLanguageSelected(lang.code)
                    onDismiss()
                },
                modifier = if (lang.code == selectedLanguage)
                    Modifier.background(/* selected highlight */)
                else Modifier,
                // Accessibility: contentDescription per option
            )
        }
    }
}
```

**Ghi chú**:
- `SUPPORTED_LANGUAGES` là `val` ở top-level của file với **chỉ 2 options** (VN + EN) — xác nhận bởi Figma design items A.1 và A.2. JA **không** được include.
- **Label format**: chỉ hiển thị mã ngôn ngữ (`VN`, `EN`) — **không** dùng format `"VN — Tiếng Việt"` của implementation hiện tại. Spec A.1: `Label: 'VN'`, A.2: `Label: 'EN'`.
- **Hover/interaction state**: A.1 và A.2 spec ghi "hover hiển thị highlight" — `DropdownMenuItem` Material3 xử lý tự động qua ripple effect, không cần custom code.
- Flag icons: Unicode emoji (`🇻🇳`, `🇬🇧`) — không cần asset file
- `DropdownMenu.onDismissRequest` tự xử lý click outside (FR-005) và back button

### `LanguagePreferenceDataSource.getLanguage()` — Bổ sung catch

```kotlin
fun getLanguage(): Flow<String> = dataStore.data
    .catch { emit(emptyPreferences()) }  // TR-006: fallback nếu I/O error
    .map { prefs -> prefs[selectedLanguageKey] ?: "VN" }
```

### `LoginViewModel.setLanguage()` — Bổ sung rapid-tap guard

```kotlin
fun setLanguage(code: String) {
    if (code == _uiState.value.selectedLanguage) return  // edge case: chọn lại option đang active
    viewModelScope.launch {
        languageRepository.setLanguage(code)
    }
    dismissLanguageSelector()
}
```

---

## Integration Testing Strategy

### Test Scope

- [x] **Component interactions**: `LoginScreen` ↔ `LanguageDropdownMenu` ↔ `LoginViewModel.setLanguage()`
- [ ] **External dependencies**: Không có (client-side only)
- [x] **Data layer**: `LanguagePreferenceDataSource` — getLanguage với DataStore error, setLanguage persistence
- [x] **User workflows**: Open dropdown → select option → UI updates → persist → reopen app

### Test Categories

| Category | Applicable? | Key Scenarios |
|----------|-------------|---------------|
| UI ↔ Logic | Yes | Click option → `setLanguage()` gọi → UI cập nhật flag + code |
| App ↔ Data Layer | Yes | `setLanguage("EN")` → DataStore write → `getLanguage()` emit "EN" |
| DataStore error | Yes | `getLanguage()` với DataStore throw IOException → emit "VN" |
| Rapid tap guard | Yes | `setLanguage("EN")` khi selectedLanguage đã là "EN" → không ghi DataStore |

### Test Files

```text
android/app/src/test/java/com/example/saa/
├── data/local/preference/
│   └── LanguagePreferenceDataSourceTest.kt   ← Unit test DataStore fallback
└── presentation/ui/login/
    └── LoginViewModelLanguageTest.kt         ← Unit test setLanguage guard
```

---

## Open Questions / Blockers

*Không còn blocker nào.* B1 và B2 đã được **giải quyết** bởi Figma design items:

| # | Blocker | Status | Resolution |
|---|---------|--------|------------|
| B1 | JA trong dropdown có/không? | ✅ Resolved | Figma items A.1 (VN) + A.2 (EN) — **không có A.3 (JA)**. JA bị loại bỏ. |
| B2 | JA localization strings placeholder | ✅ Resolved | JA không được implement — B2 không còn áp dụng. |

**→ Tất cả phases có thể implement ngay.**

---

## Success Criteria Mapping

| SC | Verifiable How |
|----|----------------|
| SC-001: Dropdown mở/đóng đúng; click outside đóng | Manual test + `DropdownMenu.onDismissRequest` covers this |
| SC-002: Chọn EN → UI text chuyển English ngay | `collectAsStateWithLifecycle` on `selectedLanguage` → recompose |
| SC-003: Thoát/mở lại app → ngôn ngữ khôi phục | Unit test `LanguagePreferenceDataSourceTest` |
| SC-004: Active option có visual state rõ ràng | `DropdownMenuItem` với `background` modifier khi selected |
