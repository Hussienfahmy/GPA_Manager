package com.hussienfahmy.core_ui.presentation.user_data

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher

/**
 * Registers a platform gallery picker at composition time and returns a launcher callback.
 * Backed by FileKit, which is genuinely multiplatform - no expect/actual split needed here,
 * unlike the hand-rolled PHPickerViewController/ActivityResultContracts.GetContent split this
 * replaced.
 */
@Composable
fun rememberImagePickerLauncher(onImagePicked: (PlatformFile) -> Unit): () -> Unit {
    val launcher = rememberFilePickerLauncher(type = FileKitType.Image) { file ->
        file?.let(onImagePicked)
    }
    return { launcher.launch() }
}
