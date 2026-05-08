package com.example.saa.data.remote.source

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class NotificationDataSource @Inject constructor(
    private val supabase: SupabaseClient,
) {
    suspend fun getUnreadCount(): Int =
        supabase.from("notifications")
            .select {
                filter { eq("is_read", false) }
            }
            .decodeList<Map<String, String>>()
            .size
}
