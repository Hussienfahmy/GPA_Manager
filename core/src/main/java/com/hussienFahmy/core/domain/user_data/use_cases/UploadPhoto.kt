package com.hussienFahmy.core.domain.user_data.use_cases

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.google.firebase.storage.FirebaseStorage
import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class UploadPhoto(
    private val repository: UserDataRepository,
    private val updatePhotoUrl: UpdatePhotoUrl,
    private val storage: FirebaseStorage,
    private val contentResolver: ContentResolver,
) {
    suspend operator fun invoke(uri: Uri) {
        val user = repository.getUserData() ?: return

        val thumbnail = createThumbnail(uri)
        val photoRef = storage.reference.child("users/${user.id}")

        val uploadTask = photoRef.putBytes(thumbnail)

        val downloadUrl = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            photoRef.downloadUrl
        }.await()

        updatePhotoUrl(downloadUrl.toString())
    }

    private fun createThumbnail(uri: Uri): ByteArray {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
        return outputStream.toByteArray()
    }
}