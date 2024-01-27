package com.hussienfahmy.sync_domain.use_case

import com.hussienFahmy.core.data.local.GradeDao
import com.hussienFahmy.core.domain.gpa_settings.use_case.UpdateGPASystem
import com.hussienFahmy.core.domain.subject_settings.repository.SubjectSettingsRepository
import com.hussienfahmy.sync_domain.model.toGrades
import com.hussienfahmy.sync_domain.repository.SyncRepository

class PullSettings(
    private val repository: SyncRepository,
    private val updateGPASystem: UpdateGPASystem,
    private val gradeDao: GradeDao,
    private val subjectSettingsRepository: SubjectSettingsRepository,
) {
    suspend operator fun invoke() {
        val settings = repository.downloadSettings() ?: return

        updateGPASystem(settings.calculationSettings.gpaSystem)
        settings.networkGrades.toGrades().onEach {
            gradeDao.upsert(it)
        }

        with(settings.calculationSettings) {
            subjectSettingsRepository.updateSubjectsDependsOn(subjectsMarksDependsOn)
            subjectSettingsRepository.updateConstantMarks(constantMarks)
            subjectSettingsRepository.updateMarksPerCreditHour(marksPerCreditHour)
        }
    }
}