package com.hussienfahmy.sync_domain.use_case

class SyncUpload(
    private val pushSubjects: PushSubjects,
    private val pushSettings: PushSettings,
) {
    suspend operator fun invoke() {
        pushSubjects()
        pushSettings()
    }
}