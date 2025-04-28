package com.tbacademy.nextstep.data.common.mapper

import android.net.Uri
import com.google.firebase.Timestamp
import com.tbacademy.nextstep.data.model.MilestoneItemDto
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
        milestone = milestone?.map { it.toDto() },
    )
}

fun GoalDto.toDomain(): Goal {
    return Goal(
        id = id,
        title = title,
        authorId = authorId,
        description = description,
        targetDate = targetDate.seconds * 1000,
        goalStatus = goalStatus.toDomain(),
        metricUnit = metricUnit,
        metricTarget = metricTarget,
        createdAt = createdAt,
        imageUri = Uri.parse(imageUrl),
        milestone = milestone?.map { it.toDomain() } ?: emptyList()
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
        goalStatus = status,
        metricTarget = metricTarget,
        createdAt = createdAt,
        imageUrl = imageUrl,
        milestone = milestone?.map { it.toDomain() },
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

fun GoalStatusDto.toDomain(): GoalStatus = when (this) {
    GoalStatusDto.ACTIVE    -> GoalStatus.ACTIVE
    GoalStatusDto.COMPLETED -> GoalStatus.COMPLETED
    GoalStatusDto.FAILED    -> GoalStatus.FAILED
}



