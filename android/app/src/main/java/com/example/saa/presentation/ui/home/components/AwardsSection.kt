package com.example.saa.presentation.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.saa.R
import com.example.saa.domain.model.Award
import com.example.saa.presentation.ui.home.AwardsLoadState
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.homeSectionLabel

@Composable
fun AwardsSection(
    loadState: AwardsLoadState,
    onDetailClick: (Award) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SectionHeader(
            label = stringResource(R.string.home_awards_section_label),
            title = stringResource(R.string.home_awards_section_title),
        )
        when (loadState) {
            AwardsLoadState.Loading -> Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = ButtonPrimaryBg)
            }

            AwardsLoadState.Empty -> Text(
                text = stringResource(R.string.home_awards_empty),
                style = MaterialTheme.typography.homeSectionLabel,
                color = TextOnDark,
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            is AwardsLoadState.Error -> Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(R.string.home_awards_error),
                    style = MaterialTheme.typography.homeSectionLabel,
                    color = TextOnDark,
                )
                TextButton(onClick = onRetry) {
                    Text(
                        text = stringResource(R.string.home_awards_retry),
                        color = ButtonPrimaryBg,
                    )
                }
            }

            is AwardsLoadState.Success -> LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(loadState.awards, key = { it.id }) { award ->
                    AwardCard(award = award, onDetailClick = onDetailClick)
                }
            }
        }
    }
}
