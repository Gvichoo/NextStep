package com.tbacademy.nextstep.domain.usecase.goal

import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UpdateGoalUseCase {
    operator fun invoke(goalId: String, updatedGoalMilestones: List<MilestoneItem>): Flow<Resource<Boolean>>
}

class UpdateGoalUseCaseImpl @Inject constructor(
    private val goalRepository: GoalRepository
) : UpdateGoalUseCase {
    override fun invoke(goalId: String, updatedGoalMilestones: List<MilestoneItem>): Flow<Resource<Boolean>> {
        return goalRepository.updateGoalMilestone(goalId = goalId, updatedGoalMilestones = updatedGoalMilestones)
    }
}