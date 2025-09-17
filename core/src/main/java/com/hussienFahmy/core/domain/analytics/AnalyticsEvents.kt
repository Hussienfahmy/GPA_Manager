package com.hussienfahmy.core.domain.analytics

object AnalyticsEvents {

    // Authentication Events
    object Auth {
        /** User successfully completed Google sign-in process */
        const val SIGN_IN_COMPLETED = "sign_in_completed"

        /** User signed out of the application */
        const val SIGN_OUT = "sign_out"

        /** User completed initial profile setup with academic information */
        const val PROFILE_SETUP_COMPLETED = "profile_setup_completed"
    }

    // Quick Calculator Events
    object QuickCalculator {
        /** User accessed the quick GPA calculator feature */
        const val OPENED = "quick_calculator_opened"

        /** User performed a GPA calculation and viewed the result */
        const val CALCULATION_COMPLETED = "quick_calculation_completed"
    }

    // Semester Calculation Events
    object SemesterCalculation {
        /** User switched between Normal and Predictive calculation modes */
        const val MODE_SWITCHED = "calculation_mode_switched"

        /** User assigned a grade to a specific subject */
        const val GRADE_ASSIGNED = "grade_assigned"

        /** Real-time calculation update occurred as user modified grades */
        const val CALCULATION_UPDATED = "semester_calculation_updated"

        /** User spent significant time in calculation session */
        const val CALCULATION_SESSION_LENGTH = "calculation_session_length"

        /** User completed grading all subjects in a semester */
        const val SEMESTER_COMPLETED = "semester_completed"
    }

    // Predictive Mode Events
    object PredictiveMode {
        /** User enabled predictive calculation mode with target GPA */
        const val ENABLED = "predictive_mode_enabled"

        /** System calculated grade predictions to achieve target GPA */
        const val PREDICTION_CALCULATED = "prediction_calculated"

        /** User fixed/locked a subject's grade in predictive mode */
        const val GRADE_FIXED = "grade_fixed"

        /** User set or modified their target cumulative GPA */
        const val TARGET_GPA_SET = "target_gpa_set"
    }

    // Subject Management Events
    object SubjectManagement {
        /** User added a new subject to their semester */
        const val SUBJECT_ADDED = "subject_added"

        /** User deleted a subject from their semester */
        const val SUBJECT_DELETED = "subject_deleted"

        /** User entered marks for midterm, final, oral, or practical assessments */
        const val MARKS_ENTERED = "marks_entered"

        /** User performed bulk operations like clear all or copy subjects */
        const val BULK_ACTION = "subjects_bulk_action"
    }

    // Settings Events
    object Settings {
        /** User changed between 4-point and 5-point GPA systems */
        const val GPA_SYSTEM_CHANGED = "gpa_system_changed"

        /** User configured custom grades or modified grade settings */
        const val GRADE_SYSTEM_CONFIGURED = "grade_system_configured"

        /** User updated subject marking dependencies (midterm, final, etc.) */
        const val SUBJECT_SETTINGS_UPDATED = "subject_settings_updated"

        /** User accessed any settings screen */
        const val SETTINGS_ACCESSED = "settings_accessed"
    }

    // Navigation & Engagement Events
    object Engagement {
        /** User discovered a new feature through navigation or exploration */
        const val FEATURE_DISCOVERED = "feature_discovered"

        /** Tracking time spent on specific screens or features */
        const val SCREEN_TIME = "screen_time"

        /** User clicked "Do you like the app?" to rate on Play Store */
        const val APP_RATING_CLICKED = "app_rating_clicked"

        /** User navigated to a specific screen via bottom navigation */
        const val BOTTOM_NAV_CLICKED = "bottom_nav_clicked"
    }
}

object AnalyticsParameters {
    // Common Parameters
    /** Unique identifier for the current user */
    const val USER_ID = "user_id"

    /** Current semester (First/Second) user is working on */
    const val SEMESTER = "semester"

    /** Total number of subjects in current calculation */
    const val SUBJECTS_COUNT = "subjects_count"

    /** Current calculation mode (normal/predictive) */
    const val CALCULATION_MODE = "calculation_mode"

    /** Name of the feature being used */
    const val FEATURE_NAME = "feature_name"

    /** Duration in seconds spent on a screen or feature */
    const val TIME_SPENT_SECONDS = "time_spent_seconds"

    // Authentication Parameters
    /** Method used for signing in (google, etc.) */
    const val SIGN_IN_METHOD = "sign_in_method"

    /** Percentage of profile information completed */
    const val PROFILE_COMPLETION = "profile_completion"

    // GPA & Academic Parameters
    /** User's current cumulative GPA before calculation */
    const val CURRENT_GPA = "current_gpa"

    /** Target GPA user wants to achieve */
    const val TARGET_GPA = "target_gpa"

    /** User's cumulative GPA across all semesters */
    const val CUMULATIVE_GPA = "cumulative_gpa"

    /** GPA achieved after calculation */
    const val ACHIEVED_GPA = "achieved_gpa"

    /** GPA system being used (4-point/5-point) */
    const val GPA_SYSTEM = "gpa_system"

    // Subject Parameters
    /** Unique identifier for a specific subject */
    const val SUBJECT_ID = "subject_id"

    /** Credit hours value for a subject */
    const val CREDIT_HOURS = "credit_hours"

    /** Grade name assigned to a subject (A+, B, etc.) */
    const val GRADE_NAME = "grade_name"

