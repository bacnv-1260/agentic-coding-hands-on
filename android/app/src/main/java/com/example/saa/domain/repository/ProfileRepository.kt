package com.example.saa.domain.repository

import com.example.saa.domain.model.Profile

interface ProfileRepository {
    suspend fun getMyProfile(): Result<Profile>
    suspend fun searchProfiles(query: String): Result<List<Profile>>
}
