# Tasks: [iOS] Login Screen

**Screen ID**: `8HGlvYGJWq`
**Frame**: `6885:8963 — [iOS] Login`
**Prerequisites**: `ios_login_plan.md` ✅ | `ios_login.md` (spec) ✅ | `ios_login_design_style.md` ✅ | `ios_login.md` (testcases) ✅

---

## Task Format

```
- [ ] T### [P?] [US?] Description | file/path.kt
```

- **[P]**: Có thể chạy song song (file khác nhau, không có dependency)
- **[US]**: Thuộc User Story nào (US1, US2, US3)
- **|**: File path bị ảnh hưởng bởi task này (relative từ `android/`)

---

## User Stories

| # | Story | Priority | Mô tả |
|---|-------|----------|--------|
| US1 | Google Sign-In | 🔴 P1 — MVP | Sunner bấm nút → Google OAuth → Supabase Auth → navigate Home / Access Denied |
| US2 | Language Selector | 🟡 P2 | Tap flag icon → overlay VN/EN/JA → lưu DataStore → áp dụng ngay |
| US3 | UI / Localization | 🟢 P3 | Màn hình đúng design tokens Figma + string resources 3 ngôn ngữ |

---

## Phase 1: Setup — Project Foundation

**Purpose**: Thiết lập toàn bộ hạ tầng trước khi implement feature. Tất cả task dưới đây là **blocking prerequisites**.

- [x] T001 Thêm tất cả dependency mới vào `libs.versions.toml` (Hilt, KSP, Supabase-kt BOM, Ktor, Credential Manager, DataStore, Navigation Compose, Lifecycle, Google Fonts) | `gradle/libs.versions.toml`
- [x] T002 Cập nhật `app/build.gradle.kts`: thêm KSP + Hilt plugin, khai báo tất cả dependencies từ T001 | `app/build.gradle.kts`
- [x] T003 Tạo `SaaApplication.kt` — `@HiltAndroidApp`, `Timber.plant(Timber.DebugTree())` trong `onCreate` | `app/src/main/java/com/example/saa/SaaApplication.kt`
- [x] T004 Cập nhật `AndroidManifest.xml`: thêm `android:name=".SaaApplication"`, `INTERNET` permission, `<queries>` block cho Credential Manager (Google Play Services / API 30+) | `app/src/main/AndroidManifest.xml`
- [x] T005 Thêm `SUPABASE_URL` + `SUPABASE_ANON_KEY` vào `local.properties`; đọc qua `BuildConfig` field trong `build.gradle.kts` | `local.properties` + `app/build.gradle.kts`
- [x] T006 [P] Tạo `SupabaseModule.kt` — cung cấp `SupabaseClient @Singleton` từ BuildConfig | `app/src/main/java/com/example/saa/di/SupabaseModule.kt`
- [x] T007 [P] Tạo `NavRoutes.kt` — `sealed class NavRoutes` với `Login`, `Home`, `AccessDenied` | `app/src/main/java/com/example/saa/NavRoutes.kt`

**Checkpoint**: Build thành công, Hilt compiles, NavRoutes có thể được import

---

## Phase 2: Foundation — Data & Domain Layers

**Purpose**: Tầng Data + Domain là blocking prerequisite cho tất cả User Stories.

⚠️ **CRITICAL**: Không bắt đầu Phase 3–5 cho đến khi Phase 2 complete.

### Data Layer

- [x] T008 [P] Tạo `UserDto.kt` — `@Serializable data class` map từ Supabase `User`; thêm `fun UserDto.toDomain(): User` mapper extension | `app/src/main/java/com/example/saa/data/remote/dto/UserDto.kt`
- [x] T009 [P] Tạo `SupabaseAuthDataSource.kt` — inject `SupabaseClient`; method `signInWithGoogle(idToken)`, `getCurrentSession()`, `signOut()` — trả về `Result<UserDto>` | `app/src/main/java/com/example/saa/data/remote/source/SupabaseAuthDataSource.kt`
- [x] T010 [P] Tạo `LanguagePreferenceDataSource.kt` — inject `DataStore<Preferences>`; `getLanguage(): Flow<String>` (default `"VN"`); `setLanguage(code)` | `app/src/main/java/com/example/saa/data/local/preference/LanguagePreferenceDataSource.kt`

