package com.example.saa.domain.repository

import com.example.saa.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithGoogle(idToken: String): Result<User>
    suspend fun getCurrentUser(): Result<User?>
    suspend fun logout(): Result<Unit>
    fun observeAuthState(): Flow<Boolean>
}
