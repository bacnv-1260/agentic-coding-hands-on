package com.example.saa.data.remote.dto

import com.example.saa.domain.model.GiftRecipient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GiftRecipientDto(
    @SerialName("id") val id: String,
    @SerialName("full_name") val fullName: String?,
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("reward_name") val rewardName: String?,
)

fun GiftRecipientDto.toDomain(): GiftRecipient = GiftRecipient(
    id = id,
    fullName = fullName.orEmpty(),
    avatarUrl = avatarUrl,
    rewardName = rewardName.orEmpty(),
)
