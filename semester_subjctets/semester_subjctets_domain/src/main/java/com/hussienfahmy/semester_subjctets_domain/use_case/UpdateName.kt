package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao

class UpdateName(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(id: Long, name: String) {
        subjectDao.updateName(id, name)
    }
}