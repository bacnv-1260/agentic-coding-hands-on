package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.domain.model.UserStats
import com.example.saa.ui.theme.Background
import com.example.saa.ui.theme.KudosGold
import com.example.saa.ui.theme.TextOnDark

// D.1 + D.2 design tokens
private val StatsCardBg = Color(0xFF00070C)
private val StatsCardBorder = Color(0xFF998C5F)
private val StatsDivider = Color(0xFF2E3940)
private val SecretBoxBtnBg = KudosGold
private val SecretBoxBtnText = Color(0xFF00101A)

@Composable
fun PersonalStatsGrid(
    stats: UserStats,
    onOpenSecretBox: (() -> Unit)? = null,
    secretBoxEnabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(shape)
            .border(width = 0.794.dp, color = StatsCardBorder, shape = shape)
            .background(StatsCardBg),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StatRow(label = "Số Kudos bạn nhận được:", value = stats.kudosReceived)
            StatRow(label = "Số Kudos bạn đã gửi:", value = stats.kudosSent)
            StatRow(label = "Số tim bạn nhận được:", value = stats.heartsReceived, suffix = " 🔥")

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = StatsDivider,
            )

            StatRow(label = "Số Secret Box bạn đã mở:", value = stats.secretBoxesOpened)
            StatRow(label = "Số Secret Box chưa mở:", value = stats.secretBoxesUnopened)

            // D.2 — Secret Box button is INSIDE this container
            if (onOpenSecretBox != null) {
                SecretBoxButton(
                    enabled = secretBoxEnabled,
                    onClick = onOpenSecretBox,
                )
            }
        }
    }
}

@Composable
private fun SecretBoxButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val btnShape = RoundedCornerShape(4.dp)
    Surface(
        color = if (enabled) SecretBoxBtnBg else SecretBoxBtnBg.copy(alpha = 0.38f),
        shape = btnShape,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(btnShape)
            .semantics { contentDescription = "Mở Secret Box" },
        onClick = onClick,
        enabled = enabled,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp),
        ) {
            Text(
                text = "Mở Secret Box",
                color = if (enabled) SecretBoxBtnText else SecretBoxBtnText.copy(alpha = 0.5f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
            Icon(
                imageVector = Icons.Default.CardGiftcard,
                contentDescription = null,
                tint = if (enabled) SecretBoxBtnText else SecretBoxBtnText.copy(alpha = 0.5f),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(24.dp),
            )
        }
    }
}

@Composable
private fun StatRow(
    label: String,
    value: Int,
    suffix: String = "",
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = TextOnDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "${value}$suffix",
            color = KudosGold,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PersonalStatsGridPreview() {
    PersonalStatsGrid(
        stats = UserStats(
            kudosReceived = 25,
            kudosSent = 12,
            heartsReceived = 87,
            secretBoxesOpened = 3,
            secretBoxesUnopened = 2,
        ),
        onOpenSecretBox = {},
    )
}
