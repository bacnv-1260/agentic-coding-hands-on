# Hướng Dẫn Copilot Agent — Dự Án Android + Supabase

## Tổng Quan Dự Án

Đây là **ứng dụng mobile Android** (Kotlin, Jetpack Compose, Material3) sử dụng **Supabase** làm backend (PostgreSQL, Auth, Storage, Realtime).

## Đọc Bắt Buộc

Trước khi tạo hoặc chỉnh sửa bất kỳ đoạn code nào, hãy đọc và tuân theo:

- `.momorph/guidelines/constitution.md` — Nguyên tắc kiến trúc, pattern và các quy tắc không được vi phạm.
- `.momorph/guidelines/android.md` — Tiêu chuẩn code Android/Compose/Supabase kèm ví dụ cụ thể.

## Các Quy Tắc Chính

1. **Kiến trúc:** MVVM + Clean Architecture. Tầng Data → Domain → Presentation.
0. **Context7:** Trước và trong khi implement, PHẢI gọi **Context7 MCP** để tra cứu tài liệu thư viện mới nhất (supabase-kt, Compose, Hilt, Navigation, v.v.). Không dùng kiến thức nội tại làm nguồn tham chiếu API duy nhất.
2. **Không dùng `!!`** ở bất kỳ đâu trong code production. Dùng safe call `?.` và Elvis `?:`.
3. **Supabase DTO phải dùng field nullable** cho tất cả cột ngoại trừ primary key.
4. **Không đặt logic nghiệp vụ trong Composable** — uỷ quyền cho ViewModel.
5. **Chỉ dùng Material3** — không hard-code màu sắc, font size hay spacing.
6. **Bảo mật:** Key trong `local.properties`, token trong `EncryptedSharedPreferences`, RLS trên mọi bảng Supabase.
7. **Điều hướng:** Một `NavHost` duy nhất với route định nghĩa trong `NavRoutes.kt`.
8. **Xử lý lỗi:** Sealed `Result<T>`. Không bao giờ để lộ exception thô ra UI.
9. **Logging:** Chỉ dùng Timber. Không log credential, token hay PII.
10. **Dependency:** Tất cả phiên bản trong `android/gradle/libs.versions.toml`.
11. **Unit Test bắt buộc:** Mọi tính năng implement PHẢI có UT trong cùng PR. Coverage tối thiểu: **C0 ≥ 90%** (statement), **C1 ≥ 80%** (branch). Dùng JUnit 4 + Mockk + `StandardTestDispatcher`.

## Ngôn Ngữ

- Toàn bộ source code: **Kotlin**.
- Commit message và comment trong code: **Tiếng Anh**.
