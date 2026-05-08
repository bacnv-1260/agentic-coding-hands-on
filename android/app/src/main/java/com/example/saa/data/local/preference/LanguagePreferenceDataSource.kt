package com.example.saa.data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguagePreferenceDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val selectedLanguageKey = stringPreferencesKey("selected_language")

    fun getLanguage(): Flow<String> = dataStore.data
        .catch { cause ->
            Timber.e(cause, "LanguagePreferenceDataSource")
            emit(emptyPreferences())
        }
        .map { prefs ->
            prefs[selectedLanguageKey] ?: "VN"
        }

    suspend fun setLanguage(code: String) {
        dataStore.edit { prefs ->
            prefs[selectedLanguageKey] = code
        }
    }
}
