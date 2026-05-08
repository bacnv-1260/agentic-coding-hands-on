package com.example.saa.presentation.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.saa.R
import com.example.saa.presentation.ui.home.components.HomeHeader
import com.example.saa.presentation.ui.home.components.HomeNavBar
import com.example.saa.presentation.ui.home.components.HomeNavTab
import com.example.saa.presentation.ui.home.components.SectionHeader
import com.example.saa.presentation.ui.kudos.components.KudosHighlightCard
import com.example.saa.presentation.ui.profile.components.IconCollectionSection
import com.example.saa.presentation.ui.profile.components.KudosFilterDropdown
import com.example.saa.presentation.ui.profile.components.ProfileInfoSection
import com.example.saa.presentation.ui.profile.components.ProfileStatsContainer
import com.example.saa.ui.theme.Background
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onNavigateToTab: (HomeNavTab) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToKudoDetail: (kudosId: String) -> Unit,
    onNavigateToSecretBox: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
    ) {
        // Keyvisual background
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

        // Content
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(top = 104.dp, bottom = 80.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            // Profile info section
            item(key = "profile_info") {
                Spacer(modifier = Modifier.height(16.dp))
                ProfileInfoSection(profile = uiState.profile)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Icon collection section
            item(key = "icon_collection") {
                IconCollectionSection(profile = uiState.profile)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Stats container
            item(key = "stats") {
                ProfileStatsContainer(
                    stats = uiState.stats,
                    isLoading = uiState.isLoading,
                    onOpenSecretBox = onNavigateToSecretBox,
                    onRetry = viewModel::loadData,
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Kudos section header
            item(key = "kudos_header") {
                SectionHeader(
                    label = "Sun* Annual Awards 2025",
                    title = "KUDOS",
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Filter dropdown
            item(key = "kudos_filter") {
                KudosFilterDropdown(
                    selectedFilter = uiState.kudosFilter,
                    sentCount = uiState.stats?.kudosSent ?: 0,
                    receivedCount = uiState.stats?.kudosReceived ?: 0,
                    isOpen = uiState.isDropdownOpen,
                    onToggle = viewModel::onDropdownToggle,
                    onDismiss = viewModel::onDropdownDismiss,
                    onFilterChange = viewModel::onFilterChange,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Kudos list or empty state
            if (uiState.kudosList.isEmpty() && !uiState.isLoading) {
                item(key = "kudos_empty") {
                    Text(
                        text = "Chưa có Kudos",
                        color = Color(0x99FFFFFF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                    )
                }
            } else {
                items(
                    items = uiState.kudosList,
                    key = { it.id },
                ) { kudos ->
                    KudosHighlightCard(
                        kudos = kudos,
                        onSenderClick = {},
                        onRecipientClick = {},
                        onLikeToggle = {},
                        onCopyLink = { url ->
                            clipboardManager.setText(AnnotatedString(url))
                        },
                        onViewDetail = { id -> onNavigateToKudoDetail(id) },
                        onHashtagClick = { tag -> onNavigateToSearch() },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Fixed header
        HomeHeader(
            unreadCount = uiState.unreadNotificationCount,
            selectedLanguage = uiState.selectedLanguage,
            showLanguageSelector = uiState.showLanguageSelector,
            onLanguageClick = viewModel::onLanguageClick,
            onLanguageSelected = { code -> viewModel.setLanguage(code) },
            onLanguageDismiss = viewModel::onLanguageDismiss,
            onSearchClick = onNavigateToSearch,
            onNotificationClick = onNavigateToNotifications,
            modifier = Modifier.align(Alignment.TopStart),
        )

        // Fixed bottom nav
        HomeNavBar(
            selected = HomeNavTab.PROFILE,
            onTabSelected = { tab ->
                if (tab == HomeNavTab.PROFILE) {
                    coroutineScope.launch { listState.animateScrollToItem(0) }
                } else {
                    onNavigateToTab(tab)
                }
            },
            modifier = Modifier.align(Alignment.BottomStart),
        )

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
        ) { data ->
            Snackbar(snackbarData = data)
        }
    }
}
