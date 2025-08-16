package com.hussienfahmy.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.model.GradeName
import kotlinx.coroutines.flow.Flow

@Dao
interface GradeDao {

    @get:Query("SELECT * FROM grade ORDER BY points DESC")
    val grades: Flow<List<Grade>>

    @Query("SELECT * FROM grade WHERE active = 1 ORDER BY points DESC")
    fun getActiveGrades(): Flow<List<Grade>>

    @Query("SELECT * FROM grade WHERE meta_data = :gradeName")
    suspend fun getGradeByName(gradeName: GradeName): Grade?

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
        WHERE meta_data == :gradeName"""
    )
    suspend fun updatePoints(gradeName: GradeName, points: Double?)

    // if the percentage is null, it means that the grade is not active
    @Query(
        """UPDATE grade SET percentage = :percentage,
            active = CASE 
                        WHEN :percentage IS NULL THEN 0 
                        ELSE active
                    END
         WHERE meta_data == :gradeName"""
    )
    suspend fun updatePercentage(gradeName: GradeName, percentage: Double?)

    @Query("UPDATE grade SET active = :isActive WHERE meta_data == :gradeName")
    suspend fun setActive(gradeName: GradeName, isActive: Boolean)

    @Query("SELECT COUNT(*) FROM grade")
    suspend fun count(): Long
}