package com.hussienfahmy.core.domain.subject_settings.di

import com.hussienfahmy.core.domain.subject_settings.use_case.GetSubjectsSettings
import org.koin.dsl.module

val coreSubjectSettingsDomainModule = module {
    single { GetSubjectsSettings(get()) }
}