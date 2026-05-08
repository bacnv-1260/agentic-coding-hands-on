package com.example.saa.domain.usecase

import com.example.saa.domain.model.Award
import com.example.saa.domain.repository.AwardRepository
import javax.inject.Inject

class GetAwardsUseCase @Inject constructor(
    private val repository: AwardRepository,
) {
    suspend operator fun invoke(): Result<List<Award>> = repository.getAwards()
}
