package com.hussienfahmy.sync_domain.repository

import com.hussienfahmy.sync_domain.model.NetworkSemester
import com.hussienfahmy.sync_domain.model.NetworkSubjects
import com.hussienfahmy.sync_domain.model.Settings
import com.hussienfahmy.sync_domain.model.Subject

interface SyncRepository {

    suspend fun uploadSubjects(userId: String, subjects: List<Subject>)

    suspend fun downloadSubjects(userId: String): NetworkSubjects?

    suspend fun uploadSettings(userId: String, settings: Settings)

    suspend fun downloadSettings(userId: String): Settings?

    suspend fun uploadSemesters(userId: String, semesters: List<NetworkSemester>)

    suspend fun downloadSemesters(userId: String): List<NetworkSemester>?

    suspend fun updateAcademicProgress(userId: String, cumulativeGPA: Double, creditHours: Int)

    suspend fun isLegacyGpaMigrated(userId: String): Boolean

    suspend fun setLegacyGpaMigrated(userId: String)
}