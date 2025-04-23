package com.tbacademy.nextstep.data.remote.dto

import com.google.firebase.Timestamp
import com.tbacademy.nextstep.presentation.model.MilestoneItem

data class GoalDto(
    val id: String = "",
    val authorId: String = "",
    val authorUsername: String = "",
    val title: String = "",
    val description: String = "",
    val metricTarget: String? = null,
    val metricUnit: String? = null,
    val targetDate: Timestamp = Timestamp.now(),
    val goalStatus: GoalStatusDto = GoalStatusDto.ACTIVE,
    val createdAt: Long = System.currentTimeMillis(),
    val imageUrl: String = "",
    val milestone : List<MilestoneItem>? = emptyList()
)

enum class GoalStatusDto{
    ACTIVE,
    COMPLETED,
    FAILED
}