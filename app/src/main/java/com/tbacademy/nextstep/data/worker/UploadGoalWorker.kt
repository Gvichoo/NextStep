package com.tbacademy.nextstep.data.worker

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.last
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltWorker
class UploadGoalWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val createGoalUseCase: CreateGoalUseCase
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "UploadGoalWorker"
        const val KEY_TITLE = "title"
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE_URI = "image_uri"
        const val KEY_IS_METRIC_BASED = "isMetricBased"
        const val KEY_METRIC_TARGET = "metricTarget"
        const val KEY_METRIC_UNIT = "metricUnit"
        const val KEY_TARGET_DATE = "targetDate"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting goal upload work")

        val title = inputData.getString(KEY_TITLE) ?: return Result.failure()
        val description = inputData.getString(KEY_DESCRIPTION)
        val imageUri = inputData.getString(KEY_IMAGE_URI)?.let { Uri.parse(it) }
        val isMetricBased = inputData.getBoolean(KEY_IS_METRIC_BASED, false)
        val metricTarget = inputData.getString(KEY_METRIC_TARGET)
        val metricUnit = inputData.getString(KEY_METRIC_UNIT)
        val targetDateMillis = inputData.getLong(KEY_TARGET_DATE, 0L)
        val targetDate = if (targetDateMillis > 0) Date(targetDateMillis) else Date()

        Log.d(TAG, "Processing goal: $title")

        val goal = Goal(
            title = title,
            description = description,
            imageUri = imageUri,
            isMetricBased = isMetricBased,
            metricTarget = metricTarget,
            metricUnit = metricUnit,
            targetDate = targetDate,
            createdAt = Date()
        )

        // Improved way to collect the final result from the flow
        try {
            // We need the final result from the flow
            return when (val finalResult = createGoalUseCase.invoke(goal).last()) {
                is Resource.Success -> {
                    Log.d(TAG, "Goal upload successful")
                    Result.success()
                }
                is Resource.Error -> {
                    Log.e(TAG, "Goal upload failed: ${finalResult.error}")
                    // For transient errors, retry rather than fail
                    Result.retry()
                }
                is Resource.Loading -> {
                    // This shouldn't be the last state, but just in case
                    Log.w(TAG, "Unexpected final Loading state")
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during upload", e)
            return Result.retry()
        }
    }
}

class  WorkManagerHelper @Inject constructor(
context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    fun scheduleGoalUpload(goal: Goal): UUID {
        // Create input data with all goal fields
        val inputData = Data.Builder()
            .putString(UploadGoalWorker.KEY_TITLE, goal.title)
            .putString(UploadGoalWorker.KEY_DESCRIPTION, goal.description)
            .putString(UploadGoalWorker.KEY_IMAGE_URI, goal.imageUri?.toString())
            .putBoolean(UploadGoalWorker.KEY_IS_METRIC_BASED, goal.isMetricBased)
            .putString(UploadGoalWorker.KEY_METRIC_TARGET, goal.metricTarget)
            .putString(UploadGoalWorker.KEY_METRIC_UNIT, goal.metricUnit)
            .putLong(UploadGoalWorker.KEY_TARGET_DATE, goal.targetDate?.time ?: 0L)
            .build()

        // Define constraints - require network connection
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Create work request
        val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadGoalWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .addTag("goal_upload")
            .build()

        // Enqueue the work
        workManager.enqueueUniqueWork(
            "goal_upload_${System.currentTimeMillis()}",
            ExistingWorkPolicy.REPLACE,
            uploadWorkRequest
        )

        return uploadWorkRequest.id
    }

    fun cancelWork(workId: UUID) {
        workManager.cancelWorkById(workId)
    }

    fun cancelAllUploads() {
        workManager.cancelAllWorkByTag("goal_upload")
    }
}


