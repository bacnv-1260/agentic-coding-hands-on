package com.example.saa.data.remote.source

import android.content.Context
import android.net.Uri
import com.example.saa.domain.usecase.ImageUploader
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

open class StorageDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val supabase: SupabaseClient,
) : ImageUploader {
    override suspend fun uploadImage(uriString: String): String {
        val uri = Uri.parse(uriString)

        val mimeType = context.contentResolver.getType(uri)
            ?: throw IllegalArgumentException("Cannot determine MIME type for: $uriString")

        if (mimeType !in ALLOWED_MIME_TYPES) {
            throw IllegalArgumentException("Unsupported image type: $mimeType")
        }

        val bytes = context.contentResolver.openInputStream(uri)
            ?.use { it.readBytes() }
            ?: throw IOException("Cannot read image data from: $uriString")

        if (bytes.size > MAX_IMAGE_SIZE_BYTES) {
            throw IllegalArgumentException("Image exceeds 10 MB limit")
        }

        val extension = when (mimeType) {
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            "image/webp" -> "webp"
            else -> throw IllegalArgumentException("Unsupported MIME type: $mimeType")
        }

        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("Not authenticated")
        val path = "$userId/${System.currentTimeMillis()}.$extension"

        supabase.storage.from(BUCKET_NAME).upload(path, bytes) {
            upsert = false
        }

        val publicUrl = supabase.storage.from(BUCKET_NAME).publicUrl(path)
        Timber.d("Image uploaded: %s", publicUrl)
        return publicUrl
    }

    companion object {
        private const val BUCKET_NAME = "kudos-images"
        private const val MAX_IMAGE_SIZE_BYTES = 10 * 1024 * 1024
        private val ALLOWED_MIME_TYPES = setOf("image/jpeg", "image/png", "image/webp")
    }
}
