package com.hussienfahmy.core.util

import android.app.Application
import android.content.Intent
import androidx.core.net.toUri
import org.koin.core.context.GlobalContext

// Application is obtained from Koin's global context rather than an injected parameter, matching
// the getDatabaseBuilder() pattern (DatabaseModule.android.kt) - keeps the expect fun signature
// argument-free so it's identical across platforms.
actual fun openUrl(url: String) {
    val appContext = GlobalContext.get().get<Application>()
    appContext.startActivity(
        Intent(Intent.ACTION_VIEW, url.toUri()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )
}
