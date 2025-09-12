package com.hussienfahmy.myGpaManager.data.user_data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.myGpaManager.data.user_data.mapper.toDomain
import com.hussienfahmy.myGpaManager.data.user_data.model.FirebaseUserData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirebaseUserDataRepository(
    authRepository: AuthRepository,
    private val db: FirebaseFirestore,
) : UserDataRepository {

    private val userDoc = authRepository.userId.map { userId ->
        db.collection(FirebaseUserData.USERS_COLLECTION_NAME).document(userId)
    }

    override suspend fun isUserExists(): Boolean {
        return userDoc.first().get().await().exists()
    }

    override suspend fun createUserData(
        id: String,
        name: String,
        photoUrl: String,
        email: String,
    ) {
        userDoc.first().set(
            FirebaseUserData(
                id = id,
                name = name,
                photoUrl = photoUrl,
                email = email,
            )
        ).await()
    }

    override fun observeUserData(): Flow<UserData?> {
        return callbackFlow {
            val registration = userDoc.first().addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (value != null && value.exists()) {
                    value.toObject<FirebaseUserData>()?.let {
                        trySend(it.toDomain())
                    } ?: kotlin.run {
                        Log.e(TAG, "observeUserData: snapshot is null or empty")
                    }
                }
            }

            awaitClose {
                registration.remove()
            }
        }
    }

    override suspend fun getUserData(): UserData? {
        return userDoc.first().get().await().toObject<FirebaseUserData>()?.toDomain()
    }

    override suspend fun updateName(name: String) {
        userDoc.first()
            .update(FirebaseUserData.PROPERTY_NAME, name)
            .await()
    }

    override suspend fun updatePhotoUrl(photoUrl: String) {
        userDoc.first()
            .update(FirebaseUserData.PROPERTY_PHOTO_URL, photoUrl)
            .await()
    }

    override suspend fun updateEmail(email: String) {
        userDoc.first()
            .update(FirebaseUserData.PROPERTY_EMAIL, email)
            .await()
    }

    override suspend fun updateUniversity(university: String) {
        userDoc.first()
            .update(
                FirebaseUserData.PROPERTY_ACADEMIC_INFO_UNIVERSITY,
                university
            )
            .await()
    }

    override suspend fun updateFaculty(faculty: String) {
        userDoc.first()
            .update(
                FirebaseUserData.PROPERTY_ACADEMIC_INFO_FACULTY,
                faculty
            )
            .await()
    }

    override suspend fun updateDepartment(department: String) {
        userDoc.first()
            .update(
                FirebaseUserData.PROPERTY_ACADEMIC_INFO_DEPARTMENT,
                department
            )
            .await()
    }

    override suspend fun updateLevel(level: Int) {
        userDoc.first()
            .update(
                FirebaseUserData.PROPERTY_ACADEMIC_INFO_LEVEL,
                level
            )
            .await()
    }

    override suspend fun updateSemester(semester: UserData.AcademicInfo.Semester) {
        userDoc.first()
            .update(
                FirebaseUserData.PROPERTY_ACADEMIC_INFO_SEMESTER,
                when (semester) {
                    UserData.AcademicInfo.Semester.First -> FirebaseUserData.AcademicInfo.Semester.First
                    UserData.AcademicInfo.Semester.Second -> FirebaseUserData.AcademicInfo.Semester.Second
                }
            )
            .await()
    }

    override suspend fun updateCumulativeGPA(cumulativeGPA: Double) {
        userDoc.first()
            .update(
                FirebaseUserData.PROPERTY_ACADEMIC_PROGRESS_CUMULATIVE_GPA,
                cumulativeGPA
            )
            .await()
    }

    override suspend fun updateCreditHours(creditHours: Int) {
        userDoc.first()
            .update(
                FirebaseUserData.PROPERTY_ACADEMIC_PROGRESS_CREDIT_HOURS,
                creditHours
            )
            .await()
    }

    override suspend fun updateFCMToken(fcmToken: String) {
        userDoc.first()
            .update(FirebaseUserData.PROPERTY_FCM_TOKEN, fcmToken)
            .await()
    }

    companion object {
        private const val TAG = "FirebaseUserDataRepository"
    }
}