    /** Type of mark being entered (midterm, final, oral, practical) */
    const val MARK_TYPE = "mark_type"

    // Predictive Parameters
    /** Result of prediction calculation (achievable/impossible) */
    const val PREDICTION_RESULT = "prediction_result"

    /** Whether target GPA is achievable with current setup */
    const val IS_ACHIEVABLE = "is_achievable"

    /** Number of subjects with fixed/locked grades */
    const val FIXED_SUBJECTS_COUNT = "fixed_subjects_count"

    /** Whether reverse calculation mode is enabled */
    const val REVERSE_CALCULATION = "reverse_calculation"

    // Workflow Parameters
    /** Whether an operation completed successfully */
    const val SUCCESS = "success"

    /** Type of error encountered during operation */
    const val ERROR_TYPE = "error_type"

    /** Status of task completion (partial/complete/failed) */
    const val COMPLETION_STATUS = "completion_status"

    /** Source of navigation (menu, button, search, etc.) */
    const val NAVIGATION_SOURCE = "navigation_source"

    /** Points value for a grade */
    const val POINTS = "points"

    /** Percentage value for a grade */
    const val PERCENTAGE = "percentage"

    /** Whether a setting/grade is active */
    const val IS_ACTIVE = "is_active"

    /** Type of setting being updated */
    const val SETTING_TYPE = "setting_type"

    /** New value being set */
    const val NEW_VALUE = "new_value"
}

object AnalyticsValues {
    // Sign-in methods
    const val SIGN_IN_METHOD_GOOGLE = "google"

    // User status
    const val IS_NEW_USER = "is_new_user"

    // Calculation modes
    const val MODE_NORMAL = "normal"
    const val MODE_PREDICTIVE = "predictive"

    // Assessment types
    const val ASSESSMENT_MIDTERM = "midterm"
    const val ASSESSMENT_FINAL = "final"
    const val ASSESSMENT_ORAL = "oral"
    const val ASSESSMENT_PRACTICAL = "practical"
    const val ASSESSMENT_PROJECT = "project"

    // Completion status values
    const val STATUS_COMPLETE = "complete"
    const val STATUS_PARTIAL = "partial"
    const val STATUS_FAILED = "failed"

    // Prediction results
    const val PREDICTION_ACHIEVABLE = "achievable"
    const val PREDICTION_IMPOSSIBLE = "impossible"

    // GPA systems
    const val GPA_SYSTEM_4_POINT = "4_point"
    const val GPA_SYSTEM_5_POINT = "5_point"

    // Settings types
    const val SETTINGS_TYPE_GPA = "gpa_settings"
    const val SETTINGS_TYPE_GRADE = "grade_settings"
    const val SETTINGS_TYPE_SUBJECT = "subject_settings"
    const val SETTINGS_TYPE_USER_DATA = "user_data"

    // Subject setting types
    const val SUBJECT_SETTING_CONSTANT_MARKS = "constant_marks"
    const val SUBJECT_SETTING_MARKS_PER_CREDIT = "marks_per_credit_hour"
    const val SUBJECT_SETTING_MARKS_DEPENDS_ON = "marks_depends_on"

    // Bulk action types
    const val BULK_ACTION_CLEAR_ALL = "clear_all"
    const val BULK_ACTION_DELETE_ALL = "delete_all"
    const val BULK_ACTION_COPY_SUBJECTS = "copy_subjects"

    // Navigation sources
    const val NAV_SOURCE_MENU = "menu"
    const val NAV_SOURCE_BUTTON = "button"

    // Screen names for time tracking
    const val SCREEN_SEMESTER = "semester"
    const val SCREEN_QUICK = "quick"
    const val SCREEN_MORE = "more"
    const val SCREEN_MARKS = "marks"
    const val SCREEN_GPA_SETTINGS = "gpa_settings_screen"
    const val NAV_SOURCE_SEARCH = "search"
    const val NAV_SOURCE_BOTTOM_NAV = "bottom_nav"

    // Bottom navigation destinations
    const val BOTTOM_NAV_QUICK = "quick"
    const val BOTTOM_NAV_SEMESTER = "semester"
    const val BOTTOM_NAV_MARKS = "marks"
    const val BOTTOM_NAV_MORE = "more"

    // Additional parameters
    const val FROM_MODE = "from_mode"
    const val TO_MODE = "to_mode"
    const val FROM_SYSTEM = "from_system"
    const val TO_SYSTEM = "to_system"
    const val IS_FIXED = "is_fixed"
    const val SUBJECTS_WITH_GRADES = "subjects_with_grades"
    const val HAS_MIDTERM = "has_midterm"
    const val HAS_PRACTICAL = "has_practical"
    const val HAS_ORAL = "has_oral"
    const val ACTION_TYPE = "action_type"
    const val AFFECTED_COUNT = "affected_count"
    const val SCREEN_NAME = "screen_name"
    const val SETTINGS_TYPE = "settings_type"
    const val DESTINATION = "destination"
}

object UserProperties {
    // Academic context properties
    const val GPA_SYSTEM = "gpa_system"
    const val ACADEMIC_LEVEL = "academic_level"
    const val UNIVERSITY = "university"
    const val FACULTY = "faculty"
    const val DEPARTMENT = "department"
    const val USER_TYPE = "user_type"
}

object UserPropertyValues {
    // User types
    const val USER_TYPE_NEW = "new_user"
    const val USER_TYPE_RETURNING = "returning_user"
    const val USER_TYPE_POWER = "power_user"
}