package com.hussienfahmy.myGpaManager.data.sync

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.sync_domain.model.NetworkSubjects
import com.hussienfahmy.sync_domain.model.Settings
import com.hussienfahmy.sync_domain.model.Subject
import com.hussienfahmy.sync_domain.repository.SyncRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirebaseSyncRepository(
    db: FirebaseFirestore,
    authRepository: AuthRepository
) : SyncRepository {
    private val userId = MutableStateFlow<String?>(null)

    private val subjectsDoc = userId.filterNotNull().map {
        db.collection(SUBJECTS_COLLECTION).document(it)
    }

    private val settingsDoc = userId.filterNotNull().map {
        db.collection(SETTINGS_COLLECTION).document(it)
    }

    init {
        authRepository.addAuthStateListener { newUserId ->
            userId.value = newUserId
        }
    }

    override suspend fun uploadSubjects(subjects: List<Subject>) {
        val networkSubjects = NetworkSubjects(subjects)
        subjectsDoc.first().set(networkSubjects).await()
    }

    override suspend fun downloadSubjects(): NetworkSubjects? {
        return subjectsDoc.first().get().await().toObject<NetworkSubjects>()
    }

    override suspend fun uploadSettings(settings: Settings) {
        settingsDoc.first().set(settings).await()
    }

    override suspend fun downloadSettings(): Settings? {
        return settingsDoc.first().get().await().toObject<Settings>()
    }

    companion object {
        const val SUBJECTS_COLLECTION = "subjects"
        const val SETTINGS_COLLECTION = "gpa_settings"
    }
}