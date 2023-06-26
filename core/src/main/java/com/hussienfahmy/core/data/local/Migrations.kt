package com.hussienfahmy.core.data.local

import androidx.room.DeleteColumn
import androidx.room.migration.AutoMigrationSpec

@DeleteColumn(
    tableName = "subject",
    columnName = "fixedGrade"
)
class AutoMigration10To11Spec : AutoMigrationSpec