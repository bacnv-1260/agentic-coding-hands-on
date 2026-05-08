package com.example.saa.data.remote.dto

import com.example.saa.domain.model.User

data class UserDto(
    val id: String,
    val email: String?,
    val name: String?,
)

fun UserDto.toDomain(): User = User(
    id = id,
    email = email ?: "",
    name = name ?: "",
)
