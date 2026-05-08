package com.example.saa.presentation.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.domain.model.Profile

private val IconSlotBg = Color(0x33FFFFFF)
private val CollectionLabelColor = Color(0xFFFFFFFF)
private const val ICON_SLOT_COUNT = 6

@Composable
fun IconCollectionSection(
    profile: Profile?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Icon badge row
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val activeSlots = (profile?.heroTier ?: 0).coerceIn(0, ICON_SLOT_COUNT)
            repeat(ICON_SLOT_COUNT) { index ->
                val isActive = index < activeSlots
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isActive) Color(0x66FFEA9E) else IconSlotBg,
                        ),
                )
            }
        }

        Spacer(modifier = Modifier.height(0.dp))

        // Label
        Text(
            text = "Bộ sưu tập icon của tôi",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = CollectionLabelColor,
            textAlign = TextAlign.Center,
        )
    }
}
