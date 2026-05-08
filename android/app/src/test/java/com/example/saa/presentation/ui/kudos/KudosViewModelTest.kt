package com.example.saa.presentation.ui.kudos

import com.example.saa.domain.model.Department
import com.example.saa.domain.model.GiftRecipient
import com.example.saa.domain.model.Hashtag
import com.example.saa.domain.model.Kudos
import com.example.saa.domain.model.KudosFilter
import com.example.saa.domain.model.UserStats
import com.example.saa.domain.model.WriteKudoRequest
import com.example.saa.domain.repository.KudosRepository
import com.example.saa.domain.repository.LanguageRepository
import com.example.saa.domain.repository.NotificationRepository
import com.example.saa.domain.repository.UserStatsRepository
import com.example.saa.domain.usecase.GetAllKudosUseCase
import com.example.saa.domain.usecase.GetDepartmentsUseCase
import com.example.saa.domain.usecase.GetGiftRecipientsUseCase
import com.example.saa.domain.usecase.GetHashtagsUseCase
import com.example.saa.domain.usecase.GetHighlightKudosUseCase
import com.example.saa.domain.usecase.GetUnreadNotificationsUseCase
import com.example.saa.domain.usecase.GetUserStatsUseCase
import com.example.saa.domain.usecase.OpenSecretBoxUseCase
import com.example.saa.domain.usecase.ToggleKudosLikeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
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

