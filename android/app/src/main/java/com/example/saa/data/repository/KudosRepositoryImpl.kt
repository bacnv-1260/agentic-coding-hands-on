package com.example.saa.data.repository

import com.example.saa.data.remote.dto.WriteKudoDto
import com.example.saa.data.remote.dto.toDomain
import com.example.saa.data.remote.source.KudosDataSource
import com.example.saa.data.remote.source.ProfileDataSource
import com.example.saa.domain.model.Kudos
import com.example.saa.domain.model.KudosFilter
import com.example.saa.domain.model.WriteKudoRequest
import com.example.saa.domain.repository.KudosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class KudosRepositoryImpl @Inject constructor(
    private val dataSource: KudosDataSource,
    private val profileDataSource: ProfileDataSource,
) : KudosRepository {

    override suspend fun getHighlightKudos(
        hashtagId: String?,
        departmentId: String?,
    ): Result<List<Kudos>> = runCatching {
        dataSource.getHighlightKudos(hashtagId, departmentId).map { it.toDomain() }
    }.onFailure { Timber.e(it, "getHighlightKudos failed") }

    override suspend fun getAllKudos(
        page: Int,
        limit: Int,
        hashtagId: String?,
        departmentId: String?,
    ): Result<List<Kudos>> = runCatching {
        dataSource.getAllKudos(page, limit, hashtagId, departmentId).map { it.toDomain() }
    }.onFailure { Timber.e(it, "getAllKudos failed") }

    override suspend fun toggleLike(kudosId: String, isCurrentlyLiked: Boolean): Result<Unit> = runCatching {
        dataSource.toggleLike(kudosId, isCurrentlyLiked)
    }.onFailure { Timber.e(it, "toggleLike failed") }

    override fun observeNewKudos(): Flow<Kudos> =
        dataSource.observeNewKudos().map { it.toDomain() }

    override suspend fun getProfileKudos(filter: KudosFilter): Result<List<Kudos>> = runCatching {
        dataSource.getProfileKudos(filter).map { it.toDomain() }
    }.onFailure { Timber.e(it, "getProfileKudos failed") }

    override suspend fun submitKudo(request: WriteKudoRequest): Result<String> = runCatching {
        val profile = profileDataSource.getMyProfile() ?: error("Not authenticated")
        val senderName = if (request.isAnonymous) null else profile.fullName
        val senderAvatarUrl = if (request.isAnonymous) null else profile.avatarUrl
        val senderEmployeeCode = if (request.isAnonymous) null else profile.employeeCode
        val senderBadgeType = if (request.isAnonymous) null else profile.badgeType
        val anonymousNickname = if (request.isAnonymous) "Ẩn danh" else ""

        val recipientProfile = profileDataSource.getProfileById(request.recipientId)

        val dto = WriteKudoDto(
            senderId = null,
            recipientId = request.recipientId,
            message = request.message,
            awardCategoryName = request.title,
            photoUrls = request.photoUrls,
            isAnonymous = request.isAnonymous,
            anonymousNickname = anonymousNickname,
            senderName = senderName,
            senderAvatarUrl = senderAvatarUrl,
            senderEmployeeCode = senderEmployeeCode,
            senderBadgeType = senderBadgeType,
            recipientName = recipientProfile?.fullName,
            recipientAvatarUrl = recipientProfile?.avatarUrl,
            recipientHeroTier = recipientProfile?.heroTier ?: 0,
        )

        val kudosId = dataSource.insertKudo(dto)
        dataSource.insertKudosHashtags(kudosId, request.hashtagIds)
        kudosId
    }.onFailure { Timber.e(it, "submitKudo failed") }

    suspend fun subscribe() = dataSource.subscribe()

    suspend fun unsubscribe() = dataSource.unsubscribe()
}
