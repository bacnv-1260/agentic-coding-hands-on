package com.example.saa.data.repository

import com.example.saa.data.remote.dto.ProfileDto
import com.example.saa.data.remote.source.ProfileDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

// ── Design mapping ────────────────────────────────────────────────────────────
// B.2 Recipient dropdown search → ProfileRepository.searchProfiles(query)
// ─────────────────────────────────────────────────────────────────────────────

class ProfileRepositoryImplSearchTest {

    // ── TC-REPO-PROFILE-SEARCH-001: Data source returns profiles ─────────────
    @Test
    fun `searchProfiles - data source returns dtos then success with mapped domain`() = runTest {
        val dtos = listOf(
            ProfileDto(
                id = "u1",
                fullName = "Nguyen Van A",
                employeeCode = "EMP001",
                avatarUrl = "https://example.com/a.jpg",
                badgeType = "Legend",
                heroTier = 3,
                departmentId = "d1",
            ),
            ProfileDto(
                id = "u2",
                fullName = "Alice B",
                employeeCode = "EMP002",
                avatarUrl = null,
                badgeType = "",
                heroTier = 0,
                departmentId = null,
            ),
        )
        val repo = ProfileRepositoryImpl(SearchFakeProfileDataSource(dtos))

        val result = repo.searchProfiles("Al")

        assertTrue(result.isSuccess)
        val profiles = result.getOrThrow()
        assertEquals(2, profiles.size)
        assertEquals("u1", profiles[0].id)
        assertEquals("Nguyen Van A", profiles[0].fullName)
        assertEquals("u2", profiles[1].id)
    }

    // ── TC-REPO-PROFILE-SEARCH-002: Data source returns empty list ───────────
    @Test
    fun `searchProfiles - no matching profiles returns empty success list`() = runTest {
        val repo = ProfileRepositoryImpl(SearchFakeProfileDataSource(emptyList()))

        val result = repo.searchProfiles("xyz")

        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrThrow().size)
    }

    // ── TC-REPO-PROFILE-SEARCH-003: Data source throws → failure ─────────────
    @Test
    fun `searchProfiles - data source throws then failure is returned`() = runTest {
        val repo = ProfileRepositoryImpl(SearchThrowingProfileDataSource(RuntimeException("Network error")))

        val result = repo.searchProfiles("query")

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}

// ── Fakes ─────────────────────────────────────────────────────────────────────

private class SearchFakeProfileDataSource(
    private val profiles: List<ProfileDto>,
) : ProfileDataSource(supabase = StubSupabaseClient) {
    override suspend fun searchProfiles(query: String): List<ProfileDto> = profiles
}

private class SearchThrowingProfileDataSource(
    private val error: Throwable,
) : ProfileDataSource(supabase = StubSupabaseClient) {
    override suspend fun searchProfiles(query: String): List<ProfileDto> = throw error
}
