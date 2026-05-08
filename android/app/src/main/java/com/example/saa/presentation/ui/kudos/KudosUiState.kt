package com.example.saa.presentation.ui.kudos

import com.example.saa.domain.model.Department
import com.example.saa.domain.model.GiftRecipient
import com.example.saa.domain.model.Hashtag
import com.example.saa.domain.model.Kudos
import com.example.saa.domain.model.UserStats

data class KudosUiState(
    val isLoading: Boolean = false,
    val highlightKudos: List<Kudos> = emptyList(),
    val allKudos: List<Kudos> = emptyList(),
    val hashtags: List<Hashtag> = emptyList(),
    val departments: List<Department> = emptyList(),
    val userStats: UserStats? = null,
    val giftRecipients: List<GiftRecipient> = emptyList(),
    val selectedHashtagId: String? = null,
    val selectedDepartmentId: String? = null,
    val currentCarouselPage: Int = 0,
    val error: String? = null,
    val isLoadingMore: Boolean = false,
    val hasMoreKudos: Boolean = true,
    val isUnauthenticated: Boolean = false,
    // Header shared state
    val unreadNotificationCount: Int = 0,
    val selectedLanguage: String = "VN",
    val showLanguageSelector: Boolean = false,
)
