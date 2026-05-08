package com.example.saa.presentation.ui.writekudo

import androidx.lifecycle.SavedStateHandle
import com.example.saa.domain.model.Department
import com.example.saa.domain.model.GiftRecipient
import com.example.saa.domain.model.Hashtag
import com.example.saa.domain.model.Kudos
import com.example.saa.domain.model.KudosFilter
import com.example.saa.domain.model.Profile
import com.example.saa.domain.model.WriteKudoRequest
import com.example.saa.domain.model.UserStats
import com.example.saa.domain.repository.KudosRepository
import com.example.saa.domain.repository.ProfileRepository
import com.example.saa.domain.repository.UserStatsRepository
import com.example.saa.domain.usecase.GetHashtagsUseCase
import com.example.saa.domain.usecase.ImageUploader
import com.example.saa.domain.usecase.SearchProfilesUseCase
import com.example.saa.domain.usecase.SubmitKudoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// ── Design mapping ────────────────────────────────────────────────────────────
// B.2  Recipient Search Dropdown  → recipientId / recipientName / showRecipientSearch
// B.4  Title Input                → title / titleError / isSubmitEnabled
// D    Message Textarea           → message / messageCharCount
// E.2  Hashtag chips              → hashtags / canAddHashtag / showHashtagPicker
// F.5  Add Image                  → imageIds / canAddImage
// G    Anonymous Toggle           → isAnonymous
// H    Cancel Button              → showCancelDialog / NavigateBack event
// I    Send Button                → isSubmitEnabled / isSending / NavigateToSuccess event
// US-1 Scenario 2-4               → isSubmitEnabled false conditions
// US-1 Scenario 5                 → error snackbar on failure
// US-1 Scenario 6                 → self-kudo recipientError
// US-2 Scenario 1                 → back with empty form → immediate navigate
// US-2 Scenario 2                 → back with dirty form → showCancelDialog
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalCoroutinesApi::class)
class WriteKudoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    // ── Fixture hashtags ──────────────────────────────────────────────────────
    private val h1 = Hashtag("h1", "teamwork")
    private val h2 = Hashtag("h2", "innovation")
    private val h3 = Hashtag("h3", "leadership")
    private val h4 = Hashtag("h4", "quality")
    private val h5 = Hashtag("h5", "collaboration")
    private val h6 = Hashtag("h6", "extra")
    private val allHashtags = listOf(h1, h2, h3, h4, h5, h6)

    private val sampleProfile = Profile(
        id = "p1",
        fullName = "Nguyen Van A",
        employeeCode = "EMP001",
        avatarUrl = null,
        badgeType = "",
        heroTier = 0,
    )

    private lateinit var statsRepo: FakeUserStatsRepository
    private lateinit var profileRepo: FakeProfileRepository
    private lateinit var imageUploader: FakeImageUploader
    private lateinit var kudosRepo: FakeWriteKudosRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        statsRepo = FakeUserStatsRepository(hashtags = allHashtags)
        profileRepo = FakeProfileRepository(searchResult = listOf(sampleProfile))
        imageUploader = FakeImageUploader()
        kudosRepo = FakeWriteKudosRepository(submitResult = Result.success("kudo-new-1"))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(
        recipientId: String? = null,
        recipientName: String? = null,
    ): WriteKudoViewModel {
        val handle = SavedStateHandle(
            buildMap {
                if (recipientId != null) put("recipientId", recipientId)
                if (recipientName != null) put("recipientName", recipientName)
            },
        )
        return WriteKudoViewModel(
            savedStateHandle = handle,
            getHashtagsUseCase = GetHashtagsUseCase(statsRepo),
            searchProfilesUseCase = SearchProfilesUseCase(profileRepo),
            submitKudoUseCase = SubmitKudoUseCase(imageUploader, kudosRepo),
        )
    }

    // ── TC-WK-VM-001: Init — no prefill ──────────────────────────────────────
    // B.2: recipientId null when not provided via deep link
    @Test
    fun `init - no prefill then recipientId is null and formDirty is false`() = runTest {
        val vm = buildViewModel()
        val state = vm.uiState.value

        assertNull(state.recipientId)
        assertEquals("", state.recipientName)
        assertFalse(state.formDirty)
    }

    // ── TC-WK-VM-002: Init — with prefill ────────────────────────────────────
    // B.2: prefill from Search Sunner deep link
    @Test
    fun `init - with prefill then recipientId and name are populated`() = runTest {
        val vm = buildViewModel(recipientId = "r1", recipientName = "Nguyen Van A")
        val state = vm.uiState.value

        assertEquals("r1", state.recipientId)
        assertEquals("Nguyen Van A", state.recipientName)
    }

    // ── TC-WK-VM-003: Init loads available hashtags ──────────────────────────
    // E.2: hashtag picker needs available options
    @Test
    fun `init - available hashtags loaded from use case`() = runTest {
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(allHashtags, vm.uiState.value.availableHashtags)
    }

    // ── TC-WK-VM-004: isSubmitEnabled false by default ───────────────────────
    // I: Send button disabled initially (US-1 Scenario 2)
    @Test
    fun `init - isSubmitEnabled is false before any fields are filled`() = runTest {
        val vm = buildViewModel()
        assertFalse(vm.uiState.value.isSubmitEnabled)
    }

    // ── TC-WK-VM-005: isSubmitEnabled true when all required fields filled ───
    // I: all fields valid → Send enabled (US-1 Scenario 1)
    @Test
    fun `isSubmitEnabled - true when recipient title message and hashtag all provided`() = runTest {
        val vm = buildViewModel(recipientId = "r1", recipientName = "Alice")
        vm.onTitleChange("Best Dev")
        vm.onMessageChange("<p>Great work!</p>")
        vm.toggleHashtag(h1)

        assertTrue(vm.uiState.value.isSubmitEnabled)
    }

    // ── TC-WK-VM-006: isSubmitEnabled false when no recipient ────────────────
    // I: disabled — US-1 Scenario 2
    @Test
    fun `isSubmitEnabled - false when recipientId is null`() = runTest {
        val vm = buildViewModel()
        vm.onTitleChange("Best Dev")
        vm.onMessageChange("<p>Great work!</p>")
        vm.toggleHashtag(h1)

        assertFalse(vm.uiState.value.isSubmitEnabled)
    }

    // ── TC-WK-VM-007: isSubmitEnabled false when title empty ─────────────────
    // I: disabled — US-1 Scenario 3a
    @Test
    fun `isSubmitEnabled - false when title is empty`() = runTest {
        val vm = buildViewModel(recipientId = "r1")
        vm.onMessageChange("<p>Great work!</p>")
        vm.toggleHashtag(h1)

        assertFalse(vm.uiState.value.isSubmitEnabled)
    }

    // ── TC-WK-VM-008: isSubmitEnabled false when no hashtag ──────────────────
    // I: disabled — US-1 Scenario 4
    @Test
    fun `isSubmitEnabled - false when no hashtag is selected`() = runTest {
        val vm = buildViewModel(recipientId = "r1")
        vm.onTitleChange("Best Dev")
        vm.onMessageChange("<p>Great work!</p>")

        assertFalse(vm.uiState.value.isSubmitEnabled)
    }

    // ── TC-WK-VM-009: onTitleChange updates title and marks dirty ─────────────
    // B.4: typing in title field
    @Test
    fun `onTitleChange - updates title and sets formDirty to true`() = runTest {
        val vm = buildViewModel()
        vm.onTitleChange("Hero of the Week")

        assertEquals("Hero of the Week", vm.uiState.value.title)
        assertTrue(vm.uiState.value.formDirty)
    }

    // ── TC-WK-VM-010: onTitleChange clears titleError ────────────────────────
    // B.4: error clears when user starts typing
    @Test
    fun `onTitleChange - clears any existing titleError`() = runTest {
        val vm = buildViewModel(recipientId = "r1")
        vm.onMessageChange("msg")
        vm.toggleHashtag(h1)
        vm.onTitleChange("A".repeat(101)) // trigger title error
        vm.onSendClick()
        assertNotNull(vm.uiState.value.titleError)

        vm.onTitleChange("Fixed title")
        assertNull(vm.uiState.value.titleError)
    }

    // ── TC-WK-VM-011: onMessageChange updates message and marks dirty ─────────
    // D: typing in message textarea
    @Test
    fun `onMessageChange - updates message html and sets formDirty to true`() = runTest {
        val vm = buildViewModel()
        vm.onMessageChange("<p>Thank you!</p>")

        assertEquals("<p>Thank you!</p>", vm.uiState.value.message)
        assertTrue(vm.uiState.value.formDirty)
        assertEquals("<p>Thank you!</p>".length, vm.uiState.value.messageCharCount)
    }

    // ── TC-WK-VM-012: toggleHashtag adds new hashtag ─────────────────────────
    // E.2: selecting hashtag from picker
    @Test
    fun `toggleHashtag - adds hashtag when not already in list`() = runTest {
        val vm = buildViewModel()
        vm.toggleHashtag(h1)

        assertTrue(vm.uiState.value.hashtags.any { it.id == "h1" })
        assertTrue(vm.uiState.value.formDirty)
    }

    // ── TC-WK-VM-013: toggleHashtag removes existing hashtag ─────────────────
    // E.2: tapping selected chip removes it
    @Test
    fun `toggleHashtag - removes hashtag when already in list`() = runTest {
        val vm = buildViewModel()
        vm.toggleHashtag(h1)
        vm.toggleHashtag(h1)

        assertFalse(vm.uiState.value.hashtags.any { it.id == "h1" })
    }

    // ── TC-WK-VM-014: toggleHashtag respects max 5 limit ─────────────────────
    // E.2: max 5 hashtags; extra tap is ignored
    @Test
    fun `toggleHashtag - does not add beyond 5 hashtags`() = runTest {
        val vm = buildViewModel()
        listOf(h1, h2, h3, h4, h5).forEach { vm.toggleHashtag(it) }

        assertEquals(5, vm.uiState.value.hashtags.size)
        assertFalse("canAddHashtag should be false at limit", vm.uiState.value.canAddHashtag)

        vm.toggleHashtag(h6) // 6th → ignored
        assertEquals(5, vm.uiState.value.hashtags.size)
    }

    // ── TC-WK-VM-015: removeHashtag removes specific tag ─────────────────────
    // E.2: tapping ✕ on chip
    @Test
    fun `removeHashtag - removes specified hashtag while keeping others`() = runTest {
        val vm = buildViewModel()
        vm.toggleHashtag(h1)
        vm.toggleHashtag(h2)
        vm.removeHashtag(h1)

        assertFalse(vm.uiState.value.hashtags.any { it.id == "h1" })
        assertTrue(vm.uiState.value.hashtags.any { it.id == "h2" })
    }

    // ── TC-WK-VM-016: onAnonymousToggle flips isAnonymous ─────────────────────
    // G: anonymous toggle checkbox
    @Test
    fun `onAnonymousToggle - flips isAnonymous and marks formDirty`() = runTest {
        val vm = buildViewModel()
        assertFalse(vm.uiState.value.isAnonymous)

        vm.onAnonymousToggle()
        assertTrue(vm.uiState.value.isAnonymous)
        assertTrue(vm.uiState.value.formDirty)

        vm.onAnonymousToggle()
        assertFalse(vm.uiState.value.isAnonymous)
    }

    // ── TC-WK-VM-017: onImagesSelected adds URIs ─────────────────────────────
    // F.5: selecting images from picker
    @Test
    fun `onImagesSelected - adds URIs to imageIds and marks formDirty`() = runTest {
        val vm = buildViewModel()
        vm.onImagesSelected(listOf("content://img1", "content://img2"))

        assertEquals(listOf("content://img1", "content://img2"), vm.uiState.value.imageIds)
        assertTrue(vm.uiState.value.formDirty)
    }

    // ── TC-WK-VM-018: onImagesSelected respects 5 image limit ────────────────
    // F.5: max 5 images — US-4 Scenario 2
    @Test
    fun `onImagesSelected - truncates list to respect 5 image limit`() = runTest {
        val vm = buildViewModel()
        vm.onImagesSelected(listOf("i1", "i2", "i3", "i4"))
        vm.onImagesSelected(listOf("i5", "i6")) // i6 should be dropped

        assertEquals(5, vm.uiState.value.imageIds.size)
        assertFalse("canAddImage should be false at limit", vm.uiState.value.canAddImage)
    }

    // ── TC-WK-VM-019: removeImage removes specific URI ────────────────────────
    // F.2–F.2b: tapping ✕ on image thumbnail — US-4 Scenario 3
    @Test
    fun `removeImage - removes specified URI while keeping others`() = runTest {
        val vm = buildViewModel()
        vm.onImagesSelected(listOf("img1", "img2"))
        vm.removeImage("img1")

        assertFalse(vm.uiState.value.imageIds.contains("img1"))
        assertTrue(vm.uiState.value.imageIds.contains("img2"))
    }

    // ── TC-WK-VM-020: onBackClick empty form → NavigateBack immediately ───────
    // H: US-2 Scenario 1 — no confirmation when form is empty
    @Test
    fun `onBackClick - empty form emits NavigateBack without showing dialog`() = runTest {
        val vm = buildViewModel()
        val events = mutableListOf<WriteKudoEvent>()
        val job = launch { vm.events.collect { events.add(it) } }

        vm.onBackClick()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(events.any { it is WriteKudoEvent.NavigateBack })
        assertFalse(vm.uiState.value.showCancelDialog)
        job.cancel()
    }

    // ── TC-WK-VM-021: onBackClick dirty form → shows cancel dialog ───────────
    // H: US-2 Scenario 2 — confirmation dialog with unsaved content
    @Test
    fun `onBackClick - dirty form shows cancel dialog without emitting NavigateBack`() = runTest {
        val vm = buildViewModel()
        vm.onTitleChange("some title")
        val events = mutableListOf<WriteKudoEvent>()
        val job = launch { vm.events.collect { events.add(it) } }

        vm.onBackClick()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(vm.uiState.value.showCancelDialog)
        assertFalse(events.any { it is WriteKudoEvent.NavigateBack })
        job.cancel()
    }

    // ── TC-WK-VM-022: dismissCancelDialog hides dialog ───────────────────────
    // H: "Tiếp tục chỉnh sửa" — US-2 Scenario 2
    @Test
    fun `dismissCancelDialog - hides cancel dialog`() = runTest {
        val vm = buildViewModel()
        vm.onTitleChange("something")
        vm.onBackClick()
        assertTrue(vm.uiState.value.showCancelDialog)

        vm.dismissCancelDialog()
        assertFalse(vm.uiState.value.showCancelDialog)
    }

    // ── TC-WK-VM-023: confirmCancel → hides dialog + NavigateBack ────────────
    // H: confirming cancel in dialog — US-2 Scenario 2
    @Test
    fun `confirmCancel - hides dialog and emits NavigateBack event`() = runTest {
        val vm = buildViewModel()
        vm.onTitleChange("something")
        vm.onBackClick()
        val events = mutableListOf<WriteKudoEvent>()
        val job = launch { vm.events.collect { events.add(it) } }

        vm.confirmCancel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(vm.uiState.value.showCancelDialog)
        assertTrue(events.any { it is WriteKudoEvent.NavigateBack })
        job.cancel()
    }

    // ── TC-WK-VM-024: onSendClick valid form → NavigateToSuccess ─────────────
    // I: US-1 Scenario 1 — happy path
    @Test
    fun `onSendClick - valid form emits NavigateToSuccess`() = runTest {
        val vm = buildViewModel(recipientId = "r1", recipientName = "Alice")
        testDispatcher.scheduler.advanceUntilIdle()
        vm.onTitleChange("Best Dev")
        vm.onMessageChange("<p>Great job!</p>")
        vm.toggleHashtag(h1)
        val events = mutableListOf<WriteKudoEvent>()
        val job = launch { vm.events.collect { events.add(it) } }

        vm.onSendClick()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(events.any { it is WriteKudoEvent.NavigateToSuccess })
        job.cancel()
    }

    // ── TC-WK-VM-025: onSendClick sets isSending during send ─────────────────
    // I: Send button shows "Đang gửi..." while request is in-flight
    @Test
    fun `onSendClick - isSending is true before coroutine completes`() = runTest {
        val vm = buildViewModel(recipientId = "r1", recipientName = "Alice")
        testDispatcher.scheduler.advanceUntilIdle()
        vm.onTitleChange("Best Dev")
        vm.onMessageChange("<p>msg</p>")
        vm.toggleHashtag(h1)

        vm.onSendClick()
        // Advance one tick so the coroutine starts and sets isSending = true,
        // but suspends before submitKudoUseCase returns
        testDispatcher.scheduler.runCurrent()
        assertTrue(vm.uiState.value.isSending)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    // ── TC-WK-VM-026: onSendClick server failure → error, isSending false ────
    // I: US-1 Scenario 5 — error snackbar, form preserved
    @Test
    fun `onSendClick - server failure sets error message and clears isSending`() = runTest {
        kudosRepo.submitResult = Result.failure(RuntimeException("Server error"))
        val vm = buildViewModel(recipientId = "r1", recipientName = "Alice")
        testDispatcher.scheduler.advanceUntilIdle()
        vm.onTitleChange("Best Dev")
        vm.onMessageChange("<p>msg</p>")
        vm.toggleHashtag(h1)

        vm.onSendClick()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(vm.uiState.value.isSending)
        assertEquals("Server error", vm.uiState.value.error)
    }

    // ── TC-WK-VM-027: onSendClick title > 100 chars → titleError ────────────
    // B.4: US-1 Scenario 3b — inline error below title field
    @Test
    fun `onSendClick - title exceeding 100 chars sets titleError and blocks navigation`() = runTest {
        val vm = buildViewModel(recipientId = "r1", recipientName = "Alice")
        vm.onTitleChange("A".repeat(101))
        vm.onMessageChange("<p>msg</p>")
        vm.toggleHashtag(h1)
        val events = mutableListOf<WriteKudoEvent>()
        val job = launch { vm.events.collect { events.add(it) } }

        vm.onSendClick()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(vm.uiState.value.titleError)
        assertTrue(vm.uiState.value.titleError!!.contains("100"))
        assertFalse(events.any { it is WriteKudoEvent.NavigateToSuccess })
        job.cancel()
    }

    // ── TC-WK-VM-028: onSendClick no recipient → recipientError ──────────────
    // B.2: validation message when recipient not selected
    @Test
    fun `onSendClick - missing recipient sets recipientError`() = runTest {
        val vm = buildViewModel()
        vm.onTitleChange("Best Dev")
        vm.onMessageChange("<p>msg</p>")
        vm.toggleHashtag(h1)

        vm.onSendClick()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(vm.uiState.value.recipientError)
    }

    // ── TC-WK-VM-029: onRecipientSelected updates state ──────────────────────
    // B.2: choosing a result from RecipientSearchSheet
    @Test
    fun `onRecipientSelected - sets recipientId name closes sheet and marks dirty`() = runTest {
        val vm = buildViewModel()
        vm.startRecipientSearch()
        vm.onRecipientSelected("r1", "Alice")

        val state = vm.uiState.value
        assertEquals("r1", state.recipientId)
        assertEquals("Alice", state.recipientName)
        assertFalse(state.showRecipientSearch)
        assertNull(state.recipientError)
        assertTrue(state.formDirty)
    }

    // ── TC-WK-VM-030: self-kudo attempt → recipientError ─────────────────────
    // B.2: US-1 Scenario 6 — cannot send kudo to yourself
    @Test
    fun `onRecipientSelected - selecting self sets recipientError`() = runTest {
        val handle = SavedStateHandle(mapOf("currentUserId" to "me-123"))
        val vm = WriteKudoViewModel(
            savedStateHandle = handle,
            getHashtagsUseCase = GetHashtagsUseCase(statsRepo),
            searchProfilesUseCase = SearchProfilesUseCase(profileRepo),
            submitKudoUseCase = SubmitKudoUseCase(imageUploader, kudosRepo),
        )

        vm.onRecipientSelected("me-123", "Myself")

        assertNotNull(vm.uiState.value.recipientError)
        assertNull(vm.uiState.value.recipientId)
    }

    // ── TC-WK-VM-031: onSearchQueryChange single char → search triggered ──────
    // B.2 search dropdown — single char query triggers debounced search
    @Test
    fun `onSearchQueryChange - query shorter than 2 chars clears results without searching`() = runTest {
        val vm = buildViewModel()
        vm.onSearchQueryChange("A")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf(sampleProfile), vm.uiState.value.searchResults)
        assertFalse(vm.uiState.value.isSearching)
    }

    // ── TC-WK-VM-032: onSearchQueryChange valid query → search results ────────
    // B.2 search dropdown — debounce 300ms then search
    @Test
    fun `onSearchQueryChange - 2+ char query triggers debounced search returning results`() = runTest {
        val vm = buildViewModel()
        vm.startRecipientSearch()

        vm.onSearchQueryChange("Al")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf(sampleProfile), vm.uiState.value.searchResults)
        assertFalse(vm.uiState.value.isSearching)
    }

    // ── TC-WK-VM-033: clearError sets error to null ───────────────────────────
    // General error recovery (snackbar dismiss)
    @Test
    fun `clearError - resets error field to null`() = runTest {
        kudosRepo.submitResult = Result.failure(RuntimeException("err"))
        val vm = buildViewModel(recipientId = "r1", recipientName = "Alice")
        testDispatcher.scheduler.advanceUntilIdle()
        vm.onTitleChange("T")
        vm.onMessageChange("M")
        vm.toggleHashtag(h1)
        vm.onSendClick()
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(vm.uiState.value.error)

        vm.clearError()
        assertNull(vm.uiState.value.error)
    }
}

