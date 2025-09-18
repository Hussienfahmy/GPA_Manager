package com.hussienfahmy.myGpaManager.data.user_data

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.myGpaManager.data.user_data.mapper.toDomain
import com.hussienfahmy.myGpaManager.data.user_data.model.FirebaseUserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
        return userDoc.first()?.get()?.await()?.exists() ?: false
    }

    override suspend fun createUserData(
        id: String,
        name: String,
        photoUrl: String,
        email: String,
    ) {
        val now = Timestamp.now()
        userDoc.first()?.set(
            FirebaseUserData(
                id = id,
                name = name,
                photoUrl = photoUrl,
                email = email,
                createdAt = now,
                updatedAt = now,
            )
        )?.await()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val userData: Flow<UserData?> =
        userDoc.flatMapLatest { docRef ->
            callbackFlow {
                if (docRef == null) {
                    // User is signed out, emit null and close
                    trySend(null)
                    close()
                    return@callbackFlow
                }

                val registration = docRef.addSnapshotListener { value, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    if (value != null && value.exists()) {
                        value.toObject<FirebaseUserData>()?.let {
                            scope.launch {
                                send(it.toDomain())
                            }
                        } ?: kotlin.run {
                            Log.e(TAG, "observeUserData: snapshot is null or empty")
                        }
                    }
                }

                awaitClose {
                    registration.remove()
                }
            }
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(1_000),
            initialValue = null
        )

    private suspend fun updateField(field: String, value: Any) {
        userDoc.first()?.update(
            mapOf(
                field to value,
                FirebaseUserData.PROPERTY_UPDATED_AT to FieldValue.serverTimestamp()
            )
        )?.await()
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

    companion object {
        private const val TAG = "FirebaseUserDataRepository"
    }
}