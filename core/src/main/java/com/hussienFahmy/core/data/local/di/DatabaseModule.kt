package com.hussienFahmy.core.data.local.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hussienFahmy.core.data.local.AppDatabase
import com.hussienFahmy.core.data.local.GradeDao
import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienFahmy.core.data.local.entity.Grade
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

    @Provides
    @Singleton
    fun provideSubjectDao(appDatabase: AppDatabase): SubjectDao {
        return appDatabase.subjectDao
    }

    @Provides
    @Singleton
    fun provideGradeDao(appDatabase: AppDatabase): GradeDao {
        return appDatabase.gradeDao
    }
}