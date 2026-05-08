package com.example.saa.presentation.ui.kudos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.saa.R
import com.example.saa.presentation.ui.home.components.HomeHeader
import com.example.saa.presentation.ui.home.components.HomeNavBar
import com.example.saa.presentation.ui.home.components.HomeNavTab
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.saa.presentation.ui.kudos.components.GiftRecipientsList
import com.example.saa.presentation.ui.kudos.components.KudosFilterRow
import com.example.saa.presentation.ui.kudos.components.KudosHighlightCarousel
import com.example.saa.presentation.ui.kudos.components.KudosKVBanner
import com.example.saa.presentation.ui.kudos.components.KudosPagination
import com.example.saa.presentation.ui.kudos.components.KudosHighlightCard
import com.example.saa.presentation.ui.kudos.components.PersonalStatsGrid
import com.example.saa.presentation.ui.kudos.components.SpotlightBoardSection
import com.example.saa.presentation.ui.kudos.components.WriteKudosButton
import com.example.saa.presentation.ui.home.components.HomeHeader
import com.example.saa.ui.theme.Background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.KudosGold
import com.example.saa.ui.theme.TextOnDark

@Composable
fun KudosScreen(
    onNavigateToWriteKudo: () -> Unit,
    onNavigateToAllKudos: () -> Unit,
    onNavigateToKudoDetail: (String) -> Unit,
    onNavigateToProfile: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToTab: (HomeNavTab) -> Unit = {},
    viewModel: KudosViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    val bgOffset by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo
                .firstOrNull { it.index == 0 }
                ?.offset ?: 0
        }
    }

    // Redirect on auth expiry
    LaunchedEffect(uiState.isUnauthenticated) {
        if (uiState.isUnauthenticated) {
            onNavigateToLogin()
            viewModel.consumeAuthError()
        }
    }

    // Show error as snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.consumeError()
        }
    }

    // Sticky button visibility: show when first visible item index > 0 (scrolled past banner)
    val showStickyButton by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    // Load more when near the bottom
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = layoutInfo.totalItemsCount
            lastVisible >= total - 3 && !uiState.isLoadingMore && uiState.hasMoreKudos
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) viewModel.loadMoreKudos()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
    ) {
        // Full-screen keyvisual background — scrolls and fades to black
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

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 80.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            // ── Section A: KV Banner ──────────────────────────────────
            item(key = "kv_banner") {
                KudosKVBanner()
            }
            item(key = "write_kudos_default") {
                Spacer(modifier = Modifier.height(8.dp))
                WriteKudosButton(onClick = onNavigateToWriteKudo)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Section B: Highlight Kudos ────────────────────────────
            item(key = "highlight_header") {
                Text(
                    text = "Sun* Annual Awards 2025",
                    color = KudosGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Text(
                    text = "HIGHLIGHT KUDOS",
                    color = KudosGold,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item(key = "filter_row") {
                KudosFilterRow(
                    hashtags = uiState.hashtags,
                    departments = uiState.departments,
                    selectedHashtagId = uiState.selectedHashtagId,
                    selectedDepartmentId = uiState.selectedDepartmentId,
                    onHashtagSelected = viewModel::onFilterHashtag,
                    onDepartmentSelected = viewModel::onFilterDepartment,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (uiState.highlightKudos.isNotEmpty()) {
                item(key = "highlight_carousel") {
                    KudosHighlightCarousel(
                        kudosList = uiState.highlightKudos,
                        currentPage = uiState.currentCarouselPage,
                        onPageChange = viewModel::onCarouselPageChange,
                        onSenderClick = onNavigateToProfile,
                        onRecipientClick = onNavigateToProfile,
                        onLikeToggle = viewModel::onToggleLike,
                        onCopyLink = { url ->
                            clipboardManager.setText(AnnotatedString(url))
                        },
                        onViewDetail = onNavigateToKudoDetail,
                        onHashtagClick = { tag -> viewModel.onFilterHashtag(tag) },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item(key = "pagination") {
                    KudosPagination(
                        currentPage = uiState.currentCarouselPage,
                        total = uiState.highlightKudos.size,
                        onPrev = {
                            if (uiState.currentCarouselPage > 0) {
                                viewModel.onCarouselPageChange(uiState.currentCarouselPage - 1)
                            }
                        },
                        onNext = {
                            if (uiState.currentCarouselPage < uiState.highlightKudos.size - 1) {
                                viewModel.onCarouselPageChange(uiState.currentCarouselPage + 1)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // ── Section B.6/B.7: Spotlight Board ─────────────────────
            item(key = "spotlight_board") {
                SpotlightBoardSection(
                    totalKudos = uiState.allKudos.size,
                    sunnerNames = uiState.allKudos.map { it.recipientName }.distinct(),
                    onSunnerClick = onNavigateToProfile,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Section C header + D.1/D.2/D.3 ──────────────────────
            item(key = "all_kudos_header") {
                Text(
                    text = "Sun* Annual Awards 2025",
                    color = KudosGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Text(
                    text = "ALL KUDOS",
                    color = KudosGold,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // ── Section D.1: Personal Stats ────────────────────────────
            uiState.userStats?.let { stats ->
                item(key = "personal_stats") {
                    // D.2 button is INSIDE PersonalStatsGrid container per Figma
                    PersonalStatsGrid(
                        stats = stats,
                        onOpenSecretBox = viewModel::onOpenSecretBox,
                        secretBoxEnabled = stats.secretBoxesUnopened > 0,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // ── Section D.3: Gift Recipients ───────────────────────────
            if (uiState.giftRecipients.isNotEmpty()) {
                item(key = "gift_recipients") {
                    GiftRecipientsList(
                        recipients = uiState.giftRecipients,
                        onClickRow = onNavigateToProfile,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // ── Section C.3: All Kudos Feed ───────────────────────────
            if (uiState.isLoading && uiState.allKudos.isEmpty()) {
                item(key = "loading") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = ButtonPrimaryBg)
                    }
                }
            } else if (uiState.allKudos.isEmpty() && !uiState.isLoading) {
                item(key = "empty_state") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Chưa có Kudos nào",
                            color = TextOnDark.copy(alpha = 0.5f),
                            fontSize = 14.sp,
                        )
                    }
                }
            } else {
                items(uiState.allKudos, key = { it.id }) { kudos ->
                    KudosHighlightCard(
                        kudos = kudos,
                        onSenderClick = onNavigateToProfile,
                        onRecipientClick = onNavigateToProfile,
                        onLikeToggle = viewModel::onToggleLike,
                        onCopyLink = { url ->
                            clipboardManager.setText(AnnotatedString(url))
                        },
                        onViewDetail = onNavigateToKudoDetail,
                        onHashtagClick = {},
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    )
                }
            }

            // View all link (C.2)
            if (uiState.allKudos.isNotEmpty()) {
                item(key = "view_all_link") {
                    TextButton(
                        onClick = onNavigateToAllKudos,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        Text(
                            text = "View all Kudos ↗",
                            color = KudosGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline,
                        )
                    }
                }
            }

            // Load more indicator
            if (uiState.isLoadingMore) {
                item(key = "load_more") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            color = ButtonPrimaryBg,
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                }
            }

            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // ── Fixed overlay: Top navigation header ─────────────────────
        HomeHeader(
            unreadCount = uiState.unreadNotificationCount,
            selectedLanguage = uiState.selectedLanguage,
            showLanguageSelector = uiState.showLanguageSelector,
            onLanguageClick = viewModel::showLanguageSelector,
            onLanguageSelected = viewModel::setLanguage,
            onLanguageDismiss = viewModel::dismissLanguageSelector,
            onSearchClick = {},
            onNotificationClick = {},
            modifier = Modifier.align(Alignment.TopStart),
        )

        // ── Fixed bottom nav bar ──────────────────────────────────────
        HomeNavBar(
            selected = HomeNavTab.KUDOS,
            onTabSelected = onNavigateToTab,
            modifier = Modifier.align(Alignment.BottomStart),
        )

        // Sticky WriteKudos button overlay (shown after user scrolls past banner)
        if (showStickyButton) {
            WriteKudosButton(
                onClick = onNavigateToWriteKudo,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(
                        top = 64.dp, // offset below the header bar
                        start = 0.dp,
                        end = 0.dp,
                        bottom = 0.dp,
                    ),
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        ) { data ->
            Snackbar(snackbarData = data)
        }
    }
}
