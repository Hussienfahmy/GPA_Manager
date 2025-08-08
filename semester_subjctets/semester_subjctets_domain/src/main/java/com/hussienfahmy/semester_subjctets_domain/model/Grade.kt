package com.hussienfahmy.semester_subjctets_domain.model

import com.hussienFahmy.core.data.local.entity.Grade
import com.hussienFahmy.core.data.local.model.GradeName

data class Grade(
    val name: GradeName,
    val points: Double,
    val percentage: Double,
) {
    constructor(grade: Grade) : this(
        name = grade.name,
        points = grade.points!!,
        percentage = grade.percentage!!
    )
}
