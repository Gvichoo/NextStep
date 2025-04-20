package com.tbacademy.nextstep.data.worker

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.onStart

@HiltWorker
class UploadGoalWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val createGoalUseCase: CreateGoalUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val imageUriString = inputData.getString("imageUri") // just one string
        val imageUri = imageUriString?.let { Uri.parse(it) }

        val targetDateString = inputData.getString("targetDate") ?: return Result.failure()
        val createdAtString = inputData.getString("createdAt") ?: return Result.failure()

        val goal = Goal(
            id = inputData.getString("id") ?: "",
            title = inputData.getString("title") ?: return Result.failure(),
            description = inputData.getString("description"),
            metricTarget = inputData.getString("metricTarget"),
            metricUnit = inputData.getString("metricUnit"),
            targetDate = targetDateString.toLongOrNull() ?: return Result.failure(),
            createdAt = createdAtString.toLongOrNull() ?: System.currentTimeMillis(),
            imageUri = imageUri
        )

        return writeUserData(goal)
    }

    private suspend fun writeUserData(goal: Goal): Result {
        var workResult = Result.failure()

        createGoalUseCase(goal)
            .onStart {
                Log.d("UploadGoalWorker", "Start goal upload...")
            }
            .collect { result ->
                when (result) {
                    is Resource.Error -> {
                        val outputData = workDataOf("error_message" to result.error)
                        workResult = Result.failure(outputData)
                    }

                    is Resource.Success -> workResult = Result.success()
                    is Resource.Loading -> Log.d("UploadGoalWorker", "Uploading...")
                }
            }

        return workResult
    }
}