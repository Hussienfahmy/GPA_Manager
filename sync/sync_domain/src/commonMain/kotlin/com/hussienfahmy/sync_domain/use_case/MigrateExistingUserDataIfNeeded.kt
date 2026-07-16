package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.entity.Semester
import com.hussienfahmy.core.domain.sync.SyncUpload
import com.hussienfahmy.core.domain.user_data.use_cases.GetUserData
import com.hussienfahmy.sync_domain.repository.SyncRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class MigrateExistingUserDataIfNeeded(
    private val syncRepository: SyncRepository,
    private val syncUpload: SyncUpload,
    private val semesterDao: SemesterDao,
    private val getUserData: GetUserData,
) {

    suspend operator fun invoke(userId: String) {
        val isLegacyMigrated = syncRepository.isLegacyGpaMigrated(userId)
        if (!isLegacyMigrated) {
            val migrated = migrateExistingUserData()
            syncRepository.setLegacyGpaMigrated(userId)

            if (migrated) {
                syncUpload()
            }
        }
    }

    private suspend fun migrateExistingUserData(): Boolean {
        val userData = getUserData().filterNotNull().first()
        val cumulativeGPA = userData.academicProgress.cumulativeGPA
        val creditHours = userData.academicProgress.creditHours

        if (cumulativeGPA == 0.0 && creditHours == 0) {
            return false
        }

        val semester = Semester(
            label = "Current GPA",
            level = userData.academicInfo.level,
            type = Semester.Type.SUMMARY,
            semesterGPA = cumulativeGPA,
            totalCreditHours = creditHours,
            status = Semester.Status.ARCHIVED,
            order = 1,
            archivedAt = System.currentTimeMillis(),
        )

        semesterDao.insert(semester)

        return true
    }
}