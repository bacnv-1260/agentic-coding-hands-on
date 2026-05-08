package com.example.saa

sealed class NavRoutes(val route: String) {
    data object Login : NavRoutes("login")
    data object Home : NavRoutes("home")
    data object AccessDenied : NavRoutes("access_denied")
    data object Awards : NavRoutes("awards")
    data object AwardDetail : NavRoutes("awards/{awardId}") {
        fun createRoute(awardId: String) = "awards/$awardId"
    }
    data object Kudos : NavRoutes("kudos")
    data object AllKudos : NavRoutes("all_kudos")
    data object KudoDetail : NavRoutes("kudo/{kudosId}") {
        fun createRoute(kudosId: String) = "kudo/$kudosId"
    }
    data object Profile : NavRoutes("profile")
    data object WriteKudo : NavRoutes("write_kudo?recipientId={recipientId}&recipientName={recipientName}") {
        fun createRoute(recipientId: String?, recipientName: String?): String {
            val params = buildList {
                if (recipientId != null) add("recipientId=${android.net.Uri.encode(recipientId)}")
                if (recipientName != null) add("recipientName=${android.net.Uri.encode(recipientName)}")
            }
            return if (params.isEmpty()) "write_kudo" else "write_kudo?${params.joinToString("&")}"
        }
    }
    data object Notifications : NavRoutes("notifications")
    data object LanguageSelector : NavRoutes("language_selector")
    data object Search : NavRoutes("search")
    data object CommunityStandards : NavRoutes("community_standards")
}
