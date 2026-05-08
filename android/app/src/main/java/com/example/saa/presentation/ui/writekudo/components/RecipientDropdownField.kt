package com.example.saa.presentation.ui.writekudo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

private val FieldBorder = Color(0xFF998C5F)
private val LabelDark = Color(0xFF00101A)
private val PlaceholderGray = Color(0xFF999999)
private val RequiredRed = Color(0xFFCF1322)

@Composable
fun RecipientDropdownField(
    recipientName: String,
    errorMessage: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Người nhận")
                    withStyle(SpanStyle(color = RequiredRed, fontWeight = FontWeight.Bold)) {
                        append("*")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = LabelDark,
                modifier = Modifier.width(96.dp),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = if (errorMessage != null) MaterialTheme.colorScheme.error else FieldBorder,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .clickable(onClick = onClick)
                    .padding(horizontal = 10.dp)
                    .semantics { contentDescription = "Recipient field" },
            ) {
                Text(
                    text = recipientName.ifEmpty { "Tìm kiếm" },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (recipientName.isEmpty()) PlaceholderGray else LabelDark,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = LabelDark,
                )
            }
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp),
            )
        }
    }
}
