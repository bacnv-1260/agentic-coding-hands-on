package com.example.saa.presentation.ui.kudos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saa.domain.repository.KudosRepository
import com.example.saa.domain.repository.LanguageRepository
import com.example.saa.domain.usecase.GetAllKudosUseCase
import com.example.saa.domain.usecase.GetDepartmentsUseCase
import com.example.saa.domain.usecase.GetGiftRecipientsUseCase
import com.example.saa.domain.usecase.GetHashtagsUseCase
import com.example.saa.domain.usecase.GetHighlightKudosUseCase
import com.example.saa.domain.usecase.GetUnreadNotificationsUseCase
import com.example.saa.domain.usecase.GetUserStatsUseCase
import com.example.saa.domain.usecase.OpenSecretBoxUseCase
import com.example.saa.domain.usecase.ToggleKudosLikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val PAGE_SIZE = 20

@HiltViewModel
class KudosViewModel @Inject constructor(
    private val getHighlightKudosUseCase: GetHighlightKudosUseCase,
    private val getAllKudosUseCase: GetAllKudosUseCase,
    private val toggleKudosLikeUseCase: ToggleKudosLikeUseCase,
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val getGiftRecipientsUseCase: GetGiftRecipientsUseCase,
    private val getHashtagsUseCase: GetHashtagsUseCase,
    private val getDepartmentsUseCase: GetDepartmentsUseCase,
    private val openSecretBoxUseCase: OpenSecretBoxUseCase,
    private val kudosRepository: KudosRepository,
    private val getUnreadNotificationsUseCase: GetUnreadNotificationsUseCase,
    private val languageRepository: LanguageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(KudosUiState())
    val uiState: StateFlow<KudosUiState> = _uiState.asStateFlow()

    private var currentPage = 1

    init {
        loadInitialData()
        observeRealtime()
        loadNotificationCount()
        collectLanguage()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            currentPage = 1

            val hashtagId = _uiState.value.selectedHashtagId
            val departmentId = _uiState.value.selectedDepartmentId

            val highlightDeferred = async { getHighlightKudosUseCase(hashtagId, departmentId) }
            val allKudosDeferred = async { getAllKudosUseCase(1, PAGE_SIZE, hashtagId, departmentId) }
            val statsDeferred = async { getUserStatsUseCase() }
            val giftsDeferred = async { getGiftRecipientsUseCase() }
            val hashtagsDeferred = async { getHashtagsUseCase() }
            val departmentsDeferred = async { getDepartmentsUseCase() }

            val highlightResult = highlightDeferred.await()
            val allKudosResult = allKudosDeferred.await()
            val statsResult = statsDeferred.await()
            val giftsResult = giftsDeferred.await()
            val hashtagsResult = hashtagsDeferred.await()
            val departmentsResult = departmentsDeferred.await()

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    highlightKudos = highlightResult.getOrElse { emptyList() },
                    allKudos = allKudosResult.getOrElse { emptyList() },
                    hasMoreKudos = allKudosResult.getOrElse { emptyList() }.size == PAGE_SIZE,
                    userStats = statsResult.getOrElse { state.userStats },
                    giftRecipients = giftsResult.getOrElse { emptyList() },
                    hashtags = hashtagsResult.getOrElse { state.hashtags },
                    departments = departmentsResult.getOrElse { state.departments },
                    error = listOf(highlightResult, allKudosResult, statsResult)
                        .firstOrNull { it.isFailure }
                        ?.exceptionOrNull()
                        ?.message,
                )
            }
        }
    }

    fun onFilterHashtag(hashtagId: String?) {
        _uiState.update { it.copy(selectedHashtagId = hashtagId, currentCarouselPage = 0) }
        loadInitialData()
    }

    fun onFilterDepartment(departmentId: String?) {
        _uiState.update { it.copy(selectedDepartmentId = departmentId, currentCarouselPage = 0) }
        loadInitialData()
    }

    fun onCarouselPageChange(page: Int) {
        _uiState.update { it.copy(currentCarouselPage = page) }
    }

    fun onToggleLike(kudosId: String) {
        // Capture current like state before optimistic update
        val wasLiked = _uiState.value.highlightKudos.find { it.id == kudosId }?.isLiked
            ?: _uiState.value.allKudos.find { it.id == kudosId }?.isLiked
            ?: false

        // Optimistic update
        _uiState.update { state ->
            val updatedHighlight = state.highlightKudos.map { kudos ->
                if (kudos.id == kudosId) {
                    val newLiked = !kudos.isLiked
                    kudos.copy(
                        isLiked = newLiked,
                        heartCount = kudos.heartCount + if (newLiked) 1 else -1,
                    )
                } else kudos
            }
            val updatedAll = state.allKudos.map { kudos ->
                if (kudos.id == kudosId) {
                    val newLiked = !kudos.isLiked
                    kudos.copy(
                        isLiked = newLiked,
                        heartCount = kudos.heartCount + if (newLiked) 1 else -1,
                    )
                } else kudos
            }
            state.copy(highlightKudos = updatedHighlight, allKudos = updatedAll)
        }

        viewModelScope.launch {
            toggleKudosLikeUseCase(kudosId, wasLiked).onFailure { e ->
                Timber.e(e, "toggleLike failed, reverting")
                // Revert on failure
                _uiState.update { state ->
                    val revertedHighlight = state.highlightKudos.map { kudos ->
                        if (kudos.id == kudosId) {
                            val revertedLiked = !kudos.isLiked
                            kudos.copy(
                                isLiked = revertedLiked,
                                heartCount = kudos.heartCount + if (revertedLiked) 1 else -1,
                            )
                        } else kudos
                    }
                    val revertedAll = state.allKudos.map { kudos ->
                        if (kudos.id == kudosId) {
                            val revertedLiked = !kudos.isLiked
                            kudos.copy(
                                isLiked = revertedLiked,
                                heartCount = kudos.heartCount + if (revertedLiked) 1 else -1,
                            )
                        } else kudos
                    }
                    state.copy(
                        highlightKudos = revertedHighlight,
                        allKudos = revertedAll,
                        error = e.message,
                    )
                }
            }
        }
    }

    fun loadMoreKudos() {
        val state = _uiState.value
        if (state.isLoadingMore || !state.hasMoreKudos) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            val nextPage = currentPage + 1
            getAllKudosUseCase(
                nextPage,
                PAGE_SIZE,
                state.selectedHashtagId,
                state.selectedDepartmentId,
            ).onSuccess { newKudos ->
                currentPage = nextPage
                _uiState.update { s ->
                    s.copy(
                        allKudos = s.allKudos + newKudos,
                        isLoadingMore = false,
                        hasMoreKudos = newKudos.size == PAGE_SIZE,
                    )
                }
            }.onFailure { e ->
                Timber.e(e, "loadMoreKudos failed")
                _uiState.update { it.copy(isLoadingMore = false, error = e.message) }
            }
        }
    }

    fun onOpenSecretBox() {
        viewModelScope.launch {
            openSecretBoxUseCase().onSuccess {
                // Refresh stats after opening
                getUserStatsUseCase().onSuccess { stats ->
                    _uiState.update { it.copy(userStats = stats) }
                }
            }.onFailure { e ->
                if (e !is IllegalStateException) {
                    Timber.e(e, "openSecretBox failed")
                    _uiState.update { it.copy(error = e.message) }
                }
            }
        }
    }

    fun consumeError() {
        _uiState.update { it.copy(error = null) }
    }

    fun consumeAuthError() {
        _uiState.update { it.copy(isUnauthenticated = false) }
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

    private fun loadNotificationCount() {
        viewModelScope.launch {
            getUnreadNotificationsUseCase()
                .onSuccess { count ->
                    _uiState.update { it.copy(unreadNotificationCount = count) }
                }
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

    private fun observeRealtime() {
        viewModelScope.launch {
            kudosRepository.observeNewKudos()
                .catch { e -> Timber.e(e, "Realtime observeNewKudos error") }
                .collect { newKudos ->
                    _uiState.update { state ->
                        state.copy(allKudos = listOf(newKudos) + state.allKudos)
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            (kudosRepository as? com.example.saa.data.repository.KudosRepositoryImpl)
                ?.unsubscribe()
        }
    }
}
