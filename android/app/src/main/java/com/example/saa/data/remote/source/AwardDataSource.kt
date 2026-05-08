package com.example.saa.data.remote.source

import com.example.saa.data.remote.dto.AwardDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class AwardDataSource @Inject constructor(
    private val supabase: SupabaseClient,
) {
    suspend fun getAwards(): List<AwardDto> =
        supabase.from("awards").select().decodeList()

    suspend fun getAwardById(id: String): AwardDto =
        supabase.from("awards").select {
            filter { eq("id", id) }
        }.decodeSingle()
}
