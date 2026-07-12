package com.hussienfahmy.myGpaManager.di

import androidx.credentials.CredentialManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
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
import dev.gitlive.firebase.Firebase as GitLiveFirebase
import dev.gitlive.firebase.firestore.FirebaseFirestore as GitLiveFirebaseFirestore
import dev.gitlive.firebase.firestore.firestore as gitLiveFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val firebaseModule = module {
    // Firestore migrated to the GitLive Firebase-Kotlin-SDK (multiplatform, kotlinx.serialization
    // -based) - Auth/Storage below are still the Android Firebase SDK, migrated in their own
    // follow-up sub-PRs. Dropped the persistent-cache-size customization (previously
    // CACHE_SIZE_UNLIMITED) since GitLive's settings API differs; defaults still provide offline
    // caching, just not explicitly unlimited-sized - flagged as a deliberate simplification to
    // revisit if offline cache size turns out to matter in practice.
    single<GitLiveFirebaseFirestore> {
        GitLiveFirebase.gitLiveFirestore
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

    singleOf(::FirebaseSyncRepository).bind<SyncRepository>()

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