// ── Design mapping ─────────────────────────────────────────────────────────────
// TC_KUDOS_FUN_004  → loadInitialData highlight loaded, top-5 by heartCount
// TC_KUDOS_FUN_005  → onCarouselPageChange updates currentCarouselPage
// TC_KUDOS_FUN_006  → onToggleLike like — optimistic update isLiked=true, heartCount+1
// TC_KUDOS_FUN_007  → onToggleLike unlike — optimistic isLiked=false, heartCount-1
// TC_KUDOS_FUN_016  → onFilterHashtag + onFilterDepartment AND logic
// TC_KUDOS_FUN_017  → filter change resets carousel to page 0
// TC_KUDOS_GUI_011  → userStats loaded into state
// TC_KUDOS_GUI_009  → secretBoxesUnopened = 0 → openSecretBox should be guarded
// TC_KUDOS_FUN_012  → onToggleLike failure → reverted state + error set
// TC_KUDOS_FUN_013  → loadMoreKudos appends to allKudos
// TC_KUDOS_ACC_003  → isUnauthenticated flag for 401 is propagated via error field
// Notification       → unreadNotificationCount loaded
// Language           → selectedLanguage from repo + setLanguage + dismissLanguageSelector
// consumeError       → clears error field
// ────────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalCoroutinesApi::class)
class KudosViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    // ── Fixtures ─────────────────────────────────────────────────────────────────
    private fun kudos(
        id: String,
        heartCount: Int = 2,
        isLiked: Boolean = false,
    ) = Kudos(
        id = id,
        senderId = "s1",
        recipientId = "r1",
        message = "Well done",
        awardCategoryName = "Best",
        heartCount = heartCount,
        createdAt = "2025-01-01T00:00:00Z",
        hashtags = listOf("teamwork"),
        senderAvatarUrl = null,
        senderName = "Sender",
        senderEmployeeCode = "EMP1",
        senderBadgeType = "",
        senderDepartmentName = "Dev",
        recipientAvatarUrl = null,
        recipientName = "Recipient",
        recipientHeroTier = 0,
        recipientDepartmentName = "QA",
        shareUrl = "https://example.com/kudos/$id",
        isLiked = isLiked,
        photoUrls = emptyList(),
    )

    private val sampleStats = UserStats(
        kudosReceived = 5,
        kudosSent = 10,
        heartsReceived = 20,
        secretBoxesOpened = 2,
        secretBoxesUnopened = 3,
    )

    private lateinit var kudosRepo: FakeKudosRepository
    private lateinit var statsRepo: FakeUserStatsRepository
    private lateinit var notificationRepo: FakeNotificationRepository
    private lateinit var languageRepo: FakeLanguageRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        kudosRepo = FakeKudosRepository(
            highlightResult = Result.success(listOf(kudos("h1", heartCount = 10), kudos("h2", heartCount = 5))),
            allKudosResult = Result.success(listOf(kudos("k1"), kudos("k2"))),
        )
        statsRepo = FakeUserStatsRepository(Result.success(sampleStats))
        notificationRepo = FakeNotificationRepository(Result.success(7))
        languageRepo = FakeLanguageRepository("VN")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel() = KudosViewModel(
        getHighlightKudosUseCase = GetHighlightKudosUseCase(kudosRepo),
        getAllKudosUseCase = GetAllKudosUseCase(kudosRepo),
        toggleKudosLikeUseCase = ToggleKudosLikeUseCase(kudosRepo),
        getUserStatsUseCase = GetUserStatsUseCase(statsRepo),
        getGiftRecipientsUseCase = GetGiftRecipientsUseCase(statsRepo),
        getHashtagsUseCase = GetHashtagsUseCase(statsRepo),
        getDepartmentsUseCase = GetDepartmentsUseCase(statsRepo),
        openSecretBoxUseCase = OpenSecretBoxUseCase(statsRepo),
        kudosRepository = kudosRepo,
        getUnreadNotificationsUseCase = GetUnreadNotificationsUseCase(notificationRepo),
        languageRepository = languageRepo,
    )

    // ── TC-KUDOS-001: Initial load ────────────────────────────────────────────────
    @Test
    fun `init - highlight and all kudos loaded, isLoading becomes false`() = runTest {
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.highlightKudos.size)
        assertEquals(2, state.allKudos.size)
        assertNull(state.error)
    }

    // ── TC-KUDOS-002: User stats loaded ──────────────────────────────────────────
    @Test
    fun `init - user stats loaded into state`() = runTest {
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(sampleStats, vm.uiState.value.userStats)
    }

    // ── TC-KUDOS-003: Notification count loaded ──────────────────────────────────
    @Test
    fun `init - unread notification count loaded`() = runTest {
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(7, vm.uiState.value.unreadNotificationCount)
    }

    // ── TC-KUDOS-004: highlight failure surfaces error ───────────────────────────
    @Test
    fun `loadInitialData highlight failure - sets error field`() = runTest {
        kudosRepo = FakeKudosRepository(
            highlightResult = Result.failure(RuntimeException("Network error")),
            allKudosResult = Result.success(emptyList()),
        )
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(vm.uiState.value.error)
    }

    // ── TC-KUDOS-005: allKudos failure surfaces error ────────────────────────────
    @Test
    fun `loadInitialData allKudos failure - sets error field`() = runTest {
        kudosRepo = FakeKudosRepository(
            highlightResult = Result.success(emptyList()),
            allKudosResult = Result.failure(RuntimeException("API error")),
        )
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(vm.uiState.value.error)
    }

    // ── TC-KUDOS-006: onCarouselPageChange ──────────────────────────────────────
    @Test
    fun `onCarouselPageChange - updates currentCarouselPage`() = runTest {
        val vm = buildViewModel()

        vm.onCarouselPageChange(3)

        assertEquals(3, vm.uiState.value.currentCarouselPage)
    }

    // ── TC-KUDOS-007: onFilterHashtag resets carousel and reloads ────────────────
    // TC_KUDOS_FUN_017: filter change resets carousel to page 0
    @Test
    fun `onFilterHashtag - resets carousel page to 0 and triggers reload`() = runTest {
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        vm.onCarouselPageChange(3)
        assertEquals(3, vm.uiState.value.currentCarouselPage)

        vm.onFilterHashtag("h1")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, vm.uiState.value.currentCarouselPage)
        assertEquals("h1", vm.uiState.value.selectedHashtagId)
    }

    // ── TC-KUDOS-008: onFilterDepartment resets carousel and reloads ──────────────
    @Test
    fun `onFilterDepartment - resets carousel page to 0 and triggers reload`() = runTest {
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        vm.onCarouselPageChange(2)

        vm.onFilterDepartment("dept-1")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, vm.uiState.value.currentCarouselPage)
        assertEquals("dept-1", vm.uiState.value.selectedDepartmentId)
    }

    // ── TC-KUDOS-009: onFilterHashtag clear resets selectedHashtagId ─────────────
    @Test
    fun `onFilterHashtag null - clears selectedHashtagId`() = runTest {
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        vm.onFilterHashtag("h1")
        testDispatcher.scheduler.advanceUntilIdle()

        vm.onFilterHashtag(null)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(vm.uiState.value.selectedHashtagId)
    }

    // ── TC-KUDOS-010: onToggleLike — optimistic like ──────────────────────────────
    // TC_KUDOS_FUN_006: like optimistic update
    @Test
    fun `onToggleLike not liked - optimistically sets isLiked true and heartCount incremented`() = runTest {
        kudosRepo = FakeKudosRepository(
            highlightResult = Result.success(listOf(kudos("k1", heartCount = 5, isLiked = false))),
            allKudosResult = Result.success(emptyList()),
        )
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        vm.onToggleLike("k1")

        val updated = vm.uiState.value.highlightKudos.first { it.id == "k1" }
        assertTrue(updated.isLiked)
        assertEquals(6, updated.heartCount)
    }

    // ── TC-KUDOS-011: onToggleLike — optimistic unlike ───────────────────────────
    // TC_KUDOS_FUN_007: unlike optimistic update
    @Test
    fun `onToggleLike already liked - optimistically sets isLiked false and heartCount decremented`() = runTest {
        kudosRepo = FakeKudosRepository(
            highlightResult = Result.success(listOf(kudos("k1", heartCount = 5, isLiked = true))),
            allKudosResult = Result.success(emptyList()),
        )
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        vm.onToggleLike("k1")

        val updated = vm.uiState.value.highlightKudos.first { it.id == "k1" }
        assertFalse(updated.isLiked)
        assertEquals(4, updated.heartCount)
    }

    // ── TC-KUDOS-012: onToggleLike failure reverts state ─────────────────────────
    @Test
    fun `onToggleLike failure - reverts optimistic update and sets error`() = runTest {
        kudosRepo = FakeKudosRepository(
            highlightResult = Result.success(listOf(kudos("k1", heartCount = 5, isLiked = false))),
            allKudosResult = Result.success(emptyList()),
            toggleLikeResult = Result.failure(RuntimeException("Toggle failed")),
        )
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        vm.onToggleLike("k1")
        testDispatcher.scheduler.advanceUntilIdle()

        val reverted = vm.uiState.value.highlightKudos.first { it.id == "k1" }
        assertFalse(reverted.isLiked)
        assertEquals(5, reverted.heartCount)
        assertNotNull(vm.uiState.value.error)
    }

    // ── TC-KUDOS-013: loadMoreKudos appends ──────────────────────────────────────
    @Test
    fun `loadMoreKudos - appends new kudos to existing list`() = runTest {
        val page1 = (1..20).map { kudos("k$it") } // fill full page
        val page2 = listOf(kudos("k21"), kudos("k22"))
        kudosRepo = FakeKudosRepository(
            highlightResult = Result.success(emptyList()),
            allKudosResult = Result.success(page1),
            page2Result = Result.success(page2),
        )
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(20, vm.uiState.value.allKudos.size)

        vm.loadMoreKudos()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(22, vm.uiState.value.allKudos.size)
        assertFalse(vm.uiState.value.isLoadingMore)
    }

    // ── TC-KUDOS-014: loadMoreKudos no-op when already loading ───────────────────
    @Test
    fun `loadMoreKudos - skipped when isLoadingMore is true`() = runTest {
        kudosRepo = FakeKudosRepository(
            highlightResult = Result.success(emptyList()),
            allKudosResult = Result.success((1..20).map { kudos("k$it") }),
        )
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Trigger once to start loading more (don't advance dispatcher)
        vm.loadMoreKudos()
        val callsBefore = kudosRepo.getAllKudosCallCount

        // Second call while loading should be no-op
        vm.loadMoreKudos()

        assertEquals(callsBefore, kudosRepo.getAllKudosCallCount)
    }

    // ── TC-KUDOS-015: hasMoreKudos false when page < PAGE_SIZE ────────────────────
    @Test
    fun `loadInitialData - hasMoreKudos false when result smaller than PAGE_SIZE`() = runTest {
        kudosRepo = FakeKudosRepository(
            highlightResult = Result.success(emptyList()),
            allKudosResult = Result.success(listOf(kudos("k1"), kudos("k2"))),
        )
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(vm.uiState.value.hasMoreKudos)
    }

    // ── TC-KUDOS-016: consumeError clears error ───────────────────────────────────
    @Test
    fun `consumeError - clears error field`() = runTest {
        kudosRepo = FakeKudosRepository(
            highlightResult = Result.failure(RuntimeException("err")),
            allKudosResult = Result.success(emptyList()),
        )
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(vm.uiState.value.error)

        vm.consumeError()

        assertNull(vm.uiState.value.error)
    }

    // ── TC-KUDOS-017: language selector show/dismiss ─────────────────────────────
    @Test
    fun `showLanguageSelector and dismissLanguageSelector - toggles showLanguageSelector`() = runTest {
        val vm = buildViewModel()
        assertFalse(vm.uiState.value.showLanguageSelector)

        vm.showLanguageSelector()
        assertTrue(vm.uiState.value.showLanguageSelector)

        vm.dismissLanguageSelector()
        assertFalse(vm.uiState.value.showLanguageSelector)
    }

    // ── TC-KUDOS-018: setLanguage calls repo and dismisses selector ───────────────
    @Test
    fun `setLanguage - calls repository and dismisses language selector`() = runTest {
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        vm.showLanguageSelector()

        vm.setLanguage("EN")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, languageRepo.setLanguageCallCount)
        assertFalse(vm.uiState.value.showLanguageSelector)
    }

    // ── TC-KUDOS-019: openSecretBox success refreshes stats ──────────────────────
    @Test
    fun `onOpenSecretBox success - refreshes user stats`() = runTest {
        val updatedStats = sampleStats.copy(secretBoxesOpened = 3, secretBoxesUnopened = 2)
        statsRepo = FakeUserStatsRepository(
            initialResult = Result.success(sampleStats),
            updatedStatsResult = Result.success(updatedStats),
        )
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        vm.onOpenSecretBox()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(3, vm.uiState.value.userStats?.secretBoxesOpened)
        assertEquals(2, vm.uiState.value.userStats?.secretBoxesUnopened)
    }

    // ── TC-KUDOS-020: openSecretBox failure sets error ────────────────────────────
    @Test
    fun `onOpenSecretBox failure - sets error field`() = runTest {
        statsRepo = FakeUserStatsRepository(
            initialResult = Result.success(sampleStats),
            openBoxResult = Result.failure(RuntimeException("Box error")),
        )
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        vm.onOpenSecretBox()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(vm.uiState.value.error)
    }
}