### Domain Layer

- [x] T011 [P] Tạo `User.kt` domain model — non-nullable fields (id, email, name) sau khi đã map từ DTO | `app/src/main/java/com/example/saa/domain/model/User.kt`
- [x] T012 [P] Tạo `AccessDeniedException.kt` — `class AccessDeniedException(message: String = "Account domain not allowed") : Exception(message)` | `app/src/main/java/com/example/saa/domain/exception/AccessDeniedException.kt`
- [x] T013 [P] Tạo `AuthRepository.kt` interface — `loginWithGoogle(idToken)`, `getCurrentUser()`, `logout()` — Auth only, không có language method | `app/src/main/java/com/example/saa/domain/repository/AuthRepository.kt`
- [x] T014 [P] Tạo `LanguageRepository.kt` interface — `getLanguage(): Flow<String>`, `setLanguage(code)` | `app/src/main/java/com/example/saa/domain/repository/LanguageRepository.kt`
- [x] T015 Tạo `LoginWithGoogleUseCase.kt` — **không** import Android/Credential Manager type; nhận `idToken: String`; validate `@sun-asterisk.com` domain; gọi `AuthRepository`; return `Result<User>` | `app/src/main/java/com/example/saa/domain/usecase/LoginWithGoogleUseCase.kt`
  > ⚠️ Depends on: T011, T012, T013

### Repository Impl & DI

- [x] T016 Tạo `AuthRepositoryImpl.kt` — implement `AuthRepository`, inject `SupabaseAuthDataSource`, map DTO → domain | `app/src/main/java/com/example/saa/data/repository/AuthRepositoryImpl.kt`
  > Depends on: T008, T009, T011, T013
- [x] T017 Tạo `LanguageRepositoryImpl.kt` — implement `LanguageRepository`, inject `LanguagePreferenceDataSource` | `app/src/main/java/com/example/saa/data/repository/LanguageRepositoryImpl.kt`
  > Depends on: T010, T014
- [x] T018 Tạo `AppModule.kt` — provide `DataStore<Preferences> @Singleton`; bind `AuthRepository → AuthRepositoryImpl`; bind `LanguageRepository → LanguageRepositoryImpl` | `app/src/main/java/com/example/saa/di/AppModule.kt`
  > Depends on: T016, T017

**Checkpoint**: Unit tests cho UseCase có thể chạy (mock repository), build xanh

---

## Phase 3: User Story 1 — Google Sign-In (Priority: P1) 🎯 MVP

**Goal**: Sunner bấm "LOGIN With Google" → Google Credential Manager → Supabase Auth → navigate to Home. Non-Sun* domain → navigate to Access Denied.

**Independent Test**: TC_LOGIN_FUN_009 (Google popup xuất hiện), TC_LOGIN_FUN_010 (login success → Home), TC_LOGIN_FUN_011 (access denied flow)

### Presentation — US1

- [x] T019 [US1] Tạo `LoginUiState.kt` — `data class` với `isLoading`, `selectedLanguage`, `showLanguageSelector`, `error`, `isLoginSuccess`, `isAccessDenied` | `app/src/main/java/com/example/saa/presentation/ui/login/LoginUiState.kt`
  > Depends on: (none — pure data class)
- [x] T020 [US1] Tạo `LoginViewModel.kt` — inject `LoginWithGoogleUseCase` + `LanguageRepository`; implement `loginWithGoogle(idToken)`, `handleCredentialError(e)`, `consumeError()`, `consumeNavigationEvent()`, `showLanguageSelector()`, `dismissLanguageSelector()`, `setLanguage(code)` | `app/src/main/java/com/example/saa/presentation/ui/login/LoginViewModel.kt`
  > Depends on: T015, T017, T019
- [x] T021 [US1] Tạo `LoginScreen.kt` — scaffold cơ bản: Scaffold + SnackbarHost + KeyVisual background; collect `uiState` via `collectAsStateWithLifecycle()` | `app/src/main/java/com/example/saa/presentation/ui/login/LoginScreen.kt`
  > Depends on: T019, T020
