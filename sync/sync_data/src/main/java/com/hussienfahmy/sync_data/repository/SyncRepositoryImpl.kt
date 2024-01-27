package com.hussienfahmy.sync_data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hussienfahmy.sync_domain.model.NetworkSubjects
import com.hussienfahmy.sync_domain.model.Settings
import com.hussienfahmy.sync_domain.model.Subject
import com.hussienfahmy.sync_domain.repository.SyncRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class SyncRepositoryImpl : SyncRepository {
    private val auth = Firebase.auth
    private val userId = MutableStateFlow(auth.currentUser?.uid)
    private val db = Firebase.firestore

    private val subjectsDoc = userId.filterNotNull().map {
        db.collection(SUBJECTS_COLLECTION).document(it)
    }

    private val settingsDoc = userId.filterNotNull().map {
        db.collection(SETTINGS_COLLECTION).document(it)
    }

    init {
        auth.addAuthStateListener {
            userId.value = it.currentUser?.uid
        }
    }

    override suspend fun uploadSubjects(subjects: List<Subject>) {
        subjectsDoc.first().set(NetworkSubjects(subjects)).await()
    }

    override suspend fun downloadSubjects(): NetworkSubjects? {
        return subjectsDoc.first().get().await().toObject()
    }

    override suspend fun uploadSettings(settings: Settings) {
        settingsDoc.first().set(settings).await()
    }

    override suspend fun downloadSettings(): Settings? {
        return settingsDoc.first().get().await().toObject()
    }

    companion object {
        const val SUBJECTS_COLLECTION = "subjects"
        const val SETTINGS_COLLECTION = "gpa_settings"
    }
}