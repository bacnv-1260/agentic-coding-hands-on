package com.example.saa.domain.usecase

import com.example.saa.domain.model.Department
import com.example.saa.domain.repository.UserStatsRepository
import javax.inject.Inject

class GetDepartmentsUseCase @Inject constructor(
    private val repository: UserStatsRepository,
) {
    suspend operator fun invoke(): Result<List<Department>> = repository.getDepartments()
}
