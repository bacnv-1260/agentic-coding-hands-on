package com.example.saa.presentation.ui.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val selectedLanguage: String = "VN",
    val showLanguageSelector: Boolean = false,
    val error: String? = null,
    val isLoginSuccess: Boolean = false,
    val isAccessDenied: Boolean = false,
)
