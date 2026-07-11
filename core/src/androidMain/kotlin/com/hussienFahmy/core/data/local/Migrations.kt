package com.hussienfahmy.core.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_10_11 = object : Migration(10, 11) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create new grade table with meta_data as primary key
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS grade_new (
                meta_data TEXT NOT NULL,
                active INTEGER NOT NULL,
                points REAL,
                percentage REAL,
                PRIMARY KEY(meta_data)
            )
        """
        )

        // Insert data from old table to new table, converting enum names to symbol strings
        // Use GROUP BY to handle potential duplicates
        db.execSQL(
            """
            INSERT INTO grade_new (meta_data, active, points, percentage)
            SELECT 
                CASE 
                    WHEN full_name = 'F' THEN 'F'
                    WHEN full_name = 'DMinus' THEN 'D-'
                    WHEN full_name = 'D' THEN 'D'
                    WHEN full_name = 'DPlus' THEN 'D+'
                    WHEN full_name = 'CMinus' THEN 'C-'
                    WHEN full_name = 'C' THEN 'C'
                    WHEN full_name = 'CPlus' THEN 'C+'
                    WHEN full_name = 'BMinus' THEN 'B-'
                    WHEN full_name = 'B' THEN 'B'
                    WHEN full_name = 'BPlus' THEN 'B+'
                    WHEN full_name = 'AMinus' THEN 'A-'
                    WHEN full_name = 'A' THEN 'A'
                    WHEN full_name = 'APlus' THEN 'A+'
                    ELSE symbol
                END as meta_data,
                MAX(active) as active,
                MAX(points) as points,
                MAX(percentage) as percentage
            FROM grade
            GROUP BY CASE 
                WHEN full_name = 'F' THEN 'F'
                WHEN full_name = 'DMinus' THEN 'D-'
                WHEN full_name = 'D' THEN 'D'
                WHEN full_name = 'DPlus' THEN 'D+'
                WHEN full_name = 'CMinus' THEN 'C-'
                WHEN full_name = 'C' THEN 'C'
                WHEN full_name = 'CPlus' THEN 'C+'
                WHEN full_name = 'BMinus' THEN 'B-'
                WHEN full_name = 'B' THEN 'B'
                WHEN full_name = 'BPlus' THEN 'B+'
                WHEN full_name = 'AMinus' THEN 'A-'
                WHEN full_name = 'A' THEN 'A'
                WHEN full_name = 'APlus' THEN 'A+'
                ELSE symbol
            END
        """
        )

        // Drop the old table
        db.execSQL("DROP TABLE grade")

        // Rename the new table to grade
        db.execSQL("ALTER TABLE grade_new RENAME TO grade")

        // Create new subject table without fixedGrade and max_grade columns (version 13 structure)
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS subject_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                creditHours REAL NOT NULL,
                gradeName TEXT,
                totalMarks REAL NOT NULL,
                midterm REAL,
                practical REAL,
                oral REAL,
                project REAL,
                midtermAvailable INTEGER NOT NULL DEFAULT 1,
                oralAvailable INTEGER NOT NULL DEFAULT 1,
                practicalAvailable INTEGER NOT NULL DEFAULT 0,
                projectAvailable INTEGER NOT NULL DEFAULT 0
            )
        """
        )

        // Copy data from old subject table, ensuring gradeName values are valid
        db.execSQL(
            """
            INSERT INTO subject_new (id, name, creditHours, gradeName, totalMarks, midterm, practical, oral, project, midtermAvailable, oralAvailable, practicalAvailable, projectAvailable)
            SELECT 
                id, 
                name, 
                creditHours, 
                CASE 
                    WHEN gradeName = 'F' THEN 'F'
                    WHEN gradeName = 'DMinus' THEN 'D-'
                    WHEN gradeName = 'D' THEN 'D'
                    WHEN gradeName = 'DPlus' THEN 'D+'
                    WHEN gradeName = 'CMinus' THEN 'C-'
                    WHEN gradeName = 'C' THEN 'C'
                    WHEN gradeName = 'CPlus' THEN 'C+'
                    WHEN gradeName = 'BMinus' THEN 'B-'
                    WHEN gradeName = 'B' THEN 'B'
                    WHEN gradeName = 'BPlus' THEN 'B+'
                    WHEN gradeName = 'AMinus' THEN 'A-'
                    WHEN gradeName = 'A' THEN 'A'
                    WHEN gradeName = 'APlus' THEN 'A+'
                    ELSE NULL
                END,
                totalMarks, 
                midterm, 
                practical, 
                oral, 
                NULL as project,
                midtermAvailable,
                oralAvailable,
                practicalAvailable,
                0 as projectAvailable
            FROM subject
        """
        )

        db.execSQL("DROP TABLE subject")
        db.execSQL("ALTER TABLE subject_new RENAME TO subject")
    }
}