package com.example.saa.domain.model

data class Award(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val imageUrl: String?,
    val quantity: Int?,
    val quantityUnit: String?,
    val prizeValue: String?,
)
