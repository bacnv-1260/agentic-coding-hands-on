package com.example.saa.data.remote.dto

import com.example.saa.domain.model.UserStats
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserStatsDto(
    @SerialName("id") val id: String,
    @SerialName("kudos_received") val kudosReceived: Int?,
    @SerialName("kudos_sent") val kudosSent: Int?,
    @SerialName("hearts_received") val heartsReceived: Int?,
    @SerialName("secret_boxes_opened") val secretBoxesOpened: Int?,
    @SerialName("secret_boxes_unopened") val secretBoxesUnopened: Int?,
)

fun UserStatsDto.toDomain(): UserStats = UserStats(
    kudosReceived = kudosReceived ?: 0,
    kudosSent = kudosSent ?: 0,
    heartsReceived = heartsReceived ?: 0,
    secretBoxesOpened = secretBoxesOpened ?: 0,
    secretBoxesUnopened = secretBoxesUnopened ?: 0,
)
