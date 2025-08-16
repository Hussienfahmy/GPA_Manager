package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.myGpaManager.core.R

class UpdateUniversity(
    private val repository: UserDataRepository
) {
    suspend operator fun invoke(university: String): UpdateResult {
        if (university.isBlank()) return UpdateResult.Failed(UiText.StringResource(R.string.cannot_be_empty))
        repository.updateUniversity(university)
        return UpdateResult.Success
    }
}
