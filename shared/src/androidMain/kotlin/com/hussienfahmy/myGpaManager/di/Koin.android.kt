package com.hussienfahmy.myGpaManager.di

import androidx.credentials.CredentialManager
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import androidx.work.WorkManager
import com.hussienfahmy.core.domain.auth.GoogleAuthUiClient
import com.hussienfahmy.core.util.PlatformContext
import com.hussienfahmy.sync_domain.di.syncWorkerModule
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.core.module.Module
import org.koin.dsl.module

// Replicates Koin's androidContext()/workManagerFactory() manually - both are KoinApplication
// builder-only calls, can't run from commonMain's initKoin().
actual fun platformModules(context: PlatformContext): List<Module> {
    WorkManager.initialize(
        context,
        Configuration.Builder()
            .setWorkerFactory(DelegatingWorkerFactory().apply { addFactory(KoinWorkerFactory()) })
            .build(),
    )

    return listOf(
        module {
            single { context }
            single { WorkManager.getInstance(context) }

            single<CredentialManager> { CredentialManager.create(context) }
            single {
                GoogleAuthUiClient(
                    credentialManager = get(),
                    authRepository = get(),
                    crashReporter = get()
                )
            }
        },
        syncWorkerModule,
    )
}
