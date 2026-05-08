package com.example.saa.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saa.domain.repository.LanguageRepository
import com.example.saa.domain.usecase.GetAwardsUseCase
import com.example.saa.domain.usecase.GetUnreadNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAwardsUseCase: GetAwardsUseCase,
    private val getUnreadNotificationsUseCase: GetUnreadNotificationsUseCase,
    private val languageRepository: LanguageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Debounce for FAB to prevent double-tap
    private var lastFabClickMs = 0L

    // Event date: 26/12/2026 00:00:00 GMT+7
    private val eventEpochMs: Long = ZonedDateTime.of(2026, 12, 26, 0, 0, 0, 0, ZoneOffset.ofHours(7))
        .toInstant()
        .toEpochMilli()

    init {
        startCountdown()
        loadAwards()
        loadNotificationCount()
        collectLanguage()
    }

    private fun collectLanguage() {
        viewModelScope.launch {
            languageRepository.getLanguage().collect { language ->
                _uiState.update { it.copy(selectedLanguage = language) }
            }
        }
    }

    private fun startCountdown() {
        viewModelScope.launch {
            while (true) {
                val diffMs = eventEpochMs - System.currentTimeMillis()
                if (diffMs > 0L) {
                    val totalSeconds = diffMs / 1_000L
                    _uiState.update { state ->
                        state.copy(
                            countdownDays = totalSeconds / 86_400L,
                            countdownHours = (totalSeconds % 86_400L) / 3_600L,
                            countdownMinutes = (totalSeconds % 3_600L) / 60L,
                        )
                    }
                } else {
                    _uiState.update { it.copy(countdownDays = 0, countdownHours = 0, countdownMinutes = 0) }
                }
                delay(1_000L)
            }
        }
    }

    private fun loadAwards() {
        viewModelScope.launch {
            _uiState.update { it.copy(awardsLoadState = AwardsLoadState.Loading) }
            getAwardsUseCase()
                .onSuccess { awards ->
                    _uiState.update {
                        it.copy(
                            awardsLoadState = if (awards.isEmpty()) AwardsLoadState.Empty
                            else AwardsLoadState.Success(awards)
                        )
                    }
                }
                .onFailure { e ->
                    Timber.e(e, "Failed to load awards")
                    handleApiError(e)
                }
        }
    }

    private fun loadNotificationCount() {
        viewModelScope.launch {
            getUnreadNotificationsUseCase()
                .onSuccess { count -> _uiState.update { it.copy(unreadNotificationCount = count) } }
                .onFailure { e -> Timber.e(e, "Failed to load notification count") }
        }
    }

    private fun handleApiError(e: Throwable) {
        val message = e.message ?: "Unknown error"
        when {
            message.contains("401") || message.contains("JWT") || message.contains("not authenticated") ->
                _uiState.update { it.copy(isUnauthenticated = true) }
            message.contains("403") || message.contains("not authorized") ->
                _uiState.update { it.copy(isForbidden = true) }
            else ->
                _uiState.update { it.copy(awardsLoadState = AwardsLoadState.Error(message)) }
        }
    }

    fun consumeAuthError() {
        _uiState.update { it.copy(isUnauthenticated = false, isForbidden = false) }
    }

    fun showLanguageSelector() {
        _uiState.update { it.copy(showLanguageSelector = true) }
    }

    fun dismissLanguageSelector() {
        _uiState.update { it.copy(showLanguageSelector = false) }
    }

    fun setLanguage(code: String) {
        if (code == _uiState.value.selectedLanguage) return
        viewModelScope.launch {
            languageRepository.setLanguage(code)
        }
    }

    fun retryLoadAwards() {
        loadAwards()
    }

    fun onFabWriteKudoClicked(navigate: () -> Unit) {
        val now = System.currentTimeMillis()
        if (now - lastFabClickMs > 500L) {
            lastFabClickMs = now
            navigate()
        }
    }

    fun onFabKudosFeedClicked(navigate: () -> Unit) {
        val now = System.currentTimeMillis()
        if (now - lastFabClickMs > 500L) {
            lastFabClickMs = now
            navigate()
        }
    }
}
