package com.hussienfahmy.gpa_system_sittings_domain.use_case

import com.hussienfahmy.core.domain.gpa_settings.repository.GPASettingsRepository

class ObserveGPASettings(
    private val repository: GPASettingsRepository,
) {
    operator fun invoke() = repository.observeGPA()
}