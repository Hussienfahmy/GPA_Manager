package com.hussienfahmy.core.data.local.di

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.hussienfahmy.core.data.local.AppDatabase
import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.MIGRATION_10_11
import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.entity.Grade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.dsl.module

// Android actual in data/local/di/DatabaseModule.android.kt; iOS actual added in the iOS phase.
expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

val databaseModule = module {

    // Room database callback for initial data
    factory<RoomDatabase.Callback> {
        object : RoomDatabase.Callback() {
            override fun onCreate(connection: SQLiteConnection) {
                super.onCreate(connection)
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
        getDatabaseBuilder()
            .fallbackToDestructiveMigration(false)
            .addCallback(get<RoomDatabase.Callback>())
            .addMigrations(MIGRATION_10_11)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
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

    // Semester DAO
    single<SemesterDao> {
        get<AppDatabase>().semesterDao
    }
}