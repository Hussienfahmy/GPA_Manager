package com.hussienfahmy.subject_settings_data.di

import com.hussienfahmy.core.domain.subject_settings.repository.SubjectSettingsRepository
import com.hussienfahmy.subject_settings_data.datastore.SubjectSettingsDataSource
import com.hussienfahmy.subject_settings_data.repository.SubjectSettingsRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val subjectSettingsDataModule = module {
    single { SubjectSettingsDataSource(get()) }
    single { SubjectSettingsRepositoryImpl(get()) }.bind<SubjectSettingsRepository>()
}