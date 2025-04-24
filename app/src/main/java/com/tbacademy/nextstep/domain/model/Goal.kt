package com.tbacademy.nextstep.domain.model

import android.net.Uri
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import java.util.Date

data class Goal(
    val id: String = "",
    val title: String,
    val authorId: String = "",
    val authorUsername: String = "",
    val description: String = "",
    val isMetricBased: Boolean = false,
    val metricTarget: String? = null,
    val metricUnit: String? = null,
    val targetDate: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val imageUri: Uri? = null,
    val imageUrl: String = "",
    val goalStatus: GoalStatus = GoalStatus.ACTIVE,
    val milestone: List<MilestoneItem>? = emptyList()
)

enum class GoalStatus{
    ACTIVE,
    COMPLETED,
    FAILED
}