package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.domain.sync.SyncUpload

class SyncUploadImpl(
    private val pushSubjects: PushSubjects,
    private val pushSettings: PushSettings,
) : SyncUpload {
    override suspend operator fun invoke() {
        pushSubjects()
        pushSettings()
    }
}