package com.tbacademy.nextstep.data.common.mapper

import com.google.firebase.Timestamp
import com.tbacademy.nextstep.data.remote.dto.GoalDto
import com.tbacademy.nextstep.domain.model.Goal
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
