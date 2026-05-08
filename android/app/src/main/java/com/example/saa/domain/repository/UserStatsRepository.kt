package com.example.saa.domain.repository

import com.example.saa.domain.model.Department
import com.example.saa.domain.model.GiftRecipient
import com.example.saa.domain.model.Hashtag
import com.example.saa.domain.model.UserStats

interface UserStatsRepository {
    suspend fun getUserStats(): Result<UserStats>
    suspend fun getGiftRecipients(): Result<List<GiftRecipient>>
    suspend fun getHashtags(): Result<List<Hashtag>>
    suspend fun getDepartments(): Result<List<Department>>
    suspend fun getNextSecretBox(): Result<String>
    suspend fun openSecretBox(boxId: String): Result<Unit>
}
