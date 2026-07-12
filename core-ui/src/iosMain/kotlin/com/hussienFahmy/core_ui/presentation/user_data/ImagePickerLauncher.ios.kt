package com.hussienfahmy.core_ui.presentation.user_data

import androidx.compose.runtime.Composable
import com.hussienfahmy.core.util.PlatformImageSource

// STUB: iOS's photo picker (PHPickerViewController via UIKit interop, or the PhotosUI
// PhotosPicker SwiftUI API) isn't implemented yet - ImageThumbnailer.ios.kt already throws
// NotImplementedError for the same reason (this codebase has no iOS photo-picker UI at all yet).
// No-op until that's designed, rather than guessing at a UIKit interop shape blind.
@Composable
actual fun rememberImagePickerLauncher(onImagePicked: (PlatformImageSource) -> Unit): () -> Unit {
    return {}
}
