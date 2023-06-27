package com.hussienFahmy.core_ui.domain.di

import com.hussienFahmy.core_ui.domain.use_cases.FilterToDigitsOnly
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
    fun provideFilterToDigitsOnly(): FilterToDigitsOnly {
        return FilterToDigitsOnly()
    }
}

