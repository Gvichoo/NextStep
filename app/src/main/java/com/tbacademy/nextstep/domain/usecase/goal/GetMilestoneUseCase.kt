package com.tbacademy.nextstep.domain.usecase.goal

import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetMilestoneUseCase {
    operator fun invoke(goalId: String): Flow<Resource<Goal>>
}

class GetMilestoneUseCaseImpl @Inject constructor(
    private val goalRepository: GoalRepository
) : GetMilestoneUseCase {
    override fun invoke(goalId: String): Flow<Resource<Goal>> {
        return goalRepository.getGoalMilestones(goalId = goalId)
    }
}

