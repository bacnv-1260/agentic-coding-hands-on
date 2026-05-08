package com.example.saa.presentation.ui.award

import androidx.lifecycle.SavedStateHandle
import com.example.saa.domain.model.Award
import com.example.saa.domain.repository.AwardRepository
import com.example.saa.domain.repository.LanguageRepository
import com.example.saa.domain.repository.NotificationRepository
import com.example.saa.domain.usecase.GetAwardByIdUseCase
import com.example.saa.domain.usecase.GetAwardsUseCase
import com.example.saa.domain.usecase.GetUnreadNotificationsUseCase
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// ── Design mapping ─────────────────────────────────────────────────────────────
// TC_IOS_AWARD_DETAIL_FUN_001 → award loads from API, all fields populated
// TC_IOS_AWARD_DETAIL_FUN_002 → isLoading true before data, false after
// TC_IOS_AWARD_DETAIL_FUN_003 → error state set on failure, retry works
// TC_IOS_AWARD_DETAIL_FUN_004 → imageUrl null handled — no crash
// TC_IOS_AWARD_DETAIL_FUN_005 → dropdown shows all awards list
// TC_IOS_AWARD_DETAIL_FUN_006 → selectAward updates award and closes dropdown
// TC_IOS_AWARD_DETAIL_FUN_007 → dismissDropdown without selection — no state change
// TC_IOS_AWARD_DETAIL_FUN_018 → showLanguageSelector toggles flag
// TC_IOS_AWARD_DETAIL_FUN_019 → setLanguage updates selectedLanguage
// TC_IOS_AWARD_DETAIL_FUN_020 → quantity/prizeValue null — no crash
// No awardId path         → auto-selects first award from list
// toggleDropdown          → toggles showDropdown flag
// notification count       → unreadNotificationCount loaded
// ────────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalCoroutinesApi::class)
class AwardDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val awardTopTalent = Award(
        id = "a1",
        name = "Top Talent",
        description = "Best talent award",
        category = "talent",
        imageUrl = "https://example.com/award.png",
        quantity = 10,
        quantityUnit = "Cá nhân",
        prizeValue = "7.000.000 VNĐ",
    )
    private val awardTopProject = Award(
        id = "a2",
        name = "Top Project",
        description = "Best project award",
        category = "project",
        imageUrl = null,
        quantity = null,
        quantityUnit = null,
        prizeValue = null,
    )

    private lateinit var awardRepo: FakeAwardRepository
    private lateinit var notificationRepo: FakeNotificationRepository
    private lateinit var languageRepo: FakeLanguageRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        awardRepo = FakeAwardRepository(
            byIdResult = Result.success(awardTopTalent),
            listResult = Result.success(listOf(awardTopTalent, awardTopProject)),
        )
        notificationRepo = FakeNotificationRepository(Result.success(3))
        languageRepo = FakeLanguageRepository("VN")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(awardId: String? = "a1") = AwardDetailViewModel(
        savedStateHandle = SavedStateHandle(
            buildMap { if (awardId != null) put("awardId", awardId) },
        ),
        getAwardByIdUseCase = GetAwardByIdUseCase(awardRepo),
        getAwardsUseCase = GetAwardsUseCase(awardRepo),
        getUnreadNotificationsUseCase = GetUnreadNotificationsUseCase(notificationRepo),
        languageRepository = languageRepo,
    )

    // ── TC-AWARD-001: Award loads successfully ────────────────────────────────────
    // TC_IOS_AWARD_DETAIL_FUN_001
    @Test
    fun `init with awardId - award loaded and isLoading becomes false`() = runTest {
        val vm = buildViewModel(awardId = "a1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals(awardTopTalent, state.award)
        assertNull(state.error)
    }

    // ── TC-AWARD-002: isLoading false after data loaded ───────────────────────────
    // TC_IOS_AWARD_DETAIL_FUN_002: loading state clears after data arrives
    @Test
    fun `init - isLoading false after successful data load`() = runTest {
        val vm = buildViewModel(awardId = "a1")
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(vm.uiState.value.isLoading)
        assertNotNull(vm.uiState.value.award)
    }

    // ── TC-AWARD-003: Error state on failure ──────────────────────────────────────
    // TC_IOS_AWARD_DETAIL_FUN_003
    @Test
    fun `init - failure sets error message and isLoading false`() = runTest {
        awardRepo = FakeAwardRepository(
            byIdResult = Result.failure(RuntimeException("Network error")),
            listResult = Result.success(listOf(awardTopTalent)),
        )
        val vm = buildViewModel(awardId = "a1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertNull(state.award)
    }

    // ── TC-AWARD-004: retry reloads award ─────────────────────────────────────────
    // TC_IOS_AWARD_DETAIL_FUN_003 (retry part)
    @Test
    fun `retry - reloads award from repository`() = runTest {
        awardRepo = FakeAwardRepository(
            byIdResult = Result.failure(RuntimeException("fail")),
            listResult = Result.success(emptyList()),
        )
        val vm = buildViewModel(awardId = "a1")
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(vm.uiState.value.error)

        awardRepo.byIdResult = Result.success(awardTopTalent)
        vm.retry()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(awardTopTalent, vm.uiState.value.award)
        assertNull(vm.uiState.value.error)
    }

    // ── TC-AWARD-005: imageUrl null — no crash ────────────────────────────────────
    // TC_IOS_AWARD_DETAIL_FUN_004 / TC_IOS_AWARD_DETAIL_GUI_007
    @Test
    fun `award with null imageUrl - loads without crash`() = runTest {
        awardRepo = FakeAwardRepository(
            byIdResult = Result.success(awardTopProject),
            listResult = Result.success(listOf(awardTopProject)),
        )
        val vm = buildViewModel(awardId = "a2")
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(vm.uiState.value.award?.imageUrl)
        assertNull(vm.uiState.value.error)
    }

    // ── TC-AWARD-006: quantity and prizeValue null — no crash ─────────────────────
    // TC_IOS_AWARD_DETAIL_FUN_020
    @Test
    fun `award with null quantity and prizeValue - loads without crash`() = runTest {
        awardRepo = FakeAwardRepository(
            byIdResult = Result.success(awardTopProject),
            listResult = Result.success(listOf(awardTopProject)),
        )
        val vm = buildViewModel(awardId = "a2")
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(vm.uiState.value.award?.quantity)
        assertNull(vm.uiState.value.award?.prizeValue)
    }

    // ── TC-AWARD-007: allAwards list loaded ───────────────────────────────────────
    // TC_IOS_AWARD_DETAIL_FUN_005
    @Test
    fun `init - allAwards loaded for dropdown`() = runTest {
        val vm = buildViewModel(awardId = "a1")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, vm.uiState.value.allAwards.size)
    }

    // ── TC-AWARD-008: No awardId — auto-selects first award ──────────────────────
    @Test
    fun `init without awardId - auto-selects first award from list`() = runTest {
        val vm = buildViewModel(awardId = null)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(awardTopTalent, vm.uiState.value.award)
    }

    // ── TC-AWARD-009: toggleDropdown ──────────────────────────────────────────────
    // TC_IOS_AWARD_DETAIL_FUN_005
    @Test
    fun `toggleDropdown - toggles showDropdown flag`() = runTest {
        val vm = buildViewModel()
        assertFalse(vm.uiState.value.showDropdown)

        vm.toggleDropdown()
        assertTrue(vm.uiState.value.showDropdown)

        vm.toggleDropdown()
        assertFalse(vm.uiState.value.showDropdown)
    }

    // ── TC-AWARD-010: dismissDropdown closes dropdown ─────────────────────────────
    // TC_IOS_AWARD_DETAIL_FUN_007
    @Test
    fun `dismissDropdown - sets showDropdown to false`() = runTest {
        val vm = buildViewModel()
        vm.toggleDropdown()
        assertTrue(vm.uiState.value.showDropdown)

        vm.dismissDropdown()

        assertFalse(vm.uiState.value.showDropdown)
    }

    // ── TC-AWARD-011: selectAward updates award and closes dropdown ───────────────
    // TC_IOS_AWARD_DETAIL_FUN_006
    @Test
    fun `selectAward - updates award and dismisses dropdown`() = runTest {
        val vm = buildViewModel(awardId = "a1")
        testDispatcher.scheduler.advanceUntilIdle()
        vm.toggleDropdown()
        assertTrue(vm.uiState.value.showDropdown)

        vm.selectAward(awardTopProject)

        assertEquals(awardTopProject, vm.uiState.value.award)
        assertFalse(vm.uiState.value.showDropdown)
    }

    // ── TC-AWARD-012: notification count loaded ───────────────────────────────────
    @Test
    fun `init - unread notification count loaded`() = runTest {
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(3, vm.uiState.value.unreadNotificationCount)
    }

    // ── TC-AWARD-013: notification failure is silent ──────────────────────────────
    @Test
    fun `notification failure - no error surfaced`() = runTest {
        notificationRepo = FakeNotificationRepository(Result.failure(RuntimeException("fail")))
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, vm.uiState.value.unreadNotificationCount)
    }

    // ── TC-AWARD-014: showLanguageSelector / dismissLanguageSelector ──────────────
    // TC_IOS_AWARD_DETAIL_FUN_018
    @Test
    fun `showLanguageSelector and dismissLanguageSelector - toggles flag`() = runTest {
        val vm = buildViewModel()
        assertFalse(vm.uiState.value.showLanguageSelector)

        vm.showLanguageSelector()
        assertTrue(vm.uiState.value.showLanguageSelector)

        vm.dismissLanguageSelector()
        assertFalse(vm.uiState.value.showLanguageSelector)
    }

    // ── TC-AWARD-015: setLanguage updates selectedLanguage ────────────────────────
    // TC_IOS_AWARD_DETAIL_FUN_019
    @Test
    fun `setLanguage - updates selectedLanguage and dismisses selector`() = runTest {
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        vm.showLanguageSelector()

        vm.setLanguage("EN")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("EN", vm.uiState.value.selectedLanguage)
        assertFalse(vm.uiState.value.showLanguageSelector)
    }

    // ── TC-AWARD-016: allAwards failure when no awardId → error set ───────────────
    @Test
    fun `init without awardId - allAwards failure sets error`() = runTest {
        awardRepo = FakeAwardRepository(
            byIdResult = Result.failure(RuntimeException("unused")),
            listResult = Result.failure(RuntimeException("List error")),
        )
        val vm = buildViewModel(awardId = null)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(vm.uiState.value.error)
        assertFalse(vm.uiState.value.isLoading)
    }

    // ── TC-AWARD-017: language collected from repo on init ────────────────────────
    @Test
    fun `init - selectedLanguage collected from repository`() = runTest {
        languageRepo = FakeLanguageRepository("EN")
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("EN", vm.uiState.value.selectedLanguage)
    }
}

// ── Fakes ─────────────────────────────────────────────────────────────────────

private class FakeAwardRepository(
    var byIdResult: Result<Award>,
    var listResult: Result<List<Award>>,
) : AwardRepository {
    override suspend fun getAwards(): Result<List<Award>> = listResult
    override suspend fun getAwardById(id: String): Result<Award> = byIdResult
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
