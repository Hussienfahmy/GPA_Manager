package com.hussienfahmy.sync_domain.use_case

import android.util.Log
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.sync_domain.repository.SyncRepository

class PullSubjects(
    private val repository: SyncRepository,
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke() {
        val networkSubjects = repository.downloadSubjects() ?: return

        Log.d("DEBUG_TAG", "invoke: network subjects fetched $networkSubjects")
        networkSubjects.subjects.onEach {
            subjectDao.upsert(it.toEntity())
        }
    }
}