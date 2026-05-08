package com.example.saa.domain.usecase

import com.example.saa.domain.model.Kudos
import com.example.saa.domain.model.KudosFilter
import com.example.saa.domain.repository.KudosRepository
import javax.inject.Inject

class GetProfileKudosUseCase @Inject constructor(
    private val repository: KudosRepository,
) {
    suspend operator fun invoke(filter: KudosFilter): Result<List<Kudos>> =
        repository.getProfileKudos(filter)
}
