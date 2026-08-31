package com.hussienfahmy.sync_domain.model

import com.hussienfahmy.core.data.local.model.GradeName
import kotlinx.serialization.Serializable
import com.hussienfahmy.core.data.local.entity.Subject as SubjectEntity

// @Serializable needed for FirebaseNetworkSubjects/FirebaseNetworkSemester's Firestore round-trip
// (app module) - this is the same Subject already used app-wide, not a separate Firebase DTO.
@Serializable
data class Subject(
    val id: Long = 0,
    val name: String = "",
    val creditHours: Double = 0.0,
    val gradeName: GradeName? = null,
    val totalMarks: Double = 0.0,
    val semesterMarks: SemesterMarks? = null,
    val metadata: MetaData = MetaData()
) {

    @Serializable
    data class SemesterMarks(
        val midterm: Double? = null,
        val practical: Double? = null,
        val oral: Double? = null,
        val project: Double? = null,
    )

    @Serializable
    data class MetaData(
        val midtermAvailable: Boolean = true,
        val practicalAvailable: Boolean = true,
        val oralAvailable: Boolean = true,
        val projectAvailable: Boolean = false,
    )

    constructor(subject: SubjectEntity, maxGradeNameCanAchieve: GradeName) : this(
        id = subject.id,
        name = subject.name,
        creditHours = subject.creditHours,
        gradeName = subject.gradeName,
        totalMarks = subject.totalMarks,
        semesterMarks = SemesterMarks(
            midterm = subject.semesterMarks?.midterm,
            practical = subject.semesterMarks?.practical,
            oral = subject.semesterMarks?.oral,
            project = subject.semesterMarks?.project,
        ),
        metadata = MetaData(
            midtermAvailable = subject.metadata.midtermAvailable,
            practicalAvailable = subject.metadata.practicalAvailable,
            oralAvailable = subject.metadata.oralAvailable,
            projectAvailable = subject.metadata.projectAvailable,
        )
    )

    fun toEntity(): SubjectEntity = SubjectEntity(
        id = id,
        name = name,
        creditHours = creditHours,
        gradeName = gradeName,
        totalMarks = totalMarks,
        semesterMarks = SubjectEntity.SemesterMarks(
            midterm = semesterMarks?.midterm,
            practical = semesterMarks?.practical,
            oral = semesterMarks?.oral,
            project = semesterMarks?.project,
        ),
        metadata = SubjectEntity.MetaData(
            midtermAvailable = metadata.midtermAvailable,
            practicalAvailable = metadata.practicalAvailable,
            oralAvailable = metadata.oralAvailable,
            projectAvailable = metadata.projectAvailable,
        )
    )
}