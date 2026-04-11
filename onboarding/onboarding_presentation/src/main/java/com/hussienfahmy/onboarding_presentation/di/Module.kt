package com.hussienfahmy.onboarding_presentation.di

import com.hussienfahmy.onboarding_presentation.sign_in.SignInViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onboardingPresentationModule = module {
    viewModelOf(::SignInViewModel)
}