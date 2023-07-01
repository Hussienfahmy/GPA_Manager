package com.hussienfahmy.gpa_system_sittings_domain.use_case

import com.hussienFahmy.core.domain.gpa_settings.model.GPA
import com.hussienFahmy.core.domain.gpa_settings.repository.GPASettingsRepository

class UpdateGPASystem(
    private val repository: GPASettingsRepository,
) {
    suspend operator fun invoke(system: GPA.System) = repository.updateGPASystem(system)
}