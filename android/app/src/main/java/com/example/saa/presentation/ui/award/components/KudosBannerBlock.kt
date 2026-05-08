package com.example.saa.presentation.ui.award.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.R
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.awardDetailBody
import com.example.saa.ui.theme.awardDetailSectionTitle

private val buttonTextColor = Color(0xFF0A1929)

@Composable
fun KudosBannerBlock(
    onKudosDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 32.dp),
    ) {
        // Label
        Text(
            text = stringResource(R.string.home_kudos_section_label),
            style = MaterialTheme.typography.bodySmall,
            color = TextOnDark.copy(alpha = 0.6f),
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = stringResource(R.string.home_kudos_section_title),
            style = MaterialTheme.typography.awardDetailSectionTitle,
            color = ButtonPrimaryBg,
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Banner image
        Image(
            painter = painterResource(R.drawable.img_kudos_banner),
            contentDescription = stringResource(R.string.cd_kudos_banner),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(8.dp)),
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Badge
        Text(
            text = stringResource(R.string.home_kudos_new_badge),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            ),
            color = TextOnDark,
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Description
        Text(
            text = stringResource(R.string.home_kudos_description),
            style = MaterialTheme.typography.awardDetailBody,
            color = TextOnDark.copy(alpha = 0.7f),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Chi tiết button — FILLED gold
        Button(
            onClick = onKudosDetailClick,
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonPrimaryBg,
                contentColor = buttonTextColor,
            ),
            modifier = Modifier
                .width(120.dp)
                .height(40.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.home_kudos_chi_tiet),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = buttonTextColor,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = buttonTextColor,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}
