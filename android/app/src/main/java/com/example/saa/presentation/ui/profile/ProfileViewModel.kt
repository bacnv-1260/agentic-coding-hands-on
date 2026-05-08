package com.example.saa.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saa.domain.model.KudosFilter
import com.example.saa.domain.repository.LanguageRepository
import com.example.saa.domain.usecase.GetMyProfileUseCase
import com.example.saa.domain.usecase.GetProfileKudosUseCase
import com.example.saa.domain.usecase.GetUnreadNotificationsUseCase
import com.example.saa.domain.usecase.GetUserStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val getProfileKudosUseCase: GetProfileKudosUseCase,
    private val getUnreadNotificationsUseCase: GetUnreadNotificationsUseCase,
    private val languageRepository: LanguageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadData()
        loadNotificationCount()
        collectLanguage()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val profileDeferred = async { getMyProfileUseCase() }
            val statsDeferred = async { getUserStatsUseCase() }
            val kudosDeferred = async { getProfileKudosUseCase(_uiState.value.kudosFilter) }

            val profileResult = profileDeferred.await()
            val statsResult = statsDeferred.await()
            val kudosResult = kudosDeferred.await()

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    profile = profileResult.getOrElse { state.profile },
                    stats = statsResult.getOrElse { state.stats },
                    kudosList = kudosResult.getOrElse { state.kudosList },
                    error = listOf(profileResult, statsResult)
                        .firstOrNull { it.isFailure }
                        ?.exceptionOrNull()
                        ?.message,
                )
            }
        }
    }

    fun onFilterChange(filter: KudosFilter) {
        _uiState.update { it.copy(kudosFilter = filter, isDropdownOpen = false) }
        loadKudos(filter)
    }

    private fun loadKudos(filter: KudosFilter) {
        viewModelScope.launch {
            getProfileKudosUseCase(filter)
                .onSuccess { list -> _uiState.update { it.copy(kudosList = list) } }
                .onFailure { e ->
                    Timber.e(e, "loadKudos failed")
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun onDropdownToggle() {
        _uiState.update { it.copy(isDropdownOpen = !it.isDropdownOpen) }
    }

    fun onDropdownDismiss() {
        _uiState.update { it.copy(isDropdownOpen = false) }
    }

    fun onLanguageClick() {
        _uiState.update { it.copy(showLanguageSelector = true) }
    }

    fun onLanguageDismiss() {
        _uiState.update { it.copy(showLanguageSelector = false) }
    }

    fun setLanguage(code: String) {
        viewModelScope.launch {
            languageRepository.setLanguage(code)
            _uiState.update { it.copy(showLanguageSelector = false) }
        }
    }

    fun consumeError() {
        _uiState.update { it.copy(error = null) }
    }

    fun consumeAuthError() {
        _uiState.update { it.copy(isUnauthenticated = false) }
    }

    private fun loadNotificationCount() {
        viewModelScope.launch {
            getUnreadNotificationsUseCase()
                .onSuccess { count -> _uiState.update { it.copy(unreadNotificationCount = count) } }
                .onFailure { Timber.w(it, "Failed to load notification count") }
        }
    }

    private fun collectLanguage() {
        viewModelScope.launch {
            languageRepository.getLanguage().collect { language ->
                _uiState.update { it.copy(selectedLanguage = language) }
            }
        }
    }
}
