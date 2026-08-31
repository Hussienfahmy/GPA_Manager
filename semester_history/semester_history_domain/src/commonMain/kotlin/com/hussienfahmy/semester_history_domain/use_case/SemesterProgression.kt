package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.domain.user_data.model.UserData

/**
 * The one place that knows what "finish semester" advances to - shared by [ArchiveCurrentSemester]
 * (which actually performs the advance) and the finish-semester confirmation sheet (which only
 * needs to preview it), so the rule can't drift between the two.
 *
 * First -> Second and Second -> (level+1) First are the normal yearly cycle. Summer only exists
 * because the user manually set their profile to it (never something "finish semester" lands on
 * by itself) - finishing it still counts as finishing the year, so it also bumps the level.
 */
object SemesterProgression {
    fun next(
        currentSemester: UserData.AcademicInfo.Semester,
        currentLevel: Int,
    ): Pair<UserData.AcademicInfo.Semester, Int> = when (currentSemester) {
        UserData.AcademicInfo.Semester.First ->
            UserData.AcademicInfo.Semester.Second to currentLevel

        UserData.AcademicInfo.Semester.Second ->
            UserData.AcademicInfo.Semester.First to currentLevel + 1

        UserData.AcademicInfo.Semester.Summer ->
            UserData.AcademicInfo.Semester.First to currentLevel + 1
    }

    fun label(level: Int, semester: UserData.AcademicInfo.Semester): String = when (semester) {
        UserData.AcademicInfo.Semester.First -> "Year $level - Semester 1"
        UserData.AcademicInfo.Semester.Second -> "Year $level - Semester 2"
        UserData.AcademicInfo.Semester.Summer -> "Year $level - Summer"
    }
}
