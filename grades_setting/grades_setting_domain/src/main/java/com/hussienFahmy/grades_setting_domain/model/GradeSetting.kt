package com.hussienFahmy.grades_setting_domain.model

import com.hussienFahmy.core.data.local.entity.Grade
import com.hussienFahmy.core.data.local.model.GradeName

data class GradeSetting(
    val name: GradeName,
    val percentage: Double?,
    val points: Double?,
    val active: Boolean
) {
    val id get() = name.ordinal
    val symbol = name.symbol

    constructor(grade: Grade) : this(
        name = grade.name,
        percentage = grade.percentage,
        points = grade.points,
        active = grade.active
    )
}