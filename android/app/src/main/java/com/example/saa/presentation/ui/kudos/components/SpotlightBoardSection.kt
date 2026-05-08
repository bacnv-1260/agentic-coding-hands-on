package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.ui.theme.KudosGold

@Composable
fun SpotlightBoardSection(
    totalKudos: Int,
    sunnerNames: List<String>,
    onSunnerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        // Section header — matches HIGHLIGHT KUDOS style
        Text(
            text = "Sun* Annual Awards 2025",
            color = KudosGold,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = "SPOTLIGHT BOARD",
            color = KudosGold,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        SpotlightBoard(
            totalKudos = totalKudos,
            sunnerNames = sunnerNames,
            onSunnerClick = onSunnerClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
    }
}
