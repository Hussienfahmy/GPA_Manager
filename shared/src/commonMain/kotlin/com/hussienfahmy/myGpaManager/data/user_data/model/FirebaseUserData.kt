package com.hussienfahmy.myGpaManager.data.user_data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// Accounts created before the switch to GitLive Firestore have createdAt/updatedAt stored as a
// native Firestore Timestamp (the old Android SDK's server-timestamp type), not the epoch-millis
// Long used now (see TimestampMapper.kt for why). Neither field is read anywhere downstream -
// they're write-only bookkeeping - so on a legacy Timestamp-shaped document there's nothing to
// recover; just don't let it crash the whole document decode.
private object LenientEpochMillisSerializer : KSerializer<Long> {
    override val descriptor = PrimitiveSerialDescriptor("LenientEpochMillis", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Long) = encoder.encodeLong(value)

    override fun deserialize(decoder: Decoder): Long {
        return try {
            decoder.decodeLong()
        } catch (e: Exception) {
            0L
        }
    }
}

// No id/@DocumentId field: the document id is always the Firebase Auth uid already known to the
// caller (userDoc(userId) = .document(userId)), so it's redundant to also store/read it from the
// document body - the repository passes it in separately when mapping to the domain UserData.
@Serializable
internal data class FirebaseUserData(
    @SerialName(PROPERTY_NAME) val name: String = "User",
    @SerialName(PROPERTY_PHOTO_URL) val photoUrl: String = "",
    @SerialName(PROPERTY_EMAIL) val email: String = "email@user.com",
    @SerialName(PROPERTY_ACADEMIC_INFO) val academicInfo: AcademicInfo = AcademicInfo(),
    @SerialName(PROPERTY_FCM_TOKEN) val fcmToken: String = "",
    @SerialName(PROPERTY_ACADEMIC_PROGRESS) val academicProgress: AcademicProgress = AcademicProgress(),
    @Serializable(with = LenientEpochMillisSerializer::class)
    @SerialName(PROPERTY_CREATED_AT) val createdAt: Long? = null,
    @Serializable(with = LenientEpochMillisSerializer::class)
    @SerialName(PROPERTY_UPDATED_AT) val updatedAt: Long? = null,
) {
    @Serializable
    data class AcademicInfo(
        @SerialName(UNIVERSITY_FIELD) val university: String = "University",
        @SerialName(FACULTY_FIELD) val faculty: String = "Faculty",
        @SerialName(DEPARTMENT_FIELD) val department: String = "Department",
        @SerialName(LEVEL_FIELD) val level: Int = 1,
        @SerialName(SEMESTER_FIELD) val semester: Semester = Semester.First
    ) {
        @Serializable
        enum class Semester {
            First, Second
        }
    }

    @Serializable
    data class AcademicProgress(
        @SerialName(CUMULATIVE_GPA_FIELD) val cumulativeGPA: Double = 0.0,
        @SerialName(CREDIT_HOURS_FIELD) val creditHours: Int = 0
    )

    companion object {
        const val USERS_COLLECTION_NAME = "users"
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
        const val PROPERTY_CREATED_AT = "createdAt"
        const val PROPERTY_UPDATED_AT = "updatedAt"
    }
}