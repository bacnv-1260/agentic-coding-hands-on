package com.example.saa.presentation.ui.award.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.saa.R
import com.example.saa.domain.model.Award
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.awardDetailBody
import com.example.saa.ui.theme.awardDetailPrizeValue
import com.example.saa.ui.theme.awardDetailQuantityValue
import com.example.saa.ui.theme.awardDetailSectionLabel
import com.example.saa.ui.theme.awardDetailTitle

private val dividerColor = Color(0x33FFFFFF)
private val imageBorderColor = Color(0x4DFFEA9E)

@Composable
fun AwardInfoBlock(
    award: Award,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Badge image
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, imageBorderColor, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (award.imageUrl != null) {
                AsyncImage(
                    model = award.imageUrl,
                    contentDescription = stringResource(R.string.award_detail_image_cd, award.name),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(240.dp)
                        .clip(RoundedCornerShape(16.dp)),
                )
            } else {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = stringResource(R.string.award_detail_image_cd, award.name),
                    tint = ButtonPrimaryBg,
                    modifier = Modifier.size(80.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = dividerColor, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Award title row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_award_badge),
                contentDescription = null,
                colorFilter = ColorFilter.tint(ButtonPrimaryBg),
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = award.name,
                style = MaterialTheme.typography.awardDetailTitle,
                color = ButtonPrimaryBg,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        Text(
            text = award.description,
            style = MaterialTheme.typography.awardDetailBody,
            color = TextOnDark,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = dividerColor, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Quantity section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_award_quantity),
                contentDescription = null,
                colorFilter = ColorFilter.tint(TextOnDark),
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.award_detail_quantity_label),
                style = MaterialTheme.typography.awardDetailSectionLabel,
                color = TextOnDark,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = award.quantity?.toString() ?: "—",
                style = MaterialTheme.typography.awardDetailQuantityValue,
                color = TextOnDark,
            )
            if (award.quantityUnit != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = award.quantityUnit,
                    style = MaterialTheme.typography.awardDetailBody,
                    color = TextOnDark.copy(alpha = 0.6f),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = dividerColor, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Prize section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_award_prize),
                contentDescription = null,
                colorFilter = ColorFilter.tint(TextOnDark),
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.award_detail_prize_label),
                style = MaterialTheme.typography.awardDetailSectionLabel,
                color = TextOnDark,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = award.prizeValue ?: "—",
                style = MaterialTheme.typography.awardDetailPrizeValue,
                color = ButtonPrimaryBg,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.award_detail_prize_note),
            style = MaterialTheme.typography.awardDetailBody,
            color = TextOnDark.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
