package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.domain.model.Department
import com.example.saa.domain.model.Hashtag
import com.example.saa.ui.theme.Background
import com.example.saa.ui.theme.KudosButtonOverlayBg
import com.example.saa.ui.theme.KudosCardBorderHighlight
import com.example.saa.ui.theme.KudosGold
import com.example.saa.ui.theme.TextOnDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KudosFilterRow(
    hashtags: List<Hashtag>,
    departments: List<Department>,
    selectedHashtagId: String?,
    selectedDepartmentId: String?,
    onHashtagSelected: (String?) -> Unit,
    onDepartmentSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showHashtagSheet by remember { mutableStateOf(false) }
    var showDepartmentSheet by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val selectedHashtag = hashtags.find { it.id == selectedHashtagId }
        FilterChip(
            label = selectedHashtag?.name ?: "Hashtag",
            isSelected = selectedHashtagId != null,
            onClick = { showHashtagSheet = true },
            contentDesc = "Lọc theo Hashtag",
            modifier = Modifier.weight(1f),
        )

        val selectedDept = departments.find { it.id == selectedDepartmentId }
        FilterChip(
            label = selectedDept?.name ?: "Phòng ban",
            isSelected = selectedDepartmentId != null,
            onClick = { showDepartmentSheet = true },
            contentDesc = "Lọc theo Phòng ban",
            modifier = Modifier.weight(1f),
        )
    }

    if (showHashtagSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showHashtagSheet = false },
            sheetState = sheetState,
            containerColor = Background,
        ) {
            FilterSheetContent(
                title = "Chọn Hashtag",
                items = hashtags.map { it.id to "#${it.name}" },
                selectedId = selectedHashtagId,
                onSelect = { id ->
                    onHashtagSelected(id)
                    showHashtagSheet = false
                },
                onClear = {
                    onHashtagSelected(null)
                    showHashtagSheet = false
                },
            )
        }
    }

    if (showDepartmentSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showDepartmentSheet = false },
            sheetState = sheetState,
            containerColor = Background,
        ) {
            FilterSheetContent(
                title = "Chọn Phòng ban",
                items = departments.map { it.id to it.name },
                selectedId = selectedDepartmentId,
                onSelect = { id ->
                    onDepartmentSelected(id)
                    showDepartmentSheet = false
                },
                onClear = {
                    onDepartmentSelected(null)
                    showDepartmentSheet = false
                },
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    contentDesc: String,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(8.dp)
    val bgColor = if (isSelected) KudosGold else KudosButtonOverlayBg
    val textColor = if (isSelected) Background else TextOnDark.copy(alpha = 0.8f)
    val borderColor = if (isSelected) KudosGold else KudosCardBorderHighlight

    Surface(
        color = bgColor,
        shape = shape,
        onClick = onClick,
        modifier = modifier
            .height(40.dp)
            .clip(shape)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .semantics { contentDescription = contentDesc },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp),
        ) {
            Text(
                text = label,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = textColor,
            )
        }
    }
}

@Composable
private fun FilterSheetContent(
    title: String,
    items: List<Pair<String, String>>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    onClear: () -> Unit,
) {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.padding(bottom = 32.dp),
    ) {
        Text(
            text = title,
            color = KudosGold,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        )
        if (selectedId != null) {
            TextButton(
                onClick = onClear,
                modifier = Modifier.padding(horizontal = 12.dp),
            ) {
                Text(text = "Xóa bộ lọc", color = TextOnDark.copy(alpha = 0.6f))
            }
        }
        items.forEach { (id, name) ->
            TextButton(
                onClick = { onSelect(id) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            ) {
                Text(
                    text = name,
                    color = if (id == selectedId) KudosGold else TextOnDark,
                    fontSize = 15.sp,
                    fontWeight = if (id == selectedId) FontWeight.SemiBold else FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KudosFilterRowPreview() {
    KudosFilterRow(
        hashtags = listOf(Hashtag("1", "Frontend"), Hashtag("2", "Backend")),
        departments = listOf(Department("1", "Engineering")),
        selectedHashtagId = null,
        selectedDepartmentId = null,
        onHashtagSelected = {},
        onDepartmentSelected = {},
    )
}
