package com.tbacademy.nextstep.domain.repository.reaction

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.ReactionType
import kotlinx.coroutines.flow.Flow

interface ReactionRepository {
    suspend fun createReaction(postId: String, type: ReactionType): Flow<Resource<Boolean>>
    suspend fun updateReaction(postId: String, newType: ReactionType): Flow<Resource<Boolean>>
    suspend fun deleteReaction(postId: String): Flow<Resource<Boolean>>
}