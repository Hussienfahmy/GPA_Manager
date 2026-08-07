package com.hussienfahmy.core.data.local.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.hussienfahmy.core.data.local.AppDatabase
import com.hussienfahmy.core.util.PlatformContext

actual fun getDatabaseBuilder(context: PlatformContext): RoomDatabase.Builder<AppDatabase> {
    val dbFile = context.getDatabasePath("database")
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath,
    )
}
