package com.hussienfahmy.gpa_system_sittings_data.di

import android.content.Context
import com.hussienfahmy.gpa_system_sittings_data.datastore.GPADatastore
import com.hussienfahmy.gpa_system_sittings_data.repository.GPASystemRepositoryImpl
import com.hussienfahmy.gpa_system_sittings_domain.repository.GPASystemRepository
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
    ): GPASystemRepository {
        return GPASystemRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideGPADatastore(
        @ApplicationContext context: Context
    ): GPADatastore {
        return GPADatastore(context)
    }
}