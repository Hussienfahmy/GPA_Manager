package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.R
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.core.model.UiText

class UpdateFaculty(
    private val repository: UserDataRepository,
) {
    suspend operator fun invoke(faculty: String): UpdateResult {
        if (faculty.isBlank()) return UpdateResult.Failed(UiText.StringResource(R.string.cannot_be_empty))
        repository.updateFaculty(faculty)
        return UpdateResult.Success
    }
}
