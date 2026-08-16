package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.domain.gpa_settings.repository.GPASettingsRepository
import com.hussienfahmy.core.domain.subject_settings.repository.SubjectSettingsRepository
import com.hussienfahmy.sync_domain.model.toGrades
import com.hussienfahmy.sync_domain.repository.SyncRepository

// Writes pulled data straight to the repositories/DAOs rather than through the equivalent
// user-facing use cases (UpdateGPASystem, etc.) - those mark the sync dirty flag, which would
// make every pull immediately re-push the data it just downloaded.
class PullSettings(
    private val repository: SyncRepository,
    private val gpaSettingsRepository: GPASettingsRepository,
    private val gradeDao: GradeDao,
    private val subjectSettingsRepository: SubjectSettingsRepository,
) {
    suspend operator fun invoke(userId: String) {
        val settings = repository.downloadSettings(userId) ?: return

        gpaSettingsRepository.updateGPASystem(settings.calculationSettings.gpaSystem)
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
