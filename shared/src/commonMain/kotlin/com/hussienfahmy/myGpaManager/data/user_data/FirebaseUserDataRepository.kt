package com.hussienfahmy.myGpaManager.data.user_data

import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.myGpaManager.data.user_data.mapper.toDomain
import com.hussienfahmy.myGpaManager.data.user_data.model.FirebaseUserData
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class FirebaseUserDataRepository(
    authRepository: AuthRepository,
    scope: CoroutineScope,
    private val db: FirebaseFirestore,
) : UserDataRepository {

    private val userDoc = authRepository.userId.map { userId ->
        userId?.let {
            db.collection(FirebaseUserData.USERS_COLLECTION_NAME).document(it)
        }
    }

    override suspend fun isUserExists(): Boolean {
        return userDoc.first()?.get()?.exists ?: false
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun createUserData(
        id: String,
        name: String,
        photoUrl: String,
        email: String,
    ) {
        val now = Clock.System.now().toEpochMilliseconds()
        userDoc.first()?.set(
            FirebaseUserData(
                name = name,
                photoUrl = photoUrl,
                email = email,
                createdAt = now,
                updatedAt = now,
            )
        )
    }

    // GitLive's DocumentReference.snapshots Flow replaces the manual
    // addSnapshotListener/callbackFlow/awaitClose wiring the Android SDK needed.
    @OptIn(ExperimentalCoroutinesApi::class)
    override val userData: Flow<UserData?> =
        userDoc.flatMapLatest { docRef ->
            if (docRef == null) {
                flowOf(null)
            } else {
                docRef.snapshots.map { snapshot ->
                    if (snapshot.exists) {
                        snapshot.data<FirebaseUserData>().toDomain(snapshot.id)
                    } else {
                        null
                    }
                }
            }
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(1_000),
            initialValue = null
        )

    // The old Android SDK's mapOf(field to value) update worked with value typed as plain Any
    // because Firestore's Android SDK serializes via reflection at runtime. GitLive's
    // updateFields{} DSL is kotlinx.serialization-based, which typically resolves serializers from
    // *static* (often reified) type info - "field to value" here has value statically typed as
    // Any, which may not resolve a serializer correctly for non-String/primitive values (in
    // particular updateSemester's FirebaseUserData.AcademicInfo.Semester enum argument), a risk
    // untested at runtime. If it doesn't serialize correctly, each updateXxx caller below may need
    // its own non-generic updateFields{} call with the concrete type inline instead of routing
    // through this shared helper.
    @OptIn(ExperimentalTime::class)
    private suspend fun updateField(field: String, value: Any) {
        userDoc.first()?.updateFields {
            field to value
            FirebaseUserData.PROPERTY_UPDATED_AT to Clock.System.now().toEpochMilliseconds()
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
        updateField(FirebaseUserData.PROPERTY_FCM_TOKEN, fcmToken)
    }
}
