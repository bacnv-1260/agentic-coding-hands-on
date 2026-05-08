# Implementation Plan: Write Kudo Screen

**Frame**: `7fFAb-K35a-iOS-WriteKudo`
**Date**: 2026-05-07
**Spec**: `specs/7fFAb-K35a-iOS-WriteKudo/spec.md`
**Design**: `specs/7fFAb-K35a-iOS-WriteKudo/design-style.md`

---

## Summary

Implement the Write Kudo form screen (`WriteKuduScreen`) as a full MVVM + Clean Architecture feature on Android/Compose. The screen is a rich form allowing the user to compose a kudo (recipient, title, rich-text message, hashtags, images, anonymous flag) and submit it to Supabase. Route `write_kudo` already exists in `NavRoutes` and `SaaNavHost` but renders only `Surface { Text("Write Kudo") }` — this plan replaces that placeholder with a production-quality implementation.

Key technical challenges:
- Rich-text textarea with formatting toolbar (bold, italic, strikethrough, numbered list, link, blockquote)
- Multi-step submission: optional image upload to Supabase Storage → insert into `kudos` table + `kudos_hashtags` join table
- Recipient search overlay + Hashtag picker sheet (E.2 opens dropdown with predefined list from `hashtags` table)
- Form dirty-check driving a system-back-button interceptor

> **DB Naming Discrepancies** (design items vs actual schema — all code must use actual column names):
> - **B.4** `columnName: "title"` → actual column: `kudos.award_category_name`
> - **E** `columnName: "tags"` → actual storage: `kudos_hashtags` join table (no `tags` column on `kudos`; insert via `KudosDataSource.insertKudosHashtags()` as a separate operation after the main kudos row is created)
> - **F** `columnName: "image_ids"` → actual column: `kudos.photo_urls` (text[] array of URL strings)

---

## Technical Context

| Property | Value |
|----------|-------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose + Material3 |
| **Architecture** | MVVM + Clean Architecture (3-layer) |
| **State** | `StateFlow<WriteKudoUiState>` in `WriteKudoViewModel` |
| **DI** | Hilt `@HiltViewModel` / `@Singleton` |
| **Backend** | Supabase PostgREST (insert kudos + kudos_hashtags), Supabase Storage (images) |
| **Navigation** | NavArgs: `recipientId: String?`, `recipientName: String?` (primitives only) |
| **Testing** | JUnit4 + kotlinx-coroutines-test; fake/stub pattern |

---

## Constitution Compliance Check

*GATE: Must pass before implementation can begin*

| Requirement | Constitution Rule | Status |
|-------------|-------------------|--------|
| No business logic in Composable | §2.2 — logic only in ViewModel | ✅ Planned |
| No `!!` operators | §3 — use `?.`, `?:` | ✅ Planned |
| No Android types in ViewModel | §2.2 — no Context/Activity | ✅ Planned |
| Single `StateFlow<UiState>` source of truth | §2.2 | ✅ Planned |
| Supabase DTOs: all fields nullable except PK | §6 | ✅ Planned |
| Result<T> for all async ops | §7 | ✅ Planned |
| Timber logging only, no PII/tokens | §7 | ✅ Planned |
| Material3 only, no hard-coded colors/sizes | §4.2 | ✅ Planned |
| Primitive NavArgs only | §4.3 | ✅ Planned |
| contentDescription on all interactive elements | §4.4 | ✅ Planned |
| 48dp minimum touch targets | §4.4 | ✅ Planned |
| Supabase Storage: client-side file validation before upload | §5.4 / OWASP A04 | ✅ Planned |
| RLS already defined on `kudos` + `kudos_hashtags` tables | §5.3 | ✅ Existing |

**Violations:**

| Violation | Justification | Alternative Rejected |
|-----------|---------------|---------------------|
| Rich-text editor requires external library (see Risk R1) | Jetpack Compose has no built-in rich-text field | `BasicTextField` with `AnnotatedString` — insufficient for toolbar UX |

---

## Architecture Decisions

### Validation Model (resolves spec Open Question Q7)

