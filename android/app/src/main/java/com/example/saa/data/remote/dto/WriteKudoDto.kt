package com.example.saa.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WriteKudoDto(
    @SerialName("id")                   val id: String? = null,
    @SerialName("sender_id")            val senderId: String?,
    @SerialName("recipient_id")         val recipientId: String?,
    @SerialName("message")              val message: String?,
    @SerialName("award_category_name")  val awardCategoryName: String?,
    @SerialName("photo_urls")           val photoUrls: List<String>?,
    @SerialName("is_anonymous")         val isAnonymous: Boolean?,
    @SerialName("anonymous_nickname")   val anonymousNickname: String?,
    @SerialName("sender_name")          val senderName: String?,
    @SerialName("sender_avatar_url")    val senderAvatarUrl: String?,
    @SerialName("sender_employee_code") val senderEmployeeCode: String?,
    @SerialName("sender_badge_type")    val senderBadgeType: String?,
    @SerialName("recipient_name")       val recipientName: String?,
    @SerialName("recipient_avatar_url") val recipientAvatarUrl: String?,
    @SerialName("recipient_hero_tier")  val recipientHeroTier: Int?,
)
