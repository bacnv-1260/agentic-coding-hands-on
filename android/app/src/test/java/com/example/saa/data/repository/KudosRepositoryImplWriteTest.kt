package com.example.saa.data.repository

import com.example.saa.data.remote.dto.ProfileDto
import com.example.saa.data.remote.dto.WriteKudoDto
import com.example.saa.data.remote.source.KudosDataSource
import com.example.saa.data.remote.source.ProfileDataSource
import com.example.saa.domain.model.KudosFilter
import com.example.saa.domain.model.WriteKudoRequest
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// ── Design mapping ────────────────────────────────────────────────────────────
// I  Send Button → KudosRepositoryImpl.submitKudo orchestrates data layer
// G  Anonymous   → anonymousNickname "Ẩn danh" when isAnonymous = true
// ─────────────────────────────────────────────────────────────────────────────

class KudosRepositoryImplWriteTest {

    // ── TC-REPO-KUDOS-WRITE-001: Happy path ───────────────────────────────────
    @Test
    fun `submitKudo - inserts kudo and hashtags returns generated id`() = runTest {
        val profileDataSource = StubProfileDataSource(
            dto = ProfileDto(
                id = "sender-1",
                fullName = "Nguyen Van B",
                employeeCode = "EMP002",
                avatarUrl = "https://example.com/avatar.jpg",
                badgeType = "Hero",
                heroTier = 2,
                departmentId = "dept-1",
            ),
        )
        val kudosDataSource = StubInsertKudosDataSource(insertedId = "kudo-abc")
        val repo = KudosRepositoryImpl(kudosDataSource, profileDataSource)

        val result = repo.submitKudo(
            WriteKudoRequest(
                recipientId = "r1",
                title = "Best Dev",
                message = "<p>Well done!</p>",
                hashtagIds = listOf("h1", "h2"),
                photoUrls = listOf("https://img.example.com/1.jpg"),
                isAnonymous = false,
            ),
        )

        assertTrue(result.isSuccess)
        assertEquals("kudo-abc", result.getOrThrow())
        assertTrue("insertKudosHashtags should have been called", kudosDataSource.hashtagsInserted)
    }

    // ── TC-REPO-KUDOS-WRITE-002: Anonymous submission fills anonymousNickname ─
    // G: is_anonymous = true → anonymousNickname = "Ẩn danh", senderName null
    @Test
    fun `submitKudo - anonymous submission sets anonymousNickname and nullifies sender info`() = runTest {
        val profileDataSource = StubProfileDataSource(
            dto = ProfileDto(
                id = "sender-1",
                fullName = "Real Name",
                employeeCode = "E001",
                avatarUrl = null,
                badgeType = "",
                heroTier = 0,
                departmentId = null,
            ),
        )
        val kudosDataSource = CapturingKudosDataSource()
        val repo = KudosRepositoryImpl(kudosDataSource, profileDataSource)

        repo.submitKudo(
            WriteKudoRequest(
                recipientId = "r1",
                title = "T",
                message = "M",
                hashtagIds = emptyList(),
                photoUrls = emptyList(),
                isAnonymous = true,
            ),
        )

        val captured = kudosDataSource.capturedDto
        assertEquals("Ẩn danh", captured?.anonymousNickname)
        assertEquals(null, captured?.senderName)
        assertEquals(null, captured?.senderAvatarUrl)
    }

    // ── TC-REPO-KUDOS-WRITE-003: Profile not found → failure ─────────────────
    @Test
    fun `submitKudo - null profile returns failure with not-authenticated message`() = runTest {
        val profileDataSource = StubProfileDataSource(dto = null)
        val kudosDataSource = StubInsertKudosDataSource(insertedId = "unreachable")
        val repo = KudosRepositoryImpl(kudosDataSource, profileDataSource)

        val result = repo.submitKudo(
            WriteKudoRequest(
                recipientId = "r1",
                title = "T",
                message = "M",
                hashtagIds = emptyList(),
                photoUrls = emptyList(),
                isAnonymous = false,
            ),
        )

        assertTrue(result.isFailure)
        assertEquals("Not authenticated", result.exceptionOrNull()?.message)
    }

    // ── TC-REPO-KUDOS-WRITE-004: DataSource insert throws → failure ───────────
    @Test
    fun `submitKudo - data source throw returns failure`() = runTest {
        val profileDataSource = StubProfileDataSource(
            dto = ProfileDto(
                id = "s1",
                fullName = "Name",
                employeeCode = "E1",
                avatarUrl = null,
                badgeType = "",
                heroTier = 0,
                departmentId = null,
            ),
        )
        val kudosDataSource = ThrowingKudosDataSource(RuntimeException("DB constraint"))
        val repo = KudosRepositoryImpl(kudosDataSource, profileDataSource)

        val result = repo.submitKudo(
            WriteKudoRequest(
                recipientId = "r1",
                title = "T",
                message = "M",
                hashtagIds = listOf("h1"),
                photoUrls = emptyList(),
                isAnonymous = false,
            ),
        )

        assertTrue(result.isFailure)
        assertEquals("DB constraint", result.exceptionOrNull()?.message)
    }
}

// ── Fakes ─────────────────────────────────────────────────────────────────────

private class StubProfileDataSource(
    private val dto: ProfileDto?,
) : ProfileDataSource(supabase = StubSupabaseClient) {
    override suspend fun getMyProfile(): ProfileDto? = dto
    override suspend fun getProfileById(id: String): ProfileDto? = dto
}

private class StubInsertKudosDataSource(
    private val insertedId: String,
) : KudosDataSource(supabase = StubSupabaseClient) {
    var hashtagsInserted = false

    override suspend fun insertKudo(dto: WriteKudoDto): String = insertedId

    override suspend fun insertKudosHashtags(kudosId: String, hashtagIds: List<String>) {
        hashtagsInserted = true
    }
}

private class CapturingKudosDataSource : KudosDataSource(supabase = StubSupabaseClient) {
    var capturedDto: WriteKudoDto? = null

    override suspend fun insertKudo(dto: WriteKudoDto): String {
        capturedDto = dto
        return "kudo-captured"
    }

    override suspend fun insertKudosHashtags(kudosId: String, hashtagIds: List<String>) = Unit
}

private class ThrowingKudosDataSource(
    private val error: Throwable,
) : KudosDataSource(supabase = StubSupabaseClient) {
    override suspend fun insertKudo(dto: WriteKudoDto): String = throw error
}