Adopt the **hybrid model**:
- Send button is **disabled** when any required field is empty (`isNotEmpty()` check).
- When the button is enabled and tapped, **full client-side validation** runs for constraint violations (max length, whitespace-only message, self-kudo, invalid URL).
- This is consistent with TC_VIETKUDO_FUN_033 (disabled → enabled on filling all fields) while also covering TC_VIETKUDO_FUN_004/007/009 (on-submit error for invalid but non-empty values).

### Rich-Text Editor

Use **Compose-RichText** (`com.mohamedrejeb.richeditor:richeditor-compose`) for the message textarea (D) and formatting toolbar (C). This library provides an `RichTextState` that models bold/italic/strikethrough/list/link/blockquote and is composable-friendly with no Context dependency.

> Rationale: No native Compose component supports formatted text editing. `AnnotatedString` in `BasicTextField` does not support block-level formatting (numbered list, blockquote) or URL insertion. The library is MIT-licensed, actively maintained, and has no transitive Android-private dependencies.

### Form Submission Flow

```
WriteKudoViewModel.onSendClick()
  ├── 1. Validate all fields (client-side)
  ├── 2. If images present: for each image → StorageDataSource.uploadImage() → get image URL
  ├── 3. KudosRepository.submitKudo(WriteKudoRequest) →
  │       ├── KudosDataSource.insertKudo(KudosDto)  → kudos table
  │       └── KudosDataSource.insertKudosHashtags() → kudos_hashtags table (batch)
  └── 4. On success → emit NavigateToSuccess event
```

### State Management

`WriteKudoUiState` is a single `data class` (not sealed — form has too many independent fields for sealed subclasses to be practical). Navigation side-effects are emitted via a `Channel<WriteKudoEvent>` consumed once in the screen.

### Recipient Search

The recipient search overlay is a **separate composable sheet** (`RecipientSearchSheet`). It calls `ProfileDataSource` directly via a dedicated `SearchProfilesUseCase` → `ProfileRepository.searchProfiles(query)`. The current user is excluded by checking against the current auth user ID (available via `supabase.auth.currentUserOrNull()?.id`).

### Image Upload

Upload images **before** inserting the kudos row:
1. Each image is uploaded to the Supabase Storage bucket (`kudos-images` assumed; confirm per Q4).
2. The returned public URLs are collected into `imageIds: List<String>` in UiState.
3. These URLs are written to `kudos.photo_urls` on insert.
4. If any upload fails, the failure is surfaced immediately and the Submit flow is aborted (no partial kudos row is created).

---

## Project Structure

### Documentation

```
.momorph/specs/7fFAb-K35a-iOS-WriteKudo/
├── spec.md          # Reviewed spec (complete)
├── design-style.md  # Design tokens
├── plan.md          # This file
└── tasks.md         # Next step (generate via /momorph.tasks)
```

### New Files to Create