// ── Fakes ─────────────────────────────────────────────────────────────────────

private class FakeUserStatsRepository(
    private val hashtags: List<Hashtag> = emptyList(),
) : UserStatsRepository {
    override suspend fun getUserStats(): Result<UserStats> = Result.success(
        UserStats(
            kudosReceived = 0,
            kudosSent = 0,
            heartsReceived = 0,
            secretBoxesOpened = 0,
            secretBoxesUnopened = 0,
        ),
    )

    override suspend fun getGiftRecipients(): Result<List<GiftRecipient>> =
        Result.success(emptyList())

    override suspend fun getHashtags(): Result<List<Hashtag>> = Result.success(hashtags)

    override suspend fun getDepartments(): Result<List<Department>> =
        Result.success(emptyList())

    override suspend fun getNextSecretBox(): Result<String> = Result.success("box-1")
    override suspend fun openSecretBox(boxId: String): Result<Unit> = Result.success(Unit)
}

private class FakeProfileRepository(
    private val searchResult: List<Profile> = emptyList(),
) : ProfileRepository {
    override suspend fun getMyProfile(): Result<Profile> = Result.success(
        Profile(
            id = "current-user",
            fullName = "Current User",
            employeeCode = "CU001",
            avatarUrl = null,
            badgeType = "",
            heroTier = 0,
        ),
    )

    override suspend fun searchProfiles(query: String): Result<List<Profile>> =
        Result.success(searchResult)
}

private class FakeImageUploader(
    private val resultUrl: String = "https://example.com/img.jpg",
) : ImageUploader {
    var uploadCallCount = 0
    override suspend fun uploadImage(uriString: String): String {
        uploadCallCount++
        return resultUrl
    }
}

private class FakeWriteKudosRepository(
    var submitResult: Result<String>,
) : KudosRepository {
    override suspend fun getHighlightKudos(hashtagId: String?, departmentId: String?) =
        Result.success(emptyList<Kudos>())

    override suspend fun getAllKudos(page: Int, limit: Int, hashtagId: String?, departmentId: String?) =
        Result.success(emptyList<Kudos>())

    override suspend fun toggleLike(kudosId: String, isCurrentlyLiked: Boolean) =
        Result.success(Unit)

    override fun observeNewKudos(): Flow<Kudos> = flow {}

    override suspend fun getProfileKudos(filter: KudosFilter) =
        Result.success(emptyList<Kudos>())

    override suspend fun submitKudo(request: WriteKudoRequest): Result<String> = submitResult
}
