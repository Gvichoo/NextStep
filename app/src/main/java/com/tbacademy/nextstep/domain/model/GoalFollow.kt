package com.tbacademy.nextstep.domain.model

import java.util.Date


data class GoalFollow(
    val id: String,
    val followerId: String,
    val followedGoalId: String,
    val createdAt: Date
)
