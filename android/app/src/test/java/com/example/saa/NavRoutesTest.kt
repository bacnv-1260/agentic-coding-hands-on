package com.example.saa

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for NavRoutes route string definitions.
 *
 * Screen: [iOS] Sun*Kudos_Tiêu chuẩn cộng đồng (xms7csmDhD)
 *
 * TR-002: Navigation uses NavRoutes.CommunityStandards.route — these tests guard
 * against accidental route renames or typos that would break navigation at runtime.
 */
class NavRoutesTest {

    // ── CommunityStandards route ───────────────────────────────────────────────

    /**
     * Verify the route string value is exactly "community_standards".
     * SaaNavHost.kt registers `composable(NavRoutes.CommunityStandards.route)` and
     * WriteKudoScreen calls `navController.navigate(NavRoutes.CommunityStandards.route)`.
     * Any mismatch → silent runtime navigation failure (no compile error).
     */
    @Test
    fun `CommunityStandards route is community_standards`() {
        assertEquals("community_standards", NavRoutes.CommunityStandards.route)
    }

    /**
     * Verify route does not start with '/'.
     * Jetpack Navigation Compose routes must NOT begin with '/' — doing so causes
     * IllegalArgumentException at runtime when building the NavGraph.
     */
    @Test
    fun `CommunityStandards route does not start with slash`() {
        assertFalse(
            "Route must not start with '/'",
            NavRoutes.CommunityStandards.route.startsWith("/"),
        )
    }

    /**
     * Verify route does not contain spaces.
     * Spaces in a route string cause NavController to fail parsing the destination URI.
     */
    @Test
    fun `CommunityStandards route contains no spaces`() {
        assertFalse(
            "Route must not contain spaces",
            NavRoutes.CommunityStandards.route.contains(" "),
        )
    }

    // ── Route uniqueness ──────────────────────────────────────────────────────

    /**
     * Verify all base route strings in NavRoutes are unique.
     * Duplicate routes cause the NavGraph to silently ignore the second registration,
     * leading to incorrect navigation targets.
     *
     * Covers: NavRoutes.CommunityStandards uniqueness as part of regression suite.
     */
    @Test
    fun `all NavRoutes base routes are unique`() {
        val allRoutes = listOf(
            NavRoutes.Login.route,
            NavRoutes.Home.route,
            NavRoutes.AccessDenied.route,
            NavRoutes.Awards.route,
            NavRoutes.AwardDetail.route,
            NavRoutes.Kudos.route,
            NavRoutes.AllKudos.route,
            NavRoutes.KudoDetail.route,
            NavRoutes.Profile.route,
            NavRoutes.WriteKudo.route,
            NavRoutes.Notifications.route,
            NavRoutes.LanguageSelector.route,
            NavRoutes.Search.route,
            NavRoutes.CommunityStandards.route,
        )

        val duplicates = allRoutes.groupBy { it }.filter { it.value.size > 1 }.keys
        assertTrue(
            "Duplicate routes found: $duplicates",
            duplicates.isEmpty(),
        )
    }

    /**
     * Verify CommunityStandards route does not collide with any other existing route.
     * Targeted check so failures clearly identify the offending pair.
     */
    @Test
    fun `CommunityStandards route does not collide with existing routes`() {
        val otherRoutes = mapOf(
            "Login" to NavRoutes.Login.route,
            "Home" to NavRoutes.Home.route,
            "AccessDenied" to NavRoutes.AccessDenied.route,
            "Awards" to NavRoutes.Awards.route,
            "AwardDetail" to NavRoutes.AwardDetail.route,
            "Kudos" to NavRoutes.Kudos.route,
            "AllKudos" to NavRoutes.AllKudos.route,
            "KudoDetail" to NavRoutes.KudoDetail.route,
            "Profile" to NavRoutes.Profile.route,
            "WriteKudo" to NavRoutes.WriteKudo.route,
            "Notifications" to NavRoutes.Notifications.route,
            "LanguageSelector" to NavRoutes.LanguageSelector.route,
            "Search" to NavRoutes.Search.route,
        )

        val communityRoute = NavRoutes.CommunityStandards.route
        otherRoutes.forEach { (name, route) ->
            assertFalse(
                "CommunityStandards route '$communityRoute' collides with $name route '$route'",
                communityRoute == route,
            )
        }
    }

    // ── WriteKudo route unaffected ────────────────────────────────────────────

    /**
     * Verify WriteKudo route template is unchanged after CommunityStandards was added.
     * WriteKudoScreen now has an extra parameter (onNavigateToCommunityStandards) but the
     * route template itself must remain stable so existing deep-links / shortcuts still work.
     */
    @Test
    fun `WriteKudo route template is unchanged`() {
        assertEquals(
            "write_kudo?recipientId={recipientId}&recipientName={recipientName}",
            NavRoutes.WriteKudo.route,
        )
    }

    /**
     * Verify WriteKudo.createRoute with null args still returns bare "write_kudo".
     * SaaNavHost navigates to WriteKudo.createRoute(null, null) from multiple places.
     */
    @Test
    fun `WriteKudo createRoute with nulls returns bare write_kudo`() {
        assertEquals("write_kudo", NavRoutes.WriteKudo.createRoute(null, null))
    }

    // NOTE: WriteKudo.createRoute(recipientId, null) calls android.net.Uri.encode()
    // which is not available in JVM unit tests (Android SDK stub). That path is
    // covered by instrumented tests or manual testing (TC_COMMUNITY_STANDARDS_FUN_003).
}
