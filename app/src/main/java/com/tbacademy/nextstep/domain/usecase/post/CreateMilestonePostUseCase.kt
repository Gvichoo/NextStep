package com.tbacademy.nextstep.domain.usecase.post

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.MilestonePost
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CreateMilestonePostUseCase {
    suspend operator fun invoke(milestonePost: MilestonePost): Flow<Resource<Boolean>>
}

class CreateMilestonePostUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository
) : CreateMilestonePostUseCase{
    override suspend fun invoke(milestonePost: MilestonePost): Flow<Resource<Boolean>> {
        return postRepository.createMilestonePost(milestonePost = milestonePost)
    }
}


