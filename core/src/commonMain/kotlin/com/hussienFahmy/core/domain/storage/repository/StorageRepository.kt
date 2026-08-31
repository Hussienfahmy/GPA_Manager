package com.hussienfahmy.core.domain.storage.repository

interface StorageRepository {
    suspend fun uploadUserPhoto(userId: String, imageData: ByteArray): String
}