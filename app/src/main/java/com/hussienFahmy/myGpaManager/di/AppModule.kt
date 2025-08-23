package com.hussienfahmy.myGpaManager.di

import androidx.work.WorkManager
import com.hussienfahmy.myGpaManager.MainViewModel
import com.hussienfahmy.myGpaManager.navigation.screens.more.MoreViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appKoinModule = module {
    single { WorkManager.getInstance(get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { MoreViewModel(get(), get()) }
}