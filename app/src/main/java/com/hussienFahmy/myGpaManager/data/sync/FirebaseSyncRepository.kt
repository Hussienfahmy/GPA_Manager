package com.hussienfahmy.myGpaManager.data.sync

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.myGpaManager.data.common.mapper.toDomain
import com.hussienfahmy.myGpaManager.data.common.mapper.toFirebase
import com.hussienfahmy.myGpaManager.data.sync.model.FirebaseNetworkSubjects
import com.hussienfahmy.myGpaManager.data.sync.model.FirebaseSettings
import com.hussienfahmy.sync_domain.model.NetworkSubjects
import com.hussienfahmy.sync_domain.model.Settings
import com.hussienfahmy.sync_domain.model.Subject
import com.hussienfahmy.sync_domain.repository.SyncRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirebaseSyncRepository(
    db: FirebaseFirestore,
    authRepository: AuthRepository
) : SyncRepository {

    private val subjectsDoc = authRepository.userId.map { userId ->
        userId?.let { db.collection(SUBJECTS_COLLECTION).document(it) }
    }

    private val settingsDoc = authRepository.userId.map { userId ->
        userId?.let { db.collection(SETTINGS_COLLECTION).document(it) }
    }

    override suspend fun uploadSubjects(subjects: List<Subject>) {
        val firebaseNetworkSubjects = FirebaseNetworkSubjects(
            subjects = subjects,
            lastUpdate = Timestamp.now()
        )
        subjectsDoc.first()?.set(firebaseNetworkSubjects)?.await()
    }

    override suspend fun downloadSubjects(): NetworkSubjects? {
        val firebaseData = subjectsDoc.first()?.get()?.await()?.toObject<FirebaseNetworkSubjects>()
        return firebaseData?.let {
            NetworkSubjects(
                subjects = it.subjects,
                lastUpdate = it.lastUpdate?.toDomain()
            )
        }
    }

    override suspend fun uploadSettings(settings: Settings) {
        val firebaseSettings = FirebaseSettings(
            networkGrades = settings.networkGrades,
            calculationSettings = settings.calculationSettings,
            lastUpdate = settings.lastUpdate?.toFirebase()
        )
        settingsDoc.first()?.set(firebaseSettings)?.await()
    }

    override suspend fun downloadSettings(): Settings? {
        val firebaseData = settingsDoc.first()?.get()?.await()?.toObject<FirebaseSettings>()
        return firebaseData?.let {
            Settings(
                networkGrades = it.networkGrades,
                calculationSettings = it.calculationSettings,
                lastUpdate = it.lastUpdate?.toDomain()
            )
        }
    }

    companion object {
        const val SUBJECTS_COLLECTION = "subjects"
        const val SETTINGS_COLLECTION = "gpa_settings"
    }
}