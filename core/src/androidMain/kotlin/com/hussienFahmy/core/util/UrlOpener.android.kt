package com.hussienfahmy.core.util

import android.content.Intent
import androidx.core.net.toUri

actual class UrlOpener actual constructor(private val context: PlatformContext) {
    actual fun open(url: String) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, url.toUri()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
