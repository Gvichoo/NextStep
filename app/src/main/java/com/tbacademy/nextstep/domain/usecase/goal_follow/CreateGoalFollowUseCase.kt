package com.tbacademy.nextstep.domain.usecase.goal_follow

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.GoalFollow
import com.tbacademy.nextstep.domain.repository.follow.GoalFollowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CreateGoalFollowUseCase {
    suspend operator fun invoke(
        followingId: String
    ): Flow<Resource<GoalFollow>>
}

class CreateGoalFollowUseCaseImpl @Inject constructor(
    private val followRepository: GoalFollowRepository
) : CreateGoalFollowUseCase {
    override suspend fun invoke(
        followingId: String
    ): Flow<Resource<GoalFollow>> {
        return followRepository.createGoalFollow(followingId = followingId)
    }
}

