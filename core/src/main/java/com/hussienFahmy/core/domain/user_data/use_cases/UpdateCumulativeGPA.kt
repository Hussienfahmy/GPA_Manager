package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.myGpaManager.core.R

class UpdateCumulativeGPA(
    private val repository: UserDataRepository,
    private val getGPASettings: GetGPASettings,
) {

    suspend operator fun invoke(cumulativeGPAString: String): UpdateResult {
        val cumulativeGPA = cumulativeGPAString.toDoubleOrNull()
            ?: return UpdateResult.Failed(UiText.StringResource(R.string.invalid_input))
        if (cumulativeGPA < 0.0) {
            return UpdateResult.Failed(UiText.StringResource(R.string.cannot_be_negative))
        }
        val maxGPA = getGPASettings().system.number
        if (cumulativeGPA > maxGPA) {
            return UpdateResult.Failed(UiText.StringResource(R.string.above_max))
        }

        repository.updateCumulativeGPA(cumulativeGPA)
        return UpdateResult.Success
    }
}
