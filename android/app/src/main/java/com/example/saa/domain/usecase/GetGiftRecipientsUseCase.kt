package com.example.saa.domain.usecase

import com.example.saa.domain.model.GiftRecipient
import com.example.saa.domain.repository.UserStatsRepository
import javax.inject.Inject

class GetGiftRecipientsUseCase @Inject constructor(
    private val repository: UserStatsRepository,
) {
    suspend operator fun invoke(): Result<List<GiftRecipient>> = repository.getGiftRecipients()
}
