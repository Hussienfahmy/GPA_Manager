package com.hussienfahmy.grades_setting_domain.use_case

import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.grades_setting_domain.model.GradeSetting
import com.hussienfahmy.myGpaManager.core.R

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