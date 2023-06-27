package com.hussienfahmy.grades_setting_domain.model

import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.model.GradeName

data class GradeSetting(
    val name: GradeName,
    val percentage: Double?,
    val points: Double?,
    val active: Boolean
) {
    val id get() = name.ordinal
    val symbol = name.symbol

    constructor(grade: Grade) : this(
        name = grade.metaData,
        percentage = grade.percentage,
        points = grade.points,
        active = grade.active
    )
}