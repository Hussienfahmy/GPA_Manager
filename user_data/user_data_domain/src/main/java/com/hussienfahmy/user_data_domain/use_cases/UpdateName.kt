package com.hussienfahmy.user_data_domain.use_cases

import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R

class UpdateName(
    private val repository: UserDataRepository,
) {
    suspend operator fun invoke(name: String): UpdateResult {
        if (name.isBlank()) return UpdateResult.Failed(UiText.StringResource(R.string.name_is_required))
        repository.updateName(name)
        return UpdateResult.Success
    }
}
