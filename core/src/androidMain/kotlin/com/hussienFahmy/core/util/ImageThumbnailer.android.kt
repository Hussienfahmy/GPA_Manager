package com.hussienfahmy.core.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

actual class PlatformImageSource(val uri: Uri)

actual class ImageThumbnailer actual constructor(context: PlatformContext) {
    private val contentResolver: ContentResolver = context.contentResolver

    actual suspend fun createThumbnail(source: PlatformImageSource, quality: Int): ByteArray {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val decoderSource = ImageDecoder.createSource(contentResolver, source.uri)
            ImageDecoder.decodeBitmap(decoderSource)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(contentResolver, source.uri)
        }

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    }
}
