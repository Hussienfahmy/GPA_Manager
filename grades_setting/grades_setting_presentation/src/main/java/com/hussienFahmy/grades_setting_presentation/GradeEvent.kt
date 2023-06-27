package com.hussienFahmy.grades_setting_presentation

import com.hussienFahmy.grades_setting_domain.model.GradeSetting

sealed class GradeEvent {
    data class UpdatePoints(val gradeSetting: GradeSetting, val points: String) : GradeEvent()
    data class UpdatePercentage(val gradeSetting: GradeSetting, val percentage: String) :
        GradeEvent()

    data class ActivateGrade(val gradeSetting: GradeSetting, val isActive: Boolean) : GradeEvent()
}
