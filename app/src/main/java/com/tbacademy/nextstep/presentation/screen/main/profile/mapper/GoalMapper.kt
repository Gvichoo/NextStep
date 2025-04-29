package com.tbacademy.nextstep.presentation.screen.main.profile.mapper

import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.model.GoalStatus
import com.tbacademy.nextstep.presentation.screen.main.profile.model.GoalPresentation
import com.tbacademy.nextstep.presentation.screen.main.profile.model.GoalStatusPresentation
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
        goalStatus = goalStatus.toPresentation(),
        metricTarget = metricTarget,
        createdAt = createdAt,
        imageUrl = imageUrl,
        milestone = milestone,
    )
}

fun List<GoalPresentation>.sortByGoalStatus(): List<GoalPresentation> {
    return sortedWith(
        compareBy<GoalPresentation> (
            { when (it.goalStatus) {
                GoalStatusPresentation.ACTIVE -> 0
                GoalStatusPresentation.COMPLETED -> 1
                GoalStatusPresentation.FAILED -> 2
            }},
            { it.title }
        )
    )
}

fun GoalStatus.toPresentation(): GoalStatusPresentation = when (this) {
    GoalStatus.ACTIVE    -> GoalStatusPresentation.ACTIVE
    GoalStatus.COMPLETED -> GoalStatusPresentation.COMPLETED
    GoalStatus.FAILED    -> GoalStatusPresentation.FAILED
}