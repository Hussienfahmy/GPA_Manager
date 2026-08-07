package com.hussienfahmy.core.util

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

// NSURL is the natural iOS analog of Android's Uri as a "picked file handle" - matches the shape
// of the Android actual (actual typealias PlatformImageSource = Uri).
actual typealias PlatformImageSource = NSURL

// Reads the picked file's raw bytes via NSData(contentsOfURL:), decodes to a UIImage, and
// re-encodes as JPEG - the same shape as the Android actual's ImageDecoder->Bitmap->JPEG pipeline.
@OptIn(ExperimentalForeignApi::class)
actual class ImageThumbnailer actual constructor(context: PlatformContext) {
    actual suspend fun createThumbnail(source: PlatformImageSource, quality: Int): ByteArray {
        val data = NSData.dataWithContentsOfURL(source)
            ?: error("Could not read image data from $source")
        val image = UIImage(data = data)
        val jpeg = UIImageJPEGRepresentation(image, quality / 100.0)
            ?: error("Could not JPEG-encode image from $source")
        return jpeg.toByteArray()
    }
}
