package com.tbacademy.nextstep.domain.repository.goal

import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.model.GoalStatus
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
     fun createGoal(goal: Goal): Flow<Resource<Boolean>>
     fun getGoalMilestones(goalId: String): Flow<Resource<Goal>>
     fun updateGoalMilestone(goalId: String, updatedGoalMilestones: List<MilestoneItem>): Flow<Resource<Boolean>>
     fun updateGoalStatus(goalStatus: GoalStatus, goalId: String): Flow<Resource<Unit>>

     fun getUserGoals(userId: String): Flow<Resource<List<Goal>>>
}