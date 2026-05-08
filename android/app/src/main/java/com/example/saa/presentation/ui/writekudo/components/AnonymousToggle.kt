package com.example.saa.presentation.ui.writekudo.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

private val CheckedGold = Color(0xFF998C5F)
private val LabelColor = Color(0xFF333333)

@Composable
fun AnonymousToggle(
    isAnonymous: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Checkbox(
            checked = isAnonymous,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = CheckedGold,
                uncheckedColor = Color(0xFFDDDDDD),
                checkmarkColor = Color.White,
            ),
            modifier = Modifier.semantics { contentDescription = "Anonymous toggle" },
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Gửi lời cám ơn và ghi nhận ẩn danh",
            style = MaterialTheme.typography.bodyMedium,
            color = LabelColor,
        )
    }
}
