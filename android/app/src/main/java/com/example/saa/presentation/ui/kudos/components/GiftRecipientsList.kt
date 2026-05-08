package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import com.example.saa.domain.model.GiftRecipient
import com.example.saa.ui.theme.KudosGold
import com.example.saa.ui.theme.TextOnDark

// D.3 design tokens
private val GiftCardBg = Color(0xFF00070C)
private val GiftCardBorder = Color(0xFF998C5F)
private val GiftDivider = Color(0xFF2E3940)

@Composable
fun GiftRecipientsList(
    recipients: List<GiftRecipient>,
    onClickRow: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(shape)
            .border(width = 0.794.dp, color = GiftCardBorder, shape = shape)
            .background(GiftCardBg),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "${recipients.size} SUNNER NHẬN QUÀ MỚI NHẤT",
                color = KudosGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
            )
            recipients.forEachIndexed { index, recipient ->
                GiftRecipientRow(
                    recipient = recipient,
                    onClick = { onClickRow(recipient.id) },
                )
                if (index < recipients.lastIndex) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = GiftDivider,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun GiftRecipientRow(
    recipient: GiftRecipient,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .semantics {
                role = Role.Button
                contentDescription = "Xem profile của ${recipient.fullName}"
            },
    ) {
        UserAvatar(
            avatarUrl = recipient.avatarUrl,
            size = 32,
            onClick = onClick,
            contentDesc = "Avatar của ${recipient.fullName}",
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = recipient.fullName,
                color = TextOnDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = recipient.rewardName,
                color = TextOnDark.copy(alpha = 0.6f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GiftRecipientsListPreview() {
    GiftRecipientsList(
        recipients = listOf(
            GiftRecipient("1", "Huỳnh Dương Xuân", null, "Nhận được 1 áo phông SAA"),
            GiftRecipient("2", "Nguyễn Văn B", null, "Nhận được 1 mug SAA"),
        ),
        onClickRow = {},
    )
}
