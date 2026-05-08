package com.example.saa.data.remote.dto

import com.example.saa.domain.model.Department
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DepartmentDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String?,
)

fun DepartmentDto.toDomain(): Department = Department(
    id = id,
    name = name.orEmpty(),
)
