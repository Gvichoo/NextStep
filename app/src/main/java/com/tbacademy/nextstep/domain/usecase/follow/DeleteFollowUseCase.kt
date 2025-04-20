package com.tbacademy.nextstep.domain.usecase.follow

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.FollowType
import com.tbacademy.nextstep.domain.repository.follow.FollowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeleteFollowUseCase {
    suspend operator fun invoke(
        followedId: String, followType: FollowType
    ): Flow<Resource<Unit>>
}

class DeleteFollowUseCaseImpl @Inject constructor(
    private val followRepository: FollowRepository
): DeleteFollowUseCase {
    override suspend fun invoke(followedId: String, followType: FollowType): Flow<Resource<Unit>> {
        return followRepository.deleteFollow(followedId = followedId, followType = followType)
    }
}