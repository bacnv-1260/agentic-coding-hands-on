package com.example.saa.domain.usecase

import com.example.saa.domain.model.WriteKudoRequest
import com.example.saa.domain.repository.KudosRepository
import timber.log.Timber
import javax.inject.Inject

class SubmitKudoUseCase @Inject constructor(
    private val imageUploader: ImageUploader,
    private val kudosRepository: KudosRepository,
) {
    suspend operator fun invoke(
        recipientId: String,
        title: String,
        message: String,
        hashtagIds: List<String>,
        imageUris: List<String>,
        isAnonymous: Boolean,
    ): Result<String> = runCatching {
        val photoUrls = imageUris.map { uriString ->
            imageUploader.uploadImage(uriString)
        }

        val request = WriteKudoRequest(
            recipientId = recipientId,
            title = title,
            message = message,
            hashtagIds = hashtagIds,
            photoUrls = photoUrls,
            isAnonymous = isAnonymous,
        )

        kudosRepository.submitKudo(request).getOrThrow()
    }.onFailure { Timber.e(it, "SubmitKudoUseCase failed") }
}
