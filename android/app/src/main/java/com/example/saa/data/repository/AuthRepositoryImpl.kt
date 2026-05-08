package com.example.saa.data.repository

import com.example.saa.data.remote.dto.toDomain
import com.example.saa.data.remote.source.SupabaseAuthDataSource
import com.example.saa.domain.model.User
import com.example.saa.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val dataSource: SupabaseAuthDataSource,
) : AuthRepository {

    override suspend fun loginWithGoogle(idToken: String): Result<User> =
        dataSource.signInWithGoogle(idToken).map { it.toDomain() }

    override suspend fun getCurrentUser(): Result<User?> =
        dataSource.getCurrentSession().map { it?.toDomain() }

    override suspend fun logout(): Result<Unit> =
        dataSource.signOut()

    override fun observeAuthState(): Flow<Boolean> =
        dataSource.observeAuthState()
}
