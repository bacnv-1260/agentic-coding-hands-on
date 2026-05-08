package com.example.saa.presentation.ui.award.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.saa.R
import com.example.saa.domain.model.Award
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.awardDetailSectionTitle

@Composable
fun AwardHighlightBlock(
    allAwards: List<Award>,
    selectedAward: Award?,
    expanded: Boolean,
    onToggle: () -> Unit,
    onSelect: (Award) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 24.dp),
    ) {
        Text(
            text = stringResource(R.string.home_awards_section_label),
            style = MaterialTheme.typography.bodySmall,
            color = TextOnDark.copy(alpha = 0.6f),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.award_detail_system_title),
            style = MaterialTheme.typography.awardDetailSectionTitle,
            color = ButtonPrimaryBg,
        )
        Spacer(modifier = Modifier.height(12.dp))
        AwardCategoryDropdown(
            allAwards = allAwards,
            selectedAward = selectedAward,
            expanded = expanded,
            onToggle = onToggle,
            onSelect = onSelect,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun AwardCategoryDropdown(
    allAwards: List<Award>,
    selectedAward: Award?,
    expanded: Boolean,
    onToggle: () -> Unit,
    onSelect: (Award) -> Unit,
    onDismiss: () -> Unit,
) {
    Column {
        OutlinedButton(
            onClick = onToggle,
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, ButtonPrimaryBg),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFF0A1929),
                contentColor = TextOnDark,
            ),
            modifier = Modifier
                .width(160.dp)
                .height(40.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = selectedAward?.name ?: "—",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextOnDark,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextOnDark,
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
        ) {
            allAwards.forEach { award ->
                val isSelected = award.id == selectedAward?.id
                DropdownMenuItem(
                    text = {
                        Text(
                            text = award.name,
                            color = if (isSelected) ButtonPrimaryBg else TextOnDark,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    onClick = { onSelect(award) },
                )
            }
        }
    }
}
