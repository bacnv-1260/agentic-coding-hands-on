# Implementation Plan: [iOS] Login Screen

**Screen ID**: `8HGlvYGJWq`
**Figma File Key**: `9ypp4enmFmdK3YAFJLIu6C`
**Frame Node ID**: `6885:8963`
**Date**: 2026-05-05
**Spec**: `.momorph/contexts/screen_specs/ios_login.md`
**Design Style**: `.momorph/contexts/screen_specs/ios_login_design_style.md`
**Test Cases**: `.momorph/contexts/testcases/ios_login.md` (37 TCs)

---

## Summary

Màn hình Login là entry point duy nhất của ứng dụng SAA 2025. Sunner xác thực bằng tài khoản Google (`@sun-asterisk.com`) qua Supabase Auth (Google OAuth). Màn hình hỗ trợ chọn ngôn ngữ (VN / EN / JA) với overlay in-screen, lưu lựa chọn vào `DataStore<Preferences>`.

**Trạng thái codebase hiện tại**: Skeleton — chỉ có `MainActivity.kt` với Hello World. Chưa có Hilt, Supabase-kt, Navigation Compose, DataStore, hay Timber.

---

## Technical Context

| Hạng mục | Giá trị |
|----------|---------|
| Language | Kotlin |
| UI Framework | Jetpack Compose + Material3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Navigation | Jetpack Navigation Compose |
| Auth | Supabase-kt GoTrue + Google Credential Manager |
| Persistence | DataStore Preferences |
| Async | Kotlin Coroutines + Flow |
| Logging | Timber |
| Min SDK | 24 |
| Target SDK | 36 |

---

## Constitution Compliance Check

*GATE: Phải pass trước khi bắt đầu implement*

- [x] MVVM + Clean Architecture 3 tầng
- [x] Không dùng `!!` — chỉ dùng safe call `?.` và `?:`
- [x] Không có logic nghiệp vụ trong Composable
- [x] Chỉ dùng Material3 — không hard-code màu/size
- [x] Key lưu trong `local.properties`, token trong `EncryptedSharedPreferences`
- [x] Một `NavHost` duy nhất, route trong `NavRoutes.kt`
- [x] Xử lý lỗi qua `Result<T>` sealed — không lộ exception ra UI
- [x] Chỉ dùng Timber để log — không log token/PII
- [x] Tất cả dependency version trong `libs.versions.toml`
- [x] RLS trên bảng Supabase (không áp dụng cho Auth)

**Violations (đã sửa sau review)**:

| Violation | Fix Applied |
|-----------|-------------|
| UseCase import Android type (Credential Manager) | Chuyển Credential Manager call lên LoginScreen/ViewModel; UseCase chỉ nhận `idToken` |
| AuthRepository chứa language concern | Tách thành `AuthRepository` + `LanguageRepository` |
| SaaApplication thiếu trong project structure | Đã thêm vào Project Structure và Phase 0.3 |

---

## Architecture Decisions

### Tầng Presentation
- **Screen**: `LoginScreen.kt` — Composable mỏng, chỉ render `UiState` + gọi event lambda
- **ViewModel**: `LoginViewModel.kt` — giữ `StateFlow<LoginUiState>`, xử lý OAuth và language selection
- **UiState**: `LoginUiState.kt` — `data class` với `isLoading`, `selectedLanguage`, `error`
- **Language Overlay**: `LanguageSelectorBottomSheet.kt` — `ModalBottomSheet` in-screen, không phải NavDestination

### Tầng Domain
- **Use Case**: `LoginWithGoogleUseCase.kt` — nhận `idToken: String` (đã lấy từ Credential Manager ở tầng Presentation), validate domain Sun*, gọi `AuthRepository.loginWithGoogle()`
- **Repository Interface**: `AuthRepository` — chỉ Auth operations (interface trong `domain/repository/`)
- **Repository Interface**: `LanguageRepository` — chỉ language preference operations (interface trong `domain/repository/`)
- **Domain Model**: `User.kt` — domain entity sau khi map từ DTO

