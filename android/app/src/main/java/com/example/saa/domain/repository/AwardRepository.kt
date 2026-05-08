package com.example.saa.domain.repository

import com.example.saa.domain.model.Award

interface AwardRepository {
    suspend fun getAwards(): Result<List<Award>>
    suspend fun getAwardById(id: String): Result<Award>
}
