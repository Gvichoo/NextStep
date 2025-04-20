package com.tbacademy.nextstep.domain.usecase.post

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetFollowedPostsUseCase {
    suspend operator fun invoke(): Flow<Resource<List<Post>>>
}

class GetFollowedPostsUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository
) : GetFollowedPostsUseCase {
    override suspend fun invoke(): Flow<Resource<List<Post>>> {
        return postRepository.getFollowedPosts()
    }
}