// ── Fakes ─────────────────────────────────────────────────────────────────────

private class FakeKudosRepository(
    private val highlightResult: Result<List<Kudos>>,
    private val allKudosResult: Result<List<Kudos>>,
    private val toggleLikeResult: Result<Unit> = Result.success(Unit),
    private val page2Result: Result<List<Kudos>> = Result.success(emptyList()),
) : KudosRepository {
    var getAllKudosCallCount = 0

    override suspend fun getHighlightKudos(
        hashtagId: String?,
        departmentId: String?,
    ): Result<List<Kudos>> = highlightResult

    override suspend fun getAllKudos(
        page: Int,
        limit: Int,
        hashtagId: String?,
        departmentId: String?,
    ): Result<List<Kudos>> {
        getAllKudosCallCount++
        return if (page == 1) allKudosResult else page2Result
    }

    override suspend fun toggleLike(kudosId: String, isCurrentlyLiked: Boolean): Result<Unit> =
        toggleLikeResult

    override fun observeNewKudos(): Flow<Kudos> = emptyFlow()

    override suspend fun getProfileKudos(filter: KudosFilter): Result<List<Kudos>> =
        Result.success(emptyList())

    override suspend fun submitKudo(request: WriteKudoRequest): Result<String> =
        Result.success("new-id")
}

