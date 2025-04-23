package com.tbacademy.nextstep.data.common.mapper

import com.google.firebase.Timestamp
import com.tbacademy.nextstep.data.remote.dto.GoalDto
import com.tbacademy.nextstep.data.remote.dto.GoalStatusDto
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.model.GoalStatus
import java.util.Date

fun Goal.toDto(): GoalDto {
    return GoalDto(
        id = id,
        title = title,
        description = description,
        targetDate = Timestamp(Date(targetDate)),
        metricUnit = metricUnit,
        metricTarget = metricTarget,
        createdAt = createdAt,
        imageUrl = imageUrl,
        milestone = milestone
    )
}

fun GoalDto.toDomainWithComputedStatus(currentTime: Long): Goal {
    val status = when {
        this.goalStatus == GoalStatusDto.COMPLETED -> GoalStatus.COMPLETED
        this.targetDate.toDate().time < currentTime -> GoalStatus.FAILED
        else -> GoalStatus.ACTIVE
    }

    return Goal(
        id = id,
        title = title,
        authorId = authorId,
        description = description,
        authorUsername = authorUsername,
        targetDate = targetDate.toDate().time,
        metricUnit = metricUnit,
        metricTarget = metricTarget,
        createdAt = createdAt,
        imageUrl = imageUrl,
        milestone = milestone,
        goalStatus = status
    )
}