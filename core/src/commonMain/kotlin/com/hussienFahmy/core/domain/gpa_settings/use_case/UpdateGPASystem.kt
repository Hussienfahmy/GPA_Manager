package com.hussienfahmy.core.domain.gpa_settings.use_case

import com.hussienfahmy.core.domain.gpa_settings.model.GPA
import com.hussienfahmy.core.domain.gpa_settings.repository.GPASettingsRepository
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker

class UpdateGPASystem(
    private val repository: GPASettingsRepository,
    private val dirtyTracker: SyncDirtyTracker,
) {
    suspend operator fun invoke(system: GPA.System) {
        repository.updateGPASystem(system)
        dirtyTracker.markSettingsChanged()
    }
}