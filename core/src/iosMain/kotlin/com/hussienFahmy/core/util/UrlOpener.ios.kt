package com.hussienfahmy.core.util

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class UrlOpener actual constructor(context: PlatformContext) {
    actual fun open(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(
            url = nsUrl,
            options = emptyMap<Any?, Any?>(),
            completionHandler = null,
        )
    }
}
