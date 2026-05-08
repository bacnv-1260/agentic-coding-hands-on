# Implementation Plan: [iOS] Sun*Kudos_Tiêu chuẩn cộng đồng

**Screen ID**: `xms7csmDhD`
**Frame**: `xms7csmDhD — [iOS] Sun*Kudos_Tiêu chuẩn cộng đồng`
**Date**: 2026-05-08
**Spec**: `.momorph/specs/xms7csmDhD-iOS-Community-Standards/spec.md` ✅ reviewed
**Design Style**: `.momorph/specs/xms7csmDhD-iOS-Community-Standards/design-style.md` ✅ reviewed

---

## Summary

Implement màn hình `CommunityStandardsScreen` — một Composable thuần UI không có ViewModel riêng, không gọi API. Màn hình hiển thị nội dung tĩnh (tiêu chuẩn cộng đồng Sun*Kudos) trong một scrollable layout. Công việc gồm 4 phần:

1. **String resources** — thêm content strings VN/EN (sau khi Figma unblock)
2. **CommunityStandardsScreen Composable** — screen mới, thin UI
3. **NavRoutes + SaaNavHost** — khai báo route + wire composable
4. **WriteKudoScreen wiring** — thay `/* TODO */` trong `onCommunityStandardsClick`

> ⚠️ **Pre-condition blocker**: Figma API rate-limited tại thời điểm tạo spec — node IDs và text content chính xác chưa xác minh. **Phải re-run `momorph_downloadFigmaImage` cho node `xms7csmDhD` trước khi điền string resources.**

---

## Technical Context

| Property | Value |
|----------|-------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose + Material3 |
| **Architecture** | MVVM + Clean Architecture — màn hình này là pure UI, không cần tầng Data/Domain |
| **DI** | Hilt — không cần `@HiltViewModel` (no ViewModel) |
| **Async** | N/A — không có async operation |
| **Data Source** | N/A — static string resources only |
| **Navigation** | `NavRoutes.CommunityStandards` (NEW) ← push từ `NavRoutes.WriteKudo` |
| **Existing Routes** | `NavRoutes.WriteKudo` ✅, `SaaNavHost.kt` ✅ |
| **Testing** | JUnit 4 / Compose UI Test (optional — static screen) |

---

## Constitution Compliance Check

- [x] MVVM + Clean Architecture — màn hình mỏng, không logic nghiệp vụ trong Composable
- [x] Không dùng `!!` — không có nullable risk trong screen này
- [x] Material3 only — `TopAppBar`, `HorizontalDivider`, `MaterialTheme.typography.*`, không hard-code màu/size inline trong Composable — màu được khai báo dưới dạng `private val` top-level constants theo pattern của `WriteKudoScreen.kt`
- [x] Một `NavHost` duy nhất (`SaaNavHost.kt`) — chỉ thêm 1 `composable()` entry, không tạo NavHost mới
- [x] Timber cho logging — không có logging cần thiết trong màn hình này
- [x] `stringResource()` cho tất cả text — không hard-code string
- [x] Không logic nghiệp vụ trong Composable — màn hình chỉ hiển thị static content
- [x] TR-002: dùng `navController.navigateUp()` để back — đồng nhất với `SaaNavHost.kt` L198

**Violations**: Không có.

---

## Architecture Decisions

### Composable structure

```
CommunityStandardsScreen(onNavigateBack: () -> Unit)   ← thin screen, no ViewModel
  ├── Scaffold (TopAppBar + content)
  │   ├── TopAppBar
  │   │   ├── IconButton(back_arrow) → onNavigateBack()
  │   │   └── Text("Tiêu chuẩn cộng đồng")
  │   └── Column + verticalScroll
  │       ├── SectionBlock(title, body) × N    ← private @Composable helper
  │       └── Spacer (bottom safe area)
  └── (no ViewModel, no state except scroll offset)
```

