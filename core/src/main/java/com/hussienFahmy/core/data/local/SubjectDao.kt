package com.hussienfahmy.core.data.local

import androidx.room.*
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.model.GradeName
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {


    /**
     * Get all subjects with their grades
     *
     * the max_grade field will be the grade with highest active percentage value of there is no semester mark values
     * otherwise will be the grade withe percentage equal or just below the semester mark percentage + the lowest grade percentage
     *
     * example:
     * oral is null and midterm is null and practical is null the max_grade will be A
     * oral is 10 and midterm is 5 and practical is 10 and the total marks is 100
     * the semester mark will be 25 from 100
     * e.g 25% + 60% (D grade) = 85% and max_grade will be A-
     */
    @get:Query(
        """
        SELECT*,
            CASE 
                WHEN midterm is null AND practical is null AND oral is null 
                    THEN (SELECT grade.meta_data as max_grade FROM grade
                                 WHERE grade.active ORDER BY grade.percentage DESC LIMIT 1)
                
                ELSE (SELECT grade.meta_data  FROM grade WHERE grade.active 
                        AND grade.percentage <= ((100 * (COALESCE(midterm, 0) + COALESCE(practical, 0) + COALESCE(oral, 0)) / totalMarks)
                        + (SELECT percentage FROM grade WHERE percentage > 0 ORDER BY percentage ASC LIMIT 1))
                        ORDER BY percentage DESC LIMIT 1)
            END as max_grade
        FROM subject LEFT JOIN grade ON gradeName = grade.meta_data ORDER BY creditHours DESC
        """
    )
    val subjectsWithGrades: Flow<Map<Subject, Grade?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(subject: Subject)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(subjects: List<Subject>)

    @Delete
    suspend fun delete(subject: Subject)

    @Query("DELETE FROM subject")
    suspend fun deleteAll()

    @Query(
        "UPDATE subject SET gradeName = NULL WHERE id = :subjectId"
    )
    suspend fun resetSubject(subjectId: Long)

    @Query(
        "UPDATE subject SET gradeName = NULL"
    )
    suspend fun resetAllSubjects()

    // ---------------- Subject Update -----------//

    @Query("UPDATE subject SET name = :name WHERE id = :subjectId")
    suspend fun updateName(subjectId: Long, name: String)

    @Query("UPDATE subject SET creditHours = :creditHours WHERE id = :subjectId")
    suspend fun updateCreditHours(subjectId: Long, creditHours: Double)

    @Query("UPDATE subject SET totalMarks = :totalMarks WHERE id = :subjectId")
    suspend fun updateTotalMarks(subjectId: Long, totalMarks: Double)

    @Query("UPDATE subject SET totalMarks = :constTotalMarks")
    suspend fun updateTotalMarksConst(constTotalMarks: Double)

    @Query("UPDATE subject SET totalMarks = creditHours * :marksPerCredit")
    suspend fun updateTotalMarksPerCredit(marksPerCredit: Double)

    @Query("UPDATE subject SET gradeName = :gradeName WHERE id = :subjectId")
    suspend fun updateGradeName(subjectId: Long, gradeName: GradeName?)

    @Query("UPDATE subject SET oral=null, practical=null, midterm=null WHERE id = :subjectId")
    suspend fun resetSubjectMarks(subjectId: Long)

    /**
     * Clear un available grades that has been set to the subjects.
     * call this method when changing the [Grade.active] property in the database
     * to remove all inactive grades from the subjects table
     */
    @Query(
        """
        UPDATE subject SET gradeName = NULL 
        WHERE gradeName IN(SELECT grade.meta_data FROM grade WHERE grade.active = 0)
    """
    )
    suspend fun clearUnAvailableGrades()

    // ---------------- SemesterMark Update -----------//

    @Query("UPDATE subject SET midterm = :midterm WHERE id = :subjectId")
    suspend fun updateMidterm(subjectId: Long, midterm: Double?)

    @Query("UPDATE subject SET practical = :practical WHERE id = :subjectId")
    suspend fun updatePractical(subjectId: Long, practical: Double?)

    @Query("UPDATE subject SET oral = :oral WHERE id = :subjectId")
    suspend fun updateOral(subjectId: Long, oral: Double?)

    // ---------------- MetaData Update -----------//

    @Query("UPDATE subject SET midtermAvailable = :available WHERE id = :subjectId")
    suspend fun setMidtermAvailability(subjectId: Long, available: Boolean)

    @Query("UPDATE subject SET practicalAvailable = :available WHERE id = :subjectId")
    suspend fun setPracticalAvailability(subjectId: Long, available: Boolean)

    @Query("UPDATE subject SET oralAvailable = :available WHERE id = :subjectId")
    suspend fun setOralAvailability(subjectId: Long, available: Boolean)
}