package com.tbacademy.nextstep.domain.repository.comment

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    suspend fun getComments(postId: String): Flow<Resource<List<Comment>>>
    suspend fun createComment(postId: String, text: String): Flow<Resource<Comment>>
}