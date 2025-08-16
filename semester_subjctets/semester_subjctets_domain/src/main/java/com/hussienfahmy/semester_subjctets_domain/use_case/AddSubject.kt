package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienfahmy.core.domain.subject_settings.use_case.GetSubjectsSettings
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.myGpaManager.core.R

class AddSubject(
    private val subjectDao: SubjectDao,
    private val getSubjectsSettings: GetSubjectsSettings,
) {
    suspend operator fun invoke(
        name: String,
        hours: String,
        midtermAvailable: Boolean,
        practicalAvailable: Boolean,
        oralAvailable: Boolean,
        projectAvailable: Boolean,
    ): UpdateResult {
        val creditHours = hours.toDoubleOrNull()
            ?: return UpdateResult.Failed(UiText.StringResource(R.string.invalid_input))

        if (name.isBlank()) return UpdateResult.Failed(UiText.StringResource(R.string.err_subject_name_empty))
        if (creditHours <= 0) return UpdateResult.Failed(UiText.StringResource(R.string.err_subject_credit_hours_negative))

        val subjectSettings = getSubjectsSettings()

        val subject = Subject(
            name = name,
            creditHours = creditHours,
            totalMarks = when (subjectSettings.subjectsMarksDependsOn) {
                SubjectSettings.SubjectsMarksDependsOn.CREDIT -> subjectSettings.marksPerCreditHour * creditHours
                SubjectSettings.SubjectsMarksDependsOn.CONSTANT -> subjectSettings.constantMarks
            },
            metadata = Subject.MetaData(
                midtermAvailable = midtermAvailable,
                practicalAvailable = practicalAvailable,
                oralAvailable = oralAvailable,
                projectAvailable = projectAvailable,
            )
        )

        subjectDao.upsert(subject)

        return UpdateResult.Success
    }
}