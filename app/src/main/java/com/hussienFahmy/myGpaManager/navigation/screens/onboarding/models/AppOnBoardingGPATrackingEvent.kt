package com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models

import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.model.GradeName

sealed class AppOnBoardingGPATrackingEvent {
    object ShowAddSheet : AppOnBoardingGPATrackingEvent()
    object HideAddSheet : AppOnBoardingGPATrackingEvent()
    data class SetAddingSubjectsSemester(val id: Long?) : AppOnBoardingGPATrackingEvent()
    data class DeleteSemesterEvent(val id: Long) : AppOnBoardingGPATrackingEvent()
    data class AddSummarySemester(
        val label: String,
        val gpa: Double,
        val hours: Int,
        val level: Int
    ) : AppOnBoardingGPATrackingEvent()

    data class AddDetailedSemester(val label: String, val level: Int) :
        AppOnBoardingGPATrackingEvent()

    data class AddSubject(
        val semesterId: Long,
        val name: String,
        val creditHours: Double,
        val gradeName: GradeName,
        val totalMarks: Double,
        val semesterMarks: Subject.SemesterMarks?,
        val metadata: Subject.MetaData,
    ) : AppOnBoardingGPATrackingEvent()
}
