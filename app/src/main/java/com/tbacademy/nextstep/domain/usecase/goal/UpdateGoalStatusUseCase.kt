package com.tbacademy.nextstep.domain.usecase.goal

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.GoalStatus
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UpdateGoalStatusUseCase {
    operator fun invoke(goalId: String, goalStatus: GoalStatus): Flow<Resource<Unit>>
}

class UpdateGoalStatusUseCaseImpl @Inject constructor(
    private val goalRepository: GoalRepository
) : UpdateGoalStatusUseCase {
    override fun invoke(goalId: String, goalStatus: GoalStatus): Flow<Resource<Unit>> {
        return goalRepository.updateGoalStatus(goalId = goalId, goalStatus = goalStatus)
    }
}