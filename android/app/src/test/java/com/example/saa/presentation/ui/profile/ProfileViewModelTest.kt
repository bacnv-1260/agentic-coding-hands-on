package com.example.saa.presentation.ui.profile

import com.example.saa.domain.model.Kudos
import com.example.saa.domain.model.KudosFilter
import com.example.saa.domain.model.Profile
import com.example.saa.domain.model.UserStats
import com.example.saa.domain.model.WriteKudoRequest
import com.example.saa.domain.repository.KudosRepository
import com.example.saa.domain.repository.LanguageRepository
import com.example.saa.domain.repository.NotificationRepository
import com.example.saa.domain.repository.ProfileRepository
import com.example.saa.domain.repository.UserStatsRepository
import com.example.saa.domain.usecase.GetMyProfileUseCase
import com.example.saa.domain.usecase.GetProfileKudosUseCase
import com.example.saa.domain.usecase.GetUnreadNotificationsUseCase
import com.example.saa.domain.usecase.GetUserStatsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// ── Design mapping ────────────────────────────────────────────────────────────
// mms_1.1_member       → profile info loaded from ProfileRepository
// mms_A.2_Name         → fullName / employeeCode / badgeType in Profile
// mms_D.1.2~D.1.7      → kudosReceived / kudosSent / hearts / secretBoxes in UserStats
// mms_2_icon collection→ heroTier driven display (always shown)
// mms_5_kudos list     → kudosList driven by KudosFilter
// mms_A_Dropdown       → isDropdownOpen toggle / dismiss
// mms_1_header         → unreadNotificationCount / language selector
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var profileRepo: FakeProfileRepository
    private lateinit var statsRepo: FakeUserStatsRepository
    private lateinit var kudosRepo: FakeKudosRepository
    private lateinit var notificationRepo: FakeNotificationRepository
    private lateinit var languageRepo: FakeLanguageRepository
    private lateinit var viewModel: ProfileViewModel

    // ── Fixture data ─────────────────────────────────────────────────────────

    private val sampleProfile = Profile(
        id = "user-1",
        fullName = "Huỳnh Dương Xuân Nhật",
        employeeCode = "CEVC3",
        avatarUrl = "https://example.com/avatar.jpg",
        badgeType = "Legend Hero",
        heroTier = 3,
    )

    private val sampleStats = UserStats(
        kudosReceived = 5,
        kudosSent = 25,
        heartsReceived = 25,
        secretBoxesOpened = 25,
        secretBoxesUnopened = 3,
    )

    private fun kudos(id: String, filter: KudosFilter = KudosFilter.RECEIVED) = Kudos(
        id = id,
        senderId = "s1",
        recipientId = "r1",
        message = "Well done",
        awardCategoryName = "Best",
        heartCount = 2,
        createdAt = "2025-01-01T00:00:00Z",
        hashtags = emptyList(),
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
        isLiked = false,
        photoUrls = emptyList(),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        profileRepo = FakeProfileRepository(Result.success(sampleProfile))
        statsRepo = FakeUserStatsRepository(Result.success(sampleStats))
        kudosRepo = FakeKudosRepository(Result.success(listOf(kudos("k1"), kudos("k2"))))
        notificationRepo = FakeNotificationRepository(Result.success(7))
        languageRepo = FakeLanguageRepository("VN")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel() = ProfileViewModel(
        getMyProfileUseCase = GetMyProfileUseCase(profileRepo),
        getUserStatsUseCase = GetUserStatsUseCase(statsRepo),
        getProfileKudosUseCase = GetProfileKudosUseCase(kudosRepo),
        getUnreadNotificationsUseCase = GetUnreadNotificationsUseCase(notificationRepo),
        languageRepository = languageRepo,
    )

    // ── TC-PROFILE-001: Initial load success ─────────────────────────────────
    // mms_1.1_member, mms_D.1.x, mms_5_kudos list
    @Test
    fun `init - all data loaded successfully sets profile stats kudos and clears loading`() = runTest {
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse("isLoading should be false after load", state.isLoading)
        assertEquals(sampleProfile, state.profile)
        assertEquals(sampleStats, state.stats)
        assertEquals(2, state.kudosList.size)
        assertNull(state.error)
    }

    // ── TC-PROFILE-002: Loading state transitions ─────────────────────────────
    @Test
    fun `init - isLoading is true before data arrives`() = runTest {
        viewModel = buildViewModel()
        // Not yet advanced — still loading
        assertTrue(viewModel.uiState.value.isLoading)
    }

    // ── TC-PROFILE-003: Profile fetch failure surfaces error ─────────────────
    // mms_1.1_member error path
    @Test
    fun `loadData - profile failure sets error and leaves profile null`() = runTest {
        profileRepo = FakeProfileRepository(Result.failure(RuntimeException("Network error")))
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.profile)
        assertEquals("Network error", state.error)
    }

    // ── TC-PROFILE-004: Stats fetch failure surfaces error ─────────────────
    // mms_D.1.2~D.1.7 error path
    @Test
    fun `loadData - stats failure sets error and leaves stats null`() = runTest {
        statsRepo = FakeUserStatsRepository(Result.failure(RuntimeException("Stats error")))
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.stats)
        assertEquals("Stats error", state.error)
    }

    // ── TC-PROFILE-005: Kudos fetch failure — no top-level error ─────────────
    // mms_5_kudos list: kudos failure alone does not block profile/stats display
    @Test
    fun `loadData - kudos failure keeps profile and stats visible`() = runTest {
        kudosRepo = FakeKudosRepository(Result.failure(RuntimeException("Kudos error")))
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(sampleProfile, state.profile)
        assertEquals(sampleStats, state.stats)
        assertEquals(0, state.kudosList.size)
        // kudos failure alone doesn't propagate to top-level error
        assertNull(state.error)
    }

    // ── TC-PROFILE-006: Filter change to RECEIVED ─────────────────────────────
    // mms_A_Dropdown → Đã nhận option
    @Test
    fun `onFilterChange RECEIVED - updates filter closes dropdown reloads kudos`() = runTest {
        val receivedKudos = listOf(kudos("r1"), kudos("r2"), kudos("r3"))
        kudosRepo = FakeKudosRepository(
            initialResult = Result.success(emptyList()),
            filterResults = mapOf(KudosFilter.RECEIVED to Result.success(receivedKudos)),
        )
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Open dropdown first
        viewModel.onDropdownToggle()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isDropdownOpen)

        viewModel.onFilterChange(KudosFilter.RECEIVED)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(KudosFilter.RECEIVED, state.kudosFilter)
        assertFalse("Dropdown should be closed after filter change", state.isDropdownOpen)
        assertEquals(3, state.kudosList.size)
    }

    // ── TC-PROFILE-007: Filter change to SENT ─────────────────────────────────
    // mms_A_Dropdown → Đã gửi option
    @Test
    fun `onFilterChange SENT - updates filter closes dropdown reloads kudos`() = runTest {
        val sentKudos = listOf(kudos("s1"), kudos("s2"))
        kudosRepo = FakeKudosRepository(
            initialResult = Result.success(emptyList()),
            filterResults = mapOf(KudosFilter.SENT to Result.success(sentKudos)),
        )
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onDropdownToggle()
        viewModel.onFilterChange(KudosFilter.SENT)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(KudosFilter.SENT, state.kudosFilter)
        assertFalse(state.isDropdownOpen)
        assertEquals(2, state.kudosList.size)
    }

    // ── TC-PROFILE-008: Dropdown toggle opens when closed ────────────────────
    // mms_A_Dropdown → tap filter pill
    @Test
    fun `onDropdownToggle - opens dropdown when it is closed`() = runTest {
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isDropdownOpen)
        viewModel.onDropdownToggle()

        assertTrue(viewModel.uiState.value.isDropdownOpen)
    }

    // ── TC-PROFILE-009: Dropdown toggle closes when open ─────────────────────
    @Test
    fun `onDropdownToggle - closes dropdown when it is open`() = runTest {
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onDropdownToggle()
        assertTrue(viewModel.uiState.value.isDropdownOpen)

        viewModel.onDropdownToggle()
        assertFalse(viewModel.uiState.value.isDropdownOpen)
    }

    // ── TC-PROFILE-010: Dropdown dismiss ─────────────────────────────────────
    // mms_A_Dropdown → tap outside
    @Test
    fun `onDropdownDismiss - always closes dropdown`() = runTest {
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onDropdownToggle()
        assertTrue(viewModel.uiState.value.isDropdownOpen)

        viewModel.onDropdownDismiss()
        assertFalse(viewModel.uiState.value.isDropdownOpen)
    }

    // ── TC-PROFILE-011: consumeError clears error ─────────────────────────────
    @Test
    fun `consumeError - sets error to null`() = runTest {
        profileRepo = FakeProfileRepository(Result.failure(RuntimeException("err")))
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("err", viewModel.uiState.value.error)

        viewModel.consumeError()
        assertNull(viewModel.uiState.value.error)
    }

    // ── TC-PROFILE-012: consumeAuthError clears auth flag ────────────────────
    @Test
    fun `consumeAuthError - sets isUnauthenticated to false`() = runTest {
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.consumeAuthError()
        assertFalse(viewModel.uiState.value.isUnauthenticated)
    }

    // ── TC-PROFILE-013: Language click opens selector ─────────────────────────
    // mms_1_header → VN flag tap
    @Test
    fun `onLanguageClick - sets showLanguageSelector to true`() = runTest {
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.showLanguageSelector)
        viewModel.onLanguageClick()
        assertTrue(viewModel.uiState.value.showLanguageSelector)
    }

    // ── TC-PROFILE-014: Language dismiss closes selector ─────────────────────
    @Test
    fun `onLanguageDismiss - sets showLanguageSelector to false`() = runTest {
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onLanguageClick()
        assertTrue(viewModel.uiState.value.showLanguageSelector)

        viewModel.onLanguageDismiss()
        assertFalse(viewModel.uiState.value.showLanguageSelector)
    }

    // ── TC-PROFILE-015: setLanguage calls repository and closes selector ──────
    @Test
    fun `setLanguage - calls repository and closes language selector`() = runTest {
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onLanguageClick()
        viewModel.setLanguage("EN")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, languageRepo.setLanguageCallCount)
        assertEquals("EN", languageRepo.lastSetLanguageCode)
        assertFalse(viewModel.uiState.value.showLanguageSelector)
    }

    // ── TC-PROFILE-016: Unread notification count loaded on init ─────────────
    // mms_1_header bell icon badge
    @Test
    fun `init - unread notification count loaded from repository`() = runTest {
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(7, viewModel.uiState.value.unreadNotificationCount)
    }

    // ── TC-PROFILE-017: Notification failure does not block profile load ──────
    @Test
    fun `init - notification failure does not affect profile or stats`() = runTest {
        notificationRepo = FakeNotificationRepository(Result.failure(RuntimeException("Notif error")))
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(sampleProfile, state.profile)
        assertEquals(sampleStats, state.stats)
        assertEquals(0, state.unreadNotificationCount)
        assertNull(state.error)
    }

    // ── TC-PROFILE-018: Language collected from repo on init ─────────────────
    @Test
    fun `init - language state reflects repository initial value`() = runTest {
        languageRepo = FakeLanguageRepository("EN")
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("EN", viewModel.uiState.value.selectedLanguage)
    }

    // ── TC-PROFILE-019: Default kudosFilter is SENT ───────────────────────────
    // mms_5_kudos list default view
    @Test
    fun `init - default kudosFilter is SENT`() = runTest {
        viewModel = buildViewModel()
        assertEquals(KudosFilter.SENT, viewModel.uiState.value.kudosFilter)
    }

    // ── TC-PROFILE-020: loadData retry after error clears previous error ──────
    @Test
    fun `loadData retry - clears previous error on success`() = runTest {
        profileRepo = FakeProfileRepository(Result.failure(RuntimeException("first error")))
        viewModel = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("first error", viewModel.uiState.value.error)

        // Fix the repo and retry
        profileRepo.result = Result.success(sampleProfile)
        viewModel.loadData()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.error)
        assertEquals(sampleProfile, viewModel.uiState.value.profile)
    }
}

