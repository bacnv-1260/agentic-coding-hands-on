package com.example.saa.presentation.ui.community_standards

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.saa.R
import com.example.saa.ui.theme.KudosGold
import com.example.saa.ui.theme.SaaTheme
import com.example.saa.ui.theme.communityStandardsScreenTitle
import com.example.saa.ui.theme.communityStandardsSectionBody
import com.example.saa.ui.theme.communityStandardsSectionTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityStandardsScreen(
    onNavigateBack: () -> Unit,
) {
    val backArrowDesc = stringResource(R.string.cd_back_arrow)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.community_standards_title),
                            color = KudosGold,
                            style = MaterialTheme.typography.communityStandardsScreenTitle,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.semantics {
                                contentDescription = backArrowDesc
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.background,
                    ),
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 24.dp),
        ) {
            CommunityStandardsSection(
                title = stringResource(R.string.community_standards_s1_title),
                body = stringResource(R.string.community_standards_s1_body),
            )
            CommunityStandardsSection(
                title = stringResource(R.string.community_standards_s2_title),
                body = stringResource(R.string.community_standards_s2_body),
            )
            CommunityStandardsSection(
                title = stringResource(R.string.community_standards_s3_title),
                body = stringResource(R.string.community_standards_s3_body),
            )
            CommunityStandardsSection(
                title = stringResource(R.string.community_standards_s4_title),
                body = stringResource(R.string.community_standards_s4_body),
            )
            CommunityStandardsSection(
                title = stringResource(R.string.community_standards_s5_title),
                body = stringResource(R.string.community_standards_s5_body),
            )
        }
    }
}

@Composable
private fun CommunityStandardsSection(
    title: String,
    body: String,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.communityStandardsSectionTitle,
        color = KudosGold,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = body,
        style = MaterialTheme.typography.communityStandardsSectionBody,
        color = MaterialTheme.colorScheme.onSurface,
    )
    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CommunityStandardsScreenPreview() {
    SaaTheme {
        CommunityStandardsScreen(onNavigateBack = {})
    }
}
