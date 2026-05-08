package com.example.saa.domain.repository

import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    fun getLanguage(): Flow<String>
    suspend fun setLanguage(code: String)
}
