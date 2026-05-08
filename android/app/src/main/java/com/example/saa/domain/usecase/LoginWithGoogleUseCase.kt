package com.example.saa.domain.usecase

import com.example.saa.domain.exception.AccessDeniedException
import com.example.saa.domain.model.User
import com.example.saa.domain.repository.AuthRepository
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(idToken: String): Result<User> =
        authRepository.loginWithGoogle(idToken).mapCatching { user ->
            if (!user.email.endsWith("@sun-asterisk.com")) {
                throw AccessDeniedException()
            }
            user
        }
}
