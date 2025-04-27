package com.tbacademy.nextstep.domain.usecase.comment

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.Comment
import com.tbacademy.nextstep.domain.repository.comment.CommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetCommentsUseCase {
    suspend operator fun invoke(postId: String): Flow<Resource<List<Comment>>>
}

class GetCommentsUseCaseImpl @Inject constructor(
    private val commentsRepository: CommentRepository
) : GetCommentsUseCase {
    override suspend fun invoke(postId: String): Flow<Resource<List<Comment>>> {
        return commentsRepository.getComments(postId = postId)
    }
}