```
android/app/src/main/java/com/example/saa/
│
├── presentation/ui/writekudo/
│   ├── WriteKudoScreen.kt            # Root composable — thin, delegates to ViewModel
│   ├── WriteKudoViewModel.kt         # HiltViewModel; owns WriteKudoUiState
│   ├── WriteKudoUiState.kt           # data class + WriteKudoEvent sealed class
│   └── components/
│       ├── WriteKudoFormCard.kt      # Scrollable card containing the form fields
│       ├── RecipientDropdownField.kt # B.1 + B.2: label + tappable dropdown trigger
│       ├── TitleInputField.kt        # B.3 + B.4 + B.5: label + TextField + hint + link
│       ├── FormattingToolbar.kt      # C (C.1–C.6): Bold/Italic/Stroke/List/Link/Quote toolbar container
│       ├── RichTextEditor.kt         # D + D.1: message textarea composable + mention hint label
│       ├── HashtagSection.kt         # E.1 + E.2: label + chip group (selected tags + "+ Hashtag" trigger)
│       ├── HashtagPickerSheet.kt     # E.2 overlay: ModalBottomSheet with predefined hashtag list
│       ├── ImageSection.kt           # F.1 + F.2~F.4 + F.5: label + thumbnail row + add button
│       ├── MessageCharCounter.kt     # D char counter: "n/1000" display tied to message length
│       ├── AnonymousToggle.kt        # G: checkbox + label
│       └── WriteKudoBottomBar.kt     # H + I: Cancel + Send fixed bottom row
│
├── presentation/ui/writekudo/search/
│   └── RecipientSearchSheet.kt       # B.2 overlay: ModalBottomSheet with search field + LazyColumn of profiles
│
├── domain/
│   ├── model/
│   │   └── WriteKudoRequest.kt       # Intermediate domain model for kudo creation
│   ├── repository/
│   │   └── KudosRepository.kt        # ADD: suspend fun submitKudo(request: WriteKudoRequest): Result<String>
│   │   └── ProfileRepository.kt      # ADD: suspend fun searchProfiles(query: String): Result<List<Profile>>
│   └── usecase/
│       ├── SubmitKudoUseCase.kt      # NEW: wraps multi-step submission logic
│       └── SearchProfilesUseCase.kt  # NEW: delegates to ProfileRepository.searchProfiles
│
├── data/
│   ├── remote/
│   │   ├── dto/
│   │   │   └── WriteKudoDto.kt       # @Serializable DTO for kudos INSERT payload
│   │   └── source/
│   │       ├── KudosDataSource.kt    # ADD: suspend fun insertKudo(...): String (returns new kudos id)│       │                         #   → uses .insert(dto).select().decodeSingle<KudosDto>() to get generated id│   │       │                         # ADD: suspend fun insertKudosHashtags(kudosId, tagIds)
│   │       ├── ProfileDataSource.kt  # ADD: suspend fun searchProfiles(query: String): List<ProfileDto>
│   │       └── StorageDataSource.kt  # NEW: suspend fun uploadImage(uri: Uri, bucket: String): String
│   └── repository/
│       ├── KudosRepositoryImpl.kt    # ADD: submitKudo implementation
│       └── ProfileRepositoryImpl.kt  # ADD: searchProfiles implementation
│
└── di/
    └── KudosModule.kt               # ADD: bind StorageDataSource (if needed as interface)
```

### Modified Files

| File | Change |
|------|--------|
| `NavRoutes.kt` | **MODIFY** (route already exists): change `NavRoutes("write_kudo")` → `NavRoutes("write_kudo?recipientId={recipientId}&recipientName={recipientName}")` with optional `String?` args; add `createRoute(recipientId, recipientName)` helper |
| `SaaNavHost.kt` | UPDATE composable declaration to declare `navArgument("recipientId")` + `navArgument("recipientName")` as nullable; REPLACE placeholder `Surface { Text("Write Kudo") }` with `WriteKudoScreen(...)` |
| `SaaNavHost.kt` (3 callsites) | UPDATE all 3 `navController.navigate(NavRoutes.WriteKudo.route)` calls (line 110, 167, and HomeScreen line 187) to use `NavRoutes.WriteKudo.createRoute(null, null)` |
| `KudosRepository.kt` (interface) | ADD: `suspend fun submitKudo(request: WriteKudoRequest): Result<String>` |
| `ProfileRepository.kt` (interface) | ADD: `suspend fun searchProfiles(query: String): Result<List<Profile>>` |
| `KudosDataSource.kt` | ADD: `insertKudo`, `insertKudosHashtags` methods |
| `ProfileDataSource.kt` | ADD: `searchProfiles` method |
| `gradle/libs.versions.toml` | ADD: `richeditor-compose` version entry |
| `app/build.gradle.kts` | ADD: `richeditor-compose` dependency |

### Dependencies to Add

| Package | Version | Purpose |
|---------|---------|---------|
| `com.mohamedrejeb.richeditor:richeditor-compose` | `1.0.0-rc08` (latest stable) | Rich-text editor for message textarea |

---

## Implementation Strategy

### Phase 0: Dependencies & Scaffolding

