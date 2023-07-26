package com.hussienFahmy.core.domain.user_data.use_cases

import com.hussienFahmy.core.domain.user_data.model.UserData
import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository

class UpdateSemester(
    private val repository: UserDataRepository,
) {
    suspend operator fun invoke(semester: UserData.AcademicInfo.Semester) {
        repository.updateSemester(semester)
    }
}
