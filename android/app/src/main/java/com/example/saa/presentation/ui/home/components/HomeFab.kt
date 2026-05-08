package com.example.saa.presentation.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.saa.R
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.TextOnButton
import com.example.saa.ui.theme.homeButtonLabel

@Composable
fun HomeFab(
    onWriteKudoClick: () -> Unit,
    onKudosFeedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = CircleShape, spotColor = ButtonPrimaryBg)
            .clip(CircleShape)
            .background(ButtonPrimaryBg)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Create,
            contentDescription = stringResource(R.string.cd_fab_write_kudo),
            tint = TextOnButton,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onWriteKudoClick),
        )
        Text(
            text = "/",
            style = MaterialTheme.typography.homeButtonLabel,
            color = TextOnButton,
        )
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = stringResource(R.string.cd_fab_kudos_feed),
            tint = TextOnButton,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onKudosFeedClick),
        )
    }
}

