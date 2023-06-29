package com.hussienfahmy.quick_domain.di

import com.hussienfahmy.quick_domain.use_cases.CalculatePercentage
import com.hussienfahmy.quick_domain.use_cases.QuickCalculate
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
    fun provideQuickCalculate() = QuickCalculate()

    @Provides
    @ViewModelScoped
    fun provideCalculatePercentage() = CalculatePercentage()
}