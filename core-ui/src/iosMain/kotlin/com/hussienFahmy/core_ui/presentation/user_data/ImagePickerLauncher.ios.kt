package com.hussienfahmy.core_ui.presentation.user_data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hussienfahmy.core.util.PlatformImageSource
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

// "public.image" is the standard Uniform Type Identifier for any image format - avoids pulling in
// UniformTypeIdentifiers just to spell the same string as UTTypeImage.identifier.
private const val IMAGE_TYPE_IDENTIFIER = "public.image"

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberImagePickerLauncher(onImagePicked: (PlatformImageSource) -> Unit): () -> Unit {
    val delegate = remember { PickerDelegate(onImagePicked) }

    return {
        val configuration = PHPickerConfiguration().apply {
            filter = PHPickerFilter.imagesFilter()
            selectionLimit = 1L
        }
        val picker = PHPickerViewController(configuration = configuration)
        picker.delegate = delegate
        UIApplication.sharedApplication.keyWindow?.rootViewController
            ?.presentViewController(picker, animated = true, completion = null)
    }
}

// PHPickerViewController needs no NSPhotoLibraryUsageDescription/Info.plist entry - unlike the
// older UIImagePickerController, it runs the actual photo library access out-of-process and only
// ever hands the app back whatever the user explicitly picked.
@OptIn(ExperimentalForeignApi::class)
private class PickerDelegate(
    private val onImagePicked: (PlatformImageSource) -> Unit
) : NSObject(), PHPickerViewControllerDelegateProtocol {
    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
        picker.dismissViewControllerAnimated(true, completion = null)
        val result = didFinishPicking.firstOrNull() as? PHPickerResult ?: return
        val provider = result.itemProvider
        if (!provider.hasItemConformingToTypeIdentifier(IMAGE_TYPE_IDENTIFIER)) return

        // loadDataRepresentation, not loadFileRepresentation: the latter's temp file is deleted
        // the instant this completion handler returns, but ImageThumbnailer.ios.kt reads the
        // PlatformImageSource asynchronously afterwards - write our own longer-lived temp file.
        provider.loadDataRepresentationForTypeIdentifier(IMAGE_TYPE_IDENTIFIER) { data, _ ->
            val imageData = data ?: return@loadDataRepresentationForTypeIdentifier
            val filePath = NSTemporaryDirectory() + NSUUID().UUIDString() + ".jpg"
            val written = NSFileManager.defaultManager.createFileAtPath(
                filePath,
                contents = imageData,
                attributes = null,
            )
            if (written) {
                val fileUrl = NSURL.fileURLWithPath(filePath)
                dispatch_async(dispatch_get_main_queue()) {
                    onImagePicked(fileUrl)
                }
            }
        }
    }
}
