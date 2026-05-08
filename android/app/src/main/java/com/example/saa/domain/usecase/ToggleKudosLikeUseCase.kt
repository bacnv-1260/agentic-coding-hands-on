package com.example.saa.domain.usecase

import com.example.saa.domain.repository.KudosRepository
import javax.inject.Inject

class ToggleKudosLikeUseCase @Inject constructor(
    private val repository: KudosRepository,
) {
    suspend operator fun invoke(kudosId: String, isCurrentlyLiked: Boolean): Result<Unit> =
        repository.toggleLike(kudosId, isCurrentlyLiked)
}
