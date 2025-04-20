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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltWorker
class UploadGoalWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val createGoalUseCase: CreateGoalUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
            val id = inputData.getString("id") ?: ""
            val title = inputData.getString("title") ?: return Result.failure()
            val description = inputData.getString("description")
            val metricTarget = inputData.getString("metricTarget")
            val metricUnit = inputData.getString("metricUnit")
            val targetDateString = inputData.getString("targetDate") ?: return Result.failure()
            val createdAtString = inputData.getString("createdAt") ?: return Result.failure()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val targetDate = dateFormat.parse(targetDateString) ?: return Result.failure()
            val createdAt = dateFormat.parse(createdAtString) ?: Date()
            val imageUriString = inputData.getString("imageUri")
            val imageUri = if (imageUriString != null) Uri.parse(imageUriString) else null

            val goal = Goal(
                id = id,
                title = title,
                description = description,
                metricTarget = metricTarget,
                metricUnit = metricUnit,
                targetDate = targetDate,
                createdAt = createdAt,
                imageUri = imageUri,
            )

        return writeUserData(goal)
    }

    private suspend fun writeUserData(goal: Goal):Result {
        var workResult = Result.failure()
        createGoalUseCase(goal).collect {result->
            when(result){
                is Resource.Error -> {
                    val outputData = workDataOf("error_message" to result.error)
                    workResult = Result.failure(outputData)
                }
                is Resource.Loading -> {
                     Log.d("UploadUserStatus","in worker, loading")
                }
                is Resource.Success -> {
                    workResult = Result.success()
                }
            }
        }

        return workResult
    }
}