- [x] T022 [US1] Thêm Google Sign-In button với Credential Manager logic vào `LoginScreen`:
  - `val credentialManager = remember { CredentialManager.create(context) }`
  - `GetGoogleIdOption` + `GetCredentialRequest` khởi tạo ngoài `launch`
  - `scope.launch { runCatching { ... }.onFailure { viewModel.handleCredentialError(it) } }`
  - Safe cast `as? GoogleIdTokenCredential`
  - Button disabled + `CircularProgressIndicator` inline khi `isLoading = true`
  | `app/src/main/java/com/example/saa/presentation/ui/login/LoginScreen.kt`
  > Depends on: T021
- [x] T023 [US1] Thêm navigation side-effects vào `LoginScreen`:
  - `LaunchedEffect(uiState.isLoginSuccess)` → `navController.navigate(NavRoutes.Home)` → `viewModel.consumeNavigationEvent()`
  - `LaunchedEffect(uiState.isAccessDenied)` → `navController.navigate(NavRoutes.AccessDenied)` → `viewModel.consumeNavigationEvent()`
  - `LaunchedEffect(uiState.error)` → `snackbarHostState.showSnackbar(...)` → `viewModel.consumeError()`
  | `app/src/main/java/com/example/saa/presentation/ui/login/LoginScreen.kt`
  > Depends on: T022
- [x] T024 [US1] Tạo `SaaNavHost.kt` — single `NavHost`; `startDestination = NavRoutes.Login`; đăng ký `composable(NavRoutes.Login)` | `app/src/main/java/com/example/saa/SaaNavHost.kt`
  > Depends on: T007, T023
- [x] T025 [US1] Cập nhật `MainActivity.kt` — xoá Hello World, thêm `@AndroidEntryPoint`, gọi `SaaNavHost()` | `app/src/main/java/com/example/saa/MainActivity.kt`
  > Depends on: T024

**Checkpoint**: App chạy được trên máy thật, bấm nút mở Google Sign-In popup, login thành công navigate sang màn hình placeholder Home

---

## Phase 4: User Story 2 — Language Selector (Priority: P2)

**Goal**: Tap flag icon (VN/EN/JA) → `ModalBottomSheet` overlay xuất hiện in-screen → chọn ngôn ngữ → overlay đóng → UI text cập nhật → lưu vào DataStore.

**Independent Test**: TC_LOGIN_FUN_002 (overlay mở), TC_LOGIN_FUN_003 (chọn EN), TC_LOGIN_FUN_026 (persist sau restart)

### Presentation — US2

- [x] T026 [P] [US2] Tạo `LanguageSelectorBottomSheet.kt` — `ModalBottomSheet` Material3; list item VN/EN/JA với flag + tên; callback `onLanguageSelected(code: String)` và `onDismiss` | `app/src/main/java/com/example/saa/presentation/ui/login/components/LanguageSelectorBottomSheet.kt`
  > Depends on: T019
- [x] T027 [US2] Thêm language selector trigger vào Header của `LoginScreen`:
  - Flag icon component hiển thị `uiState.selectedLanguage`
  - `onClick = { viewModel.showLanguageSelector() }`
  - `if (uiState.showLanguageSelector) LanguageSelectorBottomSheet(onLanguageSelected = { viewModel.setLanguage(it) }, onDismiss = { viewModel.dismissLanguageSelector() })`
  | `app/src/main/java/com/example/saa/presentation/ui/login/LoginScreen.kt`
  > Depends on: T026, T023

**Checkpoint**: Tap flag → overlay mở; chọn EN → overlay đóng, language cập nhật; restart app → language giữ nguyên (TC_LOGIN_FUN_026)

---

## Phase 5: User Story 3 — Design Tokens & Localization (Priority: P3)

**Goal**: Màn hình khớp pixel-perfect với Figma design style: Montserrat font, màu sắc `#00101A`/`#FFEA9E`, string resources VN/EN/JA.

### Design Token Integration

