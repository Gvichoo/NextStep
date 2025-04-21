package com.tbacademy.nextstep.di

import androidx.work.WorkerFactory
import com.tbacademy.nextstep.App
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {

    @Provides
    fun provideCustomWorkerFactory(
        createGoalUseCase: CreateGoalUseCase
    ): WorkerFactory {
        return App.CustomWorkerFactory(createGoalUseCase)
    }
}

