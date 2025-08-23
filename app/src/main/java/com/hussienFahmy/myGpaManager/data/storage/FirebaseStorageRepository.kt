package com.hussienfahmy.myGpaManager.data.storage

import com.google.firebase.storage.FirebaseStorage
import com.hussienfahmy.core.domain.storage.repository.StorageRepository
import kotlinx.coroutines.tasks.await

class FirebaseStorageRepository(
    private val storage: FirebaseStorage
) : StorageRepository {

    override suspend fun uploadUserPhoto(userId: String, imageData: ByteArray): String {
        val photoRef = storage.reference.child("users/$userId")

        val uploadTask = photoRef.putBytes(imageData)

        return uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            photoRef.downloadUrl
        }.await().toString()
    }

    override suspend fun uploadFile(path: String, data: ByteArray): String {
        val fileRef = storage.reference.child(path)

        val uploadTask = fileRef.putBytes(data)

        return uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            fileRef.downloadUrl
        }.await().toString()
    }

    override suspend fun downloadUrl(path: String): String {
        return storage.reference.child(path).downloadUrl.await().toString()
    }

    override suspend fun deleteFile(path: String) {
        storage.reference.child(path).delete().await()
    }
}