### Tầng Data
- **Data Source**: `SupabaseAuthDataSource.kt` — gọi Supabase GoTrue
- **Repository Impl**: `AuthRepositoryImpl.kt`
- **DTO**: `UserDto.kt` — nullable fields (trừ id)

### Persistence
- Language preference: `DataStore<Preferences>` — key `selected_language`, default `"VN"`
- Auth session: `SupabaseSessionManager` với `EncryptedSharedPreferences`

### Integration Points
- **Supabase Auth** — Google OAuth via `SupabaseClient.auth.signInWith(Google)`
- **Google Credential Manager** — Android Credential Manager API (thay thế deprecated Google Sign-In)
- **NavHost** — Login là `startDestination` khi chưa có session; navigate to Home sau login thành công
- **AppStore / language** — `LanguageRepository` inject vào `LoginViewModel`, expose `Flow<String>` qua `StateFlow`

### Google Credential Manager Flow
```
LoginScreen (Composable, có Activity context)
  → gọi CredentialManager.getCredential()
  → nhận GoogleIdTokenCredential → lấy idToken: String
  → gọi viewModel.loginWithGoogle(idToken)
  → LoginViewModel → LoginWithGoogleUseCase(idToken)  ← UseCase KHÔNG import Android types
  → AuthRepository.loginWithGoogle(idToken)
```

---

## Project Structure (Files cần tạo mới)

```
android/app/src/main/java/com/example/saa/
│
├── SaaApplication.kt                             # [NEW] @HiltAndroidApp, Timber init
├── NavRoutes.kt                                  # [NEW] Route definitions
├── SaaNavHost.kt                                 # [NEW] Single NavHost
│
├── data/
│   ├── remote/
│   │   ├── dto/
│   │   │   └── UserDto.kt                        # [NEW] Supabase user DTO
│   │   └── source/
│   │       └── SupabaseAuthDataSource.kt         # [NEW] GoTrue calls
│   ├── local/
│   │   └── preference/
│   │       └── LanguagePreferenceDataSource.kt   # [NEW] DataStore wrapper
│   └── repository/
│       ├── AuthRepositoryImpl.kt                 # [NEW]
│       └── LanguageRepositoryImpl.kt             # [NEW]
│
├── domain/
│   ├── model/
│   │   └── User.kt                               # [NEW] Domain model
│   ├── exception/
│   │   └── AccessDeniedException.kt              # [NEW] Domain exception
│   ├── repository/
│   │   ├── AuthRepository.kt                     # [NEW] Interface — Auth only
│   │   └── LanguageRepository.kt                 # [NEW] Interface — Language only
│   └── usecase/
│       └── LoginWithGoogleUseCase.kt             # [NEW] — nhận idToken, KHÔNG import Android types
│
├── presentation/
│   └── ui/
│       └── login/
│           ├── LoginScreen.kt                    # [NEW] Composable screen
│           ├── LoginViewModel.kt                 # [NEW]
│           ├── LoginUiState.kt                   # [NEW]
│           └── components/
│               └── LanguageSelectorBottomSheet.kt # [NEW]
│
└── di/
    ├── AppModule.kt                              # [NEW] DataStore, Timber
    └── SupabaseModule.kt                         # [NEW] SupabaseClient @Singleton
```

```
android/gradle/libs.versions.toml  # [MODIFY] Thêm các dependency mới
android/app/build.gradle.kts       # [MODIFY] Thêm plugins + dependencies
android/app/src/main/AndroidManifest.xml  # [MODIFY] Internet, Credential Manager
android/local.properties           # [MODIFY] SUPABASE_URL, SUPABASE_ANON_KEY
```

---

## Dependencies Cần Thêm

Tất cả version cập nhật vào `libs.versions.toml`:

