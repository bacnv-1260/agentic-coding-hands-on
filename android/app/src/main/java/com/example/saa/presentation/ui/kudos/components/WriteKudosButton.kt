package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.ui.theme.TextOnDark

// A.1 Write Kudos Button
// bg: rgba(255,234,158, 0.10) — subtle gold tint
// border: 1px solid #998C5F — golden brown
// radius: 4dp, height: 40dp
private val WriteBtnBg = Color(0x1AFFEA9E)   // 10% gold
private val WriteBtnBorder = Color(0xFF998C5F) // golden brown

@Composable
fun WriteKudosButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(4.dp)
    Surface(
        color = WriteBtnBg,
        shape = shape,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 16.dp)
            .clip(shape)
            .border(width = 1.dp, color = WriteBtnBorder, shape = shape)
            .clickable(onClick = onClick)
            .semantics {
                role = Role.Button
                contentDescription = "Hôm nay, bạn muốn gửi kudos đến ai?"
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Create,
                contentDescription = null,
                tint = TextOnDark,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = " Hôm nay, bạn muốn gửi kudos đến ai?",
                color = TextOnDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WriteKudosButtonPreview() {
    WriteKudosButton(onClick = {})
}
