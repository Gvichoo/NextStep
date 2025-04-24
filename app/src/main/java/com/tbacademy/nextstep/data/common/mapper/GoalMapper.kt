package com.tbacademy.nextstep.data.common.mapper

import android.net.Uri
import com.tbacademy.nextstep.data.model.MilestoneItemDto
import com.tbacademy.nextstep.data.remote.dto.GoalDto
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import java.sql.Date

fun Goal.toDto(): GoalDto {
    return GoalDto(
        id = id,
        title = title,
        authorId = authorId,
        description = description,
        targetDate = com.google.firebase.Timestamp(Date(targetDate)),
        metricUnit = metricUnit,
        metricTarget = metricTarget,
        createdAt = createdAt,
        imageUrl = imageUrl,
        milestone = milestone?.map { it.toDto() }
    )
}

fun GoalDto.toDomain(): Goal {
    return Goal(
        id = id,
        title = title,
        authorId = authorId,
        description = description,
        targetDate = targetDate.seconds * 1000,
        metricUnit = metricUnit,
        metricTarget = metricTarget,
        createdAt = createdAt,
        imageUri = Uri.parse(imageUrl),
        milestone = milestone?.map { it.toDomain() } ?: emptyList()
    )
}


fun MilestoneItemDto.toDomain(): MilestoneItem {
    return MilestoneItem(
        id = id,
        text = text,
        achieved = achieved,
        achievedAt = achievedAt
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




