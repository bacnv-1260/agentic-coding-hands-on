package com.example.saa.presentation.ui.login

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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelLanguageTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeLanguageRepo: FakeLanguageRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeLanguageRepo = FakeLanguageRepository(initialLanguage = "VN")
        viewModel = LoginViewModel(
            loginWithGoogleUseCase = LoginWithGoogleUseCase(FakeAuthRepository()),
            languageRepository = fakeLanguageRepo,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * TC: Rapid-tap guard — code == selectedLanguage
     * → setLanguage("VN") when current is "VN"
     * → repository.setLanguage() must NOT be called
     * Covers: spec Edge Case #1, plan Phase 2 rapid-tap guard
     */
    @Test
    fun `setLanguage does not call repository when code equals current selectedLanguage`() = runTest {
        // Let init block collect initial "VN" from fakeLanguageRepo
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("VN", viewModel.uiState.value.selectedLanguage)

        viewModel.setLanguage("VN")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, fakeLanguageRepo.setLanguageCallCount)
    }

    /**
     * TC: Normal selection — code != selectedLanguage
     * → setLanguage("EN") when current is "VN"
     * → repository.setLanguage("EN") IS called exactly once
     */
    @Test
    fun `setLanguage calls repository once when code differs from current selectedLanguage`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("VN", viewModel.uiState.value.selectedLanguage)

        viewModel.setLanguage("EN")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, fakeLanguageRepo.setLanguageCallCount)
        assertEquals("EN", fakeLanguageRepo.lastSetLanguageCode)
    }

    /**
     * TC: Consecutive rapid-taps on same option
     * → setLanguage("VN") called twice when current is "VN"
     * → repository.setLanguage() called 0 times
     */
    @Test
    fun `setLanguage called twice with same code does not call repository`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setLanguage("VN")
        viewModel.setLanguage("VN")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, fakeLanguageRepo.setLanguageCallCount)
    }
}

// ── Fakes ────────────────────────────────────────────────────────────────────

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

private class FakeAuthRepository : AuthRepository {
    override suspend fun loginWithGoogle(idToken: String): Result<User> =
        Result.failure(NotImplementedError())
    override suspend fun getCurrentUser(): Result<User?> =
        Result.failure(NotImplementedError())
    override suspend fun logout(): Result<Unit> =
        Result.failure(NotImplementedError())
    override fun observeAuthState(): kotlinx.coroutines.flow.Flow<Boolean> =
        kotlinx.coroutines.flow.flow { }
}
