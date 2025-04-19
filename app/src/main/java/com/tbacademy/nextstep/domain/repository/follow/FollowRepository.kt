package com.tbacademy.nextstep.domain.repository.follow

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Follow
import com.tbacademy.nextstep.domain.model.FollowType
import kotlinx.coroutines.flow.Flow

interface FollowRepository {
    suspend fun createFollow(
        followType: FollowType,
        followingId: String
    ): Flow<Resource<Follow>>

    suspend fun deleteFollow(followedId: String, followType: FollowType): Flow<Resource<Unit>>
}

