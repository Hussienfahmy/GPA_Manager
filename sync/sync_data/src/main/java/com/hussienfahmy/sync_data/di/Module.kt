package com.hussienfahmy.sync_data.di

import com.hussienfahmy.sync_data.datastore.AppDatastore
import com.hussienfahmy.sync_data.repository.AppDataRepositoryImpl
import com.hussienfahmy.sync_domain.repository.AppDataRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val syncDataModule = module {
    single {
        AppDatastore(androidContext())
    }

    single<AppDataRepository> {
        AppDataRepositoryImpl(get())
    }
}