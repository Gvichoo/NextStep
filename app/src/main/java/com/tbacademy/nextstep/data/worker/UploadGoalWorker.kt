package com.tbacademy.nextstep.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.tbacademy.nextstep.R
import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import com.tbacademy.nextstep.presentation.MainActivity
import com.tbacademy.nextstep.presentation.model.MilestoneItem
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


        val imageUriString = inputData.getString("imageUri")
        val imageUri = imageUriString?.let { Uri.parse(it) }

        val targetDateString = inputData.getString("targetDate")
        val createdAtString = inputData.getString("createdAt")


        if (targetDateString == null || createdAtString == null) {
            return Result.failure()
        }

        val milestoneJson = inputData.getString("milestone")
        val milestone: List<MilestoneItem> = milestoneJson?.let {
            try {
                val type = object : TypeToken<List<MilestoneItem>>() {}.type
                Gson().fromJson(it, type)
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()


        val goal = Goal(
            id = inputData.getString("id") ?: "",
            title = inputData.getString("title") ?: return Result.failure(),
            description = inputData.getString("description") ?: "",
            authorId = inputData.getString("authorId") ?: "",
            authorUsername = inputData.getString("authorUsername") ?: "",
            imageUrl = inputData.getString("imageUrl") ?: "",
            metricTarget = inputData.getString("metricTarget"),
            metricUnit = inputData.getString("metricUnit"),
            targetDate = targetDateString.toLongOrNull() ?: return Result.failure(),
            createdAt = createdAtString.toLongOrNull() ?: System.currentTimeMillis(),
            imageUri = imageUri,
            milestone = milestone
        )


        return writeUserData(goal)
    }

    private suspend fun writeUserData(goal: Goal): Result {

        var workResult = Result.failure()

        try {
            createGoalUseCase(goal)
                .onStart {
                }
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            showNotification(
                                "There was error uploading your goal."
                            )
                            val outputData = workDataOf("error_message" to result.error)
                            workResult = Result.failure(outputData)
                        }
                        is Resource.Success -> {
                            showNotification("Your goal was uploaded successfully.")
                            workResult = Result.success()
                        }
                        is Resource.Loading -> {
                            Log.d("UploadGoalWorker", "Uploading goal...")
                        }
                    }
                }
        } catch (e: Exception) {
            Log.e("UploadGoalWorker", "Exception during goal upload: ${e.message}", e)
            workResult = Result.failure()
        }

        return workResult
    }


    private fun showNotification(message: String) {
        if (isAppInForeground(applicationContext)) {
            return
        }

        val channelId = "goal_channel_id"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Goal Upload",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                setSound(Settings.System.DEFAULT_NOTIFICATION_URI, null)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.df_profile_picture)
            .setContentTitle(message)
            .setContentText("NextStep")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        notificationManager.notify(1, notification)

    }


    private fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false

        val packageName = context.packageName
        for (appProcess in appProcesses) {
            if (appProcess.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                appProcess.processName == packageName
            ) {
                return true
            }
        }
        return false
    }


}