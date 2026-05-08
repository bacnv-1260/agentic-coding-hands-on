package com.example.saa.domain.usecase

import com.example.saa.domain.model.User
import com.example.saa.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): Result<User?> = repository.getCurrentUser()
}
