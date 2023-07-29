package com.hussienfahmy.user_data_data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.hussienFahmy.core.domain.user_data.model.UserData
import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.user_data_data.mapper.toUserData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import com.hussienfahmy.user_data_data.model.UserData as NetworkUserData

class UserDataApiService(
    private val db: FirebaseFirestore,
    auth: FirebaseAuth,
) : UserDataRepository {

    private val currentUser = MutableStateFlow(auth.currentUser)
    private val userId = currentUser.filterNotNull().map { it.uid }
    private val userDoc = userId.map {
        db.collection(NetworkUserData.COLLECTION_NAME).document(it).also {
            Log.d(TAG, "doc path = ${it.path}")
        }
    }

    init {
        auth.addAuthStateListener {
            currentUser.value = it.currentUser
        }
    }

    override suspend fun isUserExists(): Boolean {
        return userDoc.first().get().await().exists()
    }

    override suspend fun createUserData(
        id: String,
        name: String,
        photoUrl: String,
        email: String,
        isEmailVerified: Boolean
    ) {
        userDoc.first().set(
            NetworkUserData(
                id = id,
                name = name,
                photoUrl = photoUrl,
                email = email,
                isEmailVerified = isEmailVerified,
            )
        ).await()
    }

    override fun observeUserData(): Flow<UserData> {
        return callbackFlow {
            val registration = userDoc.first().addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (value != null && value.exists()) {
                    value.toObject<NetworkUserData>()?.let {
                        trySend(it.toUserData())
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

    override suspend fun getUserData(): UserData {
        return userDoc.first().get().await().toObject<NetworkUserData>()!!.toUserData()
    }

    override suspend fun updateName(name: String) {
        userDoc.first()
            .update(NetworkUserData.PROPERTY_NAME, name)
            .await()
    }

    override suspend fun updatePhotoUrl(photoUrl: String) {
        userDoc.first()
            .update(NetworkUserData.PROPERTY_PHOTO_URL, photoUrl)
            .await()
    }

    override suspend fun updateEmail(email: String) {
        userDoc.first()
            .update(NetworkUserData.PROPERTY_EMAIL, email)
            .await()
    }

    override suspend fun updateUniversity(university: String) {
        userDoc.first()
            .update(
                NetworkUserData.PROPERTY_ACADEMIC_INFO_UNIVERSITY,
                university
            )
            .await()
    }

    override suspend fun updateFaculty(faculty: String) {
        userDoc.first()
            .update(
                NetworkUserData.PROPERTY_ACADEMIC_INFO_FACULTY,
                faculty
            )
            .await()
    }

    override suspend fun updateDepartment(department: String) {
        userDoc.first()
            .update(
                NetworkUserData.PROPERTY_ACADEMIC_INFO_DEPARTMENT,
                department
            )
            .await()
    }

    override suspend fun updateLevel(level: Int) {
        userDoc.first()
            .update(
                NetworkUserData.PROPERTY_ACADEMIC_INFO_LEVEL,
                level
            )
            .await()
    }

    override suspend fun updateSemester(semester: UserData.AcademicInfo.Semester) {
        userDoc.first()
            .update(
                NetworkUserData.PROPERTY_ACADEMIC_INFO_SEMESTER,
                when (semester) {
                    UserData.AcademicInfo.Semester.First -> NetworkUserData.AcademicInfo.Semester.First
                    UserData.AcademicInfo.Semester.Second -> NetworkUserData.AcademicInfo.Semester.Second
                }
            )
            .await()
    }

    override suspend fun updateCumulativeGPA(cumulativeGPA: Double) {
        userDoc.first()
            .update(
                NetworkUserData.PROPERTY_ACADEMIC_PROGRESS_CUMULATIVE_GPA,
                cumulativeGPA
            )
            .await()
    }

    override suspend fun updateCreditHours(creditHours: Int) {
        userDoc.first()
            .update(
                NetworkUserData.PROPERTY_ACADEMIC_PROGRESS_CREDIT_HOURS,
                creditHours
            )
            .await()
    }

    override suspend fun updateFCMToken(fcmToken: String) {
        userDoc.first()
            .update(NetworkUserData.PROPERTY_FCM_TOKEN, fcmToken)
            .await()
    }

    companion object {
        private const val TAG = "UserDataApiService"
    }
}