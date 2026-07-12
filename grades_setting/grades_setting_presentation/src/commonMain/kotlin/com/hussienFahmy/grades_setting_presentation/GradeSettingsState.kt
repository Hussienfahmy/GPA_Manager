package com.hussienfahmy.grades_setting_presentation

import com.hussienfahmy.grades_setting_domain.model.GradeSetting
import com.hussienfahmy.grades_setting_presentation.model.Mode

data class GradeSettingsState(
    val gradesSetting: List<GradeSetting> = emptyList(),
    val isLoading: Boolean = true,
    val mode: Mode = Mode.ALL
)