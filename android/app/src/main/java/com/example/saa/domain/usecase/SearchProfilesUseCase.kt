package com.example.saa.domain.usecase

import com.example.saa.domain.model.Profile
import com.example.saa.domain.repository.ProfileRepository
import javax.inject.Inject

class SearchProfilesUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(query: String): Result<List<Profile>> =
        profileRepository.searchProfiles(query)
}
