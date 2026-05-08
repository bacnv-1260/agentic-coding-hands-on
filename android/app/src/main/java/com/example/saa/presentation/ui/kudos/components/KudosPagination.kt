package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.ui.theme.KudosGold
import com.example.saa.ui.theme.TextOnDark

@Composable
fun KudosPagination(
    currentPage: Int,
    total: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (total == 0) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.padding(vertical = 8.dp),
    ) {
        val isPrevEnabled = currentPage > 0
        val isNextEnabled = currentPage < total - 1

        NavCircleButton(
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            enabled = isPrevEnabled,
            onClick = onPrev,
            contentDesc = "Trang trước",
        )

        Text(
            text = "${currentPage + 1}/$total",
            color = TextOnDark.copy(alpha = 0.8f),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        NavCircleButton(
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            enabled = isNextEnabled,
            onClick = onNext,
            contentDesc = "Trang tiếp theo",
        )
    }
}

@Composable
private fun NavCircleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
    contentDesc: String,
) {
    Surface(
        shape = CircleShape,
        color = if (enabled) Color(0x26FFFFFF) else Color(0x0DFFFFFF),
        modifier = Modifier.size(32.dp),
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.semantics { contentDescription = contentDesc },
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (enabled) KudosGold else TextOnDark.copy(alpha = 0.3f),
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KudosPaginationPreview() {
    KudosPagination(currentPage = 1, total = 5, onPrev = {}, onNext = {})
}
