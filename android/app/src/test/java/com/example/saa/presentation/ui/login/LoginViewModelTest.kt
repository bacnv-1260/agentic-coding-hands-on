package com.example.saa.presentation.ui.login

import com.example.saa.domain.exception.AccessDeniedException
import com.example.saa.domain.model.User
import com.example.saa.domain.repository.AuthRepository
import com.example.saa.domain.repository.LanguageRepository
import com.example.saa.domain.usecase.LoginWithGoogleUseCase
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
// TC_LOGIN_FUN_005  → loginWithGoogle initiates OAuth flow (isLoading = true)
// TC_LOGIN_FUN_006  → isLoading true while processing, false after
// TC_LOGIN_FUN_007  → success → isLoginSuccess = true, navigate to Home
// TC_LOGIN_FUN_008  → double-tap guard: second tap ignored while isLoading = true
// TC_LOGIN_FUN_020  → non @sun-asterisk.com → AccessDeniedException → isAccessDenied = true
// TC_LOGIN_FUN (abnormal) → general error → error message set in state
// TC_LOGIN_FUN_002  → showLanguageSelector toggle
// TC_LOGIN_FUN_018  → dismissLanguageSelector without selection
// TC_LOGIN_FUN_003  → setLanguage calls repo
// Rapid-tap guard   → setLanguage same code → no repo call (already covered in language test)
// consumeError      → clears error
// consumeNavigationEvent → clears isLoginSuccess, isAccessDenied
// handleCredentialError → sets error message
// ────────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var authRepo: FakeLoginAuthRepository
    private lateinit var languageRepo: FakeLoginLanguageRepository

    private val validUser = User(id = "u1", email = "user@sun-asterisk.com", name = "Tester")
    private val invalidUser = User(id = "u2", email = "user@gmail.com", name = "Outsider")

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepo = FakeLoginAuthRepository(Result.success(validUser))
        languageRepo = FakeLoginLanguageRepository("VN")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel() = LoginViewModel(
        loginWithGoogleUseCase = LoginWithGoogleUseCase(authRepo),
        languageRepository = languageRepo,
    )

    // ── TC-LOGIN-001: isLoading set during login ──────────────────────────────────
    // TC_LOGIN_FUN_006
    @Test
    fun `loginWithGoogle - sets isLoading true while processing`() = runTest {
        // delayMs = 500 so the coroutine pauses at delay(500), leaving isLoading = true
        authRepo = FakeLoginAuthRepository(Result.success(validUser), delayMs = 500)
        val vm = buildViewModel()

        vm.loginWithGoogle("token")
        // runCurrent() starts the coroutine up to the first suspension (delay(500))
        testDispatcher.scheduler.runCurrent()
        assertTrue(vm.uiState.value.isLoading)

        // Finish the coroutine so runTest cleanup has no pending tasks
        testDispatcher.scheduler.advanceUntilIdle()
    }

    // ── TC-LOGIN-002: Success → isLoginSuccess and isLoading cleared ──────────────
    // TC_LOGIN_FUN_007
    @Test
    fun `loginWithGoogle success - isLoginSuccess true, isLoading false`() = runTest {
        val vm = buildViewModel()

        vm.loginWithGoogle("valid-token")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state.isLoginSuccess)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    // ── TC-LOGIN-003: Double-tap guard while loading ───────────────────────────────
    // TC_LOGIN_FUN_008
    @Test
    fun `loginWithGoogle - second call ignored while isLoading true`() = runTest {
        authRepo = FakeLoginAuthRepository(Result.success(validUser), delayMs = 500)
        val vm = buildViewModel()

        vm.loginWithGoogle("token")
        // Start coroutine up to delay(500) — isLoading becomes true
        testDispatcher.scheduler.runCurrent()
        assertTrue(vm.uiState.value.isLoading)

        vm.loginWithGoogle("token") // second call — isLoading=true, should be ignored

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, authRepo.loginCallCount)
    }

    // ── TC-LOGIN-004: Non @sun-asterisk.com → isAccessDenied ─────────────────────
    // TC_LOGIN_FUN_020
    @Test
    fun `loginWithGoogle non-sun-asterisk email - sets isAccessDenied true`() = runTest {
        authRepo = FakeLoginAuthRepository(Result.success(invalidUser))
        val vm = buildViewModel()

        vm.loginWithGoogle("token")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state.isAccessDenied)
        assertFalse(state.isLoginSuccess)
        assertFalse(state.isLoading)
    }

    // ── TC-LOGIN-005: General error → error message set ──────────────────────────
    @Test
    fun `loginWithGoogle failure - sets error message and isLoading false`() = runTest {
        authRepo = FakeLoginAuthRepository(Result.failure(RuntimeException("Auth service unavailable")))
        val vm = buildViewModel()

        vm.loginWithGoogle("token")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isLoginSuccess)
        assertEquals("Auth service unavailable", state.error)
    }

    // ── TC-LOGIN-006: Error with null message → fallback message ──────────────────
    @Test
    fun `loginWithGoogle failure with null message - sets fallback error message`() = runTest {
        authRepo = FakeLoginAuthRepository(Result.failure(RuntimeException(null as String?)))
        val vm = buildViewModel()

        vm.loginWithGoogle("token")
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(vm.uiState.value.error)
        assertEquals("Login failed", vm.uiState.value.error)
    }

    // ── TC-LOGIN-007: handleCredentialError sets error ────────────────────────────
    @Test
    fun `handleCredentialError - sets error message and clears isLoading`() = runTest {
        val vm = buildViewModel()

        vm.handleCredentialError(RuntimeException("Cancelled"))

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Authentication cancelled or failed", state.error)
    }

    // ── TC-LOGIN-008: consumeError clears error ───────────────────────────────────
    @Test
    fun `consumeError - clears error field`() = runTest {
        authRepo = FakeLoginAuthRepository(Result.failure(RuntimeException("fail")))
        val vm = buildViewModel()
        vm.loginWithGoogle("token")
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(vm.uiState.value.error)

        vm.consumeError()

        assertNull(vm.uiState.value.error)
    }

    // ── TC-LOGIN-009: consumeNavigationEvent clears navigation flags ──────────────
    @Test
    fun `consumeNavigationEvent - clears isLoginSuccess and isAccessDenied`() = runTest {
        val vm = buildViewModel()
        vm.loginWithGoogle("token")
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(vm.uiState.value.isLoginSuccess)

        vm.consumeNavigationEvent()

        assertFalse(vm.uiState.value.isLoginSuccess)
        assertFalse(vm.uiState.value.isAccessDenied)
    }

    // ── TC-LOGIN-010: showLanguageSelector ────────────────────────────────────────
    // TC_LOGIN_FUN_002
    @Test
    fun `showLanguageSelector - sets showLanguageSelector true`() = runTest {
        val vm = buildViewModel()
        assertFalse(vm.uiState.value.showLanguageSelector)

        vm.showLanguageSelector()

        assertTrue(vm.uiState.value.showLanguageSelector)
    }

    // ── TC-LOGIN-011: dismissLanguageSelector ─────────────────────────────────────
    // TC_LOGIN_FUN_018
    @Test
    fun `dismissLanguageSelector - sets showLanguageSelector false`() = runTest {
        val vm = buildViewModel()
        vm.showLanguageSelector()

        vm.dismissLanguageSelector()

        assertFalse(vm.uiState.value.showLanguageSelector)
    }

    // ── TC-LOGIN-012: setLanguage different code calls repo ───────────────────────
    // TC_LOGIN_FUN_003
    @Test
    fun `setLanguage different code - calls repository`() = runTest {
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        vm.setLanguage("EN")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, languageRepo.setLanguageCallCount)
        assertEquals("EN", languageRepo.lastSetLanguageCode)
    }

    // ── TC-LOGIN-013: setLanguage same code does not call repo ────────────────────
    // (covered also in LoginViewModelLanguageTest, repeated here for completeness)
    @Test
    fun `setLanguage same as current - does not call repository`() = runTest {
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("VN", vm.uiState.value.selectedLanguage)

        vm.setLanguage("VN")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, languageRepo.setLanguageCallCount)
    }

    // ── TC-LOGIN-014: language collected from repo on init ────────────────────────
    @Test
    fun `init - selectedLanguage collected from repository`() = runTest {
        languageRepo = FakeLoginLanguageRepository("EN")
        val vm = buildViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("EN", vm.uiState.value.selectedLanguage)
    }

    // ── TC-LOGIN-015: AccessDeniedException directly thrown ───────────────────────
    @Test
    fun `loginWithGoogle AccessDeniedException - sets isAccessDenied true`() = runTest {
        authRepo = FakeLoginAuthRepository(Result.failure(AccessDeniedException("Domain not allowed")))
        val vm = buildViewModel()

        vm.loginWithGoogle("token")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(vm.uiState.value.isAccessDenied)
        assertFalse(vm.uiState.value.isLoginSuccess)
        assertNull(vm.uiState.value.error)
    }
}

// ── Fakes ─────────────────────────────────────────────────────────────────────

private class FakeLoginAuthRepository(
    private val result: Result<User>,
    private val delayMs: Long = 0L,
) : AuthRepository {
    var loginCallCount = 0

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        loginCallCount++
        if (delayMs > 0) kotlinx.coroutines.delay(delayMs)
        return result
    }

    override suspend fun getCurrentUser(): Result<User?> = Result.success(null)
    override suspend fun logout(): Result<Unit> = Result.success(Unit)
    override fun observeAuthState(): kotlinx.coroutines.flow.Flow<Boolean> =
        kotlinx.coroutines.flow.flow { emit(false) }
}

private class FakeLoginLanguageRepository(initialLanguage: String) : LanguageRepository {
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