- [x] T028 [P] [US3] Quyết định approach Montserrat font (Option A: Google Fonts Compose hoặc Option B: bundle TTF), thêm dependency tương ứng vào `libs.versions.toml` | `gradle/libs.versions.toml`
- [x] T029 [P] [US3] Thêm màu sắc design tokens vào `Color.kt`: `Background = Color(0xFF00101A)`, `ButtonPrimaryBg = Color(0xFFFFEA9E)`, `TextOnDark = White`, `TextOnButton = Color(0xFF00101A)` | `app/src/main/java/com/example/saa/ui/theme/Color.kt`
- [x] T030 [US3] Thêm typography tokens vào `Type.kt`: Montserrat 14sp/Light (description), 14sp/Medium (button label), 12sp/Regular (copyright) — sau khi T028 done | `app/src/main/java/com/example/saa/ui/theme/Type.kt`
  > Depends on: T028
- [x] T031 [P] [US3] Thêm `loginButtonShape = RoundedCornerShape(4.dp)` vào `Shape.kt` (hoặc `Theme.kt`) | `app/src/main/java/com/example/saa/ui/theme/Theme.kt`
- [x] T032 [US3] Áp dụng tokens vào `LoginScreen.kt` — background `#00101A`, button bg `#FFEA9E`, text colors, button radius 4dp, Montserrat typography; remove bất kỳ hard-coded value nào | `app/src/main/java/com/example/saa/presentation/ui/login/LoginScreen.kt`
  > Depends on: T029, T030, T031
- [ ] T033 [P] [US3] Verify asset `MM_MEDIA_Keyvisual BG` có trong `res/drawable/`; thêm nếu thiếu | `app/src/main/res/drawable/`
- [ ] T034 [P] [US3] Verify asset `ROOT FURTHER` image có trong `res/drawable/`; thêm nếu thiếu | `app/src/main/res/drawable/`

### Localization

- [x] T035 [P] [US3] Tạo `strings.xml` (VN — default) với keys: `login_description`, `login_button_label` (`"LOGIN With Google"` — trim trailing space từ Figma), `copyright_text`, `language_selector_label` | `app/src/main/res/values/strings.xml`
- [x] T036 [P] [US3] Tạo `strings.xml` (EN) | `app/src/main/res/values-en/strings.xml`
- [x] T037 [P] [US3] Tạo `strings.xml` (JA — placeholder) với comment `<!-- [NEEDS TRANSLATION — yêu cầu từ Localization Team trước khi dev] -->` cho mỗi key | `app/src/main/res/values-ja/strings.xml`
  > ⚠️ BLOCKER: JA strings chưa có từ Localization Team. Commit placeholder và đánh tag `TODO(localization)`
- [ ] T038 [US3] Quyết định + implement runtime language switching (Option A: `CompositionLocalProvider` + custom `LocalAppLanguage`, hoặc Option B: `AppCompatDelegate.setApplicationLocales()`) | `app/src/main/java/com/example/saa/`
  > Discuss with team trước khi implement

**Checkpoint**: Màn hình khớp Figma design; strings hiển thị đúng VN/EN (JA dùng placeholder); không có hard-coded color/text/size

---

## Phase 6: Testing

**Purpose**: Unit tests + Compose tests cho 37 TCs trong `ios_login.md`

