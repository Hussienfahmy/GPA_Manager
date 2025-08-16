package com.hussienfahmy.core.data.local

import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

@DeleteColumn(
    tableName = "subject",
    columnName = "fixedGrade"
)
class AutoMigration10To11Spec : AutoMigrationSpec

@DeleteColumn(
    tableName = "grade",
    columnName = "symbol"
)
@RenameColumn(
    tableName = "grade",
    fromColumnName = "full_name",
    toColumnName = "meta_data"
)
class AutoMigration11To12Spec : AutoMigrationSpec

@DeleteColumn(
    tableName = "subject",
    columnName = "max_grade"
)
class AutoMigration12To13Spec : AutoMigrationSpec