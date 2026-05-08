package com.example.saa.data.repository

import com.example.saa.data.local.preference.LanguagePreferenceDataSource
import com.example.saa.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageRepositoryImpl @Inject constructor(
    private val dataSource: LanguagePreferenceDataSource,
) : LanguageRepository {

    override fun getLanguage(): Flow<String> = dataSource.getLanguage()

    override suspend fun setLanguage(code: String) = dataSource.setLanguage(code)
}
