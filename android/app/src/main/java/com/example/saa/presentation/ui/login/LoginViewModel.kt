package com.example.saa.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saa.domain.exception.AccessDeniedException
import com.example.saa.domain.repository.LanguageRepository
import com.example.saa.domain.usecase.LoginWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val languageRepository: LanguageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            languageRepository.getLanguage().collect { language ->
                _uiState.update { it.copy(selectedLanguage = language) }
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            loginWithGoogleUseCase(idToken).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }
                },
                onFailure = { throwable ->
                    when (throwable) {
                        is AccessDeniedException ->
                            _uiState.update { it.copy(isLoading = false, isAccessDenied = true) }
                        else -> {
                            Timber.e("Login failed: ${throwable.javaClass.simpleName}")
                            _uiState.update {
                                it.copy(isLoading = false, error = throwable.message ?: "Login failed")
                            }
                        }
                    }
                }
            )
        }
    }

    fun handleCredentialError(e: Throwable) {
        Timber.e("Credential error: ${e.javaClass.simpleName}")
        _uiState.update { it.copy(isLoading = false, error = "Authentication cancelled or failed") }
    }

    fun setLanguage(code: String) {
        if (code == _uiState.value.selectedLanguage) return
        viewModelScope.launch {
            languageRepository.setLanguage(code)
        }
    }

    fun showLanguageSelector() {
        _uiState.update { it.copy(showLanguageSelector = true) }
    }

    fun dismissLanguageSelector() {
        _uiState.update { it.copy(showLanguageSelector = false) }
    }

    fun consumeError() {
        _uiState.update { it.copy(error = null) }
    }

    fun consumeNavigationEvent() {
        _uiState.update { it.copy(isLoginSuccess = false, isAccessDenied = false) }
    }
}
