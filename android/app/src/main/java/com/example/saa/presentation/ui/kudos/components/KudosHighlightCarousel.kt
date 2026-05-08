package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saa.domain.model.Kudos
import com.example.saa.ui.theme.KudosGold
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KudosHighlightCarousel(
    kudosList: List<Kudos>,
    currentPage: Int,
    onPageChange: (Int) -> Unit,
    onSenderClick: (String) -> Unit,
    onRecipientClick: (String) -> Unit,
    onLikeToggle: (String) -> Unit,
    onCopyLink: (String) -> Unit,
    onViewDetail: (String) -> Unit,
    onHashtagClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (kudosList.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = currentPage,
        pageCount = { kudosList.size },
    )

    // Sync external currentPage to pager
    LaunchedEffect(currentPage) {
        if (pagerState.currentPage != currentPage) {
            pagerState.animateScrollToPage(currentPage)
        }
    }

    // Report page changes back to ViewModel
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onPageChange(page)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "HIGHLIGHT KUDOS",
            color = KudosGold,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )

        Box {
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fixed(311.dp),
                beyondViewportPageCount = 1,
                contentPadding = PaddingValues(horizontal = 32.dp),
                pageSpacing = 8.dp,
                modifier = Modifier.fillMaxWidth(),
            ) { page ->
                val pageOffset = pagerState.currentPage - page +
                    pagerState.currentPageOffsetFraction
                val scale = 1f - 0.05f * abs(pageOffset).coerceIn(0f, 1f)
                val alpha = 1f - 0.6f * abs(pageOffset).coerceIn(0f, 1f)

                KudosHighlightCard(
                    kudos = kudosList[page],
                    onSenderClick = onSenderClick,
                    onRecipientClick = onRecipientClick,
                    onLikeToggle = onLikeToggle,
                    onCopyLink = onCopyLink,
                    onViewDetail = onViewDetail,
                    onHashtagClick = onHashtagClick,
                    modifier = Modifier
                        .scale(scale)
                        .alpha(alpha),
                )
            }
        }
    }
}
