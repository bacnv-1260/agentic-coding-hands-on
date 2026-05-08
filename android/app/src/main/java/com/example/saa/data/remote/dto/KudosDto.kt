package com.example.saa.data.remote.dto

import com.example.saa.domain.model.Kudos
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KudosDto(
    @SerialName("id") val id: String,
    @SerialName("sender_id") val senderId: String?,
    @SerialName("recipient_id") val recipientId: String?,
    @SerialName("message") val message: String?,
    @SerialName("award_category_name") val awardCategoryName: String?,
    @SerialName("heart_count") val heartCount: Int?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("hashtags") val hashtags: List<HashtagDto>? = null,
    @SerialName("sender_avatar_url") val senderAvatarUrl: String?,
    @SerialName("sender_name") val senderName: String?,
    @SerialName("sender_employee_code") val senderEmployeeCode: String?,
    @SerialName("sender_badge_type") val senderBadgeType: String?,
    @SerialName("sender_department_name") val senderDepartmentName: String? = null,
    @SerialName("recipient_avatar_url") val recipientAvatarUrl: String?,
    @SerialName("recipient_name") val recipientName: String?,
    @SerialName("recipient_hero_tier") val recipientHeroTier: Int?,
    @SerialName("recipient_department_name") val recipientDepartmentName: String? = null,
    @SerialName("share_url") val shareUrl: String?,
    @SerialName("is_liked") val isLiked: Boolean?,
    @SerialName("photo_urls") val photoUrls: List<String>?,
    @SerialName("is_anonymous") val isAnonymous: Boolean?,
    @SerialName("anonymous_nickname") val anonymousNickname: String?,
    @SerialName("can_like") val canLike: Boolean?,
)

fun KudosDto.toDomain(): Kudos = Kudos(
    id = id,
    senderId = senderId.orEmpty(),
    recipientId = recipientId.orEmpty(),
    message = message.orEmpty(),
    awardCategoryName = awardCategoryName.orEmpty(),
    heartCount = heartCount ?: 0,
    createdAt = createdAt.orEmpty(),
    hashtags = hashtags?.mapNotNull { it.name } ?: emptyList(),
    senderAvatarUrl = senderAvatarUrl,
    senderName = senderName.orEmpty(),
    senderEmployeeCode = senderEmployeeCode.orEmpty(),
    senderBadgeType = senderBadgeType.orEmpty(),
    senderDepartmentName = senderDepartmentName.orEmpty(),
    recipientAvatarUrl = recipientAvatarUrl,
    recipientName = recipientName.orEmpty(),
    recipientHeroTier = recipientHeroTier ?: 0,
    recipientDepartmentName = recipientDepartmentName.orEmpty(),
    shareUrl = shareUrl.orEmpty(),
    isLiked = isLiked ?: false,
    photoUrls = photoUrls ?: emptyList(),
    isAnonymous = isAnonymous ?: false,
    anonymousNickname = anonymousNickname.orEmpty(),
    canLike = canLike ?: true,
)
