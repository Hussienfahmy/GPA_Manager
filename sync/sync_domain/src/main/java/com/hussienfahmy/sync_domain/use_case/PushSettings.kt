package com.hussienfahmy.sync_domain.use_case

import com.hussienFahmy.core.data.local.GradeDao
import com.hussienFahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienFahmy.core.domain.subject_settings.repository.SubjectSettingsRepository
import com.hussienfahmy.sync_domain.model.CalculationSettings
import com.hussienfahmy.sync_domain.model.Settings
import com.hussienfahmy.sync_domain.model.toNetworkGrades
import com.hussienfahmy.sync_domain.repository.SyncRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PushSettings(
    private val repository: SyncRepository,
    private val getGPASettings: GetGPASettings,
    private val gradeDao: GradeDao,
    private val subjectSettingsRepository: SubjectSettingsRepository,
) {
    suspend operator fun invoke() {
        val subjectSettings = subjectSettingsRepository.getSubjectSettings()

        val settings = Settings(
            calculationSettings = CalculationSettings(
                gpaSystem = getGPASettings().system,
                subjectsMarksDependsOn = subjectSettings.subjectsMarksDependsOn,
                constantMarks = subjectSettings.constantMarks,
                marksPerCreditHour = subjectSettings.marksPerCreditHour,
            ),
            networkGrades = gradeDao.grades.map { it.toNetworkGrades() }.first()
        )

        repository.uploadSettings(settings)
    }
}