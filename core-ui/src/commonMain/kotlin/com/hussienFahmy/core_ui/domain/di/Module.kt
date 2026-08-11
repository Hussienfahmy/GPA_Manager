package com.hussienfahmy.core_ui.domain.di

import com.hussienfahmy.core_ui.domain.use_cases.FilterToDigitsOnly
import com.hussienfahmy.core_ui.presentation.user_data.UserDataViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val coreUiDomainModule = module {
    singleOf(::FilterToDigitsOnly)
    viewModelOf(::UserDataViewModel)
}
