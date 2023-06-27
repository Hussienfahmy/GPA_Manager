package com.hussienFahmy.grades_setting_domain.use_case

data class GradeSettingsUseCases(
    val loadGrades: LoadGrades,
    val updatePoints: UpdatePoints,
    val updatePercentage: UpdatePercentage,
    val activateGrade: ActivateGrade,
)