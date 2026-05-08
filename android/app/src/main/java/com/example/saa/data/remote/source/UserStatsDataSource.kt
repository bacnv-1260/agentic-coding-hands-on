package com.example.saa.data.remote.source

import com.example.saa.data.remote.dto.DepartmentDto
import com.example.saa.data.remote.dto.GiftRecipientDto
import com.example.saa.data.remote.dto.HashtagDto
import com.example.saa.data.remote.dto.UserStatsDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import javax.inject.Inject

open class UserStatsDataSource @Inject constructor(
    private val supabase: SupabaseClient,
) {
    open suspend fun getUserStats(): UserStatsDto {
        val rows = supabase.from("user_stats").select {
            limit(1)
        }.decodeList<UserStatsDto>()
        return rows.firstOrNull() ?: UserStatsDto(
            id = "",
            kudosReceived = null,
            kudosSent = null,
            heartsReceived = null,
            secretBoxesOpened = null,
            secretBoxesUnopened = null,
        )
    }

    suspend fun getGiftRecipients(): List<GiftRecipientDto> =
        supabase.from("gift_recipients").select {
            order("created_at", Order.DESCENDING)
            limit(10)
        }.decodeList()

    suspend fun getHashtags(): List<HashtagDto> =
        supabase.from("hashtags").select().decodeList()

    suspend fun getDepartments(): List<DepartmentDto> =
        supabase.from("departments").select().decodeList()

    suspend fun getNextSecretBox(): String =
        supabase.from("secret_boxes").select {
            filter {
                eq("opened", false)
            }
            order("created_at", Order.ASCENDING)
            limit(1)
        }.decodeSingle<Map<String, String>>()["id"]
            ?: error("No unopened secret box found")

    suspend fun openSecretBox(boxId: String) {
        supabase.from("secret_boxes").update(
            mapOf("opened" to true),
        ) {
            filter { eq("id", boxId) }
        }
    }
}
