package com.example.saa.domain.repository

import com.example.saa.domain.model.Kudos
import com.example.saa.domain.model.KudosFilter
import com.example.saa.domain.model.WriteKudoRequest
import kotlinx.coroutines.flow.Flow

interface KudosRepository {
    suspend fun getHighlightKudos(hashtagId: String?, departmentId: String?): Result<List<Kudos>>
    suspend fun getAllKudos(page: Int, limit: Int, hashtagId: String?, departmentId: String?): Result<List<Kudos>>
    suspend fun toggleLike(kudosId: String, isCurrentlyLiked: Boolean): Result<Unit>
    fun observeNewKudos(): Flow<Kudos>
    suspend fun getProfileKudos(filter: KudosFilter): Result<List<Kudos>>
    suspend fun submitKudo(request: WriteKudoRequest): Result<String>
}
