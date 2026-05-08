package com.example.saa.data.remote.dto

import com.example.saa.domain.model.Hashtag
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HashtagDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String?,
)

fun HashtagDto.toDomain(): Hashtag = Hashtag(
    id = id,
    name = name.orEmpty(),
)
