package com.hussienfahmy.myGpaManager.di

import androidx.credentials.CredentialManager
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
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.core.domain.storage.repository.StorageRepository
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.myGpaManager.data.auth.FirebaseAuthRepository
import com.hussienfahmy.myGpaManager.data.auth.GoogleAuthService
import com.hussienfahmy.myGpaManager.data.auth.GoogleAuthUiClient
import com.hussienfahmy.myGpaManager.data.storage.FirebaseStorageRepository
import com.hussienfahmy.myGpaManager.data.sync.FirebaseSyncRepository
import com.hussienfahmy.myGpaManager.data.user_data.FirebaseUserDataRepository
import com.hussienfahmy.sync_domain.repository.SyncRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val firebaseModule = module {
    // Firebase SDK instances
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

    // Repository implementations
    single<AuthRepository> {
        FirebaseAuthRepository(get(), get())
    }

    single<StorageRepository> {
        FirebaseStorageRepository(get())
    }

    single<UserDataRepository> {
        FirebaseUserDataRepository(get(), get(), get())
    }

    single<SyncRepository> {
        FirebaseSyncRepository(get(), get())
    }

    // Credential Manager and Auth UI Client
    single<CredentialManager> {
        CredentialManager.create(androidContext())
    }

    single {
        GoogleAuthUiClient(
            context = androidContext(),
            credentialManager = get(),
            authRepository = get()
        )
    }

    single<AuthService> {
        GoogleAuthService(get())
    }
}