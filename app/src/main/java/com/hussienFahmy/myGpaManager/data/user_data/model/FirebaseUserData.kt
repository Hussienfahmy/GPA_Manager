package com.hussienfahmy.myGpaManager.data.user_data.model

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

@Keep
internal data class FirebaseUserData(
    @DocumentId val id: String = "id",
    @PropertyName(PROPERTY_NAME) val name: String = "User",
    @PropertyName(PROPERTY_PHOTO_URL) val photoUrl: String = "",
    @PropertyName(PROPERTY_EMAIL) val email: String = "email@user.com",
    @PropertyName(PROPERTY_ACADEMIC_INFO) val academicInfo: AcademicInfo = AcademicInfo(),
    @PropertyName(PROPERTY_FCM_TOKEN) val fcmToken: String = "",
    @PropertyName(PROPERTY_ACADEMIC_PROGRESS) val academicProgress: AcademicProgress = AcademicProgress(),
) {
    @Keep
    data class AcademicInfo(
        @PropertyName(UNIVERSITY_FIELD) val university: String = "University",
        @PropertyName(FACULTY_FIELD) val faculty: String = "Faculty",
        @PropertyName(DEPARTMENT_FIELD) val department: String = "Department",
        @PropertyName(LEVEL_FIELD) val level: Int = 1,
        @PropertyName(SEMESTER_FIELD) val semester: Semester = Semester.First
    ) {
        enum class Semester {
            First, Second
        }
    }

    @Keep
    data class AcademicProgress(
        @PropertyName(CUMULATIVE_GPA_FIELD) val cumulativeGPA: Double = 0.0,
        @PropertyName(CREDIT_HOURS_FIELD) val creditHours: Int = 0
    )

    companion object {
        const val COLLECTION_NAME = "users"
        const val PROPERTY_NAME = "name"
        const val PROPERTY_PHOTO_URL = "photoUrl"
        const val PROPERTY_EMAIL = "email"
        const val PROPERTY_ACADEMIC_INFO = "academicInfo"
        private const val UNIVERSITY_FIELD = "university"
        private const val FACULTY_FIELD = "faculty"
        private const val DEPARTMENT_FIELD = "department"
        private const val LEVEL_FIELD = "level"
        private const val SEMESTER_FIELD = "semester"
        const val PROPERTY_ACADEMIC_INFO_UNIVERSITY = "academicInfo.$UNIVERSITY_FIELD"
        const val PROPERTY_ACADEMIC_INFO_FACULTY = "academicInfo.$FACULTY_FIELD"
        const val PROPERTY_ACADEMIC_INFO_DEPARTMENT = "academicInfo.$DEPARTMENT_FIELD"
        const val PROPERTY_ACADEMIC_INFO_LEVEL = "academicInfo.$LEVEL_FIELD"
        const val PROPERTY_ACADEMIC_INFO_SEMESTER = "academicInfo.$SEMESTER_FIELD"
        const val PROPERTY_ACADEMIC_PROGRESS = "academicProgress"
        private const val CUMULATIVE_GPA_FIELD = "cumulativeGPA"
        private const val CREDIT_HOURS_FIELD = "creditHours"
        const val PROPERTY_ACADEMIC_PROGRESS_CUMULATIVE_GPA =
            "academicProgress.$CUMULATIVE_GPA_FIELD"
        const val PROPERTY_ACADEMIC_PROGRESS_CREDIT_HOURS = "academicProgress.$CREDIT_HOURS_FIELD"
        const val PROPERTY_FCM_TOKEN = "fcm_token"
    }
}