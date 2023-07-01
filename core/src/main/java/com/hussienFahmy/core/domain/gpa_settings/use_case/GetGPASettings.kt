package com.hussienFahmy.core.domain.gpa_settings.use_case

import com.hussienFahmy.core.domain.gpa_settings.repository.GPASettingsRepository

class GetGPASettings(
    private val repository: GPASettingsRepository,
) {
    suspend operator fun invoke() = repository.getGPASettings()
}