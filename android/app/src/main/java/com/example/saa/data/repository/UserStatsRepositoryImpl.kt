package com.example.saa.data.repository

import com.example.saa.data.remote.dto.toDomain
import com.example.saa.data.remote.source.UserStatsDataSource
import com.example.saa.domain.model.Department
import com.example.saa.domain.model.GiftRecipient
import com.example.saa.domain.model.Hashtag
import com.example.saa.domain.model.UserStats
import com.example.saa.domain.repository.UserStatsRepository
import timber.log.Timber
import javax.inject.Inject

class UserStatsRepositoryImpl @Inject constructor(
    private val dataSource: UserStatsDataSource,
) : UserStatsRepository {

    override suspend fun getUserStats(): Result<UserStats> = runCatching {
        dataSource.getUserStats().toDomain()
    }.onFailure { Timber.e(it, "getUserStats failed") }

    override suspend fun getGiftRecipients(): Result<List<GiftRecipient>> = runCatching {
        dataSource.getGiftRecipients().map { it.toDomain() }
    }.onFailure { Timber.e(it, "getGiftRecipients failed") }

    override suspend fun getHashtags(): Result<List<Hashtag>> = runCatching {
        dataSource.getHashtags().map { it.toDomain() }
    }.onFailure { Timber.e(it, "getHashtags failed") }

    override suspend fun getDepartments(): Result<List<Department>> = runCatching {
        dataSource.getDepartments().map { it.toDomain() }
    }.onFailure { Timber.e(it, "getDepartments failed") }

    override suspend fun getNextSecretBox(): Result<String> = runCatching {
        dataSource.getNextSecretBox()
    }.onFailure { Timber.e(it, "getNextSecretBox failed") }

    override suspend fun openSecretBox(boxId: String): Result<Unit> = runCatching {
        dataSource.openSecretBox(boxId)
    }.onFailure { Timber.e(it, "openSecretBox failed") }
}
