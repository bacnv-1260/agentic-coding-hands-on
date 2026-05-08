# Tasks: Write Kudo Screen

**Frame**: `7fFAb-K35a-iOS-WriteKudo`
**Spec**: `specs/7fFAb-K35a-iOS-WriteKudo/spec.md`
**Plan**: `specs/7fFAb-K35a-iOS-WriteKudo/plan.md`

---

## Task Format

```
- [ ] T### [P] [US#] Description | file/path.kt
```

- **[P]**: Can run in parallel with other [P] tasks in the same phase
- **[US#]**: Which user story this task contributes to
- **|**: Primary file created or modified

---

## Phase 0: Dependencies & Scaffolding

**Purpose**: Add the new library, wire the existing route properly, and create empty stubs so the project builds before any real logic is written.

**⚠️ All tasks in Phase 0 must be complete before any subsequent phase can start.**

- [ ] T001 Add `richeditor-compose = "1.0.0-rc08"` to `[versions]` and `[libraries]` sections | android/gradle/libs.versions.toml
- [ ] T002 Add `implementation(libs.richeditor.compose)` dependency | android/app/build.gradle.kts
- [ ] T003 Modify `NavRoutes.WriteKudo` — change route string to `"write_kudo?recipientId={recipientId}&recipientName={recipientName}"`; add `createRoute(recipientId: String?, recipientName: String?)` helper | android/app/src/main/java/com/example/saa/NavRoutes.kt
- [ ] T004 Update 3 navigate callsites to use `NavRoutes.WriteKudo.createRoute(null, null)` (SaaNavHost.kt lines 110 + 167, HomeScreen.kt line 187) | android/app/src/main/java/com/example/saa/SaaNavHost.kt, android/app/src/main/java/com/example/saa/presentation/ui/home/HomeScreen.kt
- [ ] T005 Update `SaaNavHost.kt` composable entry — declare `navArgument("recipientId")` + `navArgument("recipientName")` as nullable `StringType` | android/app/src/main/java/com/example/saa/SaaNavHost.kt
- [ ] T006 [P] Create `WriteKudoUiState.kt` with full `data class WriteKudoUiState` + `sealed class WriteKudoEvent` as defined in plan.md | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoUiState.kt
- [ ] T007 [P] Create stub `WriteKudoViewModel.kt` — `@HiltViewModel`, exposes `StateFlow<WriteKudoUiState>` and `Channel<WriteKudoEvent>`, no logic yet | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoViewModel.kt
- [ ] T008 Create stub `WriteKudoScreen.kt` — root composable, collects `uiState`, renders `Text("TODO")` placeholder, wires `BackHandler` skeleton | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoScreen.kt
- [ ] T009 Replace `Surface { Text("Write Kudo") }` placeholder in `SaaNavHost.kt` with `WriteKudoScreen(...)` call passing nav callbacks | android/app/src/main/java/com/example/saa/SaaNavHost.kt
- [ ] T010 Verify project builds and app launches without errors (`./gradlew :app:assembleDebug`)

**Checkpoint**: Project builds cleanly with scaffolding in place ✓

---

## Phase 1: Domain & Data Layer

**Purpose**: Implement all backend-facing logic — domain models, repository extensions, use cases, and data sources. No UI yet.

**⚠️ Depends on Phase 0 completion. All [P] tasks within this phase can proceed in parallel.**

### Domain Models & Interfaces

- [ ] T011 [P] Create `WriteKudoRequest.kt` domain model (recipientId, title, message, hashtagIds, photoUrls, isAnonymous) | android/app/src/main/java/com/example/saa/domain/model/WriteKudoRequest.kt
- [ ] T012 [P] Add `suspend fun submitKudo(request: WriteKudoRequest): Result<String>` to `KudosRepository` interface | android/app/src/main/java/com/example/saa/domain/repository/KudosRepository.kt
- [ ] T013 [P] Add `suspend fun searchProfiles(query: String): Result<List<Profile>>` to `ProfileRepository` interface | android/app/src/main/java/com/example/saa/domain/repository/ProfileRepository.kt

### DTOs

- [ ] T014 [P] Create `WriteKudoDto.kt` — `@Serializable`, all fields nullable, includes `anonymous_nickname`, `award_category_name`, `photo_urls` (see plan.md DB discrepancy notes) | android/app/src/main/java/com/example/saa/data/remote/dto/WriteKudoDto.kt

### Data Sources

- [ ] T015 Add `insertKudo(dto: WriteKudoDto): String` to `KudosDataSource` — uses `.insert(dto).select().decodeSingle<WriteKudoDto>()` to return the generated kudos `id` | android/app/src/main/java/com/example/saa/data/remote/source/KudosDataSource.kt
- [ ] T016 Add `insertKudosHashtags(kudosId: String, hashtagIds: List<String>)` to `KudosDataSource` — batch insert into `kudos_hashtags` join table | android/app/src/main/java/com/example/saa/data/remote/source/KudosDataSource.kt
- [ ] T017 [P] Add `searchProfiles(query: String): List<ProfileDto>` to `ProfileDataSource` — SELECT from `public.profiles` filtered by `full_name ilike %query%`, exclude current user id | android/app/src/main/java/com/example/saa/data/remote/source/ProfileDataSource.kt
- [ ] T018 [P] Create `StorageDataSource.kt` — `open class`, `@Inject constructor(supabase: SupabaseClient)`, `suspend fun uploadImage(uriString: String, bucket: String): String` with client-side MIME (`image/jpeg`, `image/png`, `image/webp`) + 10 MB size guard before upload | android/app/src/main/java/com/example/saa/data/remote/source/StorageDataSource.kt

### Repository Implementations

- [ ] T019 Implement `submitKudo()` in `KudosRepositoryImpl` — wraps `insertKudo` result in `Result<String>`, logs failures via Timber | android/app/src/main/java/com/example/saa/data/repository/KudosRepositoryImpl.kt
- [ ] T020 Implement `searchProfiles()` in `ProfileRepositoryImpl` — wraps `ProfileDataSource.searchProfiles()` in `Result<List<Profile>>` | android/app/src/main/java/com/example/saa/data/repository/ProfileRepositoryImpl.kt

### Use Cases

- [ ] T021 Create `SubmitKudoUseCase.kt` — orchestrates: (1) upload each image via `StorageDataSource` → collect URLs, abort all on first failure; (2) call `kudosRepository.submitKudo(request.copy(photoUrls = uploadedUrls))`; (3) call `KudosDataSource.insertKudosHashtags(kudosId, request.hashtagIds)`; returns `Result<String>` (kudosId) | android/app/src/main/java/com/example/saa/domain/usecase/SubmitKudoUseCase.kt
- [ ] T022 [P] Create `SearchProfilesUseCase.kt` — delegates to `profileRepository.searchProfiles(query)` | android/app/src/main/java/com/example/saa/domain/usecase/SearchProfilesUseCase.kt

### DI

- [ ] T023 Register `StorageDataSource` in `KudosModule.kt` if interface-bound; add `@Binds` or `@Provides` as appropriate | android/app/src/main/java/com/example/saa/di/KudosModule.kt

### Tests (Phase 1)

- [ ] T024 [P] Write `SubmitKudoUseCaseTest` — scenarios: happy path (images + hashtags), upload fails aborts insert, insert fails returns failure, empty photoUrls skips upload | android/app/src/test/java/com/example/saa/domain/usecase/SubmitKudoUseCaseTest.kt
- [ ] T025 [P] Write `WriteKudoDtoTest` — verify `@SerialName` mappings for `award_category_name`, `photo_urls`, `anonymous_nickname`, `is_anonymous` | android/app/src/test/java/com/example/saa/data/remote/dto/WriteKudoDtoTest.kt
- [ ] T026 [P] Write `StorageDataSourceTest` — verify MIME rejection, size rejection (>10 MB), valid file passes through | android/app/src/test/java/com/example/saa/data/remote/source/StorageDataSourceTest.kt
- [ ] T027 [P] Write `KudosRepositoryImplSubmitTest` — verify `submitKudo` wraps data source result correctly | android/app/src/test/java/com/example/saa/data/repository/KudosRepositoryImplSubmitTest.kt

**Checkpoint**: All data/domain layer tests pass. `./gradlew :app:testDebugUnitTest` green ✓

---

## Phase 2: Core Form UI + Cancel (US-1 happy path, US-2)

**Purpose**: Build the visible form skeleton with all fields wired to ViewModel. Deliver the basic happy path and cancel flow.

**⚠️ Depends on Phase 1 completion. [P] tasks within this phase can proceed in parallel once T028 is done.**

### ViewModel Logic

- [ ] T028 Implement `WriteKudoViewModel` core — inject `SubmitKudoUseCase`, `GetHashtagsUseCase`; implement `onTitleChanged`, `onMessageChanged`, `onAnonymousToggled`, `onSendClick` (validates + calls use case), `onCancelRequested`, `onCancelConfirmed`, `onCancelDismissed`; `formDirty` tracking; `isSubmitEnabled` derived from state | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoViewModel.kt

### UI Components

- [ ] T029 [P] Create `WriteKudoFormCard.kt` — scrollable `Column` inside a `Card`; keyvisual background scaffold; hosts all child form sections | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/WriteKudoFormCard.kt
- [ ] T030 [P] Create `TitleInputField.kt` — B.3 label with `*` indicator, B.4 `OutlinedTextField` (max 100 chars, inline char count), B.5 `Text` link ("Tiêu chuẩn cộng đồng") calling `onCommunityLinkClick` lambda; shows `titleError` below field | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/TitleInputField.kt
- [ ] T031 [P] Create `FormattingToolbar.kt` — C container Row; C.1/C.2/C.3/C.4/C.6 as toggle `IconButton`s driven by `RichTextState`; C.5 Link as action `IconButton` that calls `onLinkClick` lambda (opens link dialog, NOT a toggle) | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/FormattingToolbar.kt
- [ ] T032 [P] Create `RichTextEditor.kt` — D: `RichTextEditor` composable from richeditor-compose bound to `RichTextState`; placeholder text; D.1: static hint text below ("@ + tên để nhắc tới đồng nghiệp"); calls `onMessageChanged(richTextState.toHtml())` on text change | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/RichTextEditor.kt
- [ ] T033 [P] Create `MessageCharCounter.kt` — displays `"${charCount}/1000"` right-aligned; color changes to error color when charCount > 1000 | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/MessageCharCounter.kt
- [ ] T034 [P] Create `AnonymousToggle.kt` — G: `Row` with `Checkbox` (default unchecked, `Role.Checkbox` semantic) + label "Gửi lời cám ơn và ghi nhận ẩn danh"; calls `onAnonymousToggled(Boolean)` lambda | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/AnonymousToggle.kt
- [ ] T035 [P] Create `WriteKudoBottomBar.kt` — H: Cancel `OutlinedButton` (always enabled, calls `onCancelClick`); I: Send `Button` (`enabled = isSubmitEnabled`; shows `CircularProgressIndicator` when `isSending`; icon: send arrow) | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/WriteKudoBottomBar.kt
- [ ] T036 Create `WriteKudoScreen.kt` (full) — compose all components; wire `BackHandler` (enabled when `formDirty`); consume `WriteKudoEvent` channel via `LaunchedEffect`; show `AlertDialog` when `showCancelDialog = true`; show link-input `AlertDialog` when `showLinkDialog = true` (C.5) | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoScreen.kt

### Tests (Phase 2)

- [ ] T037 [P] Write `WriteKudoViewModelTest` — scenarios: `isSubmitEnabled` false when any required field empty; `isSubmitEnabled` true when all filled; `formDirty` set on first field edit; `showCancelDialog` true when `onCancelRequested` with dirty form; `showCancelDialog` false when form is clean and cancel → `NavigateBack` emitted | android/app/src/test/java/com/example/saa/presentation/ui/writekudo/WriteKudoViewModelTest.kt
- [ ] T038 [P] Write `WriteKudoViewModelSubmitTest` — scenarios: submit success → `NavigateToSuccess` event emitted; submit failure → `ShowError` event emitted + `isSending` reset to false; title > 100 chars on submit → `titleError` set | android/app/src/test/java/com/example/saa/presentation/ui/writekudo/WriteKudoViewModelSubmitTest.kt

**Checkpoint**: US-1 happy path (all required fields + submit) and US-2 cancel flow work end-to-end in emulator ✓

---

## Phase 3: Recipient & Hashtag Pickers (US-1 — pickers, US-5)

**Purpose**: Complete the recipient search overlay and hashtag picker sheet. Implement US-5 community standards link.

**⚠️ Depends on Phase 2 (T028) completion. [P] tasks within this phase can proceed in parallel.**

### ViewModel Logic

- [ ] T039 Extend `WriteKudoViewModel` — inject `SearchProfilesUseCase`; add `onRecipientDropdownClicked` (sets `showRecipientSearch = true`), `onRecipientSelected(id, name)` (sets recipientId + recipientName, clears `recipientError`, `formDirty = true`), `onRecipientSearchDismissed`; add `onHashtagPickerOpened` (sets `showHashtagPicker = true`, loads `availableHashtags` from `GetHashtagsUseCase` if not yet loaded), `onHashtagSelected(hashtag)`, `onHashtagRemoved(hashtag)`, `onHashtagPickerDismissed`; self-kudo guard in `onRecipientSelected` | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoViewModel.kt

### UI Components

- [ ] T040 [P] Create `RecipientDropdownField.kt` — B.1 label Row + B.2 tappable `Box` (shows recipient name or placeholder "Tìm kiếm", chevron icon); disabled state when `isRecipientListUnavailable`; shows `recipientError` below; calls `onDropdownClick` lambda | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/RecipientDropdownField.kt
- [ ] T041 [P] Create `RecipientSearchSheet.kt` — `ModalBottomSheet` (visible when `showRecipientSearch`); search `TextField`; `LazyColumn` of profile results (name + department); empty-state text when no results; loading state; calls `onRecipientSelected(id, name)` on item tap; calls `onDismiss` on close | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/search/RecipientSearchSheet.kt
- [ ] T042 [P] Create `HashtagSection.kt` — E.1 label with `*`; E.2: `FlowRow` of selected hashtag chips (each with `×` remove); `+ Hashtag` `AssistChip` trigger (calls `onAddHashtagClick`, disabled + greyed when 5 tags selected); shows `hashtagError` below | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/HashtagSection.kt
- [ ] T043 [P] Create `HashtagPickerSheet.kt` — `ModalBottomSheet` (visible when `showHashtagPicker`); `LazyColumn` of `availableHashtags`; already-selected tags shown as checked/highlighted; tapping a tag calls `onHashtagSelected(hashtag)`; calls `onDismiss` on close | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/HashtagPickerSheet.kt
- [ ] T044 Wire `RecipientSearchSheet` and `HashtagPickerSheet` visibility into `WriteKudoScreen.kt` driven by `uiState.showRecipientSearch` and `uiState.showHashtagPicker` | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoScreen.kt
- [ ] T045 Implement US-5: wire B.5 `onCommunityLinkClick` in `WriteKudoScreen` → `navController.navigate(NavRoutes.CommunityStandards.route)` (create placeholder route if it does not yet exist) | android/app/src/main/java/com/example/saa/NavRoutes.kt, android/app/src/main/java/com/example/saa/SaaNavHost.kt

### Tests (Phase 3)

- [ ] T046 [P] Extend `WriteKudoViewModelTest` — scenarios: `onRecipientSelected` sets recipientId + name + formDirty; `onRecipientSelected` with own userId → recipientError set + isSubmitEnabled false; `onHashtagSelected` adds to hashtags; `onHashtagRemoved` removes; selecting 5 hashtags → `canAddHashtag = false` | android/app/src/test/java/com/example/saa/presentation/ui/writekudo/WriteKudoViewModelTest.kt

**Checkpoint**: Recipient search, hashtag picker, and community standards link all work in emulator. US-1 all scenarios testable ✓

---

## Phase 4: Image Upload (US-4)

**Purpose**: Implement the image attachment flow — file picker, client-side validation, upload to Supabase Storage, thumbnail display.

**⚠️ Depends on Phase 2 completion (ViewModel + UiState). Can proceed in parallel with Phase 3.**

### ViewModel Logic

- [ ] T047 Extend `WriteKudoViewModel` — add `onImagesSelected(uriStrings: List<String>)` (appends up to 5 total; triggers `StorageDataSource.uploadImage` per item in coroutine; updates `imageIds` from local URI → remote URL on success; shows error snackbar on failure); add `onImageRemoved(uri: String)` | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoViewModel.kt

### UI Components

- [ ] T048 Create `ImageSection.kt` — F.1 label; F.2–F.4: `LazyRow` of image thumbnails (each: `AsyncImage` from Coil + `×` `IconButton` overlay, `contentDescription = "Remove image"`); F.5 `+ Image` `AssistChip`/Button (hidden when `imageIds.size == 5`; calls `onAddImageClick`); shows per-image loading indicator while uploading | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/ImageSection.kt
- [ ] T049 Wire `ActivityResultContracts.GetMultipleContents` launcher in `WriteKudoScreen.kt` — on result convert each `Uri` to `String` via `uri.toString()` in the Composable before calling `viewModel.onImagesSelected(uriStrings)` | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoScreen.kt

### Tests (Phase 4)

- [ ] T050 [P] Extend `WriteKudoViewModelTest` — scenarios: `onImagesSelected` appends; selecting when 5 images already present is no-op or shows error; `onImageRemoved` removes item; `canAddImage = false` at limit | android/app/src/test/java/com/example/saa/presentation/ui/writekudo/WriteKudoViewModelTest.kt
- [ ] T051 [P] Extend `SubmitKudoUseCaseTest` — scenarios: image upload failure → kudos insert NOT called; partial upload failure → all URLs from successful uploads are NOT persisted (atomic abort) | android/app/src/test/java/com/example/saa/domain/usecase/SubmitKudoUseCaseTest.kt

**Checkpoint**: Image attachment, upload, and removal work end-to-end. US-4 all scenarios testable ✓

---

## Phase 5: Polish & Accessibility

**Purpose**: Cross-cutting quality concerns — accessibility, inline errors, loading states, previews, cache invalidation.

**⚠️ Depends on Phases 2–4. All [P] tasks within this phase can proceed in parallel.**

- [ ] T052 [P] Add `contentDescription` strings to all interactive `IconButton`s, `Checkbox`, thumbnails `×`, `+ Hashtag`, `+ Image`, Send, Cancel (constitution §4.4) | all component files
- [ ] T053 [P] Verify all toolbar buttons + remove buttons use `Modifier.minimumInteractiveComponentSize()` for ≥ 48 dp touch target | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/components/FormattingToolbar.kt, ImageSection.kt
- [ ] T054 [P] Wrap all inline error `Text` composables in `AnimatedVisibility(visible = error != null)` for smooth entry/exit | TitleInputField.kt, HashtagSection.kt, RecipientDropdownField.kt
- [ ] T055 [P] Implement error snackbar: `LaunchedEffect(event)` in `WriteKudoScreen` consuming `WriteKudoEvent.ShowError` → `snackbarHostState.showSnackbar(message)` | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoScreen.kt
- [ ] T056 [P] Implement C.5 link dialog: `AlertDialog` with `TextField` for URL input + Confirm/Cancel; validate `Patterns.WEB_URL` before calling `richTextState.addLink(text, url)`; show inline error if URL invalid | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoScreen.kt
- [ ] T057 Cache invalidation on submit success — emit a reload signal to `KudosViewModel` (via shared repository cache bust or `SharedFlow` in `KudosRepository`) so the Kudos feed refreshes after returning from Write Kudo | android/app/src/main/java/com/example/saa/data/repository/KudosRepositoryImpl.kt
- [ ] T058 [P] Add `@Preview` annotations (light + dark mode via `uiMode = UI_MODE_NIGHT_YES`) to all leaf composables: `TitleInputField`, `FormattingToolbar`, `RichTextEditor`, `HashtagSection`, `ImageSection`, `AnonymousToggle`, `WriteKudoBottomBar`, `RecipientDropdownField` | all component files
- [ ] T059 [P] Add `@Preview` to `WriteKudoScreen` — one with empty state (Send disabled), one with all fields filled (Send enabled), one with `isSending = true` | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoScreen.kt
- [ ] T060 [P] Implement US-3 anonymous nickname: in `WriteKudoViewModel.onSendClick()` when `isAnonymous = true`, set `anonymousNickname` to a generated value (e.g. current user's `employeeCode` obfuscated, or constant `"Ẩn danh"`) in `WriteKudoDto` | android/app/src/main/java/com/example/saa/presentation/ui/writekudo/WriteKudoViewModel.kt

**Checkpoint**: All 5 user stories complete, all tests pass, previews visible in IDE ✓

---

## Dependencies & Execution Order

```
Phase 0 (T001–T010)
    └── Phase 1 (T011–T027)  ← all T011+ blocked until T010 green
            └── Phase 2 (T028–T038)  ← all Phase 2 blocked until Phase 1 complete
                    ├── Phase 3 (T039–T046)  ← can start after T028
                    ├── Phase 4 (T047–T051)  ← can start after T028, in parallel with Phase 3
                    └── Phase 5 (T052–T060)  ← after Phases 2–4 complete
```

### Within Each Phase — Parallel Opportunities

| Phase | Serial dependency | Parallelisable |
|-------|-------------------|----------------|
| 0 | T001 → T002 (gradle sync); T003 → T004 → T005 (nav chain) | T006, T007 can run while T003–T005 run |
| 1 | T015, T016 (KudosDataSource methods); T021 needs T015+T016+T018 | T011–T014, T017, T018 all parallel; T024–T027 parallel |
| 2 | T028 must complete first | T029–T035 parallel after T028; T037–T038 parallel |
| 3 | T039 must complete first | T040–T044 parallel after T039 |
| 4 | T047 must complete first | T048–T049 after T047; T050–T051 parallel |
| 5 | None | T052–T060 all parallel |

---

## Commit Strategy

Commit after each checkpoint (not each task). Suggested commit messages:

```
feat(writekudo): Phase 0 — scaffolding + NavRoutes modification
feat(writekudo): Phase 1 — domain + data layer with tests
feat(writekudo): Phase 2 — core form UI + cancel flow (US-1 happy path, US-2)
feat(writekudo): Phase 3 — recipient search + hashtag picker (US-1 pickers, US-5)
feat(writekudo): Phase 4 — image upload flow (US-4)
feat(writekudo): Phase 5 — accessibility, polish, previews
```

---

## Open Questions (blocking tasks)

| Q | Blocking | Default assumed |
|---|----------|-----------------|
| Q3 — @mention storage format | T021 (`SubmitKudoUseCase` — message serialization) | HTML `<span data-mention-id="{userId}">@Name</span>` embedded in message |
| Q4 — Storage bucket name | T018 (`StorageDataSource`) | `"kudos-images"` constant in `StorageConstants.kt` |
| Q5 — Success screen route / nav args | T028 (`onSendClick` navigation), T009 (`SaaNavHost`) | Navigate with `kudosId` string arg |
