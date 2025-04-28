package com.tbacademy.nextstep.domain.usecase.goal

import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetUserGoalsUseCase {
    suspend operator fun invoke(userId: String): Flow<Resource<List<Goal>>>
}

class GetUserGoalsUseCaseImpl @Inject constructor(
    private val goalRepository: GoalRepository
): GetUserGoalsUseCase {
    override suspend fun invoke(userId: String): Flow<Resource<List<Goal>>> {
        return goalRepository.getUserGoals(userId = userId)
    }
}