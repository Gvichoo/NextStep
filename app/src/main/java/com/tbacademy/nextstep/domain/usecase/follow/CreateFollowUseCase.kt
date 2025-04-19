package com.tbacademy.nextstep.domain.usecase.follow

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Follow
import com.tbacademy.nextstep.domain.model.FollowType
import com.tbacademy.nextstep.domain.repository.follow.FollowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CreateFollowUseCase {
    suspend operator fun invoke(
        followType: FollowType,
        followingId: String
    ): Flow<Resource<Follow>>
}

class CreateFollowUseCaseImpl @Inject constructor(
    private val followRepository: FollowRepository
) : CreateFollowUseCase {
    override suspend fun invoke(
        followType: FollowType,
        followingId: String
    ): Flow<Resource<Follow>> {
        return followRepository.createFollow(followType = followType, followingId = followingId)
    }

}

