package com.hussienfahmy.sync_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienfahmy.sync_domain.model.Subject
import com.hussienfahmy.sync_domain.repository.SyncRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PushSubjects(
    private val repository: SyncRepository,
    private val subjectsDao: SubjectDao,
) {
    suspend operator fun invoke() {
        val subjects: List<Subject> = subjectsDao.subjectsWithAssignedGrade.map {
            it.keys.map { subject -> Subject(subject) }.toList()
        }.first()

        repository.uploadSubjects(subjects)
    }
}