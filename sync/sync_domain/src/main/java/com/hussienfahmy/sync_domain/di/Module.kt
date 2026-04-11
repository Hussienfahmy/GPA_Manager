package com.hussienfahmy.sync_domain.di

import com.hussienfahmy.core.domain.sync.SyncDownload
import com.hussienfahmy.core.domain.sync.SyncUpload
import com.hussienfahmy.sync_domain.use_case.MigrateExistingUserDataIfNeeded
import com.hussienfahmy.sync_domain.use_case.PullSemesters
import com.hussienfahmy.sync_domain.use_case.PullSettings
import com.hussienfahmy.sync_domain.use_case.PullSubjects
import com.hussienfahmy.sync_domain.use_case.PushSemesters
import com.hussienfahmy.sync_domain.use_case.PushSettings
import com.hussienfahmy.sync_domain.use_case.PushSubjects
import com.hussienfahmy.sync_domain.use_case.SyncDownloadImpl
import com.hussienfahmy.sync_domain.use_case.SyncUploadImpl
import com.hussienfahmy.sync_domain.worker.SyncWorkerUpload
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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
        PushSemesters(
            repository = get(),
            semesterDao = get(),
            subjectDao = get(),
        )
    }

    single {
        PullSemesters(
            repository = get(),
            semesterDao = get(),
            subjectDao = get(),
        )
    }

    singleOf(::SyncDownloadImpl).bind<SyncDownload>()

    singleOf(::SyncUploadImpl).bind<SyncUpload>()

    worker {
        SyncWorkerUpload(
            appContext = get(),
            workerParams = get(),
            syncUpload = get()
        )
    }

    singleOf(::MigrateExistingUserDataIfNeeded)
}