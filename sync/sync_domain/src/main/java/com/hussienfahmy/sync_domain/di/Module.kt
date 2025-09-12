package com.hussienfahmy.sync_domain.di

import com.hussienfahmy.sync_domain.use_case.GetIsInitialSyncDone
import com.hussienfahmy.sync_domain.use_case.PullSettings
import com.hussienfahmy.sync_domain.use_case.PullSubjects
import com.hussienfahmy.sync_domain.use_case.PushSettings
import com.hussienfahmy.sync_domain.use_case.PushSubjects
import com.hussienfahmy.sync_domain.use_case.SetIsInitialSyncDone
import com.hussienfahmy.sync_domain.use_case.SyncDownload
import com.hussienfahmy.sync_domain.use_case.SyncUpload
import com.hussienfahmy.sync_domain.worker.SyncWorkerUpload
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val syncDomainModule = module {
    single {
        PullSettings(
            repository = get(),
            updateGPASystem = get(),
            gradeDao = get(),
            subjectSettingsRepository = get(),
        )
    }

    single {
        PullSubjects(
            repository = get(),
            subjectDao = get(),
        )
    }

    single {
        PushSettings(
            repository = get(),
            getGPASettings = get(),
            gradeDao = get(),
            subjectSettingsRepository = get(),
        )
    }

    single {
        PushSubjects(
            repository = get(),
            subjectsDao = get(),
        )
    }

    single {
        GetIsInitialSyncDone(
            appDataRepository = get(),
        )
    }

    single {
        SetIsInitialSyncDone(
            appDataRepository = get(),
        )
    }

    single {
        SyncDownload(
            pullSubjects = get(),
            pullSettings = get(),
            setIsInitialSyncDone = get(),
        )
    }

    single {
        SyncUpload(
            pushSubjects = get(),
            pushSettings = get(),
        )
    }

    worker {
        SyncWorkerUpload(
            appContext = get(),
            workerParams = get(),
            syncUpload = get()
        )
    }
}