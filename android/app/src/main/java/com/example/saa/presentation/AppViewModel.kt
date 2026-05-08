package com.example.saa.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saa.domain.repository.AuthRepository
import com.example.saa.domain.repository.LanguageRepository
import com.example.saa.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthState {
    data object Loading : AuthState
    data object LoggedIn : AuthState
    data object LoggedOut : AuthState
}

@HiltViewModel
class AppViewModel @Inject constructor(
    languageRepository: LanguageRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val selectedLanguage: StateFlow<String> = languageRepository.getLanguage()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = "VN",
        )

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkSession()
        observeSessionChanges()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val result = getCurrentUserUseCase()
            _authState.value = if (result.getOrNull() != null) {
                AuthState.LoggedIn
            } else {
                AuthState.LoggedOut
            }
        }
    }

    // Continuously watch Supabase auth state. Skips the first emission (handled by
    // checkSession above) to avoid a race between the initial Loading state and the
    // flow's replay value.
    private fun observeSessionChanges() {
        viewModelScope.launch {
            authRepository.observeAuthState()
                .drop(1)
                .collect { isAuthenticated ->
                    if (!isAuthenticated) {
                        _authState.value = AuthState.LoggedOut
                    }
                }
        }
    }
}
