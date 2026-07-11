package com.hussienfahmy.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hussienfahmy.core.data.local.entity.Semester
import kotlinx.coroutines.flow.Flow

@Dao
interface SemesterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(semester: Semester): Long

    @Update
    suspend fun update(semester: Semester)

    @Query("DELETE FROM semester WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM semester WHERE id = :id")
    suspend fun getById(id: Long): Semester?

    @Query("SELECT * FROM semester WHERE id = :id")
    fun observeById(id: Long): Flow<Semester?>

    @Query("SELECT * FROM semester ORDER BY `order` ASC")
    fun getAll(): Flow<List<Semester>>

    @Query("SELECT * FROM semester WHERE status = 'ARCHIVED' ORDER BY `order` ASC")
    fun getArchived(): Flow<List<Semester>>

    @Query("SELECT COUNT(*) FROM semester WHERE status = 'ARCHIVED'")
    suspend fun getArchivedCount(): Int

    @Query("SELECT COALESCE(SUM(totalCreditHours), 0) FROM semester WHERE status = 'ARCHIVED'")
    suspend fun getTotalArchivedCreditHours(): Double

    @Query("SELECT MAX(`order`) FROM semester")
    suspend fun getMaxOrder(): Int?

    @Query("DELETE FROM semester")
    suspend fun deleteAll()

    @Query("DELETE FROM semester WHERE status = 'ARCHIVED'")
    suspend fun deleteAllArchived()

    @Transaction
    suspend fun swapOrder(a: Semester, b: Semester) {
        update(a)
        update(b)
    }

}
