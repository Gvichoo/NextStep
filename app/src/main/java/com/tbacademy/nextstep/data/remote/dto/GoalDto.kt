package com.tbacademy.nextstep.data.remote.dto

import com.google.firebase.Timestamp
import com.tbacademy.nextstep.data.model.MilestoneItemDto

data class GoalDto(
    val id: String = "",
    val authorId: String = "",
    val authorUsername: String = "",
    val title: String = "",
    val description: String? = "",
    val metricTarget: String? = null,
    val metricUnit: String? = null,
    val targetDate: Timestamp = Timestamp.now(),
    val goalStatus: GoalStatusDto = GoalStatusDto.ACTIVE,
    val createdAt: Long = System.currentTimeMillis(),
    val milestone : List<MilestoneItemDto>? = emptyList(),
    val imageUrl: String? = "",
)

enum class GoalStatusDto{
    ACTIVE,
    COMPLETED,
    FAILED
}