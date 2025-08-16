package com.hussienfahmy.sync_domain.di

import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.gpa_settings.use_case.GetGPASettings
import com.hussienfahmy.core.domain.gpa_settings.use_case.UpdateGPASystem
import com.hussienfahmy.core.domain.subject_settings.repository.SubjectSettingsRepository
import com.hussienfahmy.sync_domain.repository.AppDataRepository
import com.hussienfahmy.sync_domain.repository.SyncRepository
import com.hussienfahmy.sync_domain.use_case.GetIsFirstTimeInstall
import com.hussienfahmy.sync_domain.use_case.PullSettings
import com.hussienfahmy.sync_domain.use_case.PullSubjects
import com.hussienfahmy.sync_domain.use_case.PushSettings
import com.hussienfahmy.sync_domain.use_case.PushSubjects
import com.hussienfahmy.sync_domain.use_case.SetIsFirstTimeInstall
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {

    @Provides
    @Singleton
    fun providePullSettings(
        repository: SyncRepository,
        updateGPASystem: UpdateGPASystem,
        gradeDao: GradeDao,
        subjectSettingsRepository: SubjectSettingsRepository,
    ): PullSettings {
        return PullSettings(
            repository = repository,
            updateGPASystem = updateGPASystem,
            gradeDao = gradeDao,
            subjectSettingsRepository = subjectSettingsRepository,
        )
    }

    @Provides
    @Singleton
    fun providePullSubjects(
        repository: SyncRepository,
        subjectDao: SubjectDao,
    ): PullSubjects {
        return PullSubjects(
            repository = repository,
            subjectDao = subjectDao,
        )
    }

    @Provides
    @Singleton
    fun providePushSettings(
        repository: SyncRepository,
        getGPASettings: GetGPASettings,
        gradeDao: GradeDao,
        subjectSettingsRepository: SubjectSettingsRepository,
    ): PushSettings {
        return PushSettings(
            repository = repository,
            getGPASettings = getGPASettings,
            gradeDao = gradeDao,
            subjectSettingsRepository = subjectSettingsRepository,
        )
    }

    @Provides
    @Singleton
    fun providePushSubjects(
        repository: SyncRepository,
        subjectDao: SubjectDao,
    ): PushSubjects {
        return PushSubjects(
            repository = repository,
            subjectsDao = subjectDao,
        )
    }

    @Provides
    @Singleton
    fun provideGetIsFirstTimeInstall(
        repository: AppDataRepository,
    ): GetIsFirstTimeInstall {
        return GetIsFirstTimeInstall(
            appDataRepository = repository,
        )
    }

    @Provides
    @Singleton
    fun provideSetIsFirstTimeInstall(
        repository: AppDataRepository,
    ): SetIsFirstTimeInstall {
        return SetIsFirstTimeInstall(
            appDataRepository = repository,
        )
    }
}