Màn hình **không cần ViewModel** vì:
- Không có local mutable state (ngoài scroll offset — handled bởi `rememberScrollState()`)
- Không có API call
- Language state được inject qua `LocalContext` (đã handled bởi `SaaNavHost` `LocalizedContextWrapper`)

### Back navigation

```kotlin
// SaaNavHost.kt — trong composable(NavRoutes.CommunityStandards.route)
CommunityStandardsScreen(onNavigateBack = { navController.navigateUp() })
```

`navigateUp()` — đồng nhất với pattern hiện tại của `WriteKudoScreen` (`SaaNavHost.kt` L198).

### SectionBlock helper

Private `@Composable` function (không expose ra ngoài file), nhận `title: String` và `body: String`. Tái sử dụng trong cùng file để render N sections. Không cần tạo file riêng vì chỉ dùng trong 1 screen.

### FormattingToolbar color fix

`FormattingToolbar.kt` hard-codes `CommunityLinkRed = Color(0xFFE46060)` — sai so với Figma token `#E73928`. Fix đồng thời khi wiring `onCommunityStandardsClick` (cùng file, cùng PR) để tránh tạo thêm PR riêng.

---

## Project Structure

### Documentation (this feature)

```
.momorph/specs/xms7csmDhD-iOS-Community-Standards/
├── spec.md           ✅ reviewed
├── design-style.md   ✅ reviewed
└── plan.md           ← this file
```

### Source Code (affected areas)

```
android/app/src/main/
├── res/
│   ├── values/strings.xml                           ← MODIFY: thêm community_standards_* strings (VN)
│   └── values-en/strings.xml                        ← MODIFY: thêm community_standards_* strings (EN)
│
└── java/com/example/saa/
    ├── NavRoutes.kt                                 ← MODIFY: thêm CommunityStandards route
    ├── SaaNavHost.kt                                ← MODIFY: thêm composable entry
    ├── presentation/ui/
    │   ├── community_standards/
    │   │   └── CommunityStandardsScreen.kt          ← NEW
    │   └── writekudo/
    │       ├── WriteKudoScreen.kt                   ← MODIFY: wire onCommunityStandardsClick
    │       └── components/
    │           └── FormattingToolbar.kt             ← MODIFY: fix CommunityLinkRed color
```

**Không cần thêm dependency mới** — Compose, Navigation, Material3, Hilt đã đủ.

---

## Implementation Strategy

### Phase 0 — Unblock Figma content ⚠️ (Pre-condition)

> **Phải hoàn thành trước khi bắt đầu Phase 1.**

- Re-run `momorph_downloadFigmaImage` cho node `xms7csmDhD` (file key `9ypp4enmFmdK3YAFJLIu6C`) sau khi Figma rate-limit clear
- Xác minh: số section, text content của từng section title + body
- Update TBD node IDs trong `spec.md` và `design-style.md`
- Confirm VN/EN string values với PM/designer trước khi hardcode

---

### Phase 1 — String Resources (P2, US3)

**Mục tiêu**: Tất cả text từ `stringResource()` — không hardcode.

> ⚠️ Values below are **placeholders** — xác minh với Figma và PM trước khi điền vào file.

**`values/strings.xml`** — thêm vào sau block Kudos strings:
```xml
<!-- Community Standards screen -->
<string name="community_standards_title">Tiêu chuẩn cộng đồng</string>
<string name="community_standards_s1_title"><!-- Xác minh từ Figma --></string>
<string name="community_standards_s1_body"><!-- Xác minh từ Figma --></string>
<string name="community_standards_s2_title"><!-- Xác minh từ Figma --></string>
<string name="community_standards_s2_body"><!-- Xác minh từ Figma --></string>
<!-- ... lặp lại cho từng section theo số section xác minh từ Figma ... -->
```

