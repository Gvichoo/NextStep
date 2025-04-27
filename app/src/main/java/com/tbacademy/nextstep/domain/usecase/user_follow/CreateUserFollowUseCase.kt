package com.tbacademy.nextstep.domain.usecase.user_follow

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.UserFollow
import com.tbacademy.nextstep.domain.repository.user_follow.UserFollowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CreateUserFollowUseCase {
    suspend operator fun invoke(
        followedId: String
    ): Flow<Resource<UserFollow>>
}

class CreateUserFollowUseCaseImpl @Inject constructor(
    private val userFollowRepository: UserFollowRepository
): CreateUserFollowUseCase {
    override suspend fun invoke(followedId: String): Flow<Resource<UserFollow>> {
        return userFollowRepository.createUserFollow(followedId = followedId)
    }
}