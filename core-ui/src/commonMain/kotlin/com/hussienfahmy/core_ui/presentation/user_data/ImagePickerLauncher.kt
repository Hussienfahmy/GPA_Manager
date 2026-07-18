package com.hussienfahmy.core_ui.presentation.user_data

import androidx.compose.runtime.Composable
import com.hussienfahmy.core.util.PlatformImageSource

/**
 * Registers a platform photo picker at composition time and returns a launcher callback.
 * Mirrors the shape of Android's rememberLauncherForActivityResult, which this replaces.
 */
@Composable
expect fun rememberImagePickerLauncher(onImagePicked: (PlatformImageSource) -> Unit): () -> Unit
