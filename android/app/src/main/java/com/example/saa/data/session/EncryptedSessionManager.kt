package com.example.saa.data.session

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedSessionManager @Inject constructor(
    @ApplicationContext private val context: Context,
) : SessionManager {

    private val json = Json { ignoreUnknownKeys = true }

    private val prefs: SharedPreferences by lazy {
        try {
            createEncryptedPrefs()
        } catch (e: Exception) {
            Timber.w(e, "EncryptedSharedPreferences corrupt or missing keyset — recreating")
            context.deleteSharedPreferences(PREFS_FILE)
            createEncryptedPrefs()
        }
    }

    private fun createEncryptedPrefs(): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            PREFS_FILE,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    override suspend fun loadSession(): UserSession? {
        return try {
            val encoded = prefs.getString(KEY_SESSION, null) ?: return null
            json.decodeFromString(encoded)
        } catch (e: Exception) {
            Timber.e(e, "Failed to load persisted session — clearing")
            deleteSession()
            null
        }
    }

    override suspend fun saveSession(session: UserSession) {
        try {
            prefs.edit().putString(KEY_SESSION, json.encodeToString(session)).apply()
        } catch (e: Exception) {
            Timber.e(e, "Failed to save session")
        }
    }

    override suspend fun deleteSession() {
        prefs.edit().remove(KEY_SESSION).apply()
    }

    companion object {
        private const val PREFS_FILE = "saa_auth_session"
        private const val KEY_SESSION = "auth_session"
    }
}
