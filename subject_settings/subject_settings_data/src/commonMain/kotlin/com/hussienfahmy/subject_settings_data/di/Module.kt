package com.hussienfahmy.subject_settings_data.di

import com.hussienfahmy.core.domain.subject_settings.repository.SubjectSettingsRepository
import com.hussienfahmy.subject_settings_data.datastore.SubjectSettingsDataSource
import com.hussienfahmy.subject_settings_data.datastore.SubjectSettingsSerializer
import com.hussienfahmy.subject_settings_data.repository.SubjectSettingsRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val subjectSettingsDataModule = module {
    singleOf(::SubjectSettingsSerializer)
    singleOf(::SubjectSettingsDataSource)
    singleOf(::SubjectSettingsRepositoryImpl).bind<SubjectSettingsRepository>()
}