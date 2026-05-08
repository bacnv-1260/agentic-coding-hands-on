package com.example.saa.domain.usecase

import com.example.saa.domain.model.Kudos
import com.example.saa.domain.repository.KudosRepository
import javax.inject.Inject

class GetAllKudosUseCase @Inject constructor(
    private val repository: KudosRepository,
) {
    suspend operator fun invoke(
        page: Int,
        limit: Int,
        hashtagId: String?,
        departmentId: String?,
    ): Result<List<Kudos>> = repository.getAllKudos(page, limit, hashtagId, departmentId)
}
