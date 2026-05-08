package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.R

// Figma node: 6885:9065 "header"
// height: 104px iOS (44px status bar + 60px nav bar content)
// background: linear-gradient(#00101A → transparent)
// Left: mm_media_logo_homepage (48×44dp), x:20, below status bar
// Right: language button (VN) + search icon + bell icon (w/ badge), gap:10dp

private val HeaderGradient = Brush.verticalGradient(
    colorStops = arrayOf(
        0.00f to Color(0xFF00101A),
        0.76f to Color(0x4D00101A), // 30% opacity
        0.85f to Color(0x3300101A), // 20%
        0.89f to Color(0x2600101A), // 15%
        0.93f to Color(0x1A00101A), // 10%
        0.96f to Color(0x0D00101A), // 5%
        1.00f to Color(0x0000101A), // 0%
    ),
)

private val ActionButtonBg = Color(0x1AFFFFFF)    // white 10%
private val ActionButtonBorder = Color(0x33FFFFFF) // white 20%

@Composable
fun KudosTopBar(
    hasUnreadNotifications: Boolean = true,
    onSearchClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(HeaderGradient),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp)
                .padding(horizontal = 20.dp),
        ) {
            // Left: SAA/Sun* logo
            Image(
                painter = painterResource(R.drawable.mm_media_logo_homepage),
                contentDescription = "Sun* SAA Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(48.dp)
                    .height(44.dp),
            )

            // Right: actions row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Language toggle: VN flag + "VN" text + chevron
                LanguageButton(
                    language = "VN",
                    onClick = onLanguageClick,
                )
                // Search
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Tìm kiếm",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onSearchClick)
                        .semantics { contentDescription = "Tìm kiếm" },
                )
                // Bell with optional badge dot
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onNotificationsClick)
                        .semantics { contentDescription = "Thông báo" },
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.matchParentSize(),
                    )
                    if (hasUnreadNotifications) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape)
                                .background(Color(0xFFD4271D)), // red badge dot
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageButton(
    language: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(4.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .height(32.dp)
            .clip(shape)
            .border(width = 1.dp, color = ActionButtonBorder, shape = shape)
            .background(ActionButtonBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        // VN flag emoji as simple text (matches 24px icon slot)
        Text(
            text = "🇻🇳",
            fontSize = 16.sp,
        )
        Text(
            text = language,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(16.dp),
        )
    }
}

@Preview(showBackground = false, backgroundColor = 0xFF00101A)
@Composable
private fun KudosTopBarPreview() {
    KudosTopBar(hasUnreadNotifications = true)
}
