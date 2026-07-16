package com.hussienfahmy.core.data.local.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hussienfahmy.core.data.local.AppDatabase
import org.koin.core.context.GlobalContext

// Application is obtained from Koin's global context rather than an injected parameter, since the
// expect fun signature (shared with the future iOS actual, which needs no platform handle) takes
// no arguments - matches how the rest of this DI graph already reaches into GlobalContext (see the
// database seeding callback in DatabaseModule.kt).
actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext = GlobalContext.get().get<Application>()
    val dbFile = appContext.getDatabasePath("database")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