**`values-en/strings.xml`** — thêm tương tự:
```xml
<!-- Community Standards screen -->
<string name="community_standards_title">Community Standards</string>
<string name="community_standards_s1_title"><!-- Xác minh từ Figma --></string>
<string name="community_standards_s1_body"><!-- Xác minh từ Figma --></string>
<!-- ... -->
```

**Lưu ý**: `cd_back_arrow` (`"Quay lại"` / `"Go back"`) đã tồn tại trong cả hai files — **không thêm lại**.

---

### Phase 2 — NavRoutes.kt (P1, US1)

**File**: `android/app/src/main/java/com/example/saa/NavRoutes.kt`

Thêm entry mới vào `sealed class NavRoutes`:

```kotlin
data object CommunityStandards : NavRoutes("community_standards")
```

Đặt sau `data object Search` để giữ thứ tự gần nhau với Kudos-related routes.

---

### Phase 3 — CommunityStandardsScreen Composable (P1, US1 + US2)

**File**: `android/app/src/main/java/com/example/saa/presentation/ui/community_standards/CommunityStandardsScreen.kt`

```kotlin
// Top-level private color constants (project pattern: same as WriteKudoScreen.kt `ScreenBg`)
private val CsScreenBg   = Color(0xFF00101A)
private val CsGoldText   = Color(0xFFFFEA9E)
private val CsBodyText   = Color.White
private val CsDivider    = Color(0xFF2E3940)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityStandardsScreen(
    onNavigateBack: () -> Unit,
) {
    // Capture string resource outside semantics block (semantics lambda is not @Composable)
    val backArrowDesc = stringResource(R.string.cd_back_arrow)

    Scaffold(
        containerColor = CsScreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.community_standards_title),
                        color = CsGoldText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.semantics {
                            contentDescription = backArrowDesc
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CsScreenBg,
                    scrolledContainerColor = CsScreenBg,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            HorizontalDivider(color = CsDivider)
            Spacer(modifier = Modifier.height(16.dp))

            // Repeat SectionBlock for each section (N to be confirmed from Figma)
            CommunityStandardsSection(
                title = stringResource(R.string.community_standards_s1_title),
                body = stringResource(R.string.community_standards_s1_body),
            )
            // ... repeat for s2..sN ...

            Spacer(modifier = Modifier.height(24.dp)) // bottom safe area padding
        }
    }
}

@Composable
private fun CommunityStandardsSection(
    title: String,
    body: String,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = CsGoldText,
        fontWeight = FontWeight.SemiBold,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = body,
        style = MaterialTheme.typography.bodyMedium,
        color = CsBodyText,
    )
    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider(color = CsDivider)
    Spacer(modifier = Modifier.height(16.dp))
}
```

**Accessibility**:
- `back_arrow` IconButton → `contentDescription = stringResource(R.string.cd_back_arrow)` (key đã có)
- Section Text → `contentDescription` không cần — Text đã exposed natively qua TalkBack

**Preview**:
```kotlin
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CommunityStandardsScreenPreview() {
    SaaTheme { CommunityStandardsScreen(onNavigateBack = {}) }
}
```

---

### Phase 4 — SaaNavHost.kt (P1, US1)

**File**: `android/app/src/main/java/com/example/saa/SaaNavHost.kt`

Thêm vào trong `NavHost` block, sau `composable(NavRoutes.WriteKudo.route)` entry:

```kotlin
composable(NavRoutes.CommunityStandards.route) {
    CommunityStandardsScreen(
        onNavigateBack = { navController.navigateUp() },
    )
}
```

Thêm import tương ứng:
```kotlin
import com.example.saa.presentation.ui.community_standards.CommunityStandardsScreen
```

---

### Phase 5 — Wire WriteKudoScreen + fix FormattingToolbar color (P1, US1)

#### 5a — WriteKudoScreen.kt

**File**: `android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoScreen.kt`

Tại `onCommunityStandardsClick` (~L218):

