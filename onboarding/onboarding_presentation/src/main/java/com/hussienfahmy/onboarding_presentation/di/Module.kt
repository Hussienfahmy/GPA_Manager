package com.hussienfahmy.onboarding_presentation.di

import androidx.credentials.CredentialManager
import com.hussienfahmy.onboarding_presentation.sign_in.GoogleAuthUiClient
import com.hussienfahmy.onboarding_presentation.sign_in.SignInViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onboardingPresentationModule = module {

    single<CredentialManager> { CredentialManager.create(get()) }

    single {
        GoogleAuthUiClient(
            context = get(),
            credentialManager = get(),
            auth = get()
        )
    }

    viewModel { SignInViewModel(get()) }
}