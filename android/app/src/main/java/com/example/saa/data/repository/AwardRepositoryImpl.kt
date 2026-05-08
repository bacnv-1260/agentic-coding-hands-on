package com.example.saa.data.repository

import com.example.saa.data.remote.dto.AwardDto
import com.example.saa.data.remote.source.AwardDataSource
import com.example.saa.domain.model.Award
import com.example.saa.domain.repository.AwardRepository
import javax.inject.Inject

class AwardRepositoryImpl @Inject constructor(
    private val dataSource: AwardDataSource,
) : AwardRepository {

    override suspend fun getAwards(): Result<List<Award>> = runCatching {
        dataSource.getAwards().map { dto -> dto.toDomain() }
    }

    override suspend fun getAwardById(id: String): Result<Award> = runCatching {
        dataSource.getAwardById(id).toDomain()
    }

    private fun AwardDto.toDomain() = Award(
        id = id,
        name = name.orEmpty(),
        description = description.orEmpty(),
        category = category.orEmpty(),
        imageUrl = imageUrl,
        quantity = quantity,
        quantityUnit = quantityUnit,
        prizeValue = prizeValue,
    )
}
