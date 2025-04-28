package com.tbacademy.nextstep.domain.usecase.reaction

import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.domain.repository.reaction.ReactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UpdateReactionUseCase {
    suspend operator fun invoke(postId: String, reactionType: ReactionType): Flow<Resource<Boolean>>
}

class UpdateReactionUseCaseImpl @Inject constructor(
    private val reactionRepository: ReactionRepository
) : UpdateReactionUseCase {
    override suspend fun invoke(
        postId: String,
        reactionType: ReactionType
    ): Flow<Resource<Boolean>> {
        return reactionRepository.updateReaction(
            postId = postId, newType = reactionType
        )
    }
}