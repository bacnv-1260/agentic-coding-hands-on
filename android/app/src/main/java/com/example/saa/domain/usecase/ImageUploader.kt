package com.example.saa.domain.usecase

/**
 * Abstraction for uploading an image from a local URI and returning its public URL.
 * Decoupled from Android types so implementations can be faked in unit tests.
 */
interface ImageUploader {
    suspend fun uploadImage(uriString: String): String
}
