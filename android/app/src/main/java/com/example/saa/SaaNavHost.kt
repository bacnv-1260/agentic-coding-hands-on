package com.example.saa

import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.saa.presentation.AppViewModel
import com.example.saa.presentation.AuthState
import com.example.saa.presentation.ui.access_denied.AccessDeniedScreen
import com.example.saa.presentation.ui.award.AwardDetailScreen
import com.example.saa.presentation.ui.home.HomeScreen
import com.example.saa.presentation.ui.home.components.HomeNavTab
import com.example.saa.presentation.ui.kudos.KudosScreen
import com.example.saa.presentation.ui.login.LoginScreen
import com.example.saa.presentation.ui.profile.ProfileScreen
import com.example.saa.presentation.ui.writekudo.WriteKudoScreen
import com.example.saa.presentation.ui.community_standards.CommunityStandardsScreen
import com.example.saa.ui.theme.SaaTheme
import java.util.Locale

/**
 * Wraps the Activity context so that:
 * - getResources() returns locale-specific strings (fixes stringResource recomposition)
 * - ContextWrapper chain still contains the Activity (fixes HiltViewModelFactory lookup)
 */
private class LocalizedContextWrapper(
    base: android.content.Context,
    locale: Locale,
) : ContextWrapper(base) {
    private val localizedResources: Resources = run {
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        base.createConfigurationContext(config).resources
    }
    override fun getResources(): Resources = localizedResources
}

@Composable
fun SaaNavHost(
    appViewModel: AppViewModel = hiltViewModel(),
) {
    val selectedLanguage by appViewModel.selectedLanguage.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val locale = remember(selectedLanguage) {
        when (selectedLanguage) {
            "EN" -> Locale.ENGLISH
            else -> Locale("vi")
        }
    }
    val localizedContext = remember(context, locale) {
        LocalizedContextWrapper(context, locale)
    }

    CompositionLocalProvider(LocalContext provides localizedContext) {
        SaaTheme {
            val navController = rememberNavController()
            val authState by appViewModel.authState.collectAsStateWithLifecycle()

            // Stay on Splash (Login used as placeholder) until session check completes,
            // then navigate to the correct destination without back stack.
            LaunchedEffect(authState) {
                when (authState) {
                    AuthState.LoggedIn -> navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                    AuthState.LoggedOut -> { /* already on Login */ }
                    AuthState.Loading -> { /* wait */ }
                }
            }

            NavHost(
                navController = navController,
                startDestination = NavRoutes.Login.route,
            ) {
                composable(NavRoutes.Login.route) {
                    LoginScreen(navController = navController)
                }
                composable(NavRoutes.Home.route) {
                    HomeScreen(navController = navController)
                }
                composable(NavRoutes.AccessDenied.route) {
                    AccessDeniedScreen(navController = navController)
                }
                composable(NavRoutes.Awards.route) {
                    AwardDetailScreen(navController = navController)
                }
                composable(
                    route = NavRoutes.AwardDetail.route,
                    arguments = listOf(navArgument("awardId") { type = NavType.StringType }),
                ) {
                    AwardDetailScreen(navController = navController)
                }
                composable(NavRoutes.Kudos.route) {
                    KudosScreen(
                        onNavigateToWriteKudo = {
                            navController.navigate(NavRoutes.WriteKudo.createRoute(null, null))
                        },
                        onNavigateToAllKudos = {
                            navController.navigate(NavRoutes.AllKudos.route)
                        },
                        onNavigateToKudoDetail = { kudosId ->
                            navController.navigate(NavRoutes.KudoDetail.createRoute(kudosId))
                        },
                        onNavigateToProfile = { userId ->
                            navController.navigate("${NavRoutes.Profile.route}/$userId")
                        },
                        onNavigateToLogin = {
                            navController.navigate(NavRoutes.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToTab = { tab ->
                            when (tab) {
                                HomeNavTab.KUDOS -> Unit // already on Kudos
                                HomeNavTab.SAA -> navController.navigate(NavRoutes.Home.route) {
                                    popUpTo(NavRoutes.Kudos.route) { inclusive = true }
                                }
                                HomeNavTab.AWARDS -> navController.navigate(NavRoutes.Awards.route) {
                                    popUpTo(NavRoutes.Kudos.route) { inclusive = true }
                                }
                                HomeNavTab.PROFILE -> navController.navigate(NavRoutes.Profile.route) {
                                    popUpTo(NavRoutes.Kudos.route) { inclusive = true }
                                }
                            }
                        },
                    )
                }
                composable(NavRoutes.Profile.route) {
                    ProfileScreen(
                        onNavigateToTab = { tab ->
                            when (tab) {
                                HomeNavTab.PROFILE -> Unit // handled inside ProfileScreen (scroll-to-top)
                                HomeNavTab.SAA -> navController.navigate(NavRoutes.Home.route) {
                                    popUpTo(NavRoutes.Profile.route) { inclusive = true }
                                }
                                HomeNavTab.AWARDS -> navController.navigate(NavRoutes.Awards.route) {
                                    popUpTo(NavRoutes.Profile.route) { inclusive = true }
                                }
                                HomeNavTab.KUDOS -> navController.navigate(NavRoutes.Kudos.route) {
                                    popUpTo(NavRoutes.Profile.route) { inclusive = true }
                                }
                            }
                        },
                        onNavigateToLogin = {
                            navController.navigate(NavRoutes.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToKudoDetail = { kudosId ->
                            navController.navigate(NavRoutes.KudoDetail.createRoute(kudosId))
                        },
                        onNavigateToSecretBox = {
                            navController.navigate(NavRoutes.WriteKudo.createRoute(null, null))
                        },
                        onNavigateToNotifications = {
                            navController.navigate(NavRoutes.Notifications.route)
                        },
                        onNavigateToSearch = {
                            navController.navigate(NavRoutes.Search.route)
                        },
                    )
                }
                composable(
                    route = NavRoutes.WriteKudo.route,
                    arguments = listOf(
                        navArgument("recipientId") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        },
                        navArgument("recipientName") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        },
                    ),
                ) { backStackEntry ->
                    val recipientId = backStackEntry.arguments?.getString("recipientId")
                    val recipientName = backStackEntry.arguments?.getString("recipientName")
                    WriteKudoScreen(
                        recipientId = recipientId,
                        recipientName = recipientName,
                        onNavigateBack = { navController.navigateUp() },
                        onNavigateToCommunityStandards = {
                            navController.navigate(NavRoutes.CommunityStandards.route)
                        },
                    )
                }
                composable(NavRoutes.CommunityStandards.route) {
                    CommunityStandardsScreen(
                        onNavigateBack = { navController.navigateUp() },
                    )
                }
                composable(NavRoutes.Notifications.route) {
                    Surface { Text("Notifications") }
                }
                composable(NavRoutes.LanguageSelector.route) {
                    Surface { Text("Language Selector") }
                }
                composable(NavRoutes.Search.route) {
                    Surface { Text("Search") }
                }
                composable(NavRoutes.AllKudos.route) {
                    Surface { Text("All Kudos") }
                }
                composable(
                    route = NavRoutes.KudoDetail.route,
                    arguments = listOf(navArgument("kudosId") { type = NavType.StringType }),
                ) {
                    Surface { Text("Kudo Detail") }
                }
            }
        }
    }
}
