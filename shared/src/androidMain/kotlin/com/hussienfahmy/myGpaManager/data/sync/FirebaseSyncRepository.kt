package com.hussienfahmy.myGpaManager.data.sync

import com.hussienfahmy.myGpaManager.data.common.mapper.toDomainTimestamp
import com.hussienfahmy.myGpaManager.data.common.mapper.toEpochMillis
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
import dev.gitlive.firebase.firestore.FirebaseFirestore

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
            lastUpdate = System.currentTimeMillis()
        )
        doc.set(firebaseNetworkSubjects)
    }

    override suspend fun downloadSubjects(userId: String): NetworkSubjects? {
        val snapshot = subjectsDoc(userId).get()
        if (!snapshot.exists) return null
        val firebaseData = snapshot.data<FirebaseNetworkSubjects>()

        return NetworkSubjects(
            subjects = firebaseData.subjects,
            lastUpdate = firebaseData.lastUpdate?.toDomainTimestamp()
        )
    }

    override suspend fun uploadSettings(userId: String, settings: Settings) {
        val doc = settingsDoc(userId)
        val firebaseSettings = FirebaseSettings(
            networkGrades = settings.networkGrades,
            calculationSettings = settings.calculationSettings,
            lastUpdate = settings.lastUpdate?.toEpochMillis()
        )
        doc.set(firebaseSettings)
    }

    override suspend fun downloadSettings(userId: String): Settings? {
        val snapshot = settingsDoc(userId).get()
        if (!snapshot.exists) return null
        val firebaseData = snapshot.data<FirebaseSettings>()
        return Settings(
            networkGrades = firebaseData.networkGrades,
            calculationSettings = firebaseData.calculationSettings,
            lastUpdate = firebaseData.lastUpdate?.toDomainTimestamp()
        )
    }

    override suspend fun uploadSemesters(userId: String, semesters: List<NetworkSemester>) {
        val collection = semestersCollection(userId)

        // Use a batch to atomically delete stale documents and write the current state
        val batch = db.batch()
        val existingDocs = collection.get().documents

        existingDocs.forEach { batch.delete(it.reference) }
        semesters.forEach { semester ->
            batch.set(collection.document, semester.toFirebase())
        }
        batch.commit()
    }

    override suspend fun downloadSemesters(userId: String): List<NetworkSemester>? {
        val collection = semestersCollection(userId)
        return try {
            val docs = collection.get().documents

            docs.map { doc ->
                doc.data<FirebaseNetworkSemester>().toDomain()
            }
        } catch (e: Exception) {
            // Crashlytics reporting moved out of this repository - see the Analytics/Crashlytics
            // sub-phase; this still fails safe by returning null like the pre-migration behavior.
            null
        }
    }

    override suspend fun updateAcademicProgress(
        userId: String,
        cumulativeGPA: Double,
        creditHours: Int
    ) {
        userDoc(userId).updateFields {
            FirebaseUserData.PROPERTY_ACADEMIC_PROGRESS_CUMULATIVE_GPA to cumulativeGPA
            FirebaseUserData.PROPERTY_ACADEMIC_PROGRESS_CREDIT_HOURS to creditHours
        }
    }

    override suspend fun isLegacyGpaMigrated(userId: String): Boolean {
        val snapshot = userDoc(userId).get()
        if (!snapshot.exists) return false
        return snapshot.get<Boolean?>(PROPERTY_LEGACY_GPA_MIGRATED) ?: false
    }

    override suspend fun setLegacyGpaMigrated(userId: String) {
        userDoc(userId).updateFields {
            PROPERTY_LEGACY_GPA_MIGRATED to true
        }
    }

    companion object {
        const val SUBJECTS_COLLECTION = "subjects"
        const val SETTINGS_COLLECTION = "gpa_settings"
        const val SEMESTERS_SUBCOLLECTION = "semesters"
        private const val PROPERTY_LEGACY_GPA_MIGRATED = "legacyGpaMigrated"
    }
}
