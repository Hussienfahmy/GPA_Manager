package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.sync_domain.repository.SyncRepository

class PullSemesters(
    private val repository: SyncRepository,
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(userId: String) {
        val networkSemesters = repository.downloadSemesters(userId) ?: return

        // If Firebase has semesters, it is the authoritative source — clear any locally created
        if (networkSemesters.isNotEmpty()) {
            subjectDao.deleteAllArchivedSubjects()
            semesterDao.deleteAllArchived()
        }

        networkSemesters.forEach { networkSemester ->
            val semesterEntity = networkSemester.toEntity()
            val semesterId = semesterDao.insert(semesterEntity)

            networkSemester.subjects.forEach { networkSubject ->
                val subjectEntity = networkSubject.toEntity().copy(semesterId = semesterId)

                subjectDao.upsert(subjectEntity)
            }
        }
    }
}
