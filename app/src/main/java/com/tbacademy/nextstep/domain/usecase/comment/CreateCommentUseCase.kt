package com.tbacademy.nextstep.domain.usecase.comment

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.Comment
import com.tbacademy.nextstep.domain.repository.comment.CommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CreateCommentUseCase {
    suspend operator fun invoke(postId: String, text: String): Flow<Resource<Comment>>
}

class CreateCommentUseCaseImpl @Inject constructor(
    private val createCommentRepository: CommentRepository
) : CreateCommentUseCase {
    override suspend fun invoke(postId: String, text: String): Flow<Resource<Comment>> {
        return createCommentRepository.createComment(postId = postId, text = text)
    }
}