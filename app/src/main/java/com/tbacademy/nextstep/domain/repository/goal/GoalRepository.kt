package com.tbacademy.nextstep.domain.repository.goal

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
     fun createGoal(goal: Goal): Flow<Resource<Boolean>>
     fun getGoalById(goalId: String): Flow<Resource<Goal>>
}