package com.example.saa.presentation.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.saa.R
import com.example.saa.presentation.ui.login.components.LanguageDropdownMenu
import com.example.saa.ui.theme.NotificationBadge
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.homeLanguageLabel

private val headerGradient = Brush.verticalGradient(
    colorStops = arrayOf(
        0.00f to Color(0xFF00101A),
        0.7644f to Color(0x4D00101A),
        0.8462f to Color(0x3300101A),
        0.8870f to Color(0x2600101A),
        0.9279f to Color(0x1A00101A),
        0.9639f to Color(0x0D00101A),
        1.00f to Color.Transparent,
    )
)

@Composable
fun HomeHeader(
    unreadCount: Int,
    selectedLanguage: String,
    showLanguageSelector: Boolean,
    onLanguageClick: () -> Unit,
    onLanguageSelected: (String) -> Unit,
    onLanguageDismiss: () -> Unit,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(headerGradient)
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(60.dp)
            .padding(horizontal = 20.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.img_saa_logo),
            contentDescription = stringResource(R.string.cd_saa_logo),
            modifier = Modifier
                .size(width = 48.dp, height = 44.dp)
                .align(Alignment.CenterStart),
        )

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                LanguageSwitcher(
                    language = selectedLanguage,
                    onClick = onLanguageClick,
                )
                LanguageDropdownMenu(
                    expanded = showLanguageSelector,
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = onLanguageSelected,
                    onDismiss = onLanguageDismiss,
                )
            }
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.size(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.cd_search),
                    tint = TextOnDark,
                    modifier = Modifier.size(24.dp),
                )
            }
            Box {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = stringResource(R.string.cd_notification_bell),
                        tint = TextOnDark,
                        modifier = Modifier.size(24.dp),
                    )
                }
                if (unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .align(Alignment.TopEnd)
                            .clip(CircleShape)
                            .background(NotificationBadge),
                    )
                }
            }
        }
    }
}

private val languageFlagMap = mapOf(
    "VN" to "🇻🇳",
    "EN" to "🇬🇧",
)

@Composable
private fun LanguageSwitcher(
    language: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(start = 8.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = languageFlagMap[language] ?: "🌐")
        Text(
            text = language,
            style = MaterialTheme.typography.homeLanguageLabel,
            color = TextOnDark,
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = TextOnDark,
            modifier = Modifier.size(24.dp),
        )
    }
}

