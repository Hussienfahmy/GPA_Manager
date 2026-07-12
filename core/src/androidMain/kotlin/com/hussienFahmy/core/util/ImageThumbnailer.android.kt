package com.hussienfahmy.core.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import org.koin.core.context.GlobalContext
import java.io.ByteArrayOutputStream

actual typealias PlatformImageSource = Uri

actual class ImageThumbnailer(private val contentResolver: ContentResolver) {
    actual suspend fun createThumbnail(source: PlatformImageSource, quality: Int): ByteArray {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val decoderSource = ImageDecoder.createSource(contentResolver, source)
            ImageDecoder.decodeBitmap(decoderSource)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(contentResolver, source)
        }

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    }
}

// Same GlobalContext pattern as DatabaseModule.android.kt's getDatabaseBuilder() - the expect fun
// signature is shared with the future iOS actual, which needs no platform handle.
actual fun createImageThumbnailer(): ImageThumbnailer {
    val contentResolver = GlobalContext.get().get<Context>().contentResolver
    return ImageThumbnailer(contentResolver)
}
