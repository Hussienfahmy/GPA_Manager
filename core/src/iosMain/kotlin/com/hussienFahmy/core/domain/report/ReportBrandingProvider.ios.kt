package com.hussienfahmy.core.domain.report

import com.hussienfahmy.core.util.PlatformContext
import com.hussienfahmy.core.util.toByteArray
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// Reads the app's primary icon name out of Info.plist's CFBundleIcons (populated by Xcode from
// the asset catalog's App Icon set at build time) and loads it via UIImage(named:); falls back to
// the conventional "AppIcon" asset-catalog name some Xcode configurations don't auto-populate
// CFBundleIconFiles for.
@OptIn(ExperimentalForeignApi::class, ExperimentalEncodingApi::class)
actual class ReportBrandingProvider actual constructor(context: PlatformContext) {
    actual suspend fun loadAppIconBase64Png(): String? {
        val iconName = primaryAppIconName() ?: "AppIcon"
        val image = UIImage.imageNamed(iconName) ?: return null
        val png = UIImagePNGRepresentation(image) ?: return null
        return Base64.encode(png.toByteArray())
    }

    actual suspend fun loadQrCodeBase64Png(): String? = loadQrCodeBase64PngFromBundledResource()

    private fun primaryAppIconName(): String? {
        val info = NSBundle.mainBundle.infoDictionary ?: return null
        val icons = info["CFBundleIcons"] as? Map<Any?, *> ?: return null
        val primary = icons["CFBundlePrimaryIcon"] as? Map<Any?, *> ?: return null
        val files = primary["CFBundleIconFiles"] as? List<*> ?: return null
        return files.lastOrNull() as? String
    }
}
