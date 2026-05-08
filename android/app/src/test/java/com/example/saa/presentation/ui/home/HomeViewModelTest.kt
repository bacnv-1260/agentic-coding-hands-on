package com.example.saa.presentation.ui.home

import com.example.saa.domain.model.Award
import com.example.saa.domain.repository.AwardRepository
import com.example.saa.domain.repository.LanguageRepository
import com.example.saa.domain.repository.NotificationRepository
import com.example.saa.domain.usecase.GetAwardsUseCase
import com.example.saa.domain.usecase.GetUnreadNotificationsUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
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
// TC_HOME_FUN_001  → loadAwards success → AwardsLoadState.Success
// TC_HOME_FUN_002  → loadAwards empty  → AwardsLoadState.Empty
// TC_HOME_FUN_003  → loadAwards error  → AwardsLoadState.Error
// TC_HOME_FUN_004  → 401/JWT error     → isUnauthenticated = true
// TC_HOME_FUN_005  → 403 error         → isForbidden = true
// TC_HOME_FUN_006  → retryLoadAwards   → reload
// TC_HOME_FUN_007  → notification count loaded
// TC_HOME_FUN_008  → notification failure → silent (no error surfaced)
// TC_HOME_FUN_009  → consumeAuthError  → flags cleared
// TC_HOME_FUN_010  → language selector toggle
// TC_HOME_FUN_011  → setLanguage same code → no repo call
// TC_HOME_FUN_012  → setLanguage different code → repo called
// TC_HOME_FUN_013  → onFabWriteKudoClicked fires navigate callback
// TC_HOME_FUN_014  → onFabWriteKudoClicked double-tap guard (< 500ms)
// ────────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var awardRepo: FakeAwardRepository
    private lateinit var notificationRepo: FakeNotificationRepository
    private lateinit var languageRepo: FakeLanguageRepository

    private val sampleAward = Award(
        id = "a1",
        name = "Top Talent",
        description = "Best talent award",
        category = "talent",
        imageUrl = "https://example.com/award.png",
        quantity = 10,
        quantityUnit = "Cá nhân",
        prizeValue = "7.000.000 VNĐ",
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        awardRepo = FakeAwardRepository(Result.success(listOf(sampleAward)))
        notificationRepo = FakeNotificationRepository(Result.success(3))
        languageRepo = FakeLanguageRepository("VN")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel() = HomeViewModel(
        getAwardsUseCase = GetAwardsUseCase(awardRepo),
        getUnreadNotificationsUseCase = GetUnreadNotificationsUseCase(notificationRepo),
        languageRepository = languageRepo,
    )

    // runTest(testDispatcher) shares the scheduler with viewModelScope (which uses Main=testDispatcher)
    // so runTest's cleanup advanceUntilIdle() won't hang on the infinite countdown.
    private fun runVMTest(block: suspend TestScope.(HomeViewModel) -> Unit) =
        runTest(testDispatcher) {
            val vm = buildViewModel()
            try {
                block(vm)
            } finally {
                vm.viewModelScope.cancel()
            }
        }

    // ── TC-HOME-001: Awards load success ────────────────────────────────────────
    @Test
    fun `loadAwards success - sets AwardsLoadState Success with awards`() = runVMTest { vm ->
        testDispatcher.scheduler.advanceTimeBy(2000)

        val state = vm.uiState.value
        assertTrue(state.awardsLoadState is AwardsLoadState.Success)
        assertEquals(listOf(sampleAward), (state.awardsLoadState as AwardsLoadState.Success).awards)
    }

    // ── TC-HOME-002: Awards load empty list ─────────────────────────────────────
    @Test
    fun `loadAwards empty - sets AwardsLoadState Empty`() {
        awardRepo = FakeAwardRepository(Result.success(emptyList()))
        runVMTest { vm ->
            testDispatcher.scheduler.advanceTimeBy(2000)
            assertTrue(vm.uiState.value.awardsLoadState is AwardsLoadState.Empty)
        }
    }

    // ── TC-HOME-003: Awards load failure (generic) ──────────────────────────────
    @Test
    fun `loadAwards failure - sets AwardsLoadState Error with message`() {
        awardRepo = FakeAwardRepository(Result.failure(RuntimeException("Network error")))
        runVMTest { vm ->
            testDispatcher.scheduler.advanceTimeBy(2000)
            val state = vm.uiState.value
            assertTrue(state.awardsLoadState is AwardsLoadState.Error)
            assertEquals("Network error", (state.awardsLoadState as AwardsLoadState.Error).message)
        }
    }

    // ── TC-HOME-004: Awards 401/JWT → isUnauthenticated ─────────────────────────
    @Test
    fun `loadAwards 401 error - sets isUnauthenticated true`() {
        awardRepo = FakeAwardRepository(Result.failure(RuntimeException("401 not authenticated")))
        runVMTest { vm ->
            testDispatcher.scheduler.advanceTimeBy(2000)
            assertTrue(vm.uiState.value.isUnauthenticated)
        }
    }

    // ── TC-HOME-004b: Awards JWT error → isUnauthenticated ─────────────────────
    @Test
    fun `loadAwards JWT error - sets isUnauthenticated true`() {
        awardRepo = FakeAwardRepository(Result.failure(RuntimeException("JWT expired")))
        runVMTest { vm ->
            testDispatcher.scheduler.advanceTimeBy(2000)
            assertTrue(vm.uiState.value.isUnauthenticated)
        }
    }

    // ── TC-HOME-005: Awards 403 → isForbidden ───────────────────────────────────
    @Test
    fun `loadAwards 403 error - sets isForbidden true`() {
        awardRepo = FakeAwardRepository(Result.failure(RuntimeException("403 not authorized")))
        runVMTest { vm ->
            testDispatcher.scheduler.advanceTimeBy(2000)
            assertTrue(vm.uiState.value.isForbidden)
        }
    }

    // ── TC-HOME-006: retryLoadAwards reloads ────────────────────────────────────
    @Test
    fun `retryLoadAwards - reloads awards from repository`() {
        awardRepo = FakeAwardRepository(Result.failure(RuntimeException("fail")))
        runVMTest { vm ->
            testDispatcher.scheduler.advanceTimeBy(2000)
            assertTrue(vm.uiState.value.awardsLoadState is AwardsLoadState.Error)

        awardRepo.result = Result.success(listOf(sampleAward))
        vm.retryLoadAwards()
        testDispatcher.scheduler.advanceTimeBy(2000)

        assertTrue(vm.uiState.value.awardsLoadState is AwardsLoadState.Success)
        }
    }

    // ── TC-HOME-007: Notification count loaded ──────────────────────────────────
    @Test
    fun `init - notification count loaded into state`() = runVMTest { vm ->
        testDispatcher.scheduler.advanceTimeBy(2000)

        assertEquals(3, vm.uiState.value.unreadNotificationCount)
    }

    // ── TC-HOME-008: Notification failure is silent ─────────────────────────────
    @Test
    fun `notification failure - no error surfaced and count stays 0`() {
        notificationRepo = FakeNotificationRepository(Result.failure(RuntimeException("fail")))
        runVMTest { vm ->
            testDispatcher.scheduler.advanceTimeBy(2000)

        assertEquals(0, vm.uiState.value.unreadNotificationCount)
            assertNull(vm.uiState.value.error)
        }
    }

    // ── TC-HOME-009: consumeAuthError clears flags ──────────────────────────────
    @Test
    fun `consumeAuthError - clears isUnauthenticated and isForbidden`() {
        awardRepo = FakeAwardRepository(Result.failure(RuntimeException("401 not authenticated")))
        runVMTest { vm ->
            testDispatcher.scheduler.advanceTimeBy(2000)
        assertTrue(vm.uiState.value.isUnauthenticated)

        vm.consumeAuthError()

            assertFalse(vm.uiState.value.isUnauthenticated)
            assertFalse(vm.uiState.value.isForbidden)
        }
    }

    // ── TC-HOME-010: Language selector toggle ───────────────────────────────────
    @Test
    fun `showLanguageSelector - sets showLanguageSelector true`() = runVMTest { vm ->
        assertFalse(vm.uiState.value.showLanguageSelector)

        vm.showLanguageSelector()

        assertTrue(vm.uiState.value.showLanguageSelector)
    }

    @Test
    fun `dismissLanguageSelector - sets showLanguageSelector false`() = runVMTest { vm ->
        vm.showLanguageSelector()
        assertTrue(vm.uiState.value.showLanguageSelector)

        vm.dismissLanguageSelector()

        assertFalse(vm.uiState.value.showLanguageSelector)
    }

    // ── TC-HOME-011: setLanguage same code → no repo call ───────────────────────
    @Test
    fun `setLanguage same as current - does not call repository`() = runVMTest { vm ->
        testDispatcher.scheduler.advanceTimeBy(2000)
        assertEquals("VN", vm.uiState.value.selectedLanguage)

        vm.setLanguage("VN")
        testDispatcher.scheduler.advanceTimeBy(2000)

        assertEquals(0, languageRepo.setLanguageCallCount)
    }

    // ── TC-HOME-012: setLanguage different code → repo called ───────────────────
    @Test
    fun `setLanguage different code - calls repository once`() = runVMTest { vm ->
        testDispatcher.scheduler.advanceTimeBy(2000)

        vm.setLanguage("EN")
        testDispatcher.scheduler.advanceTimeBy(2000)

        assertEquals(1, languageRepo.setLanguageCallCount)
        assertEquals("EN", languageRepo.lastSetLanguageCode)
    }

    // ── TC-HOME-013: onFabWriteKudoClicked fires callback ───────────────────────
    @Test
    fun `onFabWriteKudoClicked - invokes navigate callback`() = runVMTest { vm ->
        var navigateCalled = false

        vm.onFabWriteKudoClicked { navigateCalled = true }

        assertTrue(navigateCalled)
    }

    // ── TC-HOME-014: Double-tap guard < 500ms ───────────────────────────────────
    @Test
    fun `onFabWriteKudoClicked double-tap guard - second tap ignored within 500ms`() = runVMTest { vm ->
        var callCount = 0

        vm.onFabWriteKudoClicked { callCount++ }
        vm.onFabWriteKudoClicked { callCount++ }

        assertEquals(1, callCount)
    }

    // ── TC-HOME-015: Language from repo collected on init ────────────────────────
    @Test
    fun `init - language collected from repository`() {
        languageRepo = FakeLanguageRepository("EN")
        runVMTest { vm ->
            testDispatcher.scheduler.advanceTimeBy(2000)
            assertEquals("EN", vm.uiState.value.selectedLanguage)
        }
    }
}

// ── Fakes ─────────────────────────────────────────────────────────────────────

private class FakeAwardRepository(var result: Result<List<Award>>) : AwardRepository {
    override suspend fun getAwards(): Result<List<Award>> = result
    override suspend fun getAwardById(id: String): Result<Award> =
        result.map { it.first { award -> award.id == id } }
}

private class FakeNotificationRepository(private val result: Result<Int>) : NotificationRepository {
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
