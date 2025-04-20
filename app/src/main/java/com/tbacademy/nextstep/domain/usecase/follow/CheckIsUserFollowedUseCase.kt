package com.tbacademy.nextstep.domain.usecase.follow

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.repository.follow.FollowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CheckIsUserFollowedUseCase {
    suspend operator fun invoke(followedId: String): Flow<Resource<Boolean>>
}

class CheckIsUerFollowedUseCaseImpl @Inject constructor(
    private val followRepository: FollowRepository
): CheckIsUserFollowedUseCase {
    override suspend fun invoke(followedId: String): Flow<Resource<Boolean>> {
        return followRepository.checkIsUserFollowed(followedId = followedId)
    }
}