1. Add `richeditor-compose` to `libs.versions.toml` and `app/build.gradle.kts`
2. **MODIFY** `NavRoutes.WriteKudo` — change route string to `"write_kudo?recipientId={recipientId}&recipientName={recipientName}"`, add `createRoute(recipientId: String?, recipientName: String?)` helper
3. Update the 3 existing navigate callsites (`SaaNavHost.kt` lines 110 + 167, `HomeScreen.kt` line 187) to use `NavRoutes.WriteKudo.createRoute(null, null)`
4. Update `SaaNavHost.kt` composable entry — add nullable `navArgument` declarations
5. Create `WriteKudoUiState.kt` (data class + events)
6. Create `WriteKudoViewModel.kt` stub (no logic yet)
7. Create `WriteKudoScreen.kt` stub (passes state to form card, no components yet)
8. Verify build is clean

### Phase 1: Domain & Data Layer (US-1 foundation)

1. Create `WriteKudoRequest.kt` domain model
2. Create `WriteKudoDto.kt` (`@Serializable`, all fields nullable except id; include `anonymous_nickname`)
3. Add `submitKudo()` to `KudosRepository` interface + `KudosRepositoryImpl`
4. Add `insertKudo()` + `insertKudosHashtags()` to `KudosDataSource`
5. Create `StorageDataSource.kt` with `uploadImage()` (client-side MIME/size validation per spec)
6. Create `SubmitKudoUseCase.kt` — orchestrates upload → insert → hashtag join; extracts `hashtag.id` from `List<Hashtag>` for `hashtagIds` in `WriteKudoRequest`
7. Add `searchProfiles()` to `ProfileRepository` interface + `ProfileDataSource` + `ProfileRepositoryImpl`
8. Create `SearchProfilesUseCase.kt`
9. Write unit tests for `SubmitKudoUseCase` and new data sources

### Phase 2: Core Form UI + Cancel (US-1 happy path + US-2)

1. `WriteKudoFormCard.kt` — scrollable card with keyvisual background behind it
2. `TitleInputField.kt` — B.3/B.4 (TextField; max 100 chars; char-count inline) + B.5 (community link)
3. `FormattingToolbar.kt` — C container + C.1–C.6:
   - C.1 Bold (`buttonType: toggle`), C.2 Italic, C.4 Number, C.6 Quote → **toggle** buttons (active state from `RichTextState`)
   - C.3 Stroke (Strikethrough) → **toggle** button (Figma `otherType: decorative` but functionally identical to toggle)
   - **C.5 Link → action button only** (opens `showLinkDialog`; NOT a toggle; tapping always opens the URL-input dialog regardless of cursor position)
4. `RichTextEditor.kt` — D (rich text field composable) + D.1 (mention hint label below textarea)
5. `MessageCharCounter.kt` — shows `"${messageCharCount}/1000"` bound to `uiState.messageCharCount`
6. `AnonymousToggle.kt` — G (Checkbox with `Role.Checkbox` semantic; default unchecked)
7. `WriteKudoBottomBar.kt` — H (Cancel, always enabled) + I (Send, `enabled = isSubmitEnabled`; loading spinner when `isSending`)
8. **Cancel / dirty check (US-2)**: track `formDirty` in UiState; `BackHandler` intercepts when `formDirty = true`; dispatch `onCancelRequested()`; ViewModel emits `ShowCancelDialog`; `AlertDialog` composable renders confirmation
9. Wire all fields to `WriteKudoViewModel` event handlers
10. Verify US-1 Scenario 1 (happy path submit) + US-2 cancel dialog end-to-end in emulator

### Phase 3: Recipient & Hashtag Selection (US-1 — pickers)

1. `RecipientDropdownField.kt` — B.1 + B.2 tappable trigger (shows selected name or placeholder)
2. `RecipientSearchSheet.kt` — `ModalBottomSheet`; search TextField + LazyColumn of `Profile` results; empty state; profile card shows name + department
3. `HashtagSection.kt` — E.1 + E.2 (chip row of selected tags; `+ Hashtag` trigger; `×` per chip)
4. `HashtagPickerSheet.kt` — `ModalBottomSheet` showing predefined `availableHashtags` list (from `UserStatsRepository.getHashtags()`); tapping a tag calls `onHashtagSelected(hashtag)` which adds to state
5. Wire recipient selection to ViewModel: `onRecipientSelected(id: String, name: String)` → sets `recipientId` + `recipientName` + `formDirty = true`
6. Wire hashtag add/remove to ViewModel: `onHashtagSelected(hashtag)` / `onHashtagRemoved(hashtag)` → update `hashtags` list
7. Verify self-kudo detection in ViewModel (compare `recipientId` against current user ID)

