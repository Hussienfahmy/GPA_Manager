package com.hussienfahmy.core.data.local.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hussienfahmy.core.data.local.AppDatabase
import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.MIGRATION_10_11
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.entity.Grade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.dsl.module

val databaseModule = module {

    // Room database callback for initial data
    factory<RoomDatabase.Callback> {
        object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // called after the build() has finished - after the first time opening the database
                // to add the initial grade data using lazy injection to avoid circular dependency
                get<CoroutineScope>().launch(Dispatchers.IO) {
                    val gradesDao = getKoin().get<AppDatabase>().gradeDao
                    Grade.generateInitialData().forEach {
                        gradesDao.upsert(it)
                    }
                }
            }
        }
    }

    // Room database
    single<AppDatabase> {
        Room.databaseBuilder(
            get<Application>(),
            AppDatabase::class.java,
            "database"
        ).fallbackToDestructiveMigration(false)
            .addCallback(get<RoomDatabase.Callback>())
            .addMigrations(MIGRATION_10_11)
            .build()
    }

    // Subject DAO
    single<SubjectDao> {
        get<AppDatabase>().subjectDao
    }

    // Grade DAO
    single<GradeDao> {
        get<AppDatabase>().gradeDao
    }
}