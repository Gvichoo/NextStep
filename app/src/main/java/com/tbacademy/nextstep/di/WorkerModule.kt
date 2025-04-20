package com.tbacademy.nextstep.di

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
    fun provideWorkerFactory(createGoalUseCase: CreateGoalUseCase): App.CustomWorkerFactory {
        return App.CustomWorkerFactory(createGoalUseCase)
    }
}

