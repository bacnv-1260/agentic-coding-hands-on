package com.example.saa.domain.usecase

import com.example.saa.domain.model.Profile
import com.example.saa.domain.repository.ProfileRepository
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(): Result<Profile> = repository.getMyProfile()
}
