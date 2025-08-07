package com.hussienfahmy.onboarding_presentation.sign_in.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.hussienfahmy.onboarding_presentation.sign_in.GoogleAuthUiClient
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
    fun provideGoogleAuthUiClient(
        @ApplicationContext context: Context,
    ) = GoogleAuthUiClient(
        context = context,
        credentialManager = CredentialManager.create(context)
    )
}