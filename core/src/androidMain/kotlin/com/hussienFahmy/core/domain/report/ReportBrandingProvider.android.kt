package com.hussienfahmy.core.domain.report

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.core.graphics.createBitmap
import com.hussienfahmy.core.domain.crash.CrashReporter
import com.hussienfahmy.core.util.PlatformContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

internal actual suspend fun loadAppIconBase64Png(
    context: PlatformContext,
    crashReporter: CrashReporter,
): String? = withContext(Dispatchers.IO) {
    try {
        val drawable = context.packageManager.getApplicationIcon(context.packageName)
        drawableToBase64Png(drawable, fallbackSize = 192)
    } catch (e: Exception) {
        crashReporter.recordException(e, mapOf("operation" to "loadAppIconBase64Png"))
        null
    }
}

private fun drawableToBase64Png(drawable: Drawable, fallbackSize: Int): String {
    val bitmap = when (drawable) {
        is BitmapDrawable -> drawable.bitmap
        else -> {
            val w = drawable.intrinsicWidth.takeIf { it > 0 } ?: fallbackSize
            val h = drawable.intrinsicHeight.takeIf { it > 0 } ?: fallbackSize
            val bm = createBitmap(w, h)
            val canvas = Canvas(bm)
            drawable.setBounds(0, 0, w, h)
            drawable.draw(canvas)
            bm
        }
    }
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
}
