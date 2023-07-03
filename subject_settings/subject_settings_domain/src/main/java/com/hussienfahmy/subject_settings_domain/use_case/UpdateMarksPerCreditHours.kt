package com.hussienfahmy.subject_settings_domain.use_case

import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.domain.subject_settings.repository.SubjectSettingsRepository
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R

class UpdateMarksPerCreditHours(
    private val repository: SubjectSettingsRepository,
    private val applySettingsToSubjects: ApplySettingsToSubjects,
) {
    suspend operator fun invoke(marksPerCreditHours: String): UpdateResult {
        if (marksPerCreditHours.isBlank()) return UpdateResult.Failed(
            UiText.StringResource(R.string.cannot_be_empty)
        )

        if (marksPerCreditHours.toDoubleOrNull() == null) return UpdateResult.Failed(
            UiText.StringResource(R.string.invalid_number)
        )

        repository.updateMarksPerCreditHour(marksPerCreditHours.toDouble())
        applySettingsToSubjects()

        return UpdateResult.Success
    }
}