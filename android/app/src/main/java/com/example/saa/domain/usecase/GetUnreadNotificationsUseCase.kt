package com.example.saa.domain.usecase

import com.example.saa.domain.repository.NotificationRepository
import javax.inject.Inject

class GetUnreadNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(): Result<Int> = repository.getUnreadCount()
}
