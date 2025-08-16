package com.hussienfahmy.subject_settings_domain.use_case

import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.subject_settings.repository.SubjectSettingsRepository
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.myGpaManager.core.R

class UpdateConstantMarks(
    private val repository: SubjectSettingsRepository,
    private val applySettingsToSubjects: ApplySettingsToSubjects,
) {
    suspend operator fun invoke(constantMarks: String): UpdateResult {
        if (constantMarks.isBlank()) return UpdateResult.Failed(
            UiText.StringResource(R.string.cannot_be_empty)
        )

        if (constantMarks.toDoubleOrNull() == null) return UpdateResult.Failed(
            UiText.StringResource(R.string.invalid_number)
        )

        repository.updateConstantMarks(constantMarks.toDouble())
        applySettingsToSubjects()

        return UpdateResult.Success
    }
}