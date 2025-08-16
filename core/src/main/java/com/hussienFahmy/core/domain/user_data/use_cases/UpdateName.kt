package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.myGpaManager.core.R

class UpdateName(
    private val repository: UserDataRepository,
) {
    suspend operator fun invoke(name: String): UpdateResult {
        if (name.isBlank()) return UpdateResult.Failed(UiText.StringResource(R.string.name_is_required))
        repository.updateName(name)
        return UpdateResult.Success
    }
}
