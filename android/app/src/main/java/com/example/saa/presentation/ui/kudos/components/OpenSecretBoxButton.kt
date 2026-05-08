package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.ui.theme.KudosGold
import com.example.saa.ui.theme.KudosSecretBoxDisabled

private val SecretBoxBtnTextColor = Color(0xFF00101A)

@Composable
fun OpenSecretBoxButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(4.dp)
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        color = if (enabled) KudosGold else KudosSecretBoxDisabled,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 16.dp)
            .clip(shape)
            .semantics { contentDescription = "Mở Secret Box" },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp),
        ) {
            Text(
                text = "Mở Secret Box",
                color = if (enabled) SecretBoxBtnTextColor else SecretBoxBtnTextColor.copy(alpha = 0.5f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
            Icon(
                imageVector = Icons.Default.CardGiftcard,
                contentDescription = null,
                tint = if (enabled) SecretBoxBtnTextColor else SecretBoxBtnTextColor.copy(alpha = 0.5f),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(24.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OpenSecretBoxButtonEnabledPreview() {
    OpenSecretBoxButton(enabled = true, onClick = {})
}

@Preview(showBackground = true)
@Composable
private fun OpenSecretBoxButtonDisabledPreview() {
    OpenSecretBoxButton(enabled = false, onClick = {})
}
