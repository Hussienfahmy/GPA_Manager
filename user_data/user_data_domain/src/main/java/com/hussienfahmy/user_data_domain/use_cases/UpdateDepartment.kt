package com.hussienfahmy.user_data_domain.use_cases

import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.myGpaManager.core.R
import com.hussienfahmy.user_data_domain.repository.UserDataRepository

class UpdateDepartment(
    private val repository: UserDataRepository
) {
    suspend operator fun invoke(department: String): UpdateResult {
        if (department.isBlank()) return UpdateResult.Failed(UiText.StringResource(R.string.cannot_be_empty))
        repository.updateDepartment(department)
        return UpdateResult.Success
    }
}
