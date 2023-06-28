package com.hussienfahmy.user_data_domain.use_cases

import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R
import com.hussienfahmy.user_data_domain.repository.UserDataRepository

class UpdateCumulativeGPA(
    private val repository: UserDataRepository
) {
    // todo add validation from the max gpa later
    suspend operator fun invoke(cumulativeGPAString: String): UpdateResult {
        val cumulativeGPA = cumulativeGPAString.toDoubleOrNull()
            ?: return UpdateResult.Failed(UiText.StringResource(R.string.invalid_input))
        if (cumulativeGPA < 0.0) {
            return UpdateResult.Failed(UiText.StringResource(R.string.cannot_be_negative))
        }
        repository.updateCumulativeGPA(cumulativeGPA)
        return UpdateResult.Success
    }
}