| Thư viện | Group / Artifact | Version |
|---------|-----------------|---------|
| Hilt Android | `com.google.dagger:hilt-android` | `2.56.1` |
| Hilt Compiler | `com.google.dagger:hilt-compiler` | `2.56.1` |
| KSP Plugin | `com.google.devtools.ksp` | `2.2.10-1.0.29` |
| Hilt Navigation Compose | `androidx.hilt:hilt-navigation-compose` | `1.2.0` |
| Navigation Compose | `androidx.navigation:navigation-compose` | `2.9.0` |
| DataStore Preferences | `androidx.datastore:datastore-preferences` | `1.1.4` |
| Lifecycle ViewModel Compose | `androidx.lifecycle:lifecycle-viewmodel-compose` | `2.10.0` |
| Supabase-kt BOM | `io.github.jan-tennert.supabase:bom` | `3.1.4` |
| Supabase GoTrue (Auth) | `io.github.jan-tennert.supabase:auth-kt` | (từ BOM) |
| ~~Supabase Postgrest~~ | ~~`supabase:postgrest-kt`~~ | ~~(từ BOM)~~ — **loại bỏ**: không cần cho Login, thêm sau |
| Ktor Android Engine | `io.ktor:ktor-client-android` | `3.1.3` |
| Lifecycle Runtime Compose | `androidx.lifecycle:lifecycle-runtime-compose` | `2.10.0` |
| Google Fonts Compose | `androidx.compose.ui:ui-text-google-fonts` | (từ Compose BOM) |
| Timber | `com.jakewharton.timber:timber` | `5.0.1` |
| Google Credential Manager | `androidx.credentials:credentials` | `1.5.0` |
| Google ID (Credential) | `com.google.android.libraries.identity.googleid:googleid` | `1.1.1` |

---

## Implementation Strategy

### Phase 0 — Project Foundation *(Prerequisites)*
> Thiết lập cơ sở hạ tầng trước khi implement feature

- [ ] **0.1** Thêm tất cả dependency vào `libs.versions.toml` + `build.gradle.kts`
- [ ] **0.2** Thêm Hilt plugin vào project-level và app-level build scripts
  - Project-level: `id("com.google.devtools.ksp")` + `id("com.google.dagger.hilt.android")`
  - App-level: apply cả hai plugin; thay `kapt` bằng `ksp` cho Hilt compiler
- [ ] **0.3** Tạo `SaaApplication.kt` với `@HiltAndroidApp`, đăng ký `Timber.plant(Timber.DebugTree())` trong `onCreate`
  - Cập nhật `AndroidManifest.xml`: thêm `android:name=".SaaApplication"` vào `<application>`
- [ ] **0.4** Thêm `SUPABASE_URL` + `SUPABASE_ANON_KEY` vào `local.properties`, đọc qua `BuildConfig`
- [ ] **0.5** Tạo `SupabaseModule.kt` — cung cấp `SupabaseClient` `@Singleton`
- [ ] **0.6** Cập nhật `AndroidManifest.xml`:
  - Thêm `INTERNET` permission
  - Thêm `<queries>` block cho package visibility (Android 11+ / API 30+) — bắt buộc để `CredentialManager` tìm được Google Play Services:
    ```xml
    <queries>
        <package android:name="com.google.android.gms" />
    </queries>
    ```
- [ ] **0.7** Tạo `NavRoutes.kt` với `sealed class` chứa `Login` và `Home` route

---

### Phase 1 — Data Layer
> Tầng Data: DTO, DataSource, Repository Impl

- [ ] **1.1** Tạo `UserDto.kt` — map từ Supabase `User` object (nullable fields trừ `id`)
  - Thêm mapper extension `fun UserDto.toDomain(): User` trong cùng file (hoặc `UserDtoMapper.kt`)
- [ ] **1.2** Tạo `SupabaseAuthDataSource.kt`:
  - `signInWithGoogle(idToken: String): Result<UserDto>`
  - `getCurrentSession(): Result<UserDto?>`
  - `signOut(): Result<Unit>`
