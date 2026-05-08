# Hiến Pháp Dự Án — Android + Supabase

## 1. Công Nghệ Sử Dụng

| Tầng | Công nghệ |
|---|---|
| Mobile Client | Android (Kotlin, minSdk 24, targetSdk 36) |
| UI Framework | Jetpack Compose + Material3 |
| Backend-as-a-Service | Supabase (PostgreSQL, Auth, Storage, Realtime) |
| Xử lý bất đồng bộ | Kotlin Coroutines + Flow |
| Dependency Injection | Hilt |
| Điều hướng | Jetpack Navigation Compose |
| HTTP / Supabase SDK | supabase-kt (official Kotlin client) |

---

## 2. Kiến Trúc

### 2.1 Mô hình: MVVM + Clean Architecture (3 tầng)

```
app/
  src/main/java/com/example/saa/
    data/
      remote/         # Nguồn dữ liệu Supabase (DTO, gọi API)
      local/          # Room database (nếu cần cache offline)
      repository/     # Triển khai cụ thể của repository
    domain/
      model/          # Domain model thuần túy (không phụ thuộc framework)
      repository/     # Interface của repository
      usecase/        # Mỗi use-case một file
    presentation/
      ui/
        <feature>/
          <Feature>Screen.kt       # Composable screen (mỏng, uỷ quyền cho ViewModel)
          <Feature>ViewModel.kt    # Giữ UiState, xử lý sự kiện
          <Feature>UiState.kt      # Sealed class / data class cho UI state
      theme/           # Material3 theme, màu sắc, typography, hình dạng
    di/                # Hilt modules
    MainActivity.kt
```

### 2.2 Quy Tắc Bắt Buộc

- **Không đặt logic nghiệp vụ trong Composable.** Màn hình chỉ được gọi các hàm của ViewModel.
- **Không dùng Android types trong ViewModel.** ViewModel không được import `Context`, `Activity` hay các kiểu Compose.
- **Nguồn dữ liệu duy nhất:** UI state chảy từ ViewModel qua `StateFlow<UiState>`.
- **Repository pattern:** Data source được ẩn sau các interface định nghĩa trong `domain/repository/`.
- **Use-case tuỳ chọn với CRUD đơn giản** nhưng bắt buộc khi kết hợp nhiều nguồn dữ liệu hoặc chứa logic nghiệp vụ phức tạp.

---

## 3. Kotlin & Quy Ước Code

- **Chỉ dùng Kotlin.** Không có file Java.
- Thụt lề: **4 spaces**. Độ dài dòng tối đa: **120 ký tự**.
- Đặt tên file: PascalCase cho class (`LoginViewModel.kt`), camelCase cho hàm.
- Mỗi file chỉ chứa một class cấp cao nhất. Tên file phải khớp với tên class.
- Ưu tiên `val` hơn `var`. Chỉ thay đổi state bên trong ViewModel.
- Dùng **`data class`** cho model; dùng `copy()` để cập nhật state.
- Hàm extension được phép nhưng phải đặt trong file có tên rõ ràng (`DateExtensions.kt`).
- Tránh lồng lambda quá 2 cấp — tách ra hàm có tên riêng.
- Không dùng `!!` (non-null assertion). Dùng safe call `?.`, `?:` hoặc `requireNotNull()` kèm thông báo mô tả.

---

## 4. Android / Jetpack Compose

### 4.1 Composable

- Hàm Composable: PascalCase, không có side effect bên trong composition.
- Chỉ nhận **kiểu primitive hoặc stable** làm tham số; truyền lambda cho callback.
- Mỗi màn hình có đúng một root composable nhận `UiState` + event lambda.
- Dùng `remember` / `rememberSaveable` một cách tiết kiệm; ưu tiên state do ViewModel quản lý.
- Gắn annotation preview: `@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)` để kiểm tra dark mode.

### 4.2 Material Design 3

