package com.tbacademy.nextstep

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.google.firebase.FirebaseApp
import com.tbacademy.nextstep.data.worker.UploadGoalWorker
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: CustomWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        WorkManager.initialize(this, workManagerConfiguration)
    }



    class CustomWorkerFactory @Inject constructor(
        private val createGoalUseCase: CreateGoalUseCase
    ) : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return when (workerClassName) {
                UploadGoalWorker::class.java.name -> UploadGoalWorker(
                    appContext,
                    workerParameters,
                    createGoalUseCase
                )

                else -> throw IllegalArgumentException("Unknown worker class name: $workerClassName")
            }
        }
    }
}