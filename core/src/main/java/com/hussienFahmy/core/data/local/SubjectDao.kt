package com.hussienfahmy.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.model.GradeName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

@Dao
interface SubjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(subject: Subject)

    @Query("DELETE FROM subject WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM subject")
    suspend fun deleteAll()

    @Query(
        "UPDATE subject SET gradeName = NULL WHERE id = :subjectId"
    )
    suspend fun clearGrade(subjectId: Long)

    @Query(
        "UPDATE subject SET gradeName = NULL"
    )
    suspend fun clearAllGrades()

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
    suspend fun updateGrade(subjectId: Long, gradeName: GradeName?)

    @Query("UPDATE subject SET oral=null, practical=null, midterm=null, project=null WHERE id = :subjectId")
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

    @Query("UPDATE subject SET project = :project WHERE id = :subjectId")
    suspend fun updateProject(subjectId: Long, project: Double?)

    // ---------------- MetaData Update -----------//

    @Query("UPDATE subject SET midtermAvailable = :available WHERE id = :subjectId")
    suspend fun setMidtermAvailability(subjectId: Long, available: Boolean)

    @Query("UPDATE subject SET practicalAvailable = :available WHERE id = :subjectId")
    suspend fun setPracticalAvailability(subjectId: Long, available: Boolean)

    @Query("UPDATE subject SET oralAvailable = :available WHERE id = :subjectId")
    suspend fun setOralAvailability(subjectId: Long, available: Boolean)

    @Query("UPDATE subject SET projectAvailable = :available WHERE id = :subjectId")
    suspend fun setProjectAvailability(subjectId: Long, available: Boolean)


    // ---------------- Semester with max grade can be achieved -----------//

    data class SubjectWithGrades(
        val subject: Subject,
        val maxGradeCanBeAssigned: Grade,
        val assignedGrade: Grade?
    )

    /**
     * Get all subjects ordered by credit hours
     */
    @Query("SELECT * FROM subject ORDER BY creditHours DESC")
    fun getAllSubjects(): Flow<List<Subject>>

    /**
     * Get all active grades
     */
    @Query("SELECT * FROM grade WHERE active ORDER BY percentage DESC")
    fun getAllActiveGrades(): Flow<List<Grade>>

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
    val subjectsWithAssignedGrade: Flow<List<SubjectWithGrades>>
        get() {
            return combine(
                getAllSubjects(), getAllActiveGrades()
            ) { subjects, allGrades ->
                val gradesForMax = allGrades.filter { grade ->
                    grade.name != GradeName.F
                }
                val highestGrade = (gradesForMax.ifEmpty { allGrades })
                    .maxByOrNull { it.percentage!! } // active grades always have percentage value
                val lowestPercentage = (gradesForMax.ifEmpty { allGrades })
                    .minByOrNull { it.percentage!! }?.percentage ?: 0.0

                subjects.map { subject ->
                    val semesterMarks = subject.semesterMarks
                    val maxGrade =
                        if (semesterMarks?.midterm == null && semesterMarks?.practical == null && semesterMarks?.oral == null && semesterMarks?.project == null) {
                            highestGrade
                        } else {
                            val semesterPercentage =
                                (100.0 * semesterMarks.value / subject.totalMarks)
                            val threshold = semesterPercentage + lowestPercentage

                            gradesForMax.filter { it.percentage!! <= threshold }
                                .maxByOrNull { it.percentage!! }
                        }

                    val assignedGrade = subject.gradeName?.let { gradeName ->
                        allGrades.find { it.name == gradeName }
                    }

                    SubjectWithGrades(
                        subject = subject,
                        maxGradeCanBeAssigned = maxGrade!!,
                        assignedGrade = assignedGrade
                    )
                }
            }.flowOn(Dispatchers.IO)
        }
}