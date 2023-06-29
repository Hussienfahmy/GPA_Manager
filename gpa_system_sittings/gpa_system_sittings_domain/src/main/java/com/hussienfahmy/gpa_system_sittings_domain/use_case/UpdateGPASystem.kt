package com.hussienfahmy.gpa_system_sittings_domain.use_case

import com.hussienfahmy.gpa_system_sittings_domain.model.GPA
import com.hussienfahmy.gpa_system_sittings_domain.repository.GPASystemRepository

class UpdateGPASystem(
    private val repository: GPASystemRepository,
) {
    suspend operator fun invoke(system: GPA.System) = repository.updateGPASystem(system)
}