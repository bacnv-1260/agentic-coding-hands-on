package com.example.saa.data.remote.source

import com.example.saa.data.remote.dto.UserDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonPrimitive
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseAuthDataSource @Inject constructor(
    private val supabase: SupabaseClient,
) {
    suspend fun signInWithGoogle(idToken: String): Result<UserDto> {
        return try {
            supabase.auth.signInWith(IDToken) {
                this.idToken = idToken
                provider = Google
            }
            val user = supabase.auth.currentUserOrNull()
                ?: error("User not found after sign-in")
            val name = (user.userMetadata?.get("full_name") as? JsonPrimitive)?.content
            Result.success(UserDto(id = user.id, email = user.email, name = name))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e("signInWithGoogle failed: ${e.javaClass.simpleName}")
            Result.failure(e)
        }
    }

    suspend fun getCurrentSession(): Result<UserDto?> {
        return try {
            // Wait until the Auth plugin has finished initializing the session from storage.
            // Without this, currentUserOrNull() races and returns null even when a valid
            // session was persisted to disk.
            val status = supabase.auth.sessionStatus
                .first { it !is SessionStatus.Initializing }

            if (status is SessionStatus.Authenticated) {
                val user = supabase.auth.currentUserOrNull()
                if (user == null) {
                    Result.success(null)
                } else {
                    val name = (user.userMetadata?.get("full_name") as? JsonPrimitive)?.content
                    Result.success(UserDto(id = user.id, email = user.email, name = name))
                }
            } else {
                Result.success(null)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e("getCurrentSession failed: ${e.javaClass.simpleName}")
            Result.failure(e)
        }
    }

    fun observeAuthState(): Flow<Boolean> = supabase.auth.sessionStatus
        .map { it is SessionStatus.Authenticated }

    suspend fun signOut(): Result<Unit> {
        return try {
            supabase.auth.signOut()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e("signOut failed: ${e.javaClass.simpleName}")
            Result.failure(e)
        }
    }
}
