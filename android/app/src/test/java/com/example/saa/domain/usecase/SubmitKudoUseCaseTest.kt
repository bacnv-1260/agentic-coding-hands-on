package com.example.saa.domain.usecase

import com.example.saa.domain.model.Kudos
import com.example.saa.domain.model.KudosFilter
import com.example.saa.domain.model.WriteKudoRequest
import com.example.saa.domain.repository.KudosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// ── Design mapping ────────────────────────────────────────────────────────────
// I Send Button → SubmitKudoUseCase orchestrates upload → insert → hashtags
// F.5 images    → uploadImage called once per image URI
// G anonymous   → isAnonymous forwarded to WriteKudoRequest
// ─────────────────────────────────────────────────────────────────────────────

class SubmitKudoUseCaseTest {

    // ── TC-USECASE-001: Happy path with images ────────────────────────────────
    @Test
    fun `invoke - uploads images and submits kudo returning generated id`() = runTest {
        val uploader = FakeImageUploader(uploadedUrl = "https://bucket.example.com/img.jpg")
        val repo = FakeKudosRepository(submitResult = Result.success("kudo-abc"))
        val useCase = SubmitKudoUseCase(uploader, repo)

        val result = useCase(
            recipientId = "r1",
            title = "Best Dev",
            message = "<p>Well done!</p>",
            hashtagIds = listOf("h1", "h2"),
            imageUris = listOf("content://img1", "content://img2"),
            isAnonymous = false,
        )

        assertTrue(result.isSuccess)
        assertEquals("kudo-abc", result.getOrThrow())
        assertEquals(2, uploader.uploadCallCount)
    }

    // ── TC-USECASE-002: No images — skips upload entirely ────────────────────
    @Test
    fun `invoke - no imageUris skips upload and submits with empty photoUrls`() = runTest {
        val uploader = FakeImageUploader()
        val repo = FakeKudosRepository(submitResult = Result.success("kudo-xyz"))
        val useCase = SubmitKudoUseCase(uploader, repo)

        val result = useCase(
            recipientId = "r1",
            title = "Best Dev",
            message = "<p>Good work!</p>",
            hashtagIds = listOf("h1"),
            imageUris = emptyList(),
            isAnonymous = false,
        )

        assertTrue(result.isSuccess)
        assertEquals(0, uploader.uploadCallCount)
        assertEquals(emptyList<String>(), repo.lastRequest?.photoUrls)
    }

    // ── TC-USECASE-003: Storage upload failure → Result.failure ──────────────
    // US-4 Scenario 4: invalid file type/size → error
    @Test
    fun `invoke - upload failure returns failure and does not call repository`() = runTest {
        val uploader = ThrowingImageUploader(RuntimeException("File too large"))
        val repo = FakeKudosRepository(submitResult = Result.success("kudo-unreached"))
        val useCase = SubmitKudoUseCase(uploader, repo)

        val result = useCase(
            recipientId = "r1",
            title = "T",
            message = "M",
            hashtagIds = listOf("h1"),
            imageUris = listOf("content://bigfile"),
            isAnonymous = false,
        )

        assertTrue(result.isFailure)
        assertEquals("File too large", result.exceptionOrNull()?.message)
        assertFalse(repo.submitWasCalled)
    }

    // ── TC-USECASE-004: Repository submitKudo failure → Result.failure ────────
    // US-1 Scenario 5: server error after images uploaded
    @Test
    fun `invoke - repository failure returns failure`() = runTest {
        val uploader = FakeImageUploader()
        val repo = FakeKudosRepository(submitResult = Result.failure(RuntimeException("DB error")))
        val useCase = SubmitKudoUseCase(uploader, repo)

        val result = useCase(
            recipientId = "r1",
            title = "T",
            message = "M",
            hashtagIds = listOf("h1"),
            imageUris = emptyList(),
            isAnonymous = false,
        )

        assertTrue(result.isFailure)
        assertEquals("DB error", result.exceptionOrNull()?.message)
    }

    // ── TC-USECASE-005: isAnonymous forwarded to request ─────────────────────
    // US-3 Scenario 1: anonymous flag carried through to repository
    @Test
    fun `invoke - isAnonymous true is forwarded to WriteKudoRequest`() = runTest {
        val uploader = FakeImageUploader()
        val repo = FakeKudosRepository(submitResult = Result.success("kudo-anon"))
        val useCase = SubmitKudoUseCase(uploader, repo)

        useCase(
            recipientId = "r1",
            title = "T",
            message = "M",
            hashtagIds = listOf("h1"),
            imageUris = emptyList(),
            isAnonymous = true,
        )

        assertTrue(repo.lastRequest?.isAnonymous ?: false)
    }
}

// ── Fakes ─────────────────────────────────────────────────────────────────────

private class FakeImageUploader(
    private val uploadedUrl: String = "https://example.com/img.jpg",
) : ImageUploader {
    var uploadCallCount = 0
    override suspend fun uploadImage(uriString: String): String {
        uploadCallCount++
        return uploadedUrl
    }
}

private class ThrowingImageUploader(private val error: Throwable) : ImageUploader {
    override suspend fun uploadImage(uriString: String): String = throw error
}

private class FakeKudosRepository(
    var submitResult: Result<String>,
) : KudosRepository {
    var submitWasCalled = false
    var lastRequest: WriteKudoRequest? = null

    override suspend fun getHighlightKudos(hashtagId: String?, departmentId: String?) =
        Result.success(emptyList<Kudos>())

    override suspend fun getAllKudos(page: Int, limit: Int, hashtagId: String?, departmentId: String?) =
        Result.success(emptyList<Kudos>())

    override suspend fun toggleLike(kudosId: String, isCurrentlyLiked: Boolean) =
        Result.success(Unit)

    override fun observeNewKudos(): Flow<Kudos> = flow {}

    override suspend fun getProfileKudos(filter: KudosFilter) =
        Result.success(emptyList<Kudos>())

    override suspend fun submitKudo(request: WriteKudoRequest): Result<String> {
        submitWasCalled = true
        lastRequest = request
        return submitResult
    }
}
