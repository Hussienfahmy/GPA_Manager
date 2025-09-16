package com.hussienfahmy.onboarding_presentation.di

import com.hussienfahmy.onboarding_presentation.sign_in.SignInViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onboardingPresentationModule = module {
    viewModel { SignInViewModel(get(), get()) }
}