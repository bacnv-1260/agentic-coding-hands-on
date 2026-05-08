package com.example.saa.presentation.ui.writekudo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BarBg = Color(0xFF00101A)
private val CancelBg = Color(0x1AFFEA9E)        // rgba(255,234,158, 0.10)
private val CancelBorder = Color(0xFF998C5F)
private val SendBg = Color(0xFFFFEA9E)
private val SendBgDisabled = Color(0x66FFEA9E)
private val SendText = Color(0xFF00101A)

@Composable
fun WriteKudoBottomBar(
    isSubmitEnabled: Boolean,
    isSending: Boolean,
    onCancelClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = BarBg,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
        ) {
            OutlinedButton(
                onClick = onCancelClick,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = CancelBg,
                    contentColor = Color.White,
                ),
                border = BorderStroke(1.dp, CancelBorder),
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .semantics { contentDescription = "Cancel button" },
            ) {
                Text(
                    text = "Hủy",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                )
            }
            Button(
                onClick = onSendClick,
                enabled = isSubmitEnabled && !isSending,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SendBg,
                    contentColor = SendText,
                    disabledContainerColor = SendBgDisabled,
                    disabledContentColor = SendText.copy(alpha = 0.4f),
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .semantics { contentDescription = "Send button" },
            ) {
                Text(
                    text = "Gửi đi",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                )
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                )
            }
        }
    }
}

