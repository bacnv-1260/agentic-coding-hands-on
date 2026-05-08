package com.example.saa.data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class LanguagePreferenceDataSourceTest {

    /**
     * TC: DataStore throws IOException on read
     * → catch block emits emptyPreferences()
     * → map returns fallback "VN"
     * Covers: TR-006, spec Edge Case: DataStore read failure
     */
    @Test
    fun `getLanguage returns VN fallback when DataStore throws IOException`() = runTest {
        val errorDataStore = object : DataStore<Preferences> {
            override val data: Flow<Preferences> = flow {
                throw IOException("Simulated DataStore I/O failure")
            }
            override suspend fun updateData(
                transform: suspend (t: Preferences) -> Preferences,
            ): Preferences = throw NotImplementedError()
        }

        val dataSource = LanguagePreferenceDataSource(errorDataStore)
        val result = dataSource.getLanguage().first()

        assertEquals("VN", result)
    }
}
