package com.example.saa.data.repository

import com.example.saa.data.remote.dto.DepartmentDto
import com.example.saa.data.remote.dto.GiftRecipientDto
import com.example.saa.data.remote.dto.HashtagDto
import com.example.saa.data.remote.dto.UserStatsDto
import com.example.saa.data.remote.source.UserStatsDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserStatsRepositoryImplTest {

    // ── TC-REPO-STATS-001: getUserStats success ───────────────────────────────
    // mms_D.1.2~D.1.7: all stats loaded and mapped correctly
    @Test
    fun `getUserStats - data source returns dto then success with mapped domain`() = runTest {
        val dto = UserStatsDto(
            id = "user-1",
            kudosReceived = 5,
            kudosSent = 25,
            heartsReceived = 25,
            secretBoxesOpened = 25,
            secretBoxesUnopened = 3,
        )
        val repo = UserStatsRepositoryImpl(FakeUserStatsDataSource(statsDto = dto))

        val result = repo.getUserStats()

        assertTrue(result.isSuccess)
        val stats = result.getOrThrow()
        assertEquals(5, stats.kudosReceived)
        assertEquals(25, stats.kudosSent)
        assertEquals(25, stats.heartsReceived)
        assertEquals(25, stats.secretBoxesOpened)
        assertEquals(3, stats.secretBoxesUnopened)
    }

    // ── TC-REPO-STATS-002: getUserStats all nulls → all zeros ─────────────────
    // mms_D.1.7: secretBoxesUnopened = 0 disables Open Secret Box button
    @Test
    fun `getUserStats - dto with all null counts maps to all zeros`() = runTest {
        val dto = UserStatsDto(
            id = "user-2",
            kudosReceived = null,
            kudosSent = null,
            heartsReceived = null,
            secretBoxesOpened = null,
            secretBoxesUnopened = null,
        )
        val repo = UserStatsRepositoryImpl(FakeUserStatsDataSource(statsDto = dto))

        val result = repo.getUserStats()

        assertTrue(result.isSuccess)
        val stats = result.getOrThrow()
        assertEquals(0, stats.kudosReceived)
        assertEquals(0, stats.kudosSent)
        assertEquals(0, stats.heartsReceived)
        assertEquals(0, stats.secretBoxesOpened)
        assertEquals(0, stats.secretBoxesUnopened)
    }

    // ── TC-REPO-STATS-003: getUserStats throws → failure ─────────────────────
    @Test
    fun `getUserStats - data source throws then failure is returned`() = runTest {
        val repo = UserStatsRepositoryImpl(
            ThrowingUserStatsDataSource(RuntimeException("Supabase error")),
        )

        val result = repo.getUserStats()

        assertTrue(result.isFailure)
        assertEquals("Supabase error", result.exceptionOrNull()?.message)
    }

    // ── TC-REPO-STATS-004: secretBoxesUnopened > 0 enables button ─────────────
    // mms_D.1.7: if secretBoxesUnopened > 0 the Open Secret Box button is enabled
    @Test
    fun `getUserStats - secretBoxesUnopened positive allows opening secret box`() = runTest {
        val dto = UserStatsDto(
            id = "user-3",
            kudosReceived = 0,
            kudosSent = 0,
            heartsReceived = 0,
            secretBoxesOpened = 0,
            secretBoxesUnopened = 1,
        )
        val repo = UserStatsRepositoryImpl(FakeUserStatsDataSource(statsDto = dto))

        val stats = repo.getUserStats().getOrThrow()
        assertTrue(stats.secretBoxesUnopened > 0)
    }
}

// ── Fakes ─────────────────────────────────────────────────────────────────────

private class FakeUserStatsDataSource(
    private val statsDto: UserStatsDto,
) : UserStatsDataSource(supabase = StubSupabaseClient) {
    override suspend fun getUserStats(): UserStatsDto = statsDto
}

private class ThrowingUserStatsDataSource(
    private val error: Throwable,
) : UserStatsDataSource(supabase = StubSupabaseClient) {
    override suspend fun getUserStats(): UserStatsDto = throw error
}
