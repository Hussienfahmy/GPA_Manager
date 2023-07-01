package com.hussienfahmy.gpa_system_settings_data.di

import android.content.Context
import com.hussienFahmy.core.domain.gpa_settings.repository.GPASettingsRepository
import com.hussienfahmy.gpa_system_settings_data.datastore.GPADatastore
import com.hussienfahmy.gpa_system_settings_data.repository.GPASettingsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object Module {

    @Provides
    @Singleton
    fun provideGPASystemRepository(
        dataStore: GPADatastore
    ): GPASettingsRepository {
        return GPASettingsRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideGPADatastore(
        @ApplicationContext context: Context
    ): GPADatastore {
        return GPADatastore(context)
    }
}