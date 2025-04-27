package com.tbacademy.nextstep.domain.repository.user_follow

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.UserFollow
import kotlinx.coroutines.flow.Flow

interface UserFollowRepository {
    suspend fun createUserFollow(
        followedId: String
    ): Flow<Resource<UserFollow>>

    suspend fun deleteUserFollow(
        followedId: String
    ): Flow<Resource<Unit>>
}