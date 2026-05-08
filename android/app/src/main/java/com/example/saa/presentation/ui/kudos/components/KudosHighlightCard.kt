package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.saa.domain.model.Kudos
import com.example.saa.ui.theme.KudosGold
import java.text.SimpleDateFormat
import java.util.Locale

// B.3 Highlight Card design tokens
private val HighlightCardBg = Color(0xFFFFF8E1)           // light cream
private val HighlightCardBorder = Color(0xFFFFEA9E)       // gold border
private val HighlightCardText = Color(0xFF00101A)         // dark navy
private val HighlightCardTextSecondary = Color(0xFF999999) // gray
private val HighlightHashtagColor = Color(0xFFD4271D)     // red
private val HighlightMessageBg = Color(0x66FFEA9E)        // rgba(255,234,158,0.40)

@Composable
fun KudosHighlightCard(
    kudos: Kudos,
    onSenderClick: (String) -> Unit,
    onRecipientClick: (String) -> Unit,
    onLikeToggle: (String) -> Unit,
    onCopyLink: (String) -> Unit,
    onViewDetail: (String) -> Unit,
    onHashtagClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = HighlightCardBorder,
                shape = RoundedCornerShape(8.dp),
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = HighlightCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            // Sender → Recipient row (62dp height, avatar + name + badge stacked per side)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
            ) {
                UserInfoColumn(
                    avatarUrl = kudos.senderAvatarUrl,
                    name = kudos.senderName,
                    departmentName = kudos.senderDepartmentName,
                    employeeCode = kudos.senderEmployeeCode,
                    badgeType = kudos.senderBadgeType,
                    heroTier = null,
                    onClick = { onSenderClick(kudos.senderId) },
                    contentDesc = "Avatar của ${kudos.senderName}",
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = HighlightCardText.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp),
                )
                UserInfoColumn(
                    avatarUrl = kudos.recipientAvatarUrl,
                    name = kudos.recipientName,
                    departmentName = kudos.recipientDepartmentName,
                    employeeCode = "",
                    badgeType = "",
                    heroTier = kudos.recipientHeroTier,
                    onClick = { onRecipientClick(kudos.recipientId) },
                    contentDesc = "Avatar của ${kudos.recipientName}",
                    modifier = Modifier.weight(1f),
                )
            }

            // Content section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Timestamp: 10sp, Medium, gray
                Text(
                    text = formatKudosTime(kudos.createdAt),
                    color = HighlightCardTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                )
                // Award category: 10sp, Bold, dark navy, centered
                if (kudos.awardCategoryName.isNotEmpty()) {
                    Text(
                        text = kudos.awardCategoryName.uppercase(),
                        color = HighlightCardText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                // Message box: gold-bordered, gold-tinted background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, HighlightCardBorder, RoundedCornerShape(6.dp))
                        .background(HighlightMessageBg, RoundedCornerShape(6.dp))
                        .padding(4.dp),
                ) {
                    Text(
                        text = kudos.message,
                        color = HighlightCardText,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        textAlign = TextAlign.Justify,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                // Hashtags: 10sp, red
                if (kudos.hashtags.isNotEmpty()) {
                    Text(
                        text = kudos.hashtags.take(5).joinToString(" ") { "#$it" },
                        color = HighlightHashtagColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { onHashtagClick(kudos.hashtags.first()) },
                    )
                }
            }

            // Action row: count+heart on left, text-icon buttons on right
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
            ) {
                // Hearts: count text first, then heart icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(enabled = kudos.canLike) {
                        onLikeToggle(kudos.id)
                    }.semantics {
                        contentDescription = if (kudos.isLiked) "Bỏ thích" else "Thích"
                    },
                ) {
                    Text(
                        text = formatHeartCount(kudos.heartCount),
                        color = if (kudos.canLike) HighlightCardText else HighlightCardText.copy(alpha = 0.3f),
                        fontSize = 10.sp,
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = if (kudos.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (kudos.canLike) Color(0xFFE84D4D) else HighlightCardText.copy(alpha = 0.3f),
                        modifier = Modifier.size(16.dp),
                    )
                }
                // Right: compact text+icon buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextIconButton(
                        label = "Copy Link",
                        icon = Icons.Default.ContentCopy,
                        onClick = { onCopyLink(kudos.shareUrl) },
                    )
                    TextIconButton(
                        label = "Xem chi tiết",
                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                        onClick = { onViewDetail(kudos.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun UserInfoColumn(
    avatarUrl: String?,
    name: String,
    departmentName: String,
    employeeCode: String,
    badgeType: String,
    heroTier: Int?,
    onClick: () -> Unit,
    contentDesc: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxHeight(),
    ) {
        UserAvatar(
            avatarUrl = avatarUrl,
            size = 24,
            onClick = onClick,
            contentDesc = contentDesc,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            color = HighlightCardText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (departmentName.isNotEmpty()) {
            Text(
                text = departmentName,
                color = HighlightCardTextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (employeeCode.isNotEmpty()) {
                Text(
                    text = employeeCode,
                    color = HighlightCardTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                )
                Box(
                    modifier = Modifier
                        .size(2.dp)
                        .background(HighlightCardTextSecondary.copy(alpha = 0.4f), CircleShape),
                )
            }
            if (badgeType.isNotEmpty()) BadgeChip(label = badgeType)
            if (heroTier != null) StarBadge(heroTier = heroTier)
        }
    }
}

@Composable
private fun TextIconButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .height(24.dp)
            .clip(RoundedCornerShape(2.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp),
    ) {
        Text(
            text = label,
            color = HighlightCardText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = HighlightCardText,
            modifier = Modifier.size(16.dp),
        )
    }
}

private fun formatHeartCount(count: Int): String = when {
    count >= 1000 -> "${count / 1000}.${(count % 1000) / 100}"
    else -> count.toString()
}

private fun formatKudosTime(iso: String): String {
    if (iso.isEmpty()) return ""
    if (iso.contains(" - ")) return iso // already formatted
    return try {
        val cleaned = iso.replaceFirst(Regex("\\.\\d+"), "")
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)
        val date = parser.parse(cleaned) ?: return iso
        SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.US).format(date)
    } catch (e: Exception) {
        iso
    }
}

@Composable
internal fun UserAvatar(
    avatarUrl: String?,
    size: Int,
    onClick: () -> Unit,
    contentDesc: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .border(1.dp, Color.White, CircleShape)
            .background(Color(0xFF2E3940))
            .clickable(onClick = onClick)
            .semantics { contentDescription = contentDesc },
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )
    }
}

