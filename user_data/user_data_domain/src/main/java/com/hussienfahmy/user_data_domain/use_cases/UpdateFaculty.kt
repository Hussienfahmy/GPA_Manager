package com.hussienfahmy.user_data_domain.use_cases

import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R
import com.hussienfahmy.user_data_domain.repository.UserDataRepository

class UpdateFaculty(
    private val repository: UserDataRepository,
) {
    suspend operator fun invoke(faculty: String): UpdateResult {
        if (faculty.isBlank()) return UpdateResult.Failed(UiText.StringResource(R.string.cannot_be_empty))
        repository.updateFaculty(faculty)
        return UpdateResult.Success
    }
}
