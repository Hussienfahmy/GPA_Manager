package com.hussienfahmy.core.domain.report

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.context.GlobalContext
import java.io.ByteArrayOutputStream

actual class ReportBrandingProvider(private val context: Context) {
    actual suspend fun loadAppIconBase64Png(): String? = withContext(Dispatchers.IO) {
        try {
            val drawable = context.packageManager.getApplicationIcon(context.packageName)
            drawableToBase64Png(drawable, fallbackSize = 192)
        } catch (_: Exception) {
            null
        }
    }

    // The QR code used to be loaded from this module's own Android res/drawable via
    // ResourcesCompat - moved to a bundled Compose Multiplatform resource (single source of truth
    // shared with the iOS actual) instead of keeping a duplicate Android-only copy.
    actual suspend fun loadQrCodeBase64Png(): String? = loadQrCodeBase64PngFromBundledResource()

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
}

actual fun createReportBrandingProvider(): ReportBrandingProvider =
    ReportBrandingProvider(GlobalContext.get().get())
