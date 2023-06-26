package com.hussienfahmy.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hussienfahmy.core.data.local.entity.Grade
import kotlinx.coroutines.flow.Flow

@Dao
interface GradeDao {

    @get:Query("SELECT * FROM grade ORDER BY points DESC")
    val grades: Flow<List<Grade>>

    @get:Query("SELECT * FROM grade WHERE active = 1 ORDER BY points DESC")
    val activeGrades: Flow<List<Grade>>

    @Query("SELECT * FROM grade WHERE full_name = :gradeName")
    fun getGradeByName(gradeName: Grade.Name): Flow<Grade>

    @Query("SELECT * FROM grade WHERE active = 1 AND points <= :points ORDER BY points DESC LIMIT 1")
    suspend fun getGradeByPoints(points: Double): Grade?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(grade: Grade)

    // if the points is null, it means that the grade is not active
    @Query(
        """UPDATE grade SET points = :points,
        active = CASE 
                    WHEN :points IS NULL THEN 0 
                    ELSE active
                END
        WHERE full_name == :gradeName"""
    )
    suspend fun updatePoints(gradeName: Grade.Name, points: Double?)

    // if the percentage is null, it means that the grade is not active
    @Query(
        """UPDATE grade SET percentage = :percentage,
            active = CASE 
                        WHEN :percentage IS NULL THEN 0 
                        ELSE active
                    END
         WHERE full_name == :gradeName"""
    )
    suspend fun updatePercentage(gradeName: Grade.Name, percentage: Double?)

    @Query("UPDATE grade SET active = NOT active WHERE full_name == :gradeName")
    suspend fun inverseActive(gradeName: Grade.Name)

    @Query("SELECT COUNT(*) FROM grade")
    suspend fun count(): Long
}