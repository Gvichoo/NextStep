package com.tbacademy.nextstep.domain.usecase.reaction

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.domain.repository.reaction.ReactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CreateReactionUseCase {
    suspend operator fun invoke(postId: String, reactionType: ReactionType): Flow<Resource<Boolean>>
}

class CreateReactionUseCaseImpl @Inject constructor(
    private val reactionRepository: ReactionRepository
) : CreateReactionUseCase {
    override suspend fun invoke(
        postId: String,
        reactionType: ReactionType
    ): Flow<Resource<Boolean>> {
        return reactionRepository.createReaction(postId = postId, type = reactionType)
    }
}

