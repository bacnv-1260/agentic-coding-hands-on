package com.example.saa.presentation.ui.award

import com.example.saa.domain.model.Award

data class AwardDetailUiState(
    val isLoading: Boolean = false,
    val award: Award? = null,
    val allAwards: List<Award> = emptyList(),
    val showDropdown: Boolean = false,
    val error: String? = null,
    val selectedLanguage: String = "VN",
    val showLanguageSelector: Boolean = false,
    val unreadNotificationCount: Int = 0,
)
