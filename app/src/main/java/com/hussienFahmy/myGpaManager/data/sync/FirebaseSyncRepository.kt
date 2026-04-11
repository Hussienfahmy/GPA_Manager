package com.hussienfahmy.myGpaManager.data.sync

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.hussienfahmy.myGpaManager.data.common.mapper.toDomain
import com.hussienfahmy.myGpaManager.data.common.mapper.toFirebase
import com.hussienfahmy.myGpaManager.data.sync.model.FirebaseNetworkSemester
import com.hussienfahmy.myGpaManager.data.sync.model.FirebaseNetworkSubjects
import com.hussienfahmy.myGpaManager.data.sync.model.FirebaseSettings
import com.hussienfahmy.myGpaManager.data.sync.model.toFirebase
import com.hussienfahmy.myGpaManager.data.user_data.model.FirebaseUserData
import com.hussienfahmy.sync_domain.model.NetworkSemester
import com.hussienfahmy.sync_domain.model.NetworkSubjects
import com.hussienfahmy.sync_domain.model.Settings
import com.hussienfahmy.sync_domain.model.Subject
import com.hussienfahmy.sync_domain.repository.SyncRepository
import kotlinx.coroutines.tasks.await

class FirebaseSyncRepository(
    private val db: FirebaseFirestore
) : SyncRepository {

    private fun subjectsDoc(userId: String) = db.collection(SUBJECTS_COLLECTION).document(userId)

    private fun settingsDoc(userId: String) = db.collection(SETTINGS_COLLECTION).document(userId)

    private fun semestersCollection(userId: String) =
        db.collection(FirebaseUserData.USERS_COLLECTION_NAME)
            .document(userId)
            .collection(SEMESTERS_SUBCOLLECTION)

    private fun userDoc(userId: String) =
        db.collection(FirebaseUserData.USERS_COLLECTION_NAME).document(userId)

    override suspend fun uploadSubjects(userId: String, subjects: List<Subject>) {
        val doc = subjectsDoc(userId)
        val firebaseNetworkSubjects = FirebaseNetworkSubjects(
            subjects = subjects,
            lastUpdate = Timestamp.now()
        )
        doc.set(firebaseNetworkSubjects).await()
    }

    override suspend fun downloadSubjects(userId: String): NetworkSubjects? {
        val doc = subjectsDoc(userId)
        val firebaseData = doc.get().await()?.toObject<FirebaseNetworkSubjects>()

        return firebaseData?.let {
            NetworkSubjects(
                subjects = it.subjects,
                lastUpdate = it.lastUpdate?.toDomain()
            )
        }
    }

    override suspend fun uploadSettings(userId: String, settings: Settings) {
        val doc = settingsDoc(userId)
        val firebaseSettings = FirebaseSettings(
            networkGrades = settings.networkGrades,
            calculationSettings = settings.calculationSettings,
            lastUpdate = settings.lastUpdate?.toFirebase()
        )
        doc.set(firebaseSettings).await()
    }

    override suspend fun downloadSettings(userId: String): Settings? {
        val doc = settingsDoc(userId)
        val firebaseData = doc.get().await()?.toObject<FirebaseSettings>()
        return firebaseData?.let {
            Settings(
                networkGrades = it.networkGrades,
                calculationSettings = it.calculationSettings,
                lastUpdate = it.lastUpdate?.toDomain()
            )
        }
    }

    override suspend fun uploadSemesters(userId: String, semesters: List<NetworkSemester>) {
        val collection = semestersCollection(userId)

        // Use a batch to atomically delete stale documents and write the current state
        val batch = db.batch()
        val existingDocs = collection.get().await().documents

        existingDocs.forEach { batch.delete(it.reference) }
        semesters.forEach { semester ->
            batch.set(collection.document(), semester.toFirebase())
        }
        batch.commit().await()
    }

    override suspend fun downloadSemesters(userId: String): List<NetworkSemester>? {
        val collection = semestersCollection(userId)
        return try {
            val docs = collection.get().await().documents

            docs.mapNotNull { doc ->
                doc.toObject(FirebaseNetworkSemester::class.java)?.toDomain()
            }
        } catch (e: Exception) {
            Firebase.crashlytics.recordException(e)
            null
        }
    }

    override suspend fun updateAcademicProgress(
        userId: String,
        cumulativeGPA: Double,
        creditHours: Int
    ) {
        val doc = userDoc(userId)

        doc.update(
            mapOf(
                FirebaseUserData.PROPERTY_ACADEMIC_PROGRESS_CUMULATIVE_GPA to cumulativeGPA,
                FirebaseUserData.PROPERTY_ACADEMIC_PROGRESS_CREDIT_HOURS to creditHours,
            )
        ).await()
    }

    override suspend fun isLegacyGpaMigrated(userId: String): Boolean {
        val doc = userDoc(userId)
        return doc.get().await().getBoolean(PROPERTY_LEGACY_GPA_MIGRATED) ?: false
    }

    override suspend fun setLegacyGpaMigrated(userId: String) {
        val doc = userDoc(userId)
        doc.update(PROPERTY_LEGACY_GPA_MIGRATED, true).await()
    }

    companion object {
        const val SUBJECTS_COLLECTION = "subjects"
        const val SETTINGS_COLLECTION = "gpa_settings"
        const val SEMESTERS_SUBCOLLECTION = "semesters"
        private const val PROPERTY_LEGACY_GPA_MIGRATED = "legacyGpaMigrated"
    }
}