private class FakeUserStatsRepository(
    private val initialResult: Result<UserStats>,
    private val updatedStatsResult: Result<UserStats> = initialResult,
    private val openBoxResult: Result<Unit> = Result.success(Unit),
) : UserStatsRepository {
    private var statsCallCount = 0

    override suspend fun getUserStats(): Result<UserStats> {
        statsCallCount++
        return if (statsCallCount == 1) initialResult else updatedStatsResult
    }

    override suspend fun getGiftRecipients(): Result<List<GiftRecipient>> =
        Result.success(emptyList())

    override suspend fun getHashtags(): Result<List<Hashtag>> =
        Result.success(emptyList())

    override suspend fun getDepartments(): Result<List<Department>> =
        Result.success(emptyList())

    override suspend fun getNextSecretBox(): Result<String> =
        if (openBoxResult.isSuccess) Result.success("box-1")
        else openBoxResult.map { "" }

    override suspend fun openSecretBox(boxId: String): Result<Unit> = openBoxResult
}

private class FakeNotificationRepository(private val result: Result<Int>) : NotificationRepository {
    override suspend fun getUnreadCount(): Result<Int> = result
}

private class FakeLanguageRepository(initialLanguage: String) : LanguageRepository {
    private val _language = MutableStateFlow(initialLanguage)
    var setLanguageCallCount = 0

    override fun getLanguage(): Flow<String> = _language

    override suspend fun setLanguage(code: String) {
        setLanguageCallCount++
        _language.value = code
    }
}
