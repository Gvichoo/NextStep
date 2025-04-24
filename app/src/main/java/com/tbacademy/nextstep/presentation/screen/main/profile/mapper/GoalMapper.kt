package com.tbacademy.nextstep.presentation.screen.main.profile.mapper

import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.presentation.screen.main.profile.model.GoalPresentation
import java.sql.Date

fun Goal.toPresentation(): GoalPresentation {
    return GoalPresentation(
        id = id,
        title = title,
        authorId = authorId,
        authorUsername = authorUsername,
        description = description,
        targetDate = Date(targetDate),
        metricUnit = metricUnit,
        metricTarget = metricTarget,
        createdAt = createdAt,
        imageUrl = imageUrl,
        milestone = milestone,
    )
}