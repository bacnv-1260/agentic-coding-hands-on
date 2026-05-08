package com.example.saa.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AwardDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String?,
    @SerialName("description") val description: String?,
    @SerialName("category") val category: String?,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("quantity") val quantity: Int?,
    @SerialName("quantity_unit") val quantityUnit: String?,
    @SerialName("prize_value") val prizeValue: String?,
)
