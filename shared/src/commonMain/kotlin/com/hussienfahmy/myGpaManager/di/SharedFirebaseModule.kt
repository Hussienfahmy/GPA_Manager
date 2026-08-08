package com.hussienfahmy.myGpaManager.di

import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.storage.repository.StorageRepository
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.myGpaManager.data.auth.FirebaseAuthRepository
import com.hussienfahmy.myGpaManager.data.storage.FirebaseStorageRepository
import com.hussienfahmy.myGpaManager.data.sync.FirebaseSyncRepository
import com.hussienfahmy.myGpaManager.data.user_data.FirebaseUserDataRepository
import com.hussienfahmy.sync_domain.repository.SyncRepository
import dev.gitlive.firebase.Firebase as GitLiveFirebase
import dev.gitlive.firebase.auth.FirebaseAuth as GitLiveFirebaseAuth
import dev.gitlive.firebase.auth.auth as gitLiveAuth
import dev.gitlive.firebase.firestore.FirebaseFirestore as GitLiveFirebaseFirestore
import dev.gitlive.firebase.firestore.firestore as gitLiveFirestore
import dev.gitlive.firebase.storage.FirebaseStorage as GitLiveFirebaseStorage
import dev.gitlive.firebase.storage.storage as gitLiveStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

// GitLive Firebase singletons + repository implementations - genuinely cross-platform (no
// Android calls at all), just physically sitting in :app's Android-only source set until this
// module was extracted. The Android-only pieces (CredentialManager, GoogleAuthUiClient,
// GoogleAuthService, and the AuthService/AuthSignIn bindings) now live in :shared's own
// androidMain platformModules() (see di/Koin.android.kt), since they need androidx.credentials,
// which this commonMain module doesn't otherwise depend on. iOS gets its own AuthService binding
// (see KoinIos.kt) since Sign in with Apple's shape differs from Android's
// CredentialManager-based flow.
val sharedFirebaseModule = module {
    // Dropped the Firestore persistent-cache-size customization (previously
    // CACHE_SIZE_UNLIMITED) since GitLive's settings API differs; defaults still provide offline
    // caching, just not explicitly unlimited-sized - flagged as a deliberate simplification to
    // revisit if offline cache size turns out to matter in practice.
    single<GitLiveFirebaseFirestore> {
        GitLiveFirebase.gitLiveFirestore
    }

    single<GitLiveFirebaseAuth> {
        GitLiveFirebase.gitLiveAuth
    }

    single<GitLiveFirebaseStorage> { GitLiveFirebase.gitLiveStorage }

    single<AuthRepository> {
        FirebaseAuthRepository(get(), get(), get())
    }

    single<StorageRepository> {
        FirebaseStorageRepository(get())
    }

    single<UserDataRepository> {
        FirebaseUserDataRepository(get(), get(), get())
    }

    singleOf(::FirebaseSyncRepository).bind<SyncRepository>()
}
