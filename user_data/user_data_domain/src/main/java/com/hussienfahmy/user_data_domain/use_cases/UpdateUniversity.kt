package com.hussienfahmy.user_data_domain.use_cases

import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R
import com.hussienfahmy.user_data_domain.repository.UserDataRepository

class UpdateUniversity(
    private val repository: UserDataRepository
) {
    suspend operator fun invoke(university: String): UpdateResult {
        if (university.isBlank()) return UpdateResult.Failed(UiText.StringResource(R.string.cannot_be_empty))
        repository.updateUniversity(university)
        return UpdateResult.Success
    }
}
