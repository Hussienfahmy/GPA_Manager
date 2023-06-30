package com.hussienfahmy.gpa_system_sittings_domain.di

import com.hussienfahmy.gpa_system_sittings_domain.repository.GPASystemRepository
import com.hussienfahmy.gpa_system_sittings_domain.use_case.GetGPA
import com.hussienfahmy.gpa_system_sittings_domain.use_case.ObserveGPA
import com.hussienfahmy.gpa_system_sittings_domain.use_case.UpdateGPASystem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object Module {

    @Provides
    @ViewModelScoped
    fun provideGetGpaSystem(
        repository: GPASystemRepository
    ): GetGPA = GetGPA(repository)

    @Provides
    @ViewModelScoped
    fun provideObserveGpaSystem(
        repository: GPASystemRepository
    ): ObserveGPA = ObserveGPA(repository)

    @Provides
    @ViewModelScoped
    fun provideUpdateGPASystem(
        repository: GPASystemRepository
    ): UpdateGPASystem = UpdateGPASystem(repository)
}