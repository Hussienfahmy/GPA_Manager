package com.hussienfahmy.sync_domain.model

import androidx.annotation.Keep
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.data.local.entity.Subject as SubjectEntity

@Keep
data class Subject(
    val id: Long = 0,
    val name: String = "",
    val creditHours: Double = 0.0,
    val gradeName: GradeName? = null,
    val totalMarks: Double = 0.0,
    val semesterMarks: SemesterMarks? = null,
    val metadata: MetaData = MetaData()
) {

    @Keep
    data class SemesterMarks(
        val midterm: Double? = null,
        val practical: Double? = null,
        val oral: Double? = null,
    )

    @Keep
    data class MetaData(
        val maxGradeNameCanAchieve: GradeName = GradeName.A,
        val midtermAvailable: Boolean = true,
        val practicalAvailable: Boolean = true,
        val oralAvailable: Boolean = true,
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
            oral = subject.semesterMarks?.oral
        ),
        metadata = MetaData(
            maxGradeNameCanAchieve = maxGradeNameCanAchieve,
            midtermAvailable = subject.metadata.midtermAvailable,
            practicalAvailable = subject.metadata.practicalAvailable,
            oralAvailable = subject.metadata.oralAvailable
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
            oral = semesterMarks?.oral
        ),
        metadata = SubjectEntity.MetaData(
            midtermAvailable = metadata.midtermAvailable,
            practicalAvailable = metadata.practicalAvailable,
            oralAvailable = metadata.oralAvailable
        )
    )
}