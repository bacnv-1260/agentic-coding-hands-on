package com.example.saa.domain.model

data class UserStats(
    val kudosReceived: Int = 0,
    val kudosSent: Int = 0,
    val heartsReceived: Int = 0,
    val secretBoxesOpened: Int = 0,
    val secretBoxesUnopened: Int = 0,
)
