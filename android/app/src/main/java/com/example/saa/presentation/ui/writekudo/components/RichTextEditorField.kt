package com.example.saa.presentation.ui.writekudo.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

@Composable
fun RichTextEditorField(
    state: RichTextState,
    isError: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier,
) {
    RichTextEditor(
        state = state,
        placeholder = {
            androidx.compose.material3.Text(
                "Hãy gửi gắm lời cám ơn và ghi nhận đến đồng đội tại đây nhé!",
                color = androidx.compose.ui.graphics.Color(0xFF999999),
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .border(
                width = 1.dp,
                color = if (isError) MaterialTheme.colorScheme.error
                else Color(0xFF998C5F),
                shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp),
            )
            .padding(8.dp)
            .semantics { contentDescription = "Message field" },
    )
}
