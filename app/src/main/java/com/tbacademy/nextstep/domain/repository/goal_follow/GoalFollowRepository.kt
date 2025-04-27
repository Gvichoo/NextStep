package com.tbacademy.nextstep.domain.repository.goal_follow

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.GoalFollow
import kotlinx.coroutines.flow.Flow

interface GoalFollowRepository {
    suspend fun createGoalFollow(
        followedId: String
    ): Flow<Resource<GoalFollow>>

    suspend fun deleteGoalFollow(followedId: String): Flow<Resource<Unit>>
}

