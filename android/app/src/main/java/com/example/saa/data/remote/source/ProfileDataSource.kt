package com.example.saa.data.remote.source

import com.example.saa.data.remote.dto.ProfileDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import javax.inject.Inject

open class ProfileDataSource @Inject constructor(
    private val supabase: SupabaseClient,
) {
    open suspend fun getMyProfile(): ProfileDto? {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return null
        return supabase.from("profiles").select {
            filter { eq("id", userId) }
        }.decodeSingleOrNull<ProfileDto>()
    }

    open suspend fun getProfileById(id: String): ProfileDto? {
        return supabase.from("profiles").select {
            filter { eq("id", id) }
        }.decodeSingleOrNull<ProfileDto>()
    }

    open suspend fun searchProfiles(query: String): List<ProfileDto> {
        val userId = supabase.auth.currentUserOrNull()?.id
        return supabase.from("profiles").select {
            filter {
                if (query.isNotBlank()) {
                    ilike("full_name", "%$query%")
                }
                if (userId != null) {
                    neq("id", userId)
                }
            }
            limit(20L)
        }.decodeList()
    }
}