// ── Fakes ─────────────────────────────────────────────────────────────────────

private class FakeProfileRepository(var result: Result<Profile>) : ProfileRepository {
    override suspend fun getMyProfile(): Result<Profile> = result
    override suspend fun searchProfiles(query: String): Result<List<Profile>> = Result.success(emptyList())
}

private class FakeUserStatsRepository(
    private val result: Result<UserStats>,
) : UserStatsRepository {
    override suspend fun getUserStats(): Result<UserStats> = result
    override suspend fun getGiftRecipients() = Result.success(emptyList<com.example.saa.domain.model.GiftRecipient>())
    override suspend fun getHashtags() = Result.success(emptyList<com.example.saa.domain.model.Hashtag>())
    override suspend fun getDepartments() = Result.success(emptyList<com.example.saa.domain.model.Department>())
    override suspend fun getNextSecretBox() = Result.success("box-1")
    override suspend fun openSecretBox(boxId: String) = Result.success(Unit)
}

private class FakeKudosRepository(
    private val initialResult: Result<List<Kudos>>,
    private val filterResults: Map<KudosFilter, Result<List<Kudos>>> = emptyMap(),
) : KudosRepository {
    override suspend fun getHighlightKudos(hashtagId: String?, departmentId: String?) =
        Result.success(emptyList<Kudos>())

    override suspend fun getAllKudos(page: Int, limit: Int, hashtagId: String?, departmentId: String?) =
        Result.success(emptyList<Kudos>())

    override suspend fun toggleLike(kudosId: String, isCurrentlyLiked: Boolean) =
        Result.success(Unit)

    override fun observeNewKudos(): kotlinx.coroutines.flow.Flow<Kudos> =
        kotlinx.coroutines.flow.flow { }

    override suspend fun getProfileKudos(filter: KudosFilter): Result<List<Kudos>> =
        filterResults[filter] ?: initialResult

    override suspend fun submitKudo(request: WriteKudoRequest): Result<String> =
        Result.success("kudo-stub")
}

private class FakeNotificationRepository(
    private val result: Result<Int>,
) : NotificationRepository {
    override suspend fun getUnreadCount(): Result<Int> = result
}

private class FakeLanguageRepository(initialLanguage: String) : LanguageRepository {
    private val _language = MutableStateFlow(initialLanguage)
    var setLanguageCallCount = 0
    var lastSetLanguageCode: String? = null

    override fun getLanguage(): Flow<String> = _language

    override suspend fun setLanguage(code: String) {
        setLanguageCallCount++
        lastSetLanguageCode = code
        _language.value = code
    }
}
