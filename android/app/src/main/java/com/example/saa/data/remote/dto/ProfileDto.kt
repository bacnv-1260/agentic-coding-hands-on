package com.example.saa.data.remote.dto

import com.example.saa.domain.model.Profile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("id") val id: String,
    @SerialName("full_name") val fullName: String?,
    @SerialName("employee_code") val employeeCode: String?,
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("badge_type") val badgeType: String?,
    @SerialName("hero_tier") val heroTier: Int?,
    @SerialName("department_id") val departmentId: String?,
)

fun ProfileDto.toDomain(): Profile = Profile(
    id = id,
    fullName = fullName.orEmpty(),
    employeeCode = employeeCode.orEmpty(),
    avatarUrl = avatarUrl,
    badgeType = badgeType.orEmpty(),
    heroTier = heroTier ?: 0,
)
