package com.example.saa.domain.usecase

import com.example.saa.domain.model.Award
import com.example.saa.domain.repository.AwardRepository
import javax.inject.Inject

class GetAwardByIdUseCase @Inject constructor(
    private val repository: AwardRepository,
) {
    suspend operator fun invoke(id: String): Result<Award> = repository.getAwardById(id)
}
