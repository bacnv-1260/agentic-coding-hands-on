package com.example.saa.presentation.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.domain.model.UserStats

private val ContainerBg = Color(0xFF00070C)
private val ContainerBorder = Color(0xFF998C5F)
private val StatsLabelColor = Color(0xFFFFFFFF)
private val StatsValueColor = Color(0xFFFFEA9E)
private val DividerColor = Color(0xFF2E3940)
private val ButtonBg = Color(0xFFFFEA9E)
private val ButtonTextColor = Color(0xFF00101A)
private val ShimmerBg = Color(0xFF1A2A35)

@Composable
fun ProfileStatsContainer(
    stats: UserStats?,
    isLoading: Boolean,
    onOpenSecretBox: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(ContainerBg)
            .border(
                width = 0.794.dp,
                color = ContainerBorder,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(12.dp),
    ) {
        when {
            isLoading -> {
                // Shimmer skeleton
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(ShimmerBg),
                        )
                    }
                }
            }
            stats == null -> {
                // Error state
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Không thể tải dữ liệu",
                        color = StatsLabelColor,
                        fontSize = 14.sp,
                    )
                    TextButton(onClick = onRetry) {
                        Text(text = "Thử lại", color = StatsValueColor)
                    }
                }
            }
            else -> {
                // Normal state
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatRow(label = "Số Kudos bạn nhận được:", value = stats.kudosReceived.toString())
                    StatRow(label = "Số Kudos bạn đã gửi:", value = stats.kudosSent.toString())
                    StatRow(label = "Số tim bạn nhận được:", value = stats.heartsReceived.toString())

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = DividerColor,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )

                    StatRow(label = "Số Secret Box bạn đã mở:", value = stats.secretBoxesOpened.toString())
                    StatRow(label = "Số Secret Box chưa mở:", value = stats.secretBoxesUnopened.toString())

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = onOpenSecretBox,
                        enabled = stats.secretBoxesUnopened > 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonBg,
                            contentColor = ButtonTextColor,
                            disabledContainerColor = ButtonBg.copy(alpha = 0.4f),
                            disabledContentColor = ButtonTextColor.copy(alpha = 0.4f),
                        ),
                        shape = RoundedCornerShape(4.dp),
                    ) {
                        Text(
                            text = "Mở Secret Box 🎁",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = ButtonTextColor,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight(300),
            color = StatsLabelColor,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = StatsValueColor,
        )
    }
}
