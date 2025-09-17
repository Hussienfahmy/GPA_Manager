package com.hussienfahmy.core.domain.analytics

import com.hussienfahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienfahmy.core.domain.user_data.use_cases.UserDataUseCases
import kotlinx.coroutines.flow.first

class AnalyticsLogger(
    private val analyticsService: AnalyticsService,
    private val userDataUseCases: UserDataUseCases,
    private val getGPASettings: GetGPASettings
) {

    // Authentication Events
    fun logSignInCompleted(userId: String, isNewUser: Boolean) {
        analyticsService.logEvent(
            AnalyticsEvents.Auth.SIGN_IN_COMPLETED,
            mapOf(
                AnalyticsParameters.SIGN_IN_METHOD to AnalyticsValues.SIGN_IN_METHOD_GOOGLE,
                AnalyticsParameters.USER_ID to userId,
                AnalyticsValues.IS_NEW_USER to isNewUser
            )
        )
        analyticsService.setUserId(userId)
    }

    fun logSignOut() {
        analyticsService.logEvent(AnalyticsEvents.Auth.SIGN_OUT)
        analyticsService.setUserId(null)
    }

    fun logProfileSetupCompleted(completionPercentage: Int) {
        analyticsService.logEvent(
            AnalyticsEvents.Auth.PROFILE_SETUP_COMPLETED,
            mapOf(AnalyticsParameters.PROFILE_COMPLETION to completionPercentage)
        )
    }

    // Quick Calculator Events
    fun logQuickCalculatorOpened() {
        analyticsService.logEvent(AnalyticsEvents.QuickCalculator.OPENED)
    }

    fun logQuickCalculationCompleted(
        targetGpa: Double,
        currentGpa: Double,
        creditHours: Int,
        achievedGpa: Double,
        isSuccess: Boolean
    ) {
        analyticsService.logEvent(
            AnalyticsEvents.QuickCalculator.CALCULATION_COMPLETED,
            mapOf(
                AnalyticsParameters.TARGET_GPA to targetGpa,
                AnalyticsParameters.CURRENT_GPA to currentGpa,
                AnalyticsParameters.CREDIT_HOURS to creditHours,
                AnalyticsParameters.ACHIEVED_GPA to achievedGpa,
                AnalyticsParameters.SUCCESS to isSuccess
            )
        )
    }

    // Semester Calculation Events
    fun logCalculationModeSwitched(fromMode: String, toMode: String, subjectsCount: Int) {
        analyticsService.logEvent(
            AnalyticsEvents.SemesterCalculation.MODE_SWITCHED,
            mapOf(
                AnalyticsValues.FROM_MODE to fromMode,
                AnalyticsValues.TO_MODE to toMode,
                AnalyticsParameters.SUBJECTS_COUNT to subjectsCount
            )
        )
    }

    fun logGradeAssigned(subjectId: Long, gradeName: String) {
        analyticsService.logEvent(
            AnalyticsEvents.SemesterCalculation.GRADE_ASSIGNED,
            mapOf(
                AnalyticsParameters.SUBJECT_ID to subjectId,
                AnalyticsParameters.GRADE_NAME to gradeName,
            )
        )
    }

    fun logCalculationUpdated(currentGpa: Double, subjectsWithGrades: Int, totalSubjects: Int) {
        analyticsService.logEvent(
            AnalyticsEvents.SemesterCalculation.CALCULATION_UPDATED,
            mapOf(
                AnalyticsParameters.CURRENT_GPA to currentGpa,
                AnalyticsValues.SUBJECTS_WITH_GRADES to subjectsWithGrades,
                AnalyticsParameters.SUBJECTS_COUNT to totalSubjects
            )
        )
    }

    fun logSemesterCompleted(
        finalGpa: Double,
        subjectsCount: Int,
        completionPercentage: Double
    ) {
        analyticsService.logEvent(
            AnalyticsEvents.SemesterCalculation.SEMESTER_COMPLETED,
            mapOf(
                AnalyticsParameters.ACHIEVED_GPA to finalGpa,
                AnalyticsParameters.SUBJECTS_COUNT to subjectsCount,
                AnalyticsParameters.PROFILE_COMPLETION to completionPercentage
            )
        )
    }

    // Predictive Mode Events
    fun logPredictiveModeEnabled(
        targetGpa: Double,
        subjectsCount: Int,
        reverseCalculation: Boolean
    ) {
        analyticsService.logEvent(
            AnalyticsEvents.PredictiveMode.ENABLED,
            mapOf(
                AnalyticsParameters.TARGET_GPA to targetGpa,
                AnalyticsParameters.SUBJECTS_COUNT to subjectsCount,
                AnalyticsParameters.REVERSE_CALCULATION to reverseCalculation
            )
        )
    }

    fun logPredictionCalculated(isAchievable: Boolean, fixedSubjectsCount: Int) {
        analyticsService.logEvent(
            AnalyticsEvents.PredictiveMode.PREDICTION_CALCULATED,
            mapOf(
                AnalyticsParameters.IS_ACHIEVABLE to isAchievable,
                AnalyticsParameters.FIXED_SUBJECTS_COUNT to fixedSubjectsCount
            )
        )
    }

    fun logGradeFixed(isFixed: Boolean) {
        analyticsService.logEvent(
            AnalyticsEvents.PredictiveMode.GRADE_FIXED,
            mapOf(
                AnalyticsValues.IS_FIXED to isFixed
            )
        )
    }

    fun logTargetGpaSet(targetGpa: Double, currentGpa: Double) {
        analyticsService.logEvent(
            AnalyticsEvents.PredictiveMode.TARGET_GPA_SET,
            mapOf(
                AnalyticsParameters.TARGET_GPA to targetGpa,
                AnalyticsParameters.CURRENT_GPA to currentGpa
            )
        )
    }

    // Subject Management Events
    fun logSubjectAdded(creditHours: Double, hasAssessments: Map<String, Boolean>) {
        analyticsService.logEvent(
            AnalyticsEvents.SubjectManagement.SUBJECT_ADDED,
            mapOf(
                AnalyticsParameters.CREDIT_HOURS to creditHours,
                AnalyticsValues.HAS_MIDTERM to (hasAssessments[AnalyticsValues.ASSESSMENT_MIDTERM]
                    ?: false),
                AnalyticsValues.HAS_PRACTICAL to (hasAssessments[AnalyticsValues.ASSESSMENT_PRACTICAL]
                    ?: false),
                AnalyticsValues.HAS_ORAL to (hasAssessments[AnalyticsValues.ASSESSMENT_ORAL]
                    ?: false)
            )
        )
    }

    fun logSubjectDeleted(subjectId: Long) {
        analyticsService.logEvent(
            AnalyticsEvents.SubjectManagement.SUBJECT_DELETED,
            mapOf(AnalyticsParameters.SUBJECT_ID to subjectId)
        )
    }

    fun logMarksEntered(subjectId: Long, markType: String, isComplete: Boolean) {
        analyticsService.logEvent(
            AnalyticsEvents.SubjectManagement.MARKS_ENTERED,
            mapOf(
                AnalyticsParameters.SUBJECT_ID to subjectId,
                AnalyticsParameters.MARK_TYPE to markType,
                AnalyticsParameters.COMPLETION_STATUS to if (isComplete) AnalyticsValues.STATUS_COMPLETE else AnalyticsValues.STATUS_PARTIAL
            )
        )
    }

    fun logBulkAction(actionType: String, affectedCount: Int) {
        analyticsService.logEvent(
            AnalyticsEvents.SubjectManagement.BULK_ACTION,
            mapOf(
                AnalyticsValues.ACTION_TYPE to actionType,
                AnalyticsValues.AFFECTED_COUNT to affectedCount
            )
        )
    }

    // Settings Events
    fun logGpaSystemChanged(fromSystem: String, toSystem: String) {
        analyticsService.logEvent(
            AnalyticsEvents.Settings.GPA_SYSTEM_CHANGED,
            mapOf(
                AnalyticsValues.FROM_SYSTEM to fromSystem,
                AnalyticsValues.TO_SYSTEM to toSystem
            )
        )
    }

    fun logSettingsAccessed(settingsType: String) {
        analyticsService.logEvent(
            AnalyticsEvents.Settings.SETTINGS_ACCESSED,
            mapOf(AnalyticsValues.SETTINGS_TYPE to settingsType)
        )
    }

    // Engagement Events
    fun logFeatureDiscovered(featureName: String, navigationSource: String) {
        analyticsService.logEvent(
            AnalyticsEvents.Engagement.FEATURE_DISCOVERED,
            mapOf(
                AnalyticsParameters.FEATURE_NAME to featureName,
                AnalyticsParameters.NAVIGATION_SOURCE to navigationSource
            )
        )
    }

    fun logScreenTime(screenName: String, timeSpentSeconds: Long) {
        analyticsService.logEvent(
            AnalyticsEvents.Engagement.SCREEN_TIME,
            mapOf(
                AnalyticsValues.SCREEN_NAME to screenName,
                AnalyticsParameters.TIME_SPENT_SECONDS to timeSpentSeconds
            )
        )
    }

    fun logAppRatingClicked() {
        analyticsService.logEvent(AnalyticsEvents.Engagement.APP_RATING_CLICKED)
    }

    fun logBottomNavClicked(destination: String) {
        analyticsService.logEvent(
            AnalyticsEvents.Engagement.BOTTOM_NAV_CLICKED,
            mapOf(
                AnalyticsValues.DESTINATION to destination,
                AnalyticsParameters.NAVIGATION_SOURCE to AnalyticsValues.NAV_SOURCE_BOTTOM_NAV
            )
        )
    }

    fun logGradeSystemConfigured(
        gradeName: String,
        points: Double,
        percentage: Double,
        isActive: Boolean
    ) {
        analyticsService.logEvent(
            AnalyticsEvents.Settings.GRADE_SYSTEM_CONFIGURED,
            mapOf(
                AnalyticsParameters.GRADE_NAME to gradeName,
                AnalyticsParameters.POINTS to points,
                AnalyticsParameters.PERCENTAGE to percentage,
                AnalyticsParameters.IS_ACTIVE to isActive,
                AnalyticsValues.SETTINGS_TYPE to AnalyticsValues.SETTINGS_TYPE_GRADE
            )
        )
    }

    fun logSubjectSettingsUpdated(settingType: String, newValue: Any) {
        analyticsService.logEvent(
            AnalyticsEvents.Settings.SUBJECT_SETTINGS_UPDATED,
            mapOf(
                AnalyticsParameters.SETTING_TYPE to settingType,
                AnalyticsParameters.NEW_VALUE to newValue.toString(),
                AnalyticsValues.SETTINGS_TYPE to AnalyticsValues.SETTINGS_TYPE_SUBJECT
            )
        )
    }

    // User Properties
    fun setUserAcademicContext(
        gpaSystem: String,
        academicLevel: Int,
        university: String,
        faculty: String,
        department: String,
        semester: String
    ) {
        analyticsService.setUserProperty(UserProperties.GPA_SYSTEM, gpaSystem)
        analyticsService.setUserProperty(UserProperties.ACADEMIC_LEVEL, academicLevel.toString())
        analyticsService.setUserProperty(UserProperties.UNIVERSITY, university)
        analyticsService.setUserProperty(UserProperties.FACULTY, faculty)
        analyticsService.setUserProperty(UserProperties.DEPARTMENT, department)
        analyticsService.setUserProperty(UserProperties.SEMESTER, semester)
    }

    fun setUserType(userType: String) {
        analyticsService.setUserProperty(UserProperties.USER_TYPE, userType)
    }

    suspend fun updateUserAcademicProperties() {
        try {
            val userData = userDataUseCases.getUserData().first()
            val gpaSettings = getGPASettings()

            userData?.let { data ->
                val gpaSystemValue = when (gpaSettings.system) {
                    com.hussienfahmy.core.domain.gpa_settings.model.GPA.System.FOUR -> AnalyticsValues.GPA_SYSTEM_4_POINT
                    com.hussienfahmy.core.domain.gpa_settings.model.GPA.System.FIVE -> AnalyticsValues.GPA_SYSTEM_5_POINT
                }

                setUserAcademicContext(
                    gpaSystem = gpaSystemValue,
                    academicLevel = data.academicInfo.level,
                    university = data.academicInfo.university,
                    faculty = data.academicInfo.faculty,
                    department = data.academicInfo.department,
                    semester = data.academicInfo.semester.name
                )
            }
        } catch (_: Exception) {
            // Handle error silently for analytics
        }
    }
}