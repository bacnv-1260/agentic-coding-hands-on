package com.example.saa.presentation.ui.writekudo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.saa.domain.model.Hashtag

private val ChipBorder = Color(0xFF998C5F)
private val ChipBg = Color(0x4DFFEA9E)
private val ChipText = Color(0xFF00101A)
private val AddBtnBorder = Color(0xFF998C5F)
private val LabelDark = Color(0xFF00101A)
private val RequiredRed = Color(0xFFCF1322)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HashtagSection(
    selectedHashtags: List<Hashtag>,
    canAddMore: Boolean,
    onAddClick: () -> Unit,
    onRemoveHashtag: (Hashtag) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = buildAnnotatedString {
                append("Hashtag")
                withStyle(SpanStyle(color = RequiredRed, fontWeight = FontWeight.Bold)) {
                    append("*")
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = LabelDark,
            modifier = Modifier
                .width(80.dp)
                .padding(top = 6.dp),
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f),
        ) {
            selectedHashtags.forEach { hashtag ->
                InputChip(
                    selected = true,
                    onClick = { onRemoveHashtag(hashtag) },
                    label = { Text("#${hashtag.name}", color = ChipText) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove ${hashtag.name}",
                            tint = ChipText,
                        )
                    },
                    colors = InputChipDefaults.inputChipColors(
                        selectedContainerColor = ChipBg,
                        selectedLabelColor = ChipText,
                        selectedTrailingIconColor = ChipText,
                    ),
                    border = BorderStroke(1.dp, ChipBorder),
                    modifier = Modifier.semantics {
                        contentDescription = "Hashtag ${hashtag.name}, tap to remove"
                    },
                )
            }
            if (canAddMore) {
                AssistChip(
                    onClick = onAddClick,
                    label = { Text("+ Hashtag (Tối đa 5)", color = ChipText) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.White,
                        labelColor = ChipText,
                    ),
                    border = BorderStroke(1.dp, AddBtnBorder),
                    modifier = Modifier.semantics { contentDescription = "Add hashtag" },
                )
            }
        }
    }
}