### Phase 4: Image Upload (US-4)

1. `ImageSection.kt` — F.1 + F.2~F.4 (thumbnail row, each with `×` remove) + F.5 ("+ Image" add button, hidden when `imageIds.size == 5`)
2. Register `ActivityResultContracts.GetMultipleContents` launcher in the **Composable** (not ViewModel — Android API). On result, convert each `Uri` to `String` via `uri.toString()` in the Composable before calling `viewModel.onImagesSelected(uriStrings: List<String>)` — constitution §2.2 forbids Android types in ViewModel
3. Client-side MIME check (`image/jpeg`, `image/png`, `image/webp`) + 10 MB size guard before calling `StorageDataSource`
4. Display per-image loading state (thumbnail placeholder with progress indicator while uploading)
5. Thumbnail shows local URI while upload is in progress, then resolves to remote URL on success; `imageIds` list updated accordingly

### Phase 5: Polish & Accessibility

1. Add `contentDescription` to all interactive components (constitution §4.4)
2. Ensure all toolbar buttons + thumbnail remove buttons have minimum 48dp touch target using `Modifier.minimumInteractiveComponentSize()`
3. Ensure `I` (Send button) uses `enabled = uiState.isSubmitEnabled` — disabled semantic propagated by Material3 automatically
4. Inline error messages: use `Text` below the relevant field, wrapped in `AnimatedVisibility` for smooth entry
5. Link dialog for C.5: `AlertDialog` with a `TextField` for URL input; validate `Patterns.WEB_URL` before inserting
6. Error snackbar on submit failure: `LaunchedEffect(event)` consuming `WriteKudoEvent.ShowError`
7. Cache invalidation: on success, call `kudosRepository.invalidateCache()` (or trigger a reload in `KudosViewModel` via shared state / repository cache bust)
8. `@Preview` annotations for all leaf composables

---

## Data Model Details

### `WriteKudoRequest` (domain model)

```kotlin
data class WriteKudoRequest(
    val recipientId: String,
    val title: String,          // maps to kudos.award_category_name
    val message: String,        // rich text (HTML string)
    val hashtagIds: List<String>,
    val photoUrls: List<String>,
    val isAnonymous: Boolean,
)
```

### `WriteKudoDto` (DTO for INSERT)

```kotlin
@Serializable
data class WriteKudoDto(
    @SerialName("sender_id")            val senderId: String?,
    @SerialName("recipient_id")         val recipientId: String?,
    @SerialName("message")              val message: String?,
    @SerialName("award_category_name")  val awardCategoryName: String?,  // B.4 design says "title" — actual column is award_category_name
    @SerialName("photo_urls")           val photoUrls: List<String>?,
    @SerialName("is_anonymous")         val isAnonymous: Boolean?,
    @SerialName("anonymous_nickname")   val anonymousNickname: String?,   // NOT NULL DEFAULT '' in DB; set when isAnonymous=true
    @SerialName("sender_name")          val senderName: String?,
    @SerialName("sender_avatar_url")    val senderAvatarUrl: String?,
    @SerialName("sender_employee_code") val senderEmployeeCode: String?,
    @SerialName("sender_badge_type")    val senderBadgeType: String?,
    @SerialName("recipient_name")       val recipientName: String?,
    @SerialName("recipient_avatar_url") val recipientAvatarUrl: String?,
    @SerialName("recipient_hero_tier")  val recipientHeroTier: Int?,
)
```

> Note: Denormalized sender/recipient fields are written at insert time using the current session profile — consistent with existing `KudosDto` pattern in the codebase.
> Note: `anonymousNickname` should be passed as `null` when `isAnonymous = false` (DB default `''` applies), and populated with a generated/configured nickname when `isAnonymous = true` (logic TBD per product spec).