- [ ] T039 [P] Unit test `LoginWithGoogleUseCase` — success path Sun* domain → `Result.success(User)` | `app/src/test/java/com/example/saa/domain/usecase/LoginWithGoogleUseCaseTest.kt`
- [ ] T040 [P] Unit test `LoginWithGoogleUseCase` — non-Sun* domain → `Result.failure(AccessDeniedException)` | `app/src/test/java/com/example/saa/domain/usecase/LoginWithGoogleUseCaseTest.kt`
- [ ] T041 [P] Unit test `LoginWithGoogleUseCase` — network error → `Result.failure(...)`, error message không lộ token/PII | `app/src/test/java/com/example/saa/domain/usecase/LoginWithGoogleUseCaseTest.kt`
- [ ] T042 [P] Unit test `LoginViewModel` — `loginWithGoogle()` success → `isLoginSuccess = true`, `isLoading` reset | `app/src/test/java/com/example/saa/presentation/ui/login/LoginViewModelTest.kt`
- [ ] T043 [P] Unit test `LoginViewModel` — `loginWithGoogle()` access denied → `isAccessDenied = true` | `app/src/test/java/com/example/saa/presentation/ui/login/LoginViewModelTest.kt`
- [ ] T044 [P] Unit test `LoginViewModel` — `loginWithGoogle()` error → `error != null` | `app/src/test/java/com/example/saa/presentation/ui/login/LoginViewModelTest.kt`
- [ ] T045 [P] Unit test `LoginViewModel` — double-call guard: gọi khi `isLoading = true` → second call bị ignore | `app/src/test/java/com/example/saa/presentation/ui/login/LoginViewModelTest.kt`
- [ ] T046 [P] Unit test `LoginViewModel` — `setLanguage(code)` → `uiState.selectedLanguage` cập nhật | `app/src/test/java/com/example/saa/presentation/ui/login/LoginViewModelTest.kt`
- [ ] T047 [P] Unit test `LanguagePreferenceDataSource` — `setLanguage("EN")` → `getLanguage()` trả về `"EN"` (DataStore persistence) | `app/src/test/java/com/example/saa/data/local/preference/LanguagePreferenceDataSourceTest.kt`
- [ ] T048 (Optional) Composable test `LoginScreen` — render với `isLoading = true` → button disabled + `CircularProgressIndicator` visible | `app/src/androidTest/java/com/example/saa/presentation/ui/login/LoginScreenTest.kt`
- [ ] T049 (Optional) Composable test `LoginScreen` — render với `showLanguageSelector = true` → `LanguageSelectorBottomSheet` visible | `app/src/androidTest/java/com/example/saa/presentation/ui/login/LoginScreenTest.kt`

**Checkpoint**: Tất cả unit tests pass, ≥ 80% coverage cho UseCase + ViewModel

---

## Dependency Map

```
T001 → T002 → T003 → T004
              T003 → T004
T005 → T006 (SupabaseModule)
T007 (NavRoutes — no deps)
T008 [P] T009 [P] T010 [P]   ← Data Layer (parallel)
T011 [P] T012 [P] T013 [P] T014 [P]   ← Domain Layer (parallel)
T015 (UseCase) ← T011, T012, T013
T016 (AuthRepoImpl) ← T008, T009, T011, T013
T017 (LangRepoImpl) ← T010, T014
T018 (AppModule) ← T016, T017

T019 (UiState — no deps)
T020 (ViewModel) ← T015, T017, T019
T021 (LoginScreen skeleton) ← T019, T020
T022 (Credential Manager) ← T021
T023 (Navigation effects) ← T022
T024 (NavHost) ← T007, T023
T025 (MainActivity) ← T024

T026 [P] (LanguageSelectorBS) ← T019
T027 (Language trigger in screen) ← T026, T023

T028 [P] T029 [P] T031 [P] T033 [P] T034 [P]   ← Design tokens (parallel)
T030 ← T028
T032 ← T029, T030, T031
T035 [P] T036 [P] T037 [P]   ← Strings (parallel)
T038 ← T035, T036, T037

T039–T049 ← Phase 2–4 complete
```

---

## Blockers Summary

| # | Blocker | Impact | Owner |
|---|---------|--------|-------|
| B1 | JA localization strings | T037 placeholder only — T038 blocked | Localization Team |
| B2 | Google OAuth **Web Client ID** (Google Cloud Console) | T022 blocked — không có key thì Credential Manager thất bại | DevOps / PM |
| B3 | Supabase project URL + Anon Key | T005 + T006 blocked | DevOps |
| B4 | Asset files: KeyVisual BG, ROOT FURTHER image, Google icon | T033, T034 blocked | Designer |
| B5 | Access Denied screen spec | T023 navigate to AccessDenied chỉ dùng placeholder route | PM / Designer |

---

## Estimated Effort

| Phase | Tasks | Estimate |
|-------|-------|----------|
| Phase 1 — Setup | T001–T007 | 2–3h |
| Phase 2 — Data + Domain | T008–T018 | 3–4h |
| Phase 3 — US1 Login | T019–T025 | 4–6h |
| Phase 4 — US2 Language | T026–T027 | 1–2h |
| Phase 5 — US3 UI/L10n | T028–T038 | 2–3h |
| Phase 6 — Testing | T039–T049 | 3–4h |
| **Total** | **49 tasks** | **~15–22h** |

> Thời gian chưa bao gồm thời gian chờ blockers (B1–B5).
