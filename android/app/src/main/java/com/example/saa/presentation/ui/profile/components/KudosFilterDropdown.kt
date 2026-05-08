package com.example.saa.presentation.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.domain.model.KudosFilter

private val PillBg = Color(0x1AFFEA9E)
private val PillBorder = Color(0xFF998C5F)
private val PillTextColor = Color(0xFFFFFFFF)
private val OverlayBg = Color(0xFF00070C)
private val OverlayBorder = Color(0xFF998C5F)
private val OptionTextColor = Color(0xFFFFFFFF)

@Composable
fun KudosFilterDropdown(
    selectedFilter: KudosFilter,
    sentCount: Int,
    receivedCount: Int,
    isOpen: Boolean,
    onToggle: () -> Unit,
    onDismiss: () -> Unit,
    onFilterChange: (KudosFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.padding(horizontal = 20.dp)) {
        // Pill button
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(PillBg)
                .border(1.dp, PillBorder, RoundedCornerShape(4.dp))
                .clickable { onToggle() }
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .height(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = when (selectedFilter) {
                    KudosFilter.SENT -> "Đã gửi ($sentCount)"
                    KudosFilter.RECEIVED -> "Đã nhận ($receivedCount)"
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = PillTextColor,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = PillTextColor,
            )
        }

        // Dropdown overlay
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = onDismiss,
            modifier = Modifier
                .width(160.dp)
                .background(OverlayBg)
                .border(1.dp, OverlayBorder, RoundedCornerShape(8.dp)),
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Đã nhận ($receivedCount)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (selectedFilter == KudosFilter.RECEIVED)
                            Color(0xFFFAE287) else OptionTextColor,
                    )
                },
                onClick = { onFilterChange(KudosFilter.RECEIVED) },
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Đã gửi ($sentCount)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (selectedFilter == KudosFilter.SENT)
                            Color(0xFFFAE287) else OptionTextColor,
                    )
                },
                onClick = { onFilterChange(KudosFilter.SENT) },
            )
        }
    }
}
