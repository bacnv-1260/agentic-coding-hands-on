package com.example.saa.domain.model

data class WriteKudoRequest(
    val recipientId: String,
    val title: String,
    val message: String,
    val hashtagIds: List<String>,
    val photoUrls: List<String>,
    val isAnonymous: Boolean,
)
