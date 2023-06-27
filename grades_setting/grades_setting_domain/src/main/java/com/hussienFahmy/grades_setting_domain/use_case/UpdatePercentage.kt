package com.hussienFahmy.grades_setting_domain.use_case

import com.hussienFahmy.core.data.local.GradeDao
import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.grades_setting_domain.model.GradeSetting
import com.hussienFahmy.myGpaManager.core.R

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