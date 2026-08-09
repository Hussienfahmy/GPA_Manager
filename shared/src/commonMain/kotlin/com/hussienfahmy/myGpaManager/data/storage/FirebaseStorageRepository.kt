package com.hussienfahmy.myGpaManager.data.storage

import com.hussienfahmy.core.domain.storage.repository.StorageRepository
import com.hussienfahmy.myGpaManager.data.user_data.model.FirebaseUserData
import dev.gitlive.firebase.storage.Data
import dev.gitlive.firebase.storage.FirebaseStorage

// Data's actual constructor takes a ByteArray on Android but an NSData on iOS, so the conversion
// itself has to be platform-specific.
expect fun ByteArray.toFirebaseStorageData(): Data

class FirebaseStorageRepository(
    private val storage: FirebaseStorage
) : StorageRepository {

    override suspend fun uploadUserPhoto(userId: String, imageData: ByteArray): String {
        val photoRef = storage.reference.child("${FirebaseUserData.USERS_COLLECTION_NAME}/$userId")
        photoRef.putData(imageData.toFirebaseStorageData())
        return photoRef.getDownloadUrl()
    }

    override suspend fun uploadFile(path: String, data: ByteArray): String {
        val fileRef = storage.reference.child(path)
        fileRef.putData(data.toFirebaseStorageData())
        return fileRef.getDownloadUrl()
    }

    override suspend fun downloadUrl(path: String): String {
        return storage.reference.child(path).getDownloadUrl()
    }

    override suspend fun deleteFile(path: String) {
        storage.reference.child(path).delete()
    }
}