- Chỉ dùng **component Material3** (`androidx.compose.material3.*`).
- Áp dụng `MaterialTheme` của dự án (màu sắc, typography, shape) từ `ui/theme/`. Không bao giờ hard-code màu hoặc font size.
- Tuân theo M3 elevation token (`ElevationTokens`) cho surface và card.
- Tuân thủ UI pattern của platform: `TopAppBar`, `NavigationBar`, `ModalBottomSheet`, `Snackbar` cho thông báo trong app.
- Hỗ trợ **Dynamic Color** (Android 12+) qua `dynamicColorScheme` với fallback tĩnh.

### 4.3 Điều Hướng

- Dùng **Jetpack Navigation Compose** với một `NavHost` duy nhất.
- Định nghĩa route dưới dạng `sealed class` hoặc `object` trong file `NavRoutes.kt` riêng.
- Chỉ truyền **primitive arguments** qua NavArgs. Không truyền object — lấy dữ liệu từ ViewModel/repository.

### 4.4 Accessibility

- Mọi thành phần tương tác phải có `contentDescription`.
- Kích thước vùng chạm tối thiểu: 48dp × 48dp.
- Hỗ trợ TalkBack bằng cách dùng semantic role (`Role.Button`, `Role.Checkbox`).

---

## 5. Tích Hợp Supabase

### 5.1 Khởi Tạo SDK

- Dùng **supabase-kt** chính thức (io.github.jan-tennert.supabase).
- Khởi tạo `SupabaseClient` một lần duy nhất qua Hilt `@Singleton` module. Không tạo nhiều instance.
- Lưu `SUPABASE_URL` và `SUPABASE_ANON_KEY` trong `local.properties` (ngoài VCS). Đọc qua `BuildConfig`.

### 5.2 Xác Thực (Authentication)

- Dùng Auth plugin của supabase-kt (`GoTrue`).
- Lưu session qua `SupabaseSessionManager` với Android `EncryptedSharedPreferences`.
- Tự động làm mới token trước khi hết hạn; không bao giờ để lộ JWT trong log.
- Xử lý deep link OAuth/magic-link dùng URI handler có sẵn của supabase-kt.

### 5.3 Database / PostgREST

- Mọi tương tác với bảng đều đi qua tầng repository; không gọi Supabase trực tiếp từ ViewModel.
- Dùng **typed query** với Kotlin data class qua `select<T>()`, `insert<T>()`, v.v.
- Định nghĩa chính sách Row Level Security (RLS) cho mọi bảng. Không chỉ dựa vào lọc phía client.
- Ưu tiên lọc phía server thay vì tải toàn bộ dữ liệu về client.

### 5.4 Storage

- Dùng Supabase Storage bucket kèm chính sách RLS.
- Kiểm tra loại file và kích thước phía client **trước khi** upload (OWASP A04).
- Không để lộ signed URL của storage trong log hoặc thông báo lỗi.

### 5.5 Realtime

- Chỉ subscribe vào channel mà user đã được xác thực có quyền truy cập qua RLS.
- Hủy subscribe và xoá channel trong `onCleared()` của ViewModel.

---

## 6. Model Dữ Liệu — Quy Tắc Nullable

> **Quy tắc:** Tất cả model response ánh xạ từ bảng Supabase **phải dùng kiểu nullable** cho mọi field ngoại trừ primary key.

**Lý do:** Supabase có thể trả về `null` cho cột tuỳ chọn, và thay đổi schema không được làm crash app.

```kotlin
// ✅ Đúng
data class UserProfileDto(
    val id: String,               // PK — non-nullable
    val displayName: String?,     // có thể null
    val avatarUrl: String?,
    val bio: String?,
    val createdAt: String?,
)

// ❌ Sai — crash nếu cột 'bio' sau này trả về null
data class UserProfileDto(
    val id: String,
    val displayName: String,
    val bio: String,
)
```

- Domain model (trong `domain/model/`) có thể dùng kiểu non-nullable sau khi đã kiểm tra null trong mapper.
- Cung cấp hàm mapper extension (`fun UserProfileDto.toDomain(): UserProfile`) ở tầng `data/remote/`.

---

## 7. Xử Lý Lỗi

