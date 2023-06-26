package com.hussienfahmy.core.data.local.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hussienfahmy.core.data.local.AppDatabase
import com.hussienfahmy.core.data.local.entity.Grade
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideInitialDataCallback(
        database: Provider<AppDatabase>,
        applicationScope: CoroutineScope
    ): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // called after the build() has finished - after the first time opening the database
                // to add the initial grade data
                applicationScope.launch(Dispatchers.IO) {
                    val gradesDao = database.get().gradeDao
                    Grade.generateInitialData().forEach {
                        gradesDao.upsert(it)
                    }
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: RoomDatabase.Callback,
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "database"
    ).fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()
}