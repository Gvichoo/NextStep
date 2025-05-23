package com.tbacademy.nextstep.presentation.screen.main.profile.model

import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import java.sql.Date

data class GoalPresentation(
    val id: String,
    val authorId: String,
    val authorUsername: String,
    val title: String,
    val description: String?,
    val metricTarget: String? = null,
    val metricUnit: String? = null,
    val targetDate: Date,
    val goalStatus: GoalStatusPresentation = GoalStatusPresentation.ACTIVE,
    val createdAt: Long = System.currentTimeMillis(),
    val imageUrl: String?,
    val milestone : List<MilestoneItem>? = emptyList()
)

enum class GoalStatusPresentation(val statusColorRes: Int, val titleRes: Int){
    ACTIVE(statusColorRes = R.color.active, titleRes = R.string.active),
    COMPLETED(statusColorRes = R.color.completed, titleRes = R.string.Complete),
    FAILED(statusColorRes = R.color.failed, titleRes = R.string.Failed)
}