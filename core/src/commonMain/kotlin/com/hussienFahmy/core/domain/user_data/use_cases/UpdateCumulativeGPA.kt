package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.core.model.UiText

class UpdateCumulativeGPA(
    private val repository: UserDataRepository,
    private val getGPASettings: GetGPASettings,
) {

    suspend operator fun invoke(cumulativeGPAString: String): UpdateResult {
        val cumulativeGPA = cumulativeGPAString.toDoubleOrNull()
            ?: return UpdateResult.Failed(UiText.Resource(Res.string.invalid_input))
        if (cumulativeGPA < 0.0) {
            return UpdateResult.Failed(UiText.Resource(Res.string.cannot_be_negative))
        }
        val maxGPA = getGPASettings().system.number
        if (cumulativeGPA > maxGPA) {
            return UpdateResult.Failed(UiText.Resource(Res.string.above_max))
        }

        repository.updateCumulativeGPA(cumulativeGPA)
        return UpdateResult.Success
    }
}
