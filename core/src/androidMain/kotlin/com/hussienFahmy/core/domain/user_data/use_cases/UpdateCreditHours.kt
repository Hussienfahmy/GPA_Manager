package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.R
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.core.model.UiText

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