- [ ] **1.3** Tạo `LanguagePreferenceDataSource.kt`:
  - Inject `DataStore<Preferences>`
  - `getLanguage(): Flow<String>` (default `"VN"`)
  - `setLanguage(code: String): Unit`
- [ ] **1.4** Tạo `AuthRepositoryImpl.kt` — implement `AuthRepository`, inject `SupabaseAuthDataSource`
- [ ] **1.5** Tạo `LanguageRepositoryImpl.kt` — implement `LanguageRepository`, inject `LanguagePreferenceDataSource`
- [ ] **1.6** Đăng ký bindings trong `AppModule.kt`:
  - `bind AuthRepository → AuthRepositoryImpl`
  - `bind LanguageRepository → LanguageRepositoryImpl`
  - Provide `DataStore<Preferences>` `@Singleton`

---

### Phase 2 — Domain Layer
> Tầng Domain: Model, Interface, Use Case

- [ ] **2.1** Tạo `User.kt` domain model — non-nullable sau khi map từ DTO
- [ ] **2.2a** Tạo `AuthRepository.kt` interface (Auth only — không có language):
  - `loginWithGoogle(idToken: String): Result<User>`
  - `getCurrentUser(): Result<User?>`
  - `logout(): Result<Unit>`
- [ ] **2.2b** Tạo `LanguageRepository.kt` interface (Language only):
  - `getLanguage(): Flow<String>`
  - `setLanguage(code: String)`
- [ ] **2.3** Tạo `LoginWithGoogleUseCase.kt`:
  - **KHÔNG** import bất kỳ Android/Credential Manager type nào
  - Nhận `idToken: String` làm tham số (đã lấy từ tầng Presentation)
  - Gọi `authRepository.loginWithGoogle(idToken)`
  - Validate email domain `@sun-asterisk.com` → `Result.failure(AccessDeniedException)` nếu sai
  - Return `Result<User>`
- [ ] **2.4** Tạo `AccessDeniedException.kt` trong `domain/exception/`:
  ```kotlin
  class AccessDeniedException(
      message: String = "Account domain not allowed"
  ) : Exception(message)
  ```

---

### Phase 3 — Presentation Layer
> Tầng Presentation: UiState, ViewModel, Screen, Components

- [ ] **3.1** Tạo `LoginUiState.kt`:
  ```kotlin
  data class LoginUiState(
      val isLoading: Boolean = false,
      val selectedLanguage: String = "VN",
      val showLanguageSelector: Boolean = false,  // managed by ViewModel
      val error: String? = null,
      val isLoginSuccess: Boolean = false,
      val isAccessDenied: Boolean = false,
  )
  ```

- [ ] **3.2** Tạo `LoginViewModel.kt`:
  - Inject `LoginWithGoogleUseCase`, `LanguageRepository`
    - (`AuthRepository` **không** inject trực tiếp vào ViewModel — UseCase đã wrap nó)
  - `fun loginWithGoogle(idToken: String)` — nhận idToken từ Credential Manager (gọi từ Screen), update `isLoading`, handle `Result`
  - `fun setLanguage(code: String)` — gọi `LanguageRepository.setLanguage()` + update UiState
  - `fun showLanguageSelector()` — `_uiState.update { it.copy(showLanguageSelector = true) }`
  - `fun dismissLanguageSelector()` — `_uiState.update { it.copy(showLanguageSelector = false) }`
  - `fun consumeError()` — clear error (sau khi Snackbar hiển thị)
  - `fun consumeNavigationEvent()` — reset `isLoginSuccess` / `isAccessDenied`
  - `fun handleCredentialError(e: Throwable)` — map lỗi Credential Manager sang `error` trong UiState (không log credential)
  - Collect `LanguageRepository.getLanguage()` Flow vào UiState khi init

- [ ] **3.3** Tạo `LanguageSelectorBottomSheet.kt`:
  - `ModalBottomSheet` (Material3) hiển thị list VN / EN / JA
  - Mỗi item: flag icon + language code + tên đầy đủ
  - Callback `onLanguageSelected(code: String)`