- Dùng sealed `Result<T>` (`Success`, `Error`, `Loading`) cho mọi thao tác bất đồng bộ.
- Bắt `RestException` (Supabase), `HttpException`, `IOException` trong repository; ánh xạ thành domain error.
- Không bao giờ để lộ stack trace thô ra UI. Hiển thị thông báo thân thiện qua `UiState.error: String?`.
- Log lỗi bằng một utility thống nhất (Timber); tắt log chi tiết ở bản release.

---

## 8. Bảo Mật (OWASP Mobile Top 10)

| OWASP | Quy tắc |
|---|---|
| M1 – Sử dụng credential không an toàn | Lưu key trong `local.properties` (không commit). Dùng `EncryptedSharedPreferences` cho token. |
| M2 – Bảo mật chuỗi cung ứng yếu | Ghim phiên bản dependency trong `libs.versions.toml`. Đánh giá thư viện bên thứ ba. |
| M3 – Xác thực không an toàn | Dùng Supabase Auth. Không tự triển khai logic xác thực. Bật MFA khi cần. |
| M4 – Thiếu kiểm tra đầu vào/đầu ra | Validate mọi input của user phía client. Ràng buộc phía Supabase qua CHECK constraint và RLS. |
| M5 – Giao tiếp không an toàn | Chỉ dùng HTTPS. Bật certificate pinning cho endpoint production. |
| M6 – Kiểm soát quyền riêng tư yếu | Chỉ yêu cầu quyền cần thiết. Giảm thiểu PII trong log. Xử lý dữ liệu nhạy cảm bằng `CharArray` và xoá sau khi dùng. |
| M7 – Bảo vệ binary yếu | Bật ProGuard/R8 ở bản release. Obfuscate tên class. |
| M8 – Cấu hình bảo mật sai | Tắt debug mode ở bản release. Xoá key/flag phát triển trước khi phát hành. |
| M9 – Lưu trữ dữ liệu không an toàn | Không lưu dữ liệu nhạy cảm trong `SharedPreferences` thông thường. Không log field nhạy cảm. |
| M10 – Mã hoá yếu | Dùng Android Keystore để quản lý key. Không tự triển khai thuật toán mã hoá. |

### Quy Tắc Bổ Sung

- Không log credential, token hoặc PII của user (`Timber.d` không được log object nhạy cảm).
- Mọi lời gọi mạng phải đi qua repository; không gọi HTTP trực tiếp từ Composable hoặc ViewModel.
- Supabase `anon` key là public theo thiết kế; kiểm soát truy cập hoàn toàn qua chính sách RLS trên database.

---

## 9. Kiểm Thử

| Tầng | Công cụ | Mục tiêu |
|---|---|---|
| Unit | JUnit 4 + Mockk | ViewModel, Use-case, Mapper |
| Integration | JUnit 4 + Supabase local (Docker) | Repository implementations |
| UI | Compose Testing (`composeTestRule`) | Luồng người dùng quan trọng |
| E2E | Espresso / UIAutomator | Smoke test trên bản release |

- File test phản chiếu cấu trúc source dưới `src/test/` và `src/androidTest/`.
- Mỗi ViewModel phải có file `*ViewModelTest.kt` tương ứng.
- Dùng `UnconfinedTestDispatcher` cho test coroutine.

### 9.1 Quy Tắc UT Bắt Buộc Khi Implement

> **Quy tắc không thể bỏ qua:** Mọi tính năng được implement PHẢI đi kèm Unit Test **trong cùng PR**. Không tách UT thành task riêng.

**Ngưỡng coverage tối thiểu:**

| Loại | Ngưỡng | Mô tả |
|---|---|---|
| **C0** (Statement Coverage) | **≥ 90%** | Mọi câu lệnh phải được thực thi qua ít nhất 1 test case |
| **C1** (Branch Coverage) | **≥ 80%** | Mọi nhánh điều kiện (`if`/`when`/`try-catch`) phải được kiểm thử cả hai chiều |

**Phạm vi áp dụng:**
- ViewModel: tất cả hàm `public` và logic xử lý event.
- Use-case: tất cả nhánh `success` / `failure`.
- Mapper: tất cả trường hợp nullable, giá trị biên.
- Repository implementation: mock data source, kiểm tra mapping và error propagation.

