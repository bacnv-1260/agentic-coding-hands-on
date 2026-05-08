package com.example.saa.data.repository

import com.example.saa.data.remote.source.NotificationDataSource
import com.example.saa.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val dataSource: NotificationDataSource,
) : NotificationRepository {

    override suspend fun getUnreadCount(): Result<Int> = runCatching {
        dataSource.getUnreadCount()
    }
}
