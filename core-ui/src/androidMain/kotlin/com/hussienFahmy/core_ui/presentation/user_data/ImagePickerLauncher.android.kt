package com.hussienfahmy.core_ui.presentation.user_data

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.hussienfahmy.core.util.PlatformImageSource

@Composable
actual fun rememberImagePickerLauncher(onImagePicked: (PlatformImageSource) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { onImagePicked(PlatformImageSource(it)) } },
    )
    return { launcher.launch("image/*") }
}
