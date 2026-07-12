package com.hussienfahmy.core.util

import platform.Foundation.NSURL

// NSURL is the natural iOS analog of Android's Uri as a "picked file handle" - matches the shape
// of the Android actual (actual typealias PlatformImageSource = Uri).
actual typealias PlatformImageSource = NSURL

// STUB: real implementation needs to load image data from the NSURL (likely via PHPickerResult/
// PHAsset if picking through PhotosUI, or NSData(contentsOfURL:) for a plain file URL) and resize/
// recompress it via Core Graphics (CGImage/CGBitmapContext) or UIImage's JPEG representation APIs.
// Depends on how the iOS photo-picker UI itself ends up being built, which doesn't exist yet -
// throws rather than returning a fabricated empty ByteArray, since callers expect real JPEG bytes.
actual class ImageThumbnailer {
    actual suspend fun createThumbnail(source: PlatformImageSource, quality: Int): ByteArray {
        throw NotImplementedError("ImageThumbnailer.createThumbnail is not yet implemented on iOS")
    }
}

actual fun createImageThumbnailer(): ImageThumbnailer = ImageThumbnailer()
