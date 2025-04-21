package com.tbacademy.nextstep.domain.usecase.goal_follow

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.repository.follow.GoalFollowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeleteGoalFollowUseCase {
    suspend operator fun invoke(
        followedId: String
    ): Flow<Resource<Unit>>
}

class DeleteGoalFollowUseCaseImpl @Inject constructor(
    private val followRepository: GoalFollowRepository
): DeleteGoalFollowUseCase {
    override suspend fun invoke(followedId: String): Flow<Resource<Unit>> {
        return followRepository.deleteGoalFollow(followedId = followedId)
    }
}