package com.hussienfahmy.grades_setting_domain.use_case

import com.hussienFahmy.myGpaManager.core.R
import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.grades_setting_domain.model.GradeSetting

class UpdatePercentage(
    private val gradeDao: GradeDao,
) {
    suspend operator fun invoke(
        gradeSetting: GradeSetting,
        percentageString: String
    ): UpdateResult {
        val percentage = percentageString.toDoubleOrNull()
        return if (percentage != null && percentage !in 0.0..100.0) {
            UpdateResult.Failed(UiText.StringResource(R.string.err_percentage_range))
        } else {
            gradeDao.updatePercentage(gradeSetting.name, percentage)
            UpdateResult.Success
        }
    }
}