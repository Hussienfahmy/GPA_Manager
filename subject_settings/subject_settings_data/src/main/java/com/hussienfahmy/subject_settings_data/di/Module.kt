package com.hussienfahmy.subject_settings_data.di

import android.content.Context
import com.hussienfahmy.subject_settings_data.datastore.SubjectSettingsDataSource
import com.hussienfahmy.subject_settings_data.repository.SubjectSettingsRepositoryImpl
import com.hussienfahmy.subject_settings_domain.repository.SubjectSettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideSubjectSettingsDataSource(
        @ApplicationContext context: Context
    ): SubjectSettingsDataSource {
        return SubjectSettingsDataSource(context)
    }

    @Provides
    @Singleton
    fun provideSubjectSettingsRepository(
        subjectSettingsDataSource: SubjectSettingsDataSource
    ): SubjectSettingsRepository {
        return SubjectSettingsRepositoryImpl(subjectSettingsDataSource)
    }
}