package com.hussienfahmy.myGpaManager.di

import androidx.credentials.CredentialManager
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.core.domain.auth.service.AuthSignIn
import com.hussienfahmy.myGpaManager.data.auth.GoogleAuthService
import com.hussienfahmy.myGpaManager.data.auth.GoogleAuthUiClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

// The GitLive Firebase singletons + AuthRepository/StorageRepository/UserDataRepository/
// SyncRepository bindings moved to :shared's sharedFirebaseModule (Phase 11 - they had zero
// Android coupling). What's left here needs androidx.credentials specifically: CredentialManager,
// GoogleAuthUiClient (Google Sign-In's CredentialManager-based flow), and the AuthService/
// AuthSignIn bindings that wrap it.
val firebaseModule = module {
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

    // .bind<T>() narrows the KoinDefinition to T on return, so chaining two of them fails once
    // AuthService and AuthSignIn are unrelated sibling interfaces (the second bind can't see the
    // original GoogleAuthService type anymore) - binds(Array<KClass<*>>) sets both without narrowing.
    singleOf(::GoogleAuthService).binds(arrayOf(AuthService::class, AuthSignIn::class))
}
