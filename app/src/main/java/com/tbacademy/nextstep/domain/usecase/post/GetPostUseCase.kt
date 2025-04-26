package com.tbacademy.nextstep.domain.usecase.post

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetPostUseCase {
    operator fun invoke(postId: String): Flow<Resource<Post>>
}

class GetPostUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository
) : GetPostUseCase {
    override fun invoke(postId: String): Flow<Resource<Post>> {
        return postRepository.getPost(postId = postId)
    }
}