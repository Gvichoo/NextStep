package com.tbacademy.nextstep.data.common.mapper

import com.google.firebase.Timestamp
import com.tbacademy.nextstep.data.remote.dto.GoalDto
import com.tbacademy.nextstep.data.remote.dto.GoalStatusDto
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.model.GoalStatus
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import java.sql.Date

fun Goal.toDto(): GoalDto {
    return GoalDto(
        id = id,
        title = title,
        authorId = authorId,
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
fun MilestoneItem.toDto(): MilestoneItemDto {
    return MilestoneItemDto(
        id = id,
        text = text,
        achieved = achieved,
        achievedAt = achievedAt
    )
}