- [ ] **3.4** Tạo `LoginScreen.kt`:
  - Collect `uiState` từ ViewModel via `collectAsStateWithLifecycle()` (`lifecycle-runtime-compose`)
  - Khởi chạy Credential Manager trong `rememberCoroutineScope` + button `onClick` lambda (không gọi trong composition):
    ```kotlin
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val credentialManager = remember { CredentialManager.create(context) }
    // trong Button onClick:
    scope.launch {
        runCatching {
            val result = credentialManager.getCredential(context, request)
            val idToken = (result.credential as? GoogleIdTokenCredential)?.idToken
                ?: error("Unexpected credential type")
            viewModel.loginWithGoogle(idToken)
        }.onFailure { e -> viewModel.handleCredentialError(e) }
    }
    ```
  - `GetGoogleIdOption` + `GetCredentialRequest` khởi tạo bên ngoài `launch` (không suspend)
  - Render: KeyVisual background → Header (Logo + LanguageSelector) → ROOT FURTHER image → Description text → Login button → Footer (Copyright)
  - Button: `CircularProgressIndicator` inline khi `isLoading = true`, `enabled = !uiState.isLoading`
  - Snackbar error: `LaunchedEffect(uiState.error)` → `snackbarHostState.showSnackbar(...)` → `viewModel.consumeError()`
  - Navigate to Home: `LaunchedEffect(uiState.isLoginSuccess)` → `navController.navigate(NavRoutes.Home)` → `viewModel.consumeNavigationEvent()`
  - Navigate to Access Denied: `LaunchedEffect(uiState.isAccessDenied)`
  - Language overlay: `if (uiState.showLanguageSelector) LanguageSelectorBottomSheet(onDismiss = { viewModel.dismissLanguageSelector() }, ...)` — **KHÔNG dùng local remember state**

- [ ] **3.5** Kết nối `LoginScreen` vào `SaaNavHost.kt`
- [ ] **3.6** Cập nhật `MainActivity.kt` — thay Hello World bằng `SaaNavHost`
- [ ] **3.7** Annotate `MainActivity` với `@AndroidEntryPoint` (bắt buộc cho `hiltViewModel()` hoạt động)

---

### Phase 4 — Design Token Integration
> Áp dụng design tokens từ `ios_login_design_style.md`

- [ ] **4.0** Thêm Montserrat font (quyết định approach trước):
  - **Option A** (recommended): Google Fonts Compose — `GoogleFont("Montserrat")` qua `ui-text-google-fonts`
  - **Option B**: Bundle file `res/font/montserrat_light.ttf`, `montserrat_medium.ttf`, `montserrat_regular.ttf`
- [ ] **4.1** Thêm màu sắc vào `Color.kt`:
  - `Background = Color(0xFF00101A)`
  - `ButtonPrimaryBg = Color(0xFFFFEA9E)`
  - `TextOnDark = Color.White`
  - `TextOnButton = Color(0xFF00101A)`
- [ ] **4.2** Cập nhật `Theme.kt` nếu cần dark-only scheme cho LoginScreen
- [ ] **4.3** Thêm `loginButtonShape = RoundedCornerShape(4.dp)` vào `Shape.kt`
- [ ] **4.4** Thêm typography tokens (sau khi 4.0 xong): Montserrat 14sp/300 (description), 14sp/500 (button), 12sp/400 (copyright)
- [ ] **4.5** Verify KeyVisual background asset đã có trong `res/drawable/`; thêm nếu thiếu
- [ ] **4.6** Verify ROOT FURTHER image asset đã có; thêm nếu thiếu

---

### Phase 5 — Localization
> String resources cho VN / EN (JA pending)

