package com.hussienfahmy.quick_domain.di

import com.hussienfahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienfahmy.quick_domain.use_cases.CalculatePercentage
import com.hussienfahmy.quick_domain.use_cases.QuickCalculate
import org.koin.dsl.module

val quickDomainModule = module {
    single { QuickCalculate(get<GetGPASettings>()) }
    single { CalculatePercentage(get<GetGPASettings>()) }
}