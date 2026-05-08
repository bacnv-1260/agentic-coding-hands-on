package com.example.saa.domain.usecase

import com.example.saa.domain.model.Hashtag
import com.example.saa.domain.repository.UserStatsRepository
import javax.inject.Inject

class GetHashtagsUseCase @Inject constructor(
    private val repository: UserStatsRepository,
) {
    suspend operator fun invoke(): Result<List<Hashtag>> = repository.getHashtags()
}
