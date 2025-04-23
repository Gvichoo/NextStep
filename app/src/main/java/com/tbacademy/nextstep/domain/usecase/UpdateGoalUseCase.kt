package com.tbacademy.nextstep.domain.usecase

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UpdateGoalUseCase {
    operator fun invoke(goalId: String, updatedGoal: Goal): Flow<Resource<Boolean>>
}

class UpdateGoalUseCaseImpl @Inject constructor(
    private val goalRepository: GoalRepository
) : UpdateGoalUseCase {
    override fun invoke(goalId: String, updatedGoal: Goal): Flow<Resource<Boolean>> {
        return goalRepository.updateGoal(goalId = goalId, updatedGoal = updatedGoal)
    }
}