@Composable
internal fun BadgeChip(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(KudosGold.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
    ) {
        Text(text = label, color = KudosGold, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
internal fun StarBadge(heroTier: Int, modifier: Modifier = Modifier) {
    val stars = when {
        heroTier >= 50 -> "★★★"
        heroTier >= 20 -> "★★"
        heroTier >= 10 -> "★"
        else -> ""
    }
    if (stars.isNotEmpty()) {
        Text(text = stars, color = KudosGold, fontSize = 12.sp, modifier = modifier)
    }
}

@Preview(showBackground = true)
@Composable
private fun KudosHighlightCardPreview() {
    val sampleKudos = Kudos(
        id = "1",
        senderId = "u1",
        recipientId = "u2",
        message = "Cảm ơn bạn đã hỗ trợ team rất nhiệt tình trong sprint vừa rồi!",
        awardCategoryName = "IDOL GIỚI TRẺ",
        heartCount = 12,
        createdAt = "10:00 - 30/10/2025",
        hashtags = listOf("frontend", "teamwork"),
        senderAvatarUrl = null,
        senderName = "Nguyễn Văn A",
        senderEmployeeCode = "CECV10",
        senderBadgeType = "Legend Hero",
        senderDepartmentName = "IT Dept",
        recipientAvatarUrl = null,
        recipientName = "Trần Thị B",
        recipientHeroTier = 20,
        recipientDepartmentName = "Design Dept",
        shareUrl = "",
        isLiked = false,
        photoUrls = emptyList(),
    )
    KudosHighlightCard(
        kudos = sampleKudos,
        onSenderClick = {},
        onRecipientClick = {},
        onLikeToggle = {},
        onCopyLink = {},
        onViewDetail = {},
        onHashtagClick = {},
    )
}
