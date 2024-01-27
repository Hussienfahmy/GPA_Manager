package com.hussienfahmy.sync_domain.repository

import com.hussienfahmy.sync_domain.model.NetworkSubjects
import com.hussienfahmy.sync_domain.model.Settings
import com.hussienfahmy.sync_domain.model.Subject

interface SyncRepository {

    suspend fun uploadSubjects(subjects: List<Subject>)

    suspend fun downloadSubjects(): NetworkSubjects?

    suspend fun uploadSettings(settings: Settings)

    suspend fun downloadSettings(): Settings?
}