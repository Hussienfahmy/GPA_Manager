package com.hussienfahmy.core.data.local.di

import com.hussienfahmy.core.data.local.AppDatabase
import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.SubjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DaoModule {

    @Provides
    @ViewModelScoped
    fun provideSubjectDao(appDatabase: AppDatabase): SubjectDao {
        return appDatabase.subjectDao
    }

    @Provides
    @ViewModelScoped
    fun provideGradeDao(appDatabase: AppDatabase): GradeDao {
        return appDatabase.gradeDao
    }
}