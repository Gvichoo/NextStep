package com.tbacademy.nextstep.domain.usecase.goal

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CreateGoalUseCase {
     operator fun invoke(goal: Goal): Flow<Resource<Boolean>>
}

class CreateGoalUseCaseImpl @Inject constructor(
    private val goalRepository: GoalRepository
) : CreateGoalUseCase {
    override fun invoke(goal: Goal): Flow<Resource<Boolean>> {
        return goalRepository.createGoal(goal)
    }
}

