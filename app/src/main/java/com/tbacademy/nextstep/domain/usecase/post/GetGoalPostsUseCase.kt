package com.tbacademy.nextstep.domain.usecase.post

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

sealed interface GetGoalPostsUseCase {
    suspend operator fun invoke(goalId: String): Flow<Resource<List<Post>>>
}

class GetGoalPostsUseCaseImpl @Inject constructor(
    private val postsRepository: PostRepository
): GetGoalPostsUseCase {
    override suspend fun invoke(goalId: String): Flow<Resource<List<Post>>> {
        return postsRepository.getGoalPosts(goalId = goalId)
    }

}