package com.hussienfahmy.core.data.local.datastore

import android.app.Application
import okio.Path
import okio.Path.Companion.toOkioPath
import org.koin.core.context.GlobalContext

actual fun dataStoreFilePath(fileName: String): Path {
    val appContext = GlobalContext.get().get<Application>()
    return appContext.filesDir.resolve(fileName).toOkioPath()
}
