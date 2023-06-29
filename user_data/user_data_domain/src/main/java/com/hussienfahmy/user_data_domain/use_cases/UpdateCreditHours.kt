package com.hussienfahmy.user_data_domain.use_cases

import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R

class UpdateCreditHours(
    private val repository: UserDataRepository,
) {
    suspend operator fun invoke(creditHoursString: String): UpdateResult {
        val creditHours = creditHoursString.toIntOrNull()
            ?: return UpdateResult.Failed(UiText.StringResource(R.string.invalid_input))

        if (creditHours < 0) return UpdateResult.Failed(UiText.StringResource(R.string.cannot_be_negative))
        repository.updateCreditHours(creditHours)
        return UpdateResult.Success
    }
}
