package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.domain.model.Kudos
import com.example.saa.ui.theme.KudosCardBorder
import com.example.saa.ui.theme.KudosCardBg
import com.example.saa.ui.theme.KudosGold
import com.example.saa.ui.theme.TextOnDark

@Composable
fun KudosPostCard(
    kudos: Kudos,
    onSenderClick: (String) -> Unit,
    onRecipientClick: (String) -> Unit,
    onLikeToggle: (String) -> Unit,
    onCopyLink: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, KudosCardBorder, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = KudosCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Sender → Recipient row (C.3.1 - C.3.3)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (kudos.isAnonymous) {
                    // Anonymous: hide sender info, show anonymous nickname
                    Text(
                        text = kudos.anonymousNickname.ifEmpty { "Ẩn danh" },
                        color = TextOnDark.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                    )
                } else {
                    UserAvatar(
                        avatarUrl = kudos.senderAvatarUrl,
                        size = 32,
                        onClick = { onSenderClick(kudos.senderId) },
                        contentDesc = "Avatar của ${kudos.senderName}",
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = kudos.senderName,
                        color = TextOnDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSenderClick(kudos.senderId) },
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = KudosGold,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(16.dp),
                )
                Text(
                    text = kudos.recipientName,
                    color = TextOnDark,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onRecipientClick(kudos.recipientId) },
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column(horizontalAlignment = Alignment.End) {
                    UserAvatar(
                        avatarUrl = kudos.recipientAvatarUrl,
                        size = 32,
                        onClick = { onRecipientClick(kudos.recipientId) },
                        contentDesc = "Avatar của ${kudos.recipientName}",
                    )
                    StarBadge(heroTier = kudos.recipientHeroTier)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Timestamp (C.3.4)
            Text(
                text = kudos.createdAt,
                color = TextOnDark.copy(alpha = 0.5f),
                fontSize = 11.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Category + Message content card (C.3.5)
            if (kudos.awardCategoryName.isNotEmpty()) {
                Text(
                    text = kudos.awardCategoryName.uppercase(),
                    color = KudosGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = kudos.message,
                color = TextOnDark.copy(alpha = 0.85f),
                fontSize = 14.sp,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
            )

            // Hashtags — max 1 line
            if (kudos.hashtags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    kudos.hashtags.take(5).forEach { tag ->
                        Text(
                            text = "#$tag",
                            color = KudosGold.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            maxLines = 1,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Actions: heart + copy link
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(enabled = kudos.canLike) { onLikeToggle(kudos.id) }
                        .semantics { contentDescription = if (kudos.isLiked) "Bỏ thích" else "Thích" },
                ) {
                    Icon(
                        imageVector = if (kudos.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (kudos.canLike) Color(0xFFE84D4D) else TextOnDark.copy(alpha = 0.3f),
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = kudos.heartCount.toString(),
                        color = if (kudos.canLike) TextOnDark else TextOnDark.copy(alpha = 0.3f),
                        fontSize = 13.sp,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onCopyLink(kudos.shareUrl) },
                    modifier = Modifier
                        .size(32.dp)
                        .semantics { contentDescription = "Copy Link" },
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        tint = TextOnDark.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KudosPostCardPreview() {
    val sampleKudos = Kudos(
        id = "1",
        senderId = "u1",
        recipientId = "u2",
        message = "Cảm ơn bạn đã hỗ trợ team rất nhiệt tình!",
        awardCategoryName = "TEAMWORK HERO",
        heartCount = 5,
        createdAt = "10:00 - 30/10/2025",
        hashtags = listOf("teamwork", "kudos"),
        senderAvatarUrl = null,
        senderName = "Nguyễn Văn A",
        senderEmployeeCode = "CECV10",
        senderBadgeType = "",
        senderDepartmentName = "",
        recipientAvatarUrl = null,
        recipientName = "Trần Thị B",
        recipientHeroTier = 10,
        recipientDepartmentName = "",
        shareUrl = "",
        isLiked = true,
        photoUrls = emptyList(),
    )
    KudosPostCard(
        kudos = sampleKudos,
        onSenderClick = {},
        onRecipientClick = {},
        onLikeToggle = {},
        onCopyLink = {},
    )
}
