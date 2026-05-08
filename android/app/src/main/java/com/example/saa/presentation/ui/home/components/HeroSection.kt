package com.example.saa.presentation.ui.home.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.saa.R
import com.example.saa.ui.theme.BorderGold
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.ButtonSecondaryOverlay
import com.example.saa.ui.theme.TextOnButton
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.homeButtonLabel
import com.example.saa.ui.theme.homeEventLabel
import com.example.saa.ui.theme.homeEventValue

@Composable
fun HeroSection(
    days: Long,
    hours: Long,
    minutes: Long,
    onAboutAwardClick: () -> Unit,
    onAboutKudosClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.img_root_further),
            contentDescription = stringResource(R.string.cd_root_further_logo),
            modifier = Modifier.size(width = 247.dp, height = 109.dp),
        )

        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            CountdownSection(days = days, hours = hours, minutes = minutes)

            EventInfo()
        }

        HeroActions(
            onAboutAwardClick = onAboutAwardClick,
            onAboutKudosClick = onAboutKudosClick,
        )
    }
}

@Composable
private fun EventInfo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.home_event_time_label),
                style = MaterialTheme.typography.homeEventLabel,
                color = TextOnDark,
            )
            Text(
                text = stringResource(R.string.home_event_time_value),
                style = MaterialTheme.typography.homeEventValue,
                color = ButtonPrimaryBg,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.home_event_venue_label),
                style = MaterialTheme.typography.homeEventLabel,
                color = TextOnDark,
            )
            Text(
                text = stringResource(R.string.home_event_venue_value),
                style = MaterialTheme.typography.homeEventValue,
                color = ButtonPrimaryBg,
            )
        }
        Text(
            text = stringResource(R.string.home_event_livestream),
            style = MaterialTheme.typography.homeEventLabel,
            color = TextOnDark,
        )
    }
}

@Composable
private fun HeroActions(
    onAboutAwardClick: () -> Unit,
    onAboutKudosClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            onClick = onAboutAwardClick,
            modifier = Modifier
                .size(width = 160.dp, height = 40.dp)
                .background(ButtonPrimaryBg, RoundedCornerShape(4.dp)),
        ) {
            Text(
                text = stringResource(R.string.home_btn_about_award),
                style = MaterialTheme.typography.homeButtonLabel,
                color = TextOnButton,
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = TextOnButton,
                modifier = Modifier.size(24.dp),
            )
        }
        TextButton(
            onClick = onAboutKudosClick,
            modifier = Modifier
                .size(width = 159.dp, height = 40.dp)
                .background(ButtonSecondaryOverlay, RoundedCornerShape(4.dp))
                .border(1.dp, BorderGold, RoundedCornerShape(4.dp)),
        ) {
            Text(
                text = stringResource(R.string.home_btn_about_kudos),
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