**Không áp dụng coverage cho:**
- File DI module (`AppModule.kt`, `SupabaseModule.kt`) — không chứa logic.
- Composable (`*Screen.kt`) — kiểm thử qua Compose Testing ở tầng UI riêng.
- DTO data class thuần (không có logic).

**Công cụ đo coverage:**
- Dùng JaCoCo (tích hợp sẵn qua AGP). Chạy `./gradlew testDebugUnitTestCoverage`.
- Báo cáo HTML tại `build/reports/jacoco/`.

---

## 10. Quản Lý Version Control & Commit

- Chiến lược branch: `main` (production), `develop` (tích hợp), `feature/<ticket>`, `fix/<ticket>`.
- Commit message theo **Conventional Commits**: `feat:`, `fix:`, `chore:`, `refactor:`, `test:`, `docs:`.
- Không push trực tiếp vào `main` hoặc `develop`. Mọi thay đổi qua Pull Request với ít nhất 1 reviewer.
- CI phải pass trước khi merge (build + unit test).

---

## 11. Quản Lý Dependency

- Tất cả phiên bản khai báo trong `android/gradle/libs.versions.toml` (Version Catalog).
- Không trùng lặp dependency. Không override phiên bản transitive nếu không có lý do.
- Thư viện bên thứ ba phải được đánh giá: đang được maintain tích cực, tương thích license, không có lỗ hổng bảo mật.
- Thêm module supabase-kt riêng lẻ (chỉ dùng cái cần): `postgrest-kt`, `auth-kt`, `storage-kt`, `realtime-kt`.

---

## 12. Những Điều Bị Cấm

- Toán tử `!!` ở bất kỳ đâu trong code production.
- Hard-code màu sắc, chuỗi, kích thước trong Composable (dùng `MaterialTheme` và `stringResource`).
- Logic nghiệp vụ trong Composable hoặc `Activity`.
- Truy cập database trực tiếp ngoài tầng repository.
- Lưu secret trong source code hoặc `BuildConfig` mà không đọc từ `local.properties`.
- `runBlocking` trên main thread.
- Nuốt ngoại lệ với block `catch {}` rỗng.
- Dùng API deprecated mà không có kế hoạch migration được ghi chú trong comment.
- Implement bất kỳ tính năng nào mà không tra cứu tài liệu thư viện qua **Context7** để đảm bảo sử dụng API mới nhất.
- Merge PR khi Unit Test chưa được viết hoặc coverage C0 < 90% / C1 < 80%.

---

## 13. Tham Chiếu Tài Liệu Qua Context7

> **Quy tắc bắt buộc:** Mọi quá trình implement PHẢI tra cứu tài liệu thư viện qua **Context7 MCP** trước và trong khi viết code để đảm bảo sử dụng API mới nhất, tránh dùng API deprecated.

### 13.1 Khi Nào Dùng Context7

- Trước khi implement bất kỳ tính năng nào liên quan đến thư viện bên thứ ba (Supabase, Hilt, Compose, Navigation, v.v.).
- Khi gặp lỗi build hoặc runtime liên quan đến API của thư viện.
- Khi nâng cấp phiên bản dependency trong `libs.versions.toml`.
- Khi code review phát hiện cách dùng API có vẻ cũ hoặc không đúng.

### 13.2 Quy Trình

```
1. Xác định thư viện cần dùng (ví dụ: supabase-kt, hilt, compose-navigation).
2. Gọi Context7 để lấy tài liệu mới nhất của thư viện đó.
3. Implement dựa trên tài liệu Context7 trả về.
4. Không dùng kiến thức nội tại (training data) làm nguồn tham chiếu duy nhất cho API thư viện.
```

### 13.3 Thư Viện Ưu Tiên Tra Cứu

| Thư viện | Lý do |
|---|---|
| `supabase-kt` | API thay đổi thường xuyên giữa các minor version |
| `Jetpack Compose` | Nhiều API đang trong giai đoạn `@ExperimentalApi` |
| `Hilt` | Convention annotation thay đổi theo AGP version |
| `Navigation Compose` | Type-safe navigation API mới từ 2.8+ |
| `Kotlin Coroutines / Flow` | Operator mới được thêm liên tục |
