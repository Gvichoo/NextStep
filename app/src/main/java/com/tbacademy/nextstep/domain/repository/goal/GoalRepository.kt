package com.tbacademy.nextstep.domain.repository.goal

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
     fun createGoal(goal: Goal): Flow<Resource<Boolean>>
     fun getGoalMilestones(goalId: String): Flow<Resource<Goal>>
     fun updateGoalMilestone(goalId: String, updatedGoalMilestones: List<MilestoneItem>): Flow<Resource<Boolean>>

     fun getUserGoals(userId: String): Flow<Resource<List<Goal>>>
}