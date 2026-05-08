package com.example.saa.presentation.ui.award

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.saa.NavRoutes
import com.example.saa.R
import com.example.saa.presentation.ui.award.components.AwardHighlightBlock
import com.example.saa.presentation.ui.award.components.AwardInfoBlock
import com.example.saa.presentation.ui.award.components.KudosBannerBlock
import com.example.saa.presentation.ui.award.components.KudosKVBanner
import com.example.saa.presentation.ui.home.components.HomeHeader
import com.example.saa.presentation.ui.home.components.HomeNavBar
import com.example.saa.presentation.ui.home.components.HomeNavTab
import com.example.saa.ui.theme.Background
import com.example.saa.ui.theme.ButtonPrimaryBg
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import timber.log.Timber

@Composable
fun AwardDetailScreen(
    navController: NavController,
    viewModel: AwardDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val bgOffset by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo
                .firstOrNull { it.index == 0 }
                ?.offset ?: 0
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            Timber.e("AwardDetailScreen error: $error")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
    ) {
        // Keyvisual background — scrolls with content and fades to black
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(723.dp)
                .align(Alignment.TopEnd)
                .offset { IntOffset(0, bgOffset) },
        ) {
            Image(
                painter = painterResource(R.drawable.img_keyvisual_bg),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.55f to Color.Transparent,
                                0.80f to Background.copy(alpha = 0.7f),
                                1.0f to Background,
                            ),
                        ),
                    ),
            )
        }

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    color = ButtonPrimaryBg,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center),
                )
            }

            uiState.error != null && uiState.award == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    androidx.compose.foundation.layout.Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.award_detail_error),
                            style = MaterialTheme.typography.bodyMedium,
                            color = androidx.compose.ui.graphics.Color.White,
                        )
                        androidx.compose.foundation.layout.Spacer(
                            modifier = Modifier.height(16.dp),
                        )
                        Button(onClick = viewModel::retry) {
                            Text(text = stringResource(R.string.home_awards_retry))
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 144.dp, bottom = 120.dp),
                ) {
                    item {
                        KudosKVBanner()
                    }

                    item {
                        AwardHighlightBlock(
                            allAwards = uiState.allAwards,
                            selectedAward = uiState.award,
                            expanded = uiState.showDropdown,
                            onToggle = viewModel::toggleDropdown,
                            onSelect = viewModel::selectAward,
                            onDismiss = viewModel::dismissDropdown,
                        )
                    }

                    uiState.award?.let { award ->
                        item {
                            AwardInfoBlock(award = award)
                        }
                    }

                    item {
                        KudosBannerBlock(
                            onKudosDetailClick = {
                                navController.navigate(NavRoutes.Kudos.route)
                            },
                        )
                    }
                }
            }
        }

        // Fixed header overlay
        HomeHeader(
            unreadCount = uiState.unreadNotificationCount,
            selectedLanguage = uiState.selectedLanguage,
            showLanguageSelector = uiState.showLanguageSelector,
            onLanguageClick = viewModel::showLanguageSelector,
            onLanguageSelected = viewModel::setLanguage,
            onLanguageDismiss = viewModel::dismissLanguageSelector,
            onSearchClick = {
                navController.navigate(NavRoutes.Search.route)
            },
            onNotificationClick = {
                navController.navigate(NavRoutes.Notifications.route)
            },
            modifier = Modifier.align(Alignment.TopStart),
        )

        // Fixed bottom nav
        HomeNavBar(
            selected = HomeNavTab.AWARDS,
            onTabSelected = { tab ->
                when (tab) {
                    HomeNavTab.SAA -> navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = false }
                    }
                    HomeNavTab.AWARDS -> Unit
                    HomeNavTab.KUDOS -> navController.navigate(NavRoutes.Kudos.route)
                    HomeNavTab.PROFILE -> navController.navigate(NavRoutes.Profile.route)
                }
            },
            modifier = Modifier.align(Alignment.BottomStart),
        )
    }
}