### `WriteKudoUiState`

```kotlin
data class WriteKudoUiState(
    val recipientId: String? = null,
    val recipientName: String = "",
    val title: String = "",                     // display alias; maps to award_category_name on submit
    val message: String = "",                   // serialised rich text (HTML string from RichTextState)
    val hashtags: List<Hashtag> = emptyList(),   // full Hashtag objects; IDs extracted to hashtagIds on submit
    val imageIds: List<String> = emptyList(),    // local URI strings (pre-upload) or remote URLs (post-upload)
    val isAnonymous: Boolean = false,
    val isSending: Boolean = false,
    val formDirty: Boolean = false,
    val error: String? = null,
    val titleError: String? = null,              // inline field error
    val messageError: String? = null,
    val recipientError: String? = null,
    val showCancelDialog: Boolean = false,
    val showLinkDialog: Boolean = false,
    val showHashtagPicker: Boolean = false,       // drives HashtagPickerSheet visibility (E.2)
    val showRecipientSearch: Boolean = false,     // drives RecipientSearchSheet visibility (B.2)
    val pendingLinkUrl: String = "",
    val availableHashtags: List<Hashtag> = emptyList(), // loaded from UserStatsRepository.getHashtags()
) {
    val isSubmitEnabled: Boolean
        get() = recipientId != null && title.isNotEmpty() &&
                message.isNotEmpty() && hashtags.isNotEmpty() && !isSending
    val canAddImage: Boolean get() = imageIds.size < 5
    val canAddHashtag: Boolean get() = hashtags.size < 5
    val messageCharCount: Int get() = message.length  // drives MessageCharCounter display (D, max 1000)
}

sealed class WriteKudoEvent {
    data object NavigateToSuccess : WriteKudoEvent()
    data object NavigateBack : WriteKudoEvent()
    data class ShowError(val message: String) : WriteKudoEvent()
}
```

---

## Integration Testing Strategy

### Test Scope

- [x] **Component/UI ↔ ViewModel**: form field changes update state; derived state controls button enabled
- [x] **ViewModel ↔ Use Cases**: `SubmitKudoUseCase` orchestrates upload + insert correctly
- [x] **Data Layer**: `KudosDataSource.insertKudo`, `KudosDataSource.insertKudosHashtags` build correct payloads
- [x] **Storage**: `StorageDataSource.uploadImage` validates MIME/size before sending

### Test Categories

| Category | Applicable | Key Scenarios |
|----------|------------|---------------|
| UI ↔ Logic | Yes | isSubmitEnabled transitions; inline error appearance; Cancel dialog |
| Service ↔ Service | Yes | SubmitKudoUseCase: upload failure aborts kudos insert |
| App ↔ Supabase | Yes (faked) | kudos INSERT, kudos_hashtags INSERT, Storage upload |
| Cross-feature | Yes | Cache invalidation on success; KudosViewModel reload |

### Mocking Strategy

| Dependency | Strategy | Rationale |
|------------|----------|-----------|
| `KudosDataSource` | Fake subclass (`open class`) | Consistent with existing pattern in codebase |
| `ProfileDataSource` | Fake subclass | Same pattern |
| `StorageDataSource` | Fake subclass | No real storage in unit tests |
| `SupabaseClient` | `StubSupabaseClient` | Already exists in test directory |
| `RichTextState` | Real | Library is pure Kotlin, no Android deps |

### Coverage Goals

| Area | Target | Priority |
|------|--------|----------|
| `WriteKudoViewModel` | ≥ 90% | High |
| `SubmitKudoUseCase` | ≥ 85% | High |
| `WriteKudoDto` mapper | ≥ 90% | High |
| `StorageDataSource` validation logic | ≥ 90% | High |

---

