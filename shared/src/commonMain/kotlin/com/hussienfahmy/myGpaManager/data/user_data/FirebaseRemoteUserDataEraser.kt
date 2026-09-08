package com.hussienfahmy.myGpaManager.data.user_data

import com.hussienfahmy.core.domain.crash.CrashReporter
import com.hussienfahmy.core.domain.user_data.repository.RemoteUserDataEraser
import com.hussienfahmy.myGpaManager.data.sync.FirebaseSyncRepository
import com.hussienfahmy.myGpaManager.data.user_data.model.FirebaseUserData
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.FirebaseStorage

/**
 * Deletes everything the app writes for a user in Firestore + Storage:
 * `users/{uid}` (and its `semesters` subcollection - Firestore does NOT cascade, so the
 * subcollection docs must be deleted explicitly or they orphan), `subjects/{uid}`,
 * `gpa_settings/{uid}`, and the profile photo blob at `users/{uid}`.
 *
 * The Firestore side goes in one atomic batch; Storage is a separate service so it can't join
 * the batch. Each side is wrapped so a failure (offline, rules, already gone) is logged but the
 * caller still proceeds to delete the auth account.
 */
class FirebaseRemoteUserDataEraser(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val crashReporter: CrashReporter,
) : RemoteUserDataEraser {

    override suspend fun erase(userId: String) {
        runCatching {
            val userDoc = db.collection(FirebaseUserData.USERS_COLLECTION_NAME).document(userId)
            val semesters = userDoc.collection(FirebaseSyncRepository.SEMESTERS_SUBCOLLECTION)
                .get()
                .documents

            val batch = db.batch()
            semesters.forEach { batch.delete(it.reference) }
            batch.delete(userDoc)
            batch.delete(db.collection(FirebaseSyncRepository.SUBJECTS_COLLECTION).document(userId))
            batch.delete(db.collection(FirebaseSyncRepository.SETTINGS_COLLECTION).document(userId))
            batch.commit()
        }.onFailure {
            crashReporter.recordException(it, mapOf("operation" to "eraseRemoteUserData.firestore", "userId" to userId))
        }

        runCatching {
            storage.reference.child("${FirebaseUserData.USERS_COLLECTION_NAME}/$userId").delete()
        }
    }
}
