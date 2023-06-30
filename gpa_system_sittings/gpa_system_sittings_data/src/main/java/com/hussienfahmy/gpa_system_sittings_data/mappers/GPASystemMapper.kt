package com.hussienfahmy.gpa_system_sittings_data.mappers

import com.hussienfahmy.gpa_system_sittings_data.model.GPA
import com.hussienfahmy.gpa_system_sittings_domain.model.GPA as DomainGPA


internal fun GPA.toDomain() = DomainGPA(
    system = when (system) {
        GPA.System.FOUR -> DomainGPA.System.FOUR
        GPA.System.FIVE -> DomainGPA.System.FIVE
    }
)