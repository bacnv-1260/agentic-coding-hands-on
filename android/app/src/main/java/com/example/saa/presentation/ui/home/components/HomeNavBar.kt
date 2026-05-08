package com.example.saa.presentation.ui.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.saa.R
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.homeSectionLabel

enum class HomeNavTab { SAA, AWARDS, KUDOS, PROFILE }

private data class NavTabConfig(
    val tab: HomeNavTab,
    val labelRes: Int,
    val icon: ImageVector,
)

private val navTabs = listOf(
    NavTabConfig(HomeNavTab.SAA, R.string.home_nav_saa, Icons.Default.Home),
    NavTabConfig(HomeNavTab.AWARDS, R.string.home_nav_awards, Icons.Default.Star),
    NavTabConfig(HomeNavTab.KUDOS, R.string.home_nav_kudos, Icons.Default.ThumbUp),
    NavTabConfig(HomeNavTab.PROFILE, R.string.home_nav_profile, Icons.Default.AccountCircle),
)

@Composable
fun HomeNavBar(
    selected: HomeNavTab,
    onTabSelected: (HomeNavTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        containerColor = Color(0xFF001828),
        tonalElevation = 0.dp,
    ) {
        navTabs.forEach { config ->
            val isSelected = selected == config.tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(config.tab) },
                icon = {
                    Icon(
                        imageVector = config.icon,
                        contentDescription = stringResource(config.labelRes),
                    )
                },
                label = {
                    Text(
                        text = stringResource(config.labelRes),
                        style = MaterialTheme.typography.homeSectionLabel,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ButtonPrimaryBg,
                    selectedTextColor = ButtonPrimaryBg,
                    unselectedIconColor = TextOnDark.copy(alpha = 0.6f),
                    unselectedTextColor = TextOnDark.copy(alpha = 0.6f),
                    indicatorColor = Color.Transparent,
                ),
            )
        }
    }
}

