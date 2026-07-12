package com.hussienfahmy.core.util

// Platform-native handle to a picked image (Uri on Android). The iOS actual/PlatformImageSource
// shape (NSURL, PHAsset, or raw ByteArray) is deferred to the iOS phase - it depends on how the
// iOS photo-picker UI ends up being built, which doesn't exist yet.
expect class PlatformImageSource

expect class ImageThumbnailer {
    suspend fun createThumbnail(source: PlatformImageSource, quality: Int = 60): ByteArray
}

expect fun createImageThumbnailer(): ImageThumbnailer
