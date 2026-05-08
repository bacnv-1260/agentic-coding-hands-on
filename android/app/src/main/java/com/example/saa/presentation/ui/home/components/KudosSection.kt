package com.example.saa.presentation.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.saa.R
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.TextOnButton
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.homeButtonLabel
import com.example.saa.ui.theme.homeNoteBody

@Composable
fun KudosSection(
    onDetailClick: () -> Unit,
    isKudosAvailable: Boolean = true,
    modifier: Modifier = Modifier,
) {
    if (!isKudosAvailable) return
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SectionHeader(
            label = stringResource(R.string.home_kudos_section_label),
            title = stringResource(R.string.home_kudos_section_title),
        )

        Image(
            painter = painterResource(R.drawable.img_kudos_banner),
            contentDescription = stringResource(R.string.cd_kudos_banner),
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(5.dp)),
        )

        Text(
            text = stringResource(R.string.home_kudos_new_badge),
            style = MaterialTheme.typography.homeNoteBody,
            color = TextOnDark,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Text(
            text = stringResource(R.string.home_kudos_description),
            style = MaterialTheme.typography.homeNoteBody,
            color = TextOnDark,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        TextButton(
            onClick = onDetailClick,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .size(width = 160.dp, height = 40.dp)
                .background(ButtonPrimaryBg, RoundedCornerShape(4.dp)),
        ) {
            Text(
                text = stringResource(R.string.home_kudos_chi_tiet),
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
    }
}

@Preview(name = "KudosSection - available", showBackground = true, backgroundColor = 0xFF0E1A26)
@Composable
private fun KudosSectionAvailablePreview() {
    KudosSection(onDetailClick = {}, isKudosAvailable = true)
}

@Preview(name = "KudosSection - hidden (isKudosAvailable=false)", showBackground = true, backgroundColor = 0xFF0E1A26)
@Composable
private fun KudosSectionHiddenPreview() {
    KudosSection(onDetailClick = {}, isKudosAvailable = false)
}
