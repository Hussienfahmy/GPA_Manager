package com.hussienfahmy.myGpaManager.di

import com.hussienfahmy.myGpaManager.MainViewModel
import com.hussienfahmy.myGpaManager.navigation.screens.more.MoreViewModel
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingGPATrackingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedKoinModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::MoreViewModel)
    viewModelOf(::AppOnBoardingGPATrackingViewModel)
}
