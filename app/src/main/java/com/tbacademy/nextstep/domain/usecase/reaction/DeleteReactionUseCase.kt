package com.tbacademy.nextstep.domain.usecase.reaction

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.repository.reaction.ReactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeleteReactionUseCase {
    suspend operator fun invoke(postId: String): Flow<Resource<Boolean>>
}

class DeleteReactionUseCaseImpl @Inject constructor(
    private val reactionRepository: ReactionRepository
) : DeleteReactionUseCase {
    override suspend fun invoke(postId: String): Flow<Resource<Boolean>> {
        return reactionRepository.deleteReaction(
            postId = postId
        )
    }
}