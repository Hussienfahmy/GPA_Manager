package com.hussienfahmy.onboarding_presentation.sign_in.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
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
        oneTapClient = Identity.getSignInClient(context.applicationContext)
    )
}