- [ ] **5.1** Tạo `res/values/strings.xml` (VN — default)
- [ ] **5.2** Tạo `res/values-en/strings.xml` (EN)
- [ ] **5.3** Tạo `res/values-ja/strings.xml` (JA — placeholder, chờ Localization Team)
- [ ] **5.4** String keys: `login_description`, `login_button_label`, `copyright_text`, `language_selector_label`
- [ ] **5.5a** Quyết định approach runtime language switching (chọn 1):
  - **Option A** (recommended cho Compose): `CompositionLocalProvider` với custom `LocalAppLanguage` — không cần restart Activity, quản lý string thủ công qua `stringResource` với locale override
  - **Option B**: `AppCompatDelegate.setApplicationLocales()` — cần restart Activity khi đổi ngôn ngữ, system handles string resource
- [ ] **5.5b** Implement theo approach đã chọn ở 5.5a

---

### Phase 6 — Testing
> Unit test + UI test theo 37 TCs trong `ios_login.md`

- [ ] **6.1** Unit test `LoginWithGoogleUseCase`:
  - Success path (Sun* domain) → `Result.success(User)`
  - Non-Sun* domain → `Result.failure(AccessDeniedException)`
  - Network error → `Result.failure(NetworkException)`
- [ ] **6.2** Unit test `LoginViewModel`:
  - `loginWithGoogle()` khi success → `isLoginSuccess = true`
  - `loginWithGoogle()` khi access denied → `isAccessDenied = true`
  - `loginWithGoogle()` khi error → `error != null`
  - `setLanguage()` → `selectedLanguage` cập nhật
  - Double-call prevention: `isLoading = true` → second call ignored
- [ ] **6.3** Unit test `LanguagePreferenceDataSource`:
  - `setLanguage()` → `getLanguage()` trả về giá trị mới sau restart (DataStore persistence)
- [ ] **6.4** Composable test (optional) — `LoginScreen` render với mocked `UiState`

---

## Dependency Graph

```
LoginScreen (Composable)
  │  CredentialManager.getCredential() → idToken
  └── LoginViewModel (HiltViewModel, @AndroidEntryPoint on Activity)
        ├── LoginWithGoogleUseCase(idToken)   ← KHÔNG import Android types
        │     └── AuthRepository (interface)
        │           └── AuthRepositoryImpl
        │                 └── SupabaseAuthDataSource → SupabaseClient (@Singleton)
        └── LanguageRepository (interface)
              └── LanguageRepositoryImpl
                    └── LanguagePreferenceDataSource → DataStore<Preferences> (@Singleton)

SaaApplication (@HiltAndroidApp) → Hilt root, Timber init
MainActivity (@AndroidEntryPoint) → SaaNavHost → LoginScreen
```

---

## Open Items / Blockers

| # | Item | Owner | Priority |
|---|------|-------|----------|
| 1 | JA localization strings cho `login_description` và `copyright_text` | Localization Team | 🔴 HIGH — block Phase 5.3 |
| 2 | Google OAuth **Web Client ID** (từ Google Cloud Console) — cần cho `GetGoogleIdOption`; `google-services.json` chỉ cần nếu dùng Firebase | DevOps / PM | 🔴 HIGH — block Phase 3.4 |
| 3 | Confirm Supabase project URL và Anon Key (production vs staging) | DevOps | 🔴 HIGH — block Phase 0.4 |
| 4 | Asset files: KeyVisual BG image, ROOT FURTHER image, Google icon | Designer | 🟡 MEDIUM — block Phase 4.5/4.6 |
| 5 | Confirm Access Denied screen spec (điều hướng tiếp theo từ đó) | PM / Designer | 🟡 MEDIUM — block Phase 3.4 navigation |

---

## Estimated Effort

| Phase | Mô tả | Estimate |
|-------|-------|----------|
| Phase 0 | Project Foundation | 2–3h |
| Phase 1 | Data Layer | 2–3h |
| Phase 2 | Domain Layer | 1–2h |
| Phase 3 | Presentation Layer | 4–6h |
| Phase 4 | Design Token Integration | 1–2h |
| Phase 5 | Localization | 1–2h |
| Phase 6 | Testing | 3–4h |
| **Total** | | **~14–22h** |

> Thời gian chưa tính blockers (JA strings, OAuth Client ID, Supabase credentials).
