package com.hussienfahmy.sync_domain.di

import com.hussienfahmy.sync_domain.use_case.GetIsFirstTimeInstall
import com.hussienfahmy.sync_domain.use_case.PullSettings
import com.hussienfahmy.sync_domain.use_case.PullSubjects
import com.hussienfahmy.sync_domain.use_case.PushSettings
import com.hussienfahmy.sync_domain.use_case.PushSubjects
import com.hussienfahmy.sync_domain.use_case.SetIsFirstTimeInstall
import com.hussienfahmy.sync_domain.worker.SyncWorker
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
        GetIsFirstTimeInstall(
            appDataRepository = get(),
        )
    }

    single {
        SetIsFirstTimeInstall(
            appDataRepository = get(),
        )
    }

    worker {
        SyncWorker(
            appContext = get(),
            workerParams = get(),
            pullSubjects = get(),
            pushSubjects = get(),
            pushSettings = get(),
            pullSettings = get(),
            setIsFirstTimeInstall = get(),
            getIsFirstTimeInstall = get()
        )
    }
}