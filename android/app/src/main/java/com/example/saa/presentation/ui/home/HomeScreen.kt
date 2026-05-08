package com.example.saa.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.saa.NavRoutes
import com.example.saa.R
import com.example.saa.presentation.ui.home.components.AwardsSection
import com.example.saa.presentation.ui.home.components.HeroSection
import com.example.saa.presentation.ui.home.components.HomeFab
import com.example.saa.presentation.ui.home.components.HomeHeader
import com.example.saa.presentation.ui.home.components.HomeNavBar
import com.example.saa.presentation.ui.home.components.HomeNavTab
import com.example.saa.presentation.ui.home.components.KudosSection
import com.example.saa.presentation.ui.home.components.NoteSection
import com.example.saa.ui.theme.Background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isUnauthenticated) {
        if (uiState.isUnauthenticated) {
            navController.navigate(NavRoutes.Login.route) {
                popUpTo(0) { inclusive = true }
            }
            viewModel.consumeAuthError()
        }
    }

    LaunchedEffect(uiState.isForbidden) {
        if (uiState.isForbidden) {
            navController.navigate(NavRoutes.AccessDenied.route)
            viewModel.consumeAuthError()
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedTab = when (currentRoute) {
        NavRoutes.Awards.route -> HomeNavTab.AWARDS
        NavRoutes.Kudos.route -> HomeNavTab.KUDOS
        NavRoutes.Profile.route -> HomeNavTab.PROFILE
        else -> HomeNavTab.SAA
    }

    val listState = rememberLazyListState()
    val bgOffset by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo
                .firstOrNull { it.index == 0 }
                ?.offset ?: 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
    ) {
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

        // Scrollable content
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp),
        ) {
            item {
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(144.dp))
            }

            item {
                HeroSection(
                    days = uiState.countdownDays,
                    hours = uiState.countdownHours,
                    minutes = uiState.countdownMinutes,
                    onAboutAwardClick = {
                        navController.navigate(NavRoutes.Awards.route)
                    },
                    onAboutKudosClick = {
                        navController.navigate(NavRoutes.Kudos.route)
                    },
                    modifier = Modifier.padding(top = 20.dp),
                )
            }

            item {
                NoteSection(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 48.dp),
                )
            }

            item {
                AwardsSection(
                    loadState = uiState.awardsLoadState,
                onDetailClick = { award ->
                    navController.navigate(NavRoutes.AwardDetail.createRoute(award.id))
                },
                    onRetry = viewModel::retryLoadAwards,
                    modifier = Modifier.padding(vertical = 48.dp),
                )
            }

            if (uiState.isKudosAvailable) {
                item {
                    KudosSection(
                        onDetailClick = { navController.navigate(NavRoutes.Kudos.route) },
                        modifier = Modifier.padding(vertical = 48.dp),
                    )
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
            onSearchClick = { navController.navigate(NavRoutes.Search.route) },
            onNotificationClick = { navController.navigate(NavRoutes.Notifications.route) },
            modifier = Modifier.align(Alignment.TopStart),
        )

        // Fixed FAB
        HomeFab(
            onWriteKudoClick = {
                viewModel.onFabWriteKudoClicked {
                    navController.navigate(NavRoutes.WriteKudo.createRoute(null, null))
                }
            },
            onKudosFeedClick = {
                viewModel.onFabKudosFeedClicked {
                    navController.navigate(NavRoutes.Kudos.route)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 88.dp),
        )

        // Fixed NavBar
        HomeNavBar(
            selected = selectedTab,
            onTabSelected = { tab ->
                val route = when (tab) {
                    HomeNavTab.SAA -> NavRoutes.Home.route
                    HomeNavTab.AWARDS -> NavRoutes.Awards.route
                    HomeNavTab.KUDOS -> NavRoutes.Kudos.route
                    HomeNavTab.PROFILE -> NavRoutes.Profile.route
                }
                navController.navigate(route) {
                    popUpTo(NavRoutes.Home.route) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier.align(Alignment.BottomStart),
        )
    }
}
