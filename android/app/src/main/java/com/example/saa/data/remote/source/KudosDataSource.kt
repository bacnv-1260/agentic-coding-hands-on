package com.example.saa.data.remote.source

import com.example.saa.data.remote.dto.KudosDto
import com.example.saa.data.remote.dto.WriteKudoDto
import com.example.saa.domain.model.KudosFilter
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject

@Serializable
private data class KudosIdDto(@SerialName("kudos_id") val kudosId: String)

@Serializable
private data class SenderIdDto(@SerialName("id") val id: String)

@Serializable
private data class KudosHashtagInsert(
    @SerialName("kudos_id") val kudosId: String,
    @SerialName("hashtag_id") val hashtagId: String,
)

open class KudosDataSource @Inject constructor(
    private val supabase: SupabaseClient,
) {
    private var realtimeChannel: RealtimeChannel? = null

    private suspend fun getKudosIdsByHashtag(hashtagId: String): List<String> =
        supabase.from("kudos_hashtags").select(columns = Columns.list("kudos_id")) {
            filter { eq("hashtag_id", hashtagId) }
        }.decodeList<KudosIdDto>().map { it.kudosId }

    /** Returns sender_ids whose profile belongs to the given department. */
    private suspend fun getSenderIdsByDepartment(departmentId: String): List<String> =
        supabase.from("profiles").select(columns = Columns.list("id")) {
            filter { eq("department_id", departmentId) }
        }.decodeList<SenderIdDto>().map { it.id }

    suspend fun getHighlightKudos(hashtagId: String?, departmentId: String?): List<KudosDto> {
        val kudosIds = if (hashtagId != null) getKudosIdsByHashtag(hashtagId) else null
        if (kudosIds != null && kudosIds.isEmpty()) return emptyList()
        val senderIds = if (departmentId != null) getSenderIdsByDepartment(departmentId) else null
        if (senderIds != null && senderIds.isEmpty()) return emptyList()
        return supabase.from("kudos_view").select {
            filter {
                if (kudosIds != null) isIn("id", kudosIds)
                if (senderIds != null) isIn("sender_id", senderIds)
            }
            order("heart_count", Order.DESCENDING)
            limit(5)
        }.decodeList()
    }

    suspend fun getAllKudos(
        page: Int,
        limit: Int,
        hashtagId: String?,
        departmentId: String?,
    ): List<KudosDto> {
        val kudosIds = if (hashtagId != null) getKudosIdsByHashtag(hashtagId) else null
        if (kudosIds != null && kudosIds.isEmpty()) return emptyList()
        val senderIds = if (departmentId != null) getSenderIdsByDepartment(departmentId) else null
        if (senderIds != null && senderIds.isEmpty()) return emptyList()
        val offset = (page - 1) * limit
        return supabase.from("kudos_view").select {
            filter {
                if (kudosIds != null) isIn("id", kudosIds)
                if (senderIds != null) isIn("sender_id", senderIds)
            }
            order("created_at", Order.DESCENDING)
            limit(limit.toLong())
            range(offset.toLong(), (offset + limit - 1).toLong())
        }.decodeList()
    }

    suspend fun toggleLike(kudosId: String, isCurrentlyLiked: Boolean) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return
        runCatching {
            if (isCurrentlyLiked) {
                supabase.from("kudos_likes").delete {
                    filter {
                        eq("kudos_id", kudosId)
                        eq("user_id", userId)
                    }
                }
            } else {
                supabase.from("kudos_likes").insert(
                    mapOf("kudos_id" to kudosId, "user_id" to userId),
                )
            }
        }.onFailure { e ->
            if (e is PostgrestRestException) {
                when {
                    e.message?.contains("kudos_likes_user_id_fkey") == true -> {
                        // Stale session: user no longer exists in auth.users after db reset
                        supabase.auth.signOut()
                        throw e
                    }
                    // PostgreSQL 23505 unique_violation: row already exists (UI/DB state desync).
                    // Like is already recorded — treat as success, do not rethrow.
                    !isCurrentlyLiked && e.code == "23505" -> { /* swallow */ }
                    else -> throw e
                }
            } else {
                throw e
            }
        }
    }

    suspend fun getProfileKudos(kudosFilter: KudosFilter): List<KudosDto> {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return emptyList()
        return supabase.from("kudos_view").select {
            filter {
                when (kudosFilter) {
                    KudosFilter.SENT -> eq("sender_id", userId)
                    KudosFilter.RECEIVED -> eq("recipient_id", userId)
                }
            }
            order("created_at", Order.DESCENDING)
            limit(20L)
        }.decodeList()
    }

    fun observeNewKudos(): Flow<KudosDto> {
        val channel = supabase.channel("kudos_feed")
        realtimeChannel = channel
        return channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
            table = "kudos"
        }.mapNotNull { action ->
            runCatching {
                Json.decodeFromJsonElement(KudosDto.serializer(), action.record)
            }.getOrNull()
        }
    }

    suspend fun subscribe() {
        realtimeChannel?.subscribe()
    }

    suspend fun unsubscribe() {
        realtimeChannel?.let { channel ->
            supabase.realtime.removeChannel(channel)
        }
        realtimeChannel = null
    }

    open suspend fun insertKudo(dto: WriteKudoDto): String {
        val userId = supabase.auth.currentUserOrNull()?.id ?: error("Not authenticated")
        val dtoWithSender = dto.copy(senderId = userId)
        return supabase.from("kudos")
            .insert(dtoWithSender) { select() }
            .decodeSingle<WriteKudoDto>()
            .id ?: error("Insert returned no id")
    }

    open suspend fun insertKudosHashtags(kudosId: String, hashtagIds: List<String>) {
        if (hashtagIds.isEmpty()) return
        val rows = hashtagIds.map { KudosHashtagInsert(kudosId = kudosId, hashtagId = it) }
        supabase.from("kudos_hashtags").insert(rows)
    }
}
