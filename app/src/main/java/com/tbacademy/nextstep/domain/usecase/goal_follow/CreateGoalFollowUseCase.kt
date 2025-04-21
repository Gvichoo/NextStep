package com.tbacademy.nextstep.domain.usecase.goal_follow

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.GoalFollow
import com.tbacademy.nextstep.domain.repository.goal_follow.GoalFollowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CreateGoalFollowUseCase {
    suspend operator fun invoke(
        followedId: String
    ): Flow<Resource<GoalFollow>>
}

class CreateGoalFollowUseCaseImpl @Inject constructor(
    private val followRepository: GoalFollowRepository
) : CreateGoalFollowUseCase {
    override suspend fun invoke(
        followedId: String
    ): Flow<Resource<GoalFollow>> {
        return followRepository.createGoalFollow(followedId = followedId)
    }
}

