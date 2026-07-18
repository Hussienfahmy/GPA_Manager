package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.domain.sync.SemesterDirtyTracker
import kotlinx.coroutines.flow.first

class ReorderSemester(
    private val semesterDao: SemesterDao,
    private val dirtyTracker: SemesterDirtyTracker,
) {
    enum class Direction { UP, DOWN }

    suspend operator fun invoke(semesterId: Long, direction: Direction) {
        val semesters = semesterDao.getArchived().first()
        val index = semesters.indexOfFirst { it.id == semesterId }
        if (index < 0) return

        val targetIndex = when (direction) {
            Direction.UP -> index - 1
            Direction.DOWN -> index + 1
        }
        if (targetIndex < 0 || targetIndex >= semesters.size) return

        val current = semesters[index]
        val target = semesters[targetIndex]

        semesterDao.swapOrder(
            a = current.copy(order = target.order),
            b = target.copy(order = current.order),
        )
        dirtyTracker.markChanged()
    }
}
