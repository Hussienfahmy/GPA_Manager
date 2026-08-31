package com.hussienfahmy.core.domain.gpa_settings.use_case

import com.hussienfahmy.core.domain.gpa_settings.repository.GPASettingsRepository

class GetGPASettings(
    private val repository: GPASettingsRepository,
) {
    suspend operator fun invoke() = repository.getGPASettings()
}