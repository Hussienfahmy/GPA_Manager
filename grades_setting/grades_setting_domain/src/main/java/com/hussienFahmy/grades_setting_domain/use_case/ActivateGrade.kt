package com.hussienfahmy.grades_setting_domain.use_case

import com.hussienfahmy.core.R
import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.grades_setting_domain.model.GradeSetting

class ActivateGrade(
    private val gradeDao: GradeDao,
    private val subjectDao: SubjectDao,
    private val getGradeByName: GetGradeByName,
) {
    suspend operator fun invoke(gradeSetting: GradeSetting, isActive: Boolean): UpdateResult {
        val grade =
            getGradeByName(gradeSetting.name)
                ?: return UpdateResult.Failed(UiText.StringResource(R.string.err_grade_not_found))

        return if (isActive) {
            // the user want to active the grade so we have to check if that possible
            if (grade.percentage != null && grade.points != null) {
                gradeDao.setActive(grade.name, true)
                UpdateResult.Success
            } else {
                UpdateResult.Failed(UiText.StringResource(R.string.err_cant_activate))
            }
        } else {
            gradeDao.setActive(grade.name, false)
            subjectDao.clearUnAvailableGrades()
            UpdateResult.Success
        }
    }
}