package com.example.saa.presentation.ui.award

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saa.domain.model.Award
import com.example.saa.domain.repository.LanguageRepository
import com.example.saa.domain.usecase.GetAwardByIdUseCase
import com.example.saa.domain.usecase.GetAwardsUseCase
import com.example.saa.domain.usecase.GetUnreadNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AwardDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAwardByIdUseCase: GetAwardByIdUseCase,
    private val getAwardsUseCase: GetAwardsUseCase,
    private val getUnreadNotificationsUseCase: GetUnreadNotificationsUseCase,
    private val languageRepository: LanguageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AwardDetailUiState())
    val uiState: StateFlow<AwardDetailUiState> = _uiState.asStateFlow()

    private val awardId: String? get() = savedStateHandle["awardId"]

    init {
        loadAward()
        loadAllAwards()
        loadNotificationCount()
        collectLanguage()
    }

    private fun loadAward() {
        val id = awardId ?: run {
            // No awardId (reached via Awards tab) — loadAllAwards() will select the first award
            _uiState.update { it.copy(isLoading = true, error = null) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getAwardByIdUseCase(id)
                .onSuccess { award ->
                    _uiState.update { it.copy(isLoading = false, award = award) }
                }
                .onFailure { throwable ->
                    Timber.e(throwable, "Failed to load award id=$id")
                    _uiState.update { it.copy(isLoading = false, error = "Không thể tải giải thưởng. Thử lại?") }
                }
        }
    }

    private fun loadAllAwards() {
        viewModelScope.launch {
            getAwardsUseCase()
                .onSuccess { awards ->
                    _uiState.update { state ->
                        // When no specific awardId, auto-select the first available award
                        val award = if (awardId == null && state.award == null) {
                            awards.firstOrNull()
                        } else {
                            state.award
                        }
                        state.copy(allAwards = awards, award = award, isLoading = false)
                    }
                }
                .onFailure { throwable ->
                    Timber.e(throwable, "Failed to load all awards for dropdown")
                    if (awardId == null) {
                        _uiState.update { it.copy(isLoading = false, error = "Không thể tải giải thưởng. Thử lại?") }
                    }
                }
        }
    }

    private fun loadNotificationCount() {
        viewModelScope.launch {
            getUnreadNotificationsUseCase()
                .onSuccess { count ->
                    _uiState.update { it.copy(unreadNotificationCount = count) }
                }
                .onFailure { throwable ->
                    Timber.w(throwable, "Failed to load notification count")
                }
        }
    }

    private fun collectLanguage() {
        viewModelScope.launch {
            languageRepository.getLanguage().collect { language ->
                _uiState.update { it.copy(selectedLanguage = language) }
            }
        }
    }

    fun retry() {
        loadAward()
        if (awardId == null) loadAllAwards()
    }

    fun toggleDropdown() {
        _uiState.update { it.copy(showDropdown = !it.showDropdown) }
    }

    fun dismissDropdown() {
        _uiState.update { it.copy(showDropdown = false) }
    }

    fun selectAward(award: Award) {
        _uiState.update { it.copy(award = award, showDropdown = false) }
    }

    fun showLanguageSelector() {
        _uiState.update { it.copy(showLanguageSelector = true) }
    }

    fun dismissLanguageSelector() {
        _uiState.update { it.copy(showLanguageSelector = false) }
    }

    fun setLanguage(code: String) {
        viewModelScope.launch {
            languageRepository.setLanguage(code)
            _uiState.update { it.copy(showLanguageSelector = false) }
        }
    }
}
