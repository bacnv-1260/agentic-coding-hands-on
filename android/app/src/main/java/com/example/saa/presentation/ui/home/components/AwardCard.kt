package com.example.saa.presentation.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.saa.R
import com.example.saa.domain.model.Award
import com.example.saa.ui.theme.BorderGold
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.TextOnButton
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.homeButtonLabel
import com.example.saa.ui.theme.homeEventLabel
import com.example.saa.ui.theme.homeEventValue

@Composable
fun AwardCard(
    award: Award,
    onDetailClick: (Award) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(160.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(11.dp))
                .border(0.5.dp, BorderGold, RoundedCornerShape(11.dp)),
        ) {
            AsyncImage(
                model = award.imageUrl,
                contentDescription = award.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = award.name,
                style = MaterialTheme.typography.homeEventValue,
                color = ButtonPrimaryBg,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = award.description,
                style = MaterialTheme.typography.homeEventLabel,
                color = TextOnDark,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        TextButton(
            onClick = { onDetailClick(award) },
            modifier = Modifier
                .size(width = 84.dp, height = 32.dp)
                .background(Color.Transparent, RoundedCornerShape(4.dp)),
        ) {
            Text(
                text = stringResource(R.string.home_award_chi_tiet),
                style = MaterialTheme.typography.homeButtonLabel,
                color = TextOnDark,
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = TextOnDark,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
