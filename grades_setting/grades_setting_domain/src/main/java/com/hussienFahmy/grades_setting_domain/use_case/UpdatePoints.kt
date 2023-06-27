package com.hussienFahmy.grades_setting_domain.use_case

import com.hussienFahmy.core.data.local.GradeDao
import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.grades_setting_domain.model.GradeSetting
import com.hussienFahmy.myGpaManager.core.R

class UpdatePoints(
    private val gradeDao: GradeDao,
) {
    suspend operator fun invoke(gradeSetting: GradeSetting, pointsString: String): UpdateResult {
        val points = pointsString.toDoubleOrNull()
        return if (points != null && points < 0.0) {
            UpdateResult.Failed(UiText.StringResource(R.string.err_points_less_zero))
        } else {
            gradeDao.updatePoints(gradeSetting.name, points)
            UpdateResult.Success
        }
    }
}