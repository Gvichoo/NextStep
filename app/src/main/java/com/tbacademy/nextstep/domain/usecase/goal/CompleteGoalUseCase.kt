package com.tbacademy.nextstep.domain.usecase.goal

import android.net.Uri
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.GoalStatus
import com.tbacademy.nextstep.domain.model.PostType
import com.tbacademy.nextstep.domain.usecase.post.CreatePostUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface CompleteGoalUseCase {
    operator fun invoke(
        title: String,
        description: String,
        imageUri: Uri,
        goalId: String,
    ): Flow<Resource<Unit>>
}

class CompleteGoalUseCaseImpl @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val updateGoalStatusUseCase: UpdateGoalStatusUseCase
) : CompleteGoalUseCase {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(
        title: String,
        description: String,
        imageUri: Uri,
        goalId: String
    ): Flow<Resource<Unit>> {
        return updateGoalStatusUseCase(goalId = goalId, goalStatus = GoalStatus.COMPLETED)
            .flatMapConcat { updateResult ->
                when (updateResult) {
                    is Resource.Loading -> flowOf(Resource.Loading(updateResult.loading))
                    is Resource.Error -> flowOf(Resource.Error(updateResult.error))
                    is Resource.Success -> {
                        createPostUseCase(
                            title = title,
                            description = description,
                            imageUri = imageUri,
                            type = PostType.GOAL_COMPLETE,
                            goalId = goalId
                        ).map { postResult ->
                            when (postResult) {
                                is Resource.Loading -> Resource.Loading(postResult.loading)
                                is Resource.Error -> Resource.Error(postResult.error)
                                is Resource.Success -> Resource.Success(Unit)
                            }
                        }
                    }
                }
            }
    }
}