Bước 1 — thêm `onNavigateToCommunityStandards: () -> Unit` parameter vào `WriteKudoScreen`; bước 2 — thay thế body của TODO:

```kotlin
// BEFORE (WriteKudoScreen.kt body):
onCommunityStandardsClick = { /* TODO: navigate to community standards */ },

// AFTER (WriteKudoScreen.kt body — dùng lambda parameter, không dùng navController trực tiếp):
onCommunityStandardsClick = onNavigateToCommunityStandards,
```

> **Lý do**: `navController` không có trong scope của `WriteKudoScreen.kt` — nó chỉ tồn tại trong `SaaNavHost.kt`. Truyền navigation callback qua lambda parameter là pattern đúng.

**WriteKudoScreen signature update**:
```kotlin
fun WriteKudoScreen(
    recipientId: String?,
    recipientName: String?,
    onNavigateBack: () -> Unit,
    onNavigateToCommunityStandards: () -> Unit,    // ← NEW
    viewModel: WriteKudoViewModel = hiltViewModel(),
)
```

**SaaNavHost.kt update** (trong WriteKudo composable):
```kotlin
WriteKudoScreen(
    recipientId = recipientId,
    recipientName = recipientName,
    onNavigateBack = { navController.navigateUp() },
    onNavigateToCommunityStandards = {              // ← NEW
        navController.navigate(NavRoutes.CommunityStandards.route)
    },
)
```

#### 5b — FormattingToolbar.kt — fix color (code deviation)

**File**: `android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/FormattingToolbar.kt`

```kotlin
// BEFORE:
private val CommunityLinkRed = Color(0xFFE46060)

// AFTER:
private val CommunityLinkRed = Color(0xFFE73928)  // Figma token: --color-heart-active / red accent
```

---

## Implementation Order

```
Phase 0 (blocker) → Phase 2 → Phase 3 → Phase 4 → Phase 5a → Phase 1
                                                ↘ Phase 5b (independent, any time)
```

- **Phase 0** — unblock Figma content (prerequisite for Phase 1)
- **Phase 2** — NavRoutes (no dependency, safe to do first)
- **Phase 3** — Screen Composable (depends on Phase 1 for string keys — can use placeholder strings initially; does NOT reference NavRoutes directly)
- **Phase 4** — SaaNavHost wiring (depends on Phase 2 + 3 — references `NavRoutes.CommunityStandards.route` and calls `CommunityStandardsScreen`)
- **Phase 5a** — WriteKudoScreen wiring (depends on Phase 2 + 4 — adds `onNavigateToCommunityStandards` param and updates SaaNavHost WriteKudo call)
- **Phase 5b** — FormattingToolbar color fix (independent — can be done at any time after Phase 0)
- **Phase 1** — String resources (depends on Phase 0 Figma content)

---

## Success Criteria Mapping

| SC | Verified by |
|----|-------------|
| SC-001: Tất cả sections hiển thị | Manual: launch app → WriteKudo → tap toolbar button → all sections visible |
| SC-002: Scroll hoạt động | Manual: scroll qua hết content |
| SC-003: Back quay về WriteKudo, form không mất | Manual: điền form → navigate → back → form còn nguyên |
| SC-004: Nội dung đúng ngôn ngữ | Manual: switch VN↔EN → re-enter screen → text đổi |
| SC-005: Không có API call | Code review: không có Supabase/repository call trong Composable |

---

## Open Questions

| # | Question | Owner | Blocker? |
|---|----------|-------|---------|
| Q-1 | Số section chính xác và text content của từng section từ Figma | Designer | ✅ Yes — cần trước Phase 1 |
| Q-2 | Màu link `#E73928` có được confirm từ Figma spec không? (hiện tại code dùng `#E46060`) | Designer | No — có thể implement với assumption `#E73928` |
| Q-3 | Screen navigate dưới dạng full-screen push hay bottom sheet? (spec nói full-screen) | PM/Designer | No — giữ full-screen per spec |
