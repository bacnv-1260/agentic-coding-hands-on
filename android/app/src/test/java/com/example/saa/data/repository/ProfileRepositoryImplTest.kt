package com.example.saa.data.repository

import com.example.saa.data.remote.dto.ProfileDto
import com.example.saa.data.remote.source.ProfileDataSource
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileRepositoryImplTest {

    // ── TC-REPO-PROFILE-001: Data source returns DTO → success ───────────────
    // Covers the happy-path of mms_1.1_member loading from Supabase
    @Test
    fun `getMyProfile - data source returns dto then success with mapped domain`() = runTest {
        val dto = ProfileDto(
            id = "user-1",
            fullName = "Huỳnh Dương Xuân Nhật",
            employeeCode = "CEVC3",
            avatarUrl = "https://example.com/avatar.jpg",
            badgeType = "Legend Hero",
            heroTier = 3,
            departmentId = "dept-1",
        )
        val repo = ProfileRepositoryImpl(FakeProfileDataSource(dto))

        val result = repo.getMyProfile()

        assertTrue(result.isSuccess)
        val profile = result.getOrThrow()
        assertEquals("user-1", profile.id)
        assertEquals("Huỳnh Dương Xuân Nhật", profile.fullName)
        assertEquals("CEVC3", profile.employeeCode)
        assertEquals("https://example.com/avatar.jpg", profile.avatarUrl)
        assertEquals("Legend Hero", profile.badgeType)
        assertEquals(3, profile.heroTier)
    }

    // ── TC-REPO-PROFILE-002: Data source returns null → failure ──────────────
    // Covers the case when user has no profile row (e.g. new account)
    @Test
    fun `getMyProfile - data source returns null then failure with message`() = runTest {
        val repo = ProfileRepositoryImpl(FakeProfileDataSource(null))

        val result = repo.getMyProfile()

        assertTrue(result.isFailure)
        assertEquals("Profile not found", result.exceptionOrNull()?.message)
    }

    // ── TC-REPO-PROFILE-003: Data source throws → failure ────────────────────
    // Covers network / Supabase error
    @Test
    fun `getMyProfile - data source throws then failure is returned`() = runTest {
        val repo = ProfileRepositoryImpl(ThrowingProfileDataSource(RuntimeException("Network error")))

        val result = repo.getMyProfile()

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    // ── TC-REPO-PROFILE-004: Nullable DTO fields map to domain defaults ───────
    @Test
    fun `getMyProfile - dto with all nullable fields maps to domain defaults`() = runTest {
        val dto = ProfileDto(
            id = "user-2",
            fullName = null,
            employeeCode = null,
            avatarUrl = null,
            badgeType = null,
            heroTier = null,
            departmentId = null,
        )
        val repo = ProfileRepositoryImpl(FakeProfileDataSource(dto))

        val result = repo.getMyProfile()

        assertTrue(result.isSuccess)
        val profile = result.getOrThrow()
        assertEquals("", profile.fullName)
        assertEquals("", profile.employeeCode)
        assertNull(profile.avatarUrl)
        assertEquals("", profile.badgeType)
        assertEquals(0, profile.heroTier)
    }
}

// ── Fakes ─────────────────────────────────────────────────────────────────────

private class FakeProfileDataSource(
    private val dto: ProfileDto?,
) : ProfileDataSource(supabase = StubSupabaseClient) {
    override suspend fun getMyProfile(): ProfileDto? = dto
}

private class ThrowingProfileDataSource(
    private val error: Throwable,
) : ProfileDataSource(supabase = StubSupabaseClient) {
    override suspend fun getMyProfile(): ProfileDto? = throw error
}

