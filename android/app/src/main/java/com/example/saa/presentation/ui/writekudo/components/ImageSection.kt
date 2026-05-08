package com.example.saa.presentation.ui.writekudo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

private val AddBtnBorder = Color(0xFF998C5F)
private val LabelDark = Color(0xFF00101A)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImageSection(
    imageUris: List<String>,
    canAddMore: Boolean,
    onAddImageClick: () -> Unit,
    onRemoveImage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Image",
            style = MaterialTheme.typography.bodyMedium,
            color = LabelDark,
            modifier = Modifier
                .width(56.dp)
                .padding(top = 6.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            if (imageUris.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    imageUris.forEach { uri ->
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .semantics { contentDescription = "Attached image" },
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                            )
                            IconButton(
                                onClick = { onRemoveImage(uri) },
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.TopEnd)
                                    .semantics { contentDescription = "Remove image" },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp),
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (canAddMore) {
                AssistChip(
                    onClick = onAddImageClick,
                    label = { Text("+ Image (Tối đa 5)", color = LabelDark) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.White,
                        labelColor = LabelDark,
                    ),
                    border = BorderStroke(1.dp, AddBtnBorder),
                    modifier = Modifier.semantics { contentDescription = "Add image" },
                )
            }
        }
    }
}
