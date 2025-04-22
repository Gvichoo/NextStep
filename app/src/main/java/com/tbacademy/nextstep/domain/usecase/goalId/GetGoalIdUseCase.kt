package com.tbacademy.nextstep.domain.usecase.goalId

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetGoalIdUseCase {
    operator fun invoke(goalId: String): Flow<Resource<Goal>>
}

class GetGoalIdUseCaseImpl @Inject constructor(
    private val goalRepository: GoalRepository
) : GetGoalIdUseCase {
    override fun invoke(goalId: String): Flow<Resource<Goal>> {
        return goalRepository.getGoalById(goalId)
    }
}