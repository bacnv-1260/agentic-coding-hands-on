package com.example.saa.domain.model

data class Profile(
    val id: String,
    val fullName: String,
    val employeeCode: String,
    val avatarUrl: String?,
    val badgeType: String,
    val heroTier: Int,
)