## Risk Assessment

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| R1 — `richeditor-compose` version compatibility with existing Compose BOM | Medium | High | Pin version; test on minSdk 24; fallback: `BasicTextField` + `AnnotatedString` for basic formatting |
| R2 — Hashtag source unknown (Q1) | High | Medium | Default to `UserStatsDataSource.getHashtags()` (already implemented); adjust if predefined-list endpoint differs |
| R3 — Image Storage bucket name unknown (Q4) | High | Low | Hardcode `"kudos-images"` as constant; make it configurable via `BuildConfig` |
| R4 — @mention storage format unknown (Q3) | Medium | Medium | Default: embed as HTML `<a data-mention-id="...">@Name</a>` in message field; document assumption |
| R5 — Success screen nav args (Q5) | Low | Low | Pass `kudosId: String` as optional nav arg to success route; success screen can ignore if not needed |
| R6 — `formDirty` false positive on prefill | Low | Low | Set `formDirty = false` after prefill completes; only set `true` on user-initiated field changes |

---

## Assumptions (Open Questions resolved for implementation)

| Q | Resolution for this plan |
|---|--------------------------|
| Q1 — Hashtag source | Use existing `UserStatsRepository.getHashtags()` (reads from `public.hashtags` table). Update if backend changes. |
| Q2 — Confirm dialog scope | `formDirty = true` when ANY field is changed by user (including recipient selection, hashtag addition). Matches broadest safe interpretation. |
| Q3 — @mention storage | Embed in `message` HTML string as `<span data-mention-id="{userId}">@Name</span>`. Document in code. |
| Q4 — Image bucket | Bucket name constant `"kudos-images"`; extracted to `StorageConstants.kt`. |
| Q5 — Success screen nav args | Navigate with `kudosId` as string arg: `NavRoutes.WriteKudoSuccess.createRoute(kudosId)`. |
| Q6 — Offline | Show immediate error snackbar ("No network connection. Please try again."); no local queue. |
| Q7 — Validation model | Hybrid: Send disabled on empty fields; on-submit validation for constraint violations. |

---

## Constitution Compliance Notes

- **Rule 4 (no business logic in Composable)**: All validation, dirty-check, form state, and submission logic lives in `WriteKudoViewModel`. Composables only invoke `viewModel.onXxx()` lambdas and observe `uiState`.
- **Rule NavArgs (§4.3)**: `write_kudo?recipientId={recipientId}&recipientName={recipientName}` (query-param style for optional args). Both declared as `nullable StringType` in `navArgument("recipientId") { type = NavType.StringType; nullable = true }`. Decoded in `SaaNavHost` from `backStackEntry.arguments`.
- **Rule sealed Result<T> (§7)**: All `suspend fun` in repository/use-case returns `Result<T>`; `WriteKudoViewModel` maps `Result.failure` to `uiState.error` string.
- **Rule no `!!` (§3)**: `recipientId` and current user ID accessed via safe call with `requireNotNull()` in ViewModel methods where null would indicate a programming error.
- **Rule Timber (§7)**: All caught exceptions logged as `Timber.e(it, "submitKudo failed")`.

---

## Dependencies & Prerequisites

### Required Before Start

- [x] `spec.md` reviewed and approved (status: `spec`)
- [x] `design-style.md` complete
- [x] `KudosModule.kt` DI module exists and is wired
- [x] `StubSupabaseClient.kt` exists in test directory
- [x] `NavRoutes.WriteKudo` already defined
- [ ] Confirm Supabase Storage bucket name for images (Q4 — defaulting to `"kudos-images"`)
- [ ] Confirm `[iOS] Sun*Kudos_Gửi lời chúc Kudos` screen route exists or create placeholder

### External Dependencies

- Supabase: `public.kudos` table (INSERT), `public.kudos_hashtags` (INSERT), `public.hashtags` (SELECT), `public.profiles` or equivalent for recipient search (SELECT)
- Supabase Storage: bucket for kudo images (confirm name)

---

## Next Steps

After plan approval:

1. **Run** `/momorph.tasks` to generate task breakdown
2. **Answer** Open Questions Q1–Q7 with product/tech team before Phase 1 begins
3. **Begin** Phase 0 (dependencies + scaffolding) — unblocked today
4. **Block** Phases 1+ on Q4 (Storage bucket) and Q3 (@mention storage format)
