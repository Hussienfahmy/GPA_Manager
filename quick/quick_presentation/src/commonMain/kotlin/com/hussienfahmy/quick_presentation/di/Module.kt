package com.hussienfahmy.quick_presentation.di

import com.hussienfahmy.quick_presentation.QuickViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val quickPresentationModule = module {
    viewModelOf(::QuickViewModel)
}