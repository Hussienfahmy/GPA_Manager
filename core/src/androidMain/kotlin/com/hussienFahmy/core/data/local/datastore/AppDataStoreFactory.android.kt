package com.hussienfahmy.core.data.local.datastore

import com.hussienfahmy.core.util.PlatformContext
import okio.Path
import okio.Path.Companion.toOkioPath

actual fun dataStoreFilePath(context: PlatformContext, fileName: String): Path {
    return context.filesDir.resolve(fileName).toOkioPath()
}
