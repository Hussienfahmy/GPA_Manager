package com.hussienfahmy.core.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.type_converter.GradeNameTypeConverter

@Database(
    entities = [Subject::class, Grade::class],
    version = 13,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11, spec = AutoMigration10To11Spec::class),
        AutoMigration(from = 11, to = 12, spec = AutoMigration11To12Spec::class),
        AutoMigration(from = 12, to = 13, spec = AutoMigration12To13Spec::class)
    ]
)
@TypeConverters(GradeNameTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val subjectDao: SubjectDao
    abstract val gradeDao: GradeDao
}