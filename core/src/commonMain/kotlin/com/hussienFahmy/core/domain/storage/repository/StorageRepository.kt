package com.hussienfahmy.core.domain.storage.repository

interface StorageRepository {
    suspend fun uploadUserPhoto(userId: String, imageData: ByteArray): String
    suspend fun uploadFile(path: String, data: ByteArray): String
    suspend fun downloadUrl(path: String): String
    suspend fun deleteFile(path: String)
}