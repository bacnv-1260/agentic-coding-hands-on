package com.example.saa.data.repository

import com.example.saa.data.remote.dto.toDomain
import com.example.saa.data.remote.source.ProfileDataSource
import com.example.saa.domain.model.Profile
import com.example.saa.domain.repository.ProfileRepository
import timber.log.Timber
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val dataSource: ProfileDataSource,
) : ProfileRepository {

    override suspend fun getMyProfile(): Result<Profile> = runCatching {
        dataSource.getMyProfile()?.toDomain() ?: error("Profile not found")
    }.onFailure { Timber.e(it, "getMyProfile failed") }

    override suspend fun searchProfiles(query: String): Result<List<Profile>> = runCatching {
        dataSource.searchProfiles(query).map { it.toDomain() }
    }.onFailure { Timber.e(it, "searchProfiles failed") }
}
