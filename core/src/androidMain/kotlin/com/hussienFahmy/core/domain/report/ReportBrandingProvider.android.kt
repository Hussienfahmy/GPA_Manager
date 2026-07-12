package com.hussienfahmy.core.domain.report

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import com.hussienfahmy.core.R
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

    actual suspend fun loadQrCodeBase64Png(): String? = withContext(Dispatchers.IO) {
        try {
            val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.qr_code, null)!!
            drawableToBase64Png(drawable, fallbackSize = 256)
        } catch (_: Exception) {
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
}

actual fun createReportBrandingProvider(): ReportBrandingProvider =
    ReportBrandingProvider(GlobalContext.get().get())
