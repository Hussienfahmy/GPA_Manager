package com.hussienfahmy.myGpaManager.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import org.koin.dsl.module

val firebaseModule = module {
    single<FirebaseFirestore> {
        val settings = firestoreSettings {
            setLocalCacheSettings(persistentCacheSettings {
                setSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            })
        }
        val db = Firebase.firestore
        db.firestoreSettings = settings
        db
    }

    single<FirebaseAuth> {
        Firebase.auth
    }

    single<FirebaseStorage> { Firebase.storage }
}