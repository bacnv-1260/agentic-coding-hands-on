# Hướng Dẫn Android — Jetpack Compose + Supabase

> File này là nguồn dữ liệu duy nhất cho các quy tắc code Android.
> Tất cả agent phải đọc và tuân theo file này khi tạo hoặc chỉnh sửa code Android.

---

## Cấu Trúc Thư Mục

```
android/app/src/main/java/com/example/saa/
  data/
    remote/
      dto/           # DTO response từ Supabase (field nullable, xem §4)
      source/        # Các class SupabaseDataSource
    local/
      db/            # Room DB + DAO (nếu cần cache offline)
    repository/      # Triển khai cụ thể repository
  domain/
    model/           # Domain entity (non-nullable sau khi mapping)
    repository/      # Interface repository
    usecase/         # Mỗi file = một use-case
  presentation/
    ui/
      <feature>/
        <Feature>Screen.kt
        <Feature>ViewModel.kt
        <Feature>UiState.kt
    theme/
      Color.kt
      Theme.kt
      Type.kt
      Shape.kt
  di/
    AppModule.kt     # Hilt module
    SupabaseModule.kt
  NavRoutes.kt
  MainActivity.kt
```

---

## Quy Tắc ViewModel

```kotlin
// UiState: luôn là data class với error nullable
data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
)

// ViewModel: expose StateFlow, xử lý sự kiện qua hàm
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            loginUseCase(email, password)
                .onSuccess { _uiState.update { it.copy(isLoading = false, isSuccess = true) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }
}
```

---

## Quy Tắc Composable Screen

```kotlin
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Phản ứng với sự kiện một lần qua LaunchedEffect
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onLoginSuccess()
    }

    LoginContent(
        uiState = uiState,
        onLoginClick = viewModel::login,
    )
}

// Tách composable stateless riêng để dùng cho preview
@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onLoginClick: (email: String, password: String) -> Unit,
) { /* ... */ }
```

---

## Sử Dụng Material3

- Luôn dùng `MaterialTheme.colorScheme.*`, `MaterialTheme.typography.*`, `MaterialTheme.shapes.*`.
- Không hard-code màu hex. Không dùng giá trị `sp`/`dp` thô cho text — dùng style từ `MaterialTheme.typography`.
- Dùng `Scaffold` với `TopAppBar` cho màn hình có app bar.
- Thông báo lỗi: hiển thị qua `Snackbar` hoặc `Text(color = MaterialTheme.colorScheme.error)` inline.
- Trạng thái loading: `CircularProgressIndicator` căn giữa màn hình hoặc overlay với surface bán trong suốt.

---

## Supabase DTO — Kiểu Nullable (BẮT BUỘC)

Mọi DTO ánh xạ từ bảng Supabase **phải** khai báo tất cả field là nullable (ngoại trừ primary key).

```kotlin
// ✅ DTO đúng
@Serializable
data class UserProfileDto(
    val id: String,               // PK — non-nullable
    @SerialName("display_name")
    val displayName: String?,
    @SerialName("avatar_url")
    val avatarUrl: String?,
    val bio: String?,
    @SerialName("created_at")
    val createdAt: String?,
)

// Mapper sang domain model (áp dụng giá trị mặc định / kiểm tra tại đây)
fun UserProfileDto.toDomain() = UserProfile(
    id = id,
    displayName = displayName ?: "Ẩn danh",
    avatarUrl = avatarUrl,
    bio = bio.orEmpty(),
    createdAt = createdAt ?: "",
)
```

---

## Điều Hướng (Navigation)

```kotlin
// NavRoutes.kt
sealed class NavRoute(val route: String) {
    data object Login : NavRoute("login")
    data object Home : NavRoute("home")
    data class Profile(val userId: String) : NavRoute("profile/{userId}") {
        fun withArgs() = "profile/$userId"
    }
}

// NavHost
NavHost(navController = navController, startDestination = NavRoute.Login.route) {
    composable(NavRoute.Login.route) { LoginScreen(onLoginSuccess = { navController.navigate(NavRoute.Home.route) }) }
    composable(NavRoute.Home.route) { HomeScreen() }
    composable(NavRoute.Profile("{userId}").route) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
        ProfileScreen(userId = userId)
    }
}
```

---

## Pattern Xử Lý Lỗi

```kotlin
// Kiểu Result cho domain
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val cause: Throwable? = null) : Result<Nothing>()
}

// Repository bọc lời gọi Supabase
override suspend fun getUserProfile(userId: String): Result<UserProfile> =
    runCatching {
        supabase.from("user_profiles")
            .select { filter { eq("id", userId) } }
            .decodeSingle<UserProfileDto>()
            .toDomain()
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { e -> Result.Error(e.message ?: "Lỗi không xác định", e) },
    )
```

---

## Quy Tắc Bảo Mật

1. `SUPABASE_URL` và `SUPABASE_ANON_KEY` phải đọc từ `local.properties` và expose qua `BuildConfig` — không được commit lên VCS.
2. Dùng `EncryptedSharedPreferences` (Jetpack Security) để lưu session token của Supabase.
3. Bật R8/ProGuard ở bản release (`isMinifyEnabled = true`).
4. Không log credential, JWT token hoặc PII của user qua `Timber` hoặc `Log`.
5. Validate mọi trường input của user trước khi gửi lên Supabase (độ dài, định dạng, ký tự được phép).
6. Kiểm tra loại file và kích thước trước khi upload lên Supabase Storage.

---

## Dependency Injection (Hilt)

```kotlin
// SupabaseModule.kt
@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(
        @ApplicationContext context: Context,
    ): SupabaseClient = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY,
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
        install(Realtime)
    }
}
```

---

## Coroutines & Threading

- Tất cả suspend function trong repository chạy trên `Dispatchers.IO` (hoặc dùng `withContext(Dispatchers.IO)`).
- ViewModel launch coroutine trên `viewModelScope` (Main dispatcher); chuyển context khi gọi repository.
- Không dùng `runBlocking` trên main thread.
- Dùng `Flow` cho stream dữ liệu (Realtime subscription, danh sách phân trang). Collect trong UI bằng `collectAsStateWithLifecycle()`.

---

## Logging

- Chỉ dùng **Timber**. Khởi tạo với `Timber.plant(Timber.DebugTree())` trong `Application.onCreate()` cho debug; no-op tree cho release.
- Không log: mật khẩu, token, URL đầy đủ chứa key trong query param, email của user.

---

## Những Pattern Bị Cấm (Tổng Hợp Nhanh)

| Bị cấm | Lý do |
|---|---|
| `!!` trong code production | Gây `NullPointerException` |
| Hard-code màu sắc/chuỗi trong Composable | Vi phạm hệ thống token Material Design |
| Logic nghiệp vụ trong `Screen` composable | Phá vỡ tách biệt mối quan tâm |
| Nhiều instance `SupabaseClient` | Rò rỉ tài nguyên + xung đột auth session |
| `SharedPreferences` để lưu token | Không an toàn (không mã hoá) |
| `catch {}` rỗng | Nuốt lỗi không thông báo |
| `runBlocking` trên Main thread | Nguy cơ ANR |
| Field non-nullable trong DTO | Nguy cơ crash khi schema thay đổi |
