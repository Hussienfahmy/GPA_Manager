package com.hussienfahmy.myGpaManager.data.user_data

import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.crash.CrashReporter
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.myGpaManager.data.user_data.mapper.toDomain
import com.hussienfahmy.myGpaManager.data.user_data.model.FirebaseUserData
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.FirestoreExceptionCode
import dev.gitlive.firebase.firestore.code
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Clock

class FirebaseUserDataRepository(
    private val authRepository: AuthRepository,
    scope: CoroutineScope,
    private val db: FirebaseFirestore,
    private val crashReporter: CrashReporter,
) : UserDataRepository {

    private fun currentDoc() = authRepository.userId.value?.let {
        db.collection(FirebaseUserData.USERS_COLLECTION_NAME).document(it)
    }

    override suspend fun isUserExists(): Boolean {
        return currentDoc()?.get()?.exists ?: false
    }

    override suspend fun createUserData(
        id: String,
        name: String,
        photoUrl: String,
        email: String,
    ) {
        val now = Clock.System.now().toEpochMilliseconds()
        currentDoc()?.set(
            FirebaseUserData(
                name = name,
                photoUrl = photoUrl,
                email = email,
                createdAt = now,
                updatedAt = now,
            )
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val userData: Flow<UserData?> =
        authRepository.userId.flatMapLatest { id ->
            val docRef = id?.let { db.collection(FirebaseUserData.USERS_COLLECTION_NAME).document(it) }
            docRef?.snapshots?.map { snapshot ->
                if (snapshot.exists) {
                    snapshot.data<FirebaseUserData>().toDomain(snapshot.id)
                } else {
                    null
                }
            }
                ?: flowOf(null)
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(1_000),
            initialValue = null
        )

    private suspend fun updateField(field: String, value: Any) {
        val doc = currentDoc() ?: return
        try {
            doc.updateFields {
                field to value
                FirebaseUserData.PROPERTY_UPDATED_AT to Clock.System.now().toEpochMilliseconds()
            }
        } catch (e: FirebaseFirestoreException) {
            // No backing doc yet - best-effort write, dropped rather than crashing.
            if (e.code != FirestoreExceptionCode.NOT_FOUND) throw e
            crashReporter.recordException(e, mapOf("operation" to "updateField", "field" to field))
        }
    }

    override suspend fun updateName(name: String) {
        updateField(FirebaseUserData.PROPERTY_NAME, name)
    }

    override suspend fun updatePhotoUrl(photoUrl: String) {
        updateField(FirebaseUserData.PROPERTY_PHOTO_URL, photoUrl)
    }

    override suspend fun updateEmail(email: String) {
        updateField(FirebaseUserData.PROPERTY_EMAIL, email)
    }

    override suspend fun updateUniversity(university: String) {
        updateField(FirebaseUserData.PROPERTY_ACADEMIC_INFO_UNIVERSITY, university)
    }

    override suspend fun updateFaculty(faculty: String) {
        updateField(FirebaseUserData.PROPERTY_ACADEMIC_INFO_FACULTY, faculty)
    }

    override suspend fun updateDepartment(department: String) {
        updateField(FirebaseUserData.PROPERTY_ACADEMIC_INFO_DEPARTMENT, department)
    }

    override suspend fun updateLevel(level: Int) {
        updateField(FirebaseUserData.PROPERTY_ACADEMIC_INFO_LEVEL, level)
    }

    override suspend fun updateSemester(semester: UserData.AcademicInfo.Semester) {
        updateField(
            FirebaseUserData.PROPERTY_ACADEMIC_INFO_SEMESTER,
            when (semester) {
                UserData.AcademicInfo.Semester.First -> FirebaseUserData.AcademicInfo.Semester.First
                UserData.AcademicInfo.Semester.Second -> FirebaseUserData.AcademicInfo.Semester.Second
                UserData.AcademicInfo.Semester.Summer -> FirebaseUserData.AcademicInfo.Semester.Summer
            }
        )
    }

    override suspend fun updateCumulativeGPA(cumulativeGPA: Double) {
        updateField(FirebaseUserData.PROPERTY_ACADEMIC_PROGRESS_CUMULATIVE_GPA, cumulativeGPA)
    }

    override suspend fun updateCreditHours(creditHours: Int) {
        updateField(FirebaseUserData.PROPERTY_ACADEMIC_PROGRESS_CREDIT_HOURS, creditHours)
    }

    override suspend fun updateFCMToken(fcmToken: String) {
        if (authRepository.isAnonymousFlow.value == true) return // guests aren't targeted for push
        updateField(FirebaseUserData.PROPERTY_FCM_TOKEN, fcmToken)
    }
}
