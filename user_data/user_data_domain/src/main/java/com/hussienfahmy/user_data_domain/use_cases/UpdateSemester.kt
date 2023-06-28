package com.hussienfahmy.user_data_domain.use_cases

import com.hussienfahmy.user_data_domain.model.UserData
import com.hussienfahmy.user_data_domain.repository.UserDataRepository

class UpdateSemester(
    private val repository: UserDataRepository,
) {
    suspend operator fun invoke(semester: UserData.AcademicInfo.Semester) {
        repository.updateSemester(semester)
    }
}
