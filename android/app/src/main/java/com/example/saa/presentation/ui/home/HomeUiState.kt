package com.example.saa.presentation.ui.home

import com.example.saa.domain.model.Award

data class HomeUiState(
    val isLoading: Boolean = false,
    val awardsLoadState: AwardsLoadState = AwardsLoadState.Loading,
    val countdownDays: Long = 0L,
    val countdownHours: Long = 0L,
    val countdownMinutes: Long = 0L,
    val unreadNotificationCount: Int = 0,
    val isKudosAvailable: Boolean = true,
    val isUnauthenticated: Boolean = false,
    val isForbidden: Boolean = false,
    val error: String? = null,
    val selectedLanguage: String = "VN",
    val showLanguageSelector: Boolean = false,
)

sealed interface AwardsLoadState {
    data object Loading : AwardsLoadState
    data object Empty : AwardsLoadState
    data class Success(val awards: List<Award>) : AwardsLoadState
    data class Error(val message: String) : AwardsLoadState
}
