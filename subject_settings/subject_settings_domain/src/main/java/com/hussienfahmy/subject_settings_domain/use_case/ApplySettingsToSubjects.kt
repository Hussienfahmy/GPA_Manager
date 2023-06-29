package com.hussienfahmy.subject_settings_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienfahmy.subject_settings_domain.model.SubjectSettings

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