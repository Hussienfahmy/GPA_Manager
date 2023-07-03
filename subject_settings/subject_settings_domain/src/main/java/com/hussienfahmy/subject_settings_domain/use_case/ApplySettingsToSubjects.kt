package com.hussienfahmy.subject_settings_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienFahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienFahmy.core.domain.subject_settings.use_case.GetSubjectsSettings

class ApplySettingsToSubjects(
    private val subjectDao: SubjectDao,
    private val getSubjectsSettings: GetSubjectsSettings,
) {
    suspend operator fun invoke() {
        val settings = getSubjectsSettings()
        when (settings.subjectsMarksDependsOn) {
            SubjectSettings.SubjectsMarksDependsOn.CREDIT -> subjectDao.updateTotalMarksPerCredit(
                settings.marksPerCreditHour
            )

            SubjectSettings.SubjectsMarksDependsOn.CONSTANT -> subjectDao.updateTotalMarksConst(
                settings.constantMarks
            )
        }
    }
}