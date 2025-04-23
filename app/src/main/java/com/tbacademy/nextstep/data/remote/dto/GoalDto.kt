package com.tbacademy.nextstep.data.remote.dto

import com.google.firebase.Timestamp
import com.tbacademy.nextstep.data.model.MilestoneItemDto

data class GoalDto(
    val id: String = "",
    val authorId: String = "",
    val authorUsername: String = "",
    val title: String = "",
    val description: String? = null,
    val metricTarget: String? = null,
    val metricUnit: String? = null,
    val targetDate: Timestamp = Timestamp.now(),
    val createdAt: Long = System.currentTimeMillis(),
    val imageUrl: String? = null,
    val milestone : List<MilestoneItemDto>? = emptyList()
)