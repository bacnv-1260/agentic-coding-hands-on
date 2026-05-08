package com.example.saa.presentation.ui.award.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.saa.R
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.awardDetailKudosTitle

@Composable
fun KudosKVBanner(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 20.dp),
    ) {
        Text(
            text = stringResource(R.string.award_detail_kudos_intro),
            style = MaterialTheme.typography.bodySmall,
            color = TextOnDark.copy(alpha = 0.6f),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_kudos_flame),
                contentDescription = null,
                modifier = Modifier
                    .width(40.dp)
                    .height(32.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "KUDOS",
                style = MaterialTheme.typography.awardDetailKudosTitle,
                color = ButtonPrimaryBg,
            )
        }
    }
}
