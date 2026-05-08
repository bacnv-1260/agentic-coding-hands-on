package com.example.saa.presentation.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.DividerLine
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.homeSectionLabel
import com.example.saa.ui.theme.homeSectionTitle

@Composable
fun SectionHeader(
    label: String,
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.homeSectionLabel,
            color = TextOnDark,
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            thickness = 1.dp,
            color = DividerLine,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.homeSectionTitle,
            color = ButtonPrimaryBg,
        )
    }
}
