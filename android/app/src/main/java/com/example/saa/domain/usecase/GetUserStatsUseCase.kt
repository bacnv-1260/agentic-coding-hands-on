package com.example.saa.domain.usecase

import com.example.saa.domain.model.UserStats
import com.example.saa.domain.repository.UserStatsRepository
import javax.inject.Inject

class GetUserStatsUseCase @Inject constructor(
    private val repository: UserStatsRepository,
) {
    suspend operator fun invoke(): Result<UserStats> = repository.getUserStats()
}
