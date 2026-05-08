package com.example.saa.domain.usecase

import com.example.saa.domain.repository.UserStatsRepository
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class OpenSecretBoxUseCase @Inject constructor(
    private val repository: UserStatsRepository,
) {
    private val isOpening = AtomicBoolean(false)

    suspend operator fun invoke(): Result<Unit> {
        if (!isOpening.compareAndSet(false, true)) {
            return Result.failure(IllegalStateException("Already opening a secret box"))
        }
        return try {
            val boxIdResult = repository.getNextSecretBox()
            boxIdResult.fold(
                onSuccess = { boxId -> repository.openSecretBox(boxId) },
                onFailure = { e -> Result.failure(e) },
            )
        } finally {
            isOpening.set(false)
        }
    }
}
