package com.example.saa.domain.repository

interface NotificationRepository {
    suspend fun getUnreadCount(): Result<Int>
}
