package com.example.saa.domain.usecase

import com.example.saa.domain.model.Kudos
import com.example.saa.domain.repository.KudosRepository
import javax.inject.Inject

class GetHighlightKudosUseCase @Inject constructor(
    private val repository: KudosRepository,
) {
    suspend operator fun invoke(hashtagId: String?, departmentId: String?): Result<List<Kudos>> =
        repository.getHighlightKudos(hashtagId, departmentId)
}
