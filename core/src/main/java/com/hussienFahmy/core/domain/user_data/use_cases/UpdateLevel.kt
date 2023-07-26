package com.hussienFahmy.core.domain.user_data.use_cases

import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R

class UpdateLevel(
    private val repository: UserDataRepository
) {
    suspend operator fun invoke(level: String): UpdateResult {
        if (level.isBlank()) return UpdateResult.Failed(UiText.StringResource(R.string.cannot_be_empty))
        val levelInt = level.toIntOrNull()
        if (levelInt == null || levelInt < 1) return UpdateResult.Failed(UiText.StringResource(R.string.cannot_be_less_than_one))

        repository.updateLevel(levelInt)

        return UpdateResult.Success
    }
}
