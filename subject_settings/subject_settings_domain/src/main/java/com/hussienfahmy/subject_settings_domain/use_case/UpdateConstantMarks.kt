package com.hussienfahmy.subject_settings_domain.use_case

import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R
import com.hussienfahmy.subject_settings_domain.repository.SubjectSettingsRepository

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