package com.tbacademy.nextstep.domain.usecase.user_follow

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.repository.user_follow.UserFollowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeleteUserFollowUseCase {
    suspend operator fun invoke(followedId: String): Flow<Resource<Unit>>
}

class DeleteUserFollowUseCaseImpl @Inject constructor(
    private val userFollowRepository: UserFollowRepository
): DeleteUserFollowUseCase {
    override suspend fun invoke(followedId: String): Flow<Resource<Unit>> {
        return userFollowRepository.deleteUserFollow(followedId = followedId)
    }
}