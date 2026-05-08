package com.example.saa.presentation.ui.profile

import com.example.saa.domain.model.Kudos
import com.example.saa.domain.model.KudosFilter
import com.example.saa.domain.model.Profile
import com.example.saa.domain.model.UserStats

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val stats: UserStats? = null,
    val kudosList: List<Kudos> = emptyList(),
    val kudosFilter: KudosFilter = KudosFilter.SENT,
    val isDropdownOpen: Boolean = false,
    val error: String? = null,
    val isUnauthenticated: Boolean = false,
    // HomeHeader shared state (same pattern as KudosUiState)
    val unreadNotificationCount: Int = 0,
    val selectedLanguage: String = "VN",
    val showLanguageSelector: Boolean = false,
)
