package com.hussienfahmy.core.data.local.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.hussienfahmy.core.data.local.AppDatabase
import com.hussienfahmy.core.util.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

// Verified against developer.android.com's Room KMP setup guide - the standard
// NSFileManager/NSDocumentDirectory pattern for resolving the app's Documents directory.
actual fun getDatabaseBuilder(context: PlatformContext): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "/database"
    return Room.databaseBuilder<AppDatabase>(name = dbFilePath)
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
