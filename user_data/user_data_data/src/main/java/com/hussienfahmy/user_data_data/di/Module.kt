package com.hussienfahmy.user_data_data.di

import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.user_data_data.repository.UserDataApiService
import org.koin.dsl.module

val userDataDataModule = module {
    single<UserDataRepository> {
        UserDataApiService(
            db = get(),
            auth = get()
        )
    }
}