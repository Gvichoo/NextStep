package com.tbacademy.nextstep.data.common.mapper

import com.tbacademy.nextstep.data.remote.dto.UserFollowDto
import com.tbacademy.nextstep.domain.model.UserFollow

fun UserFollowDto.toDomain(): UserFollow {
    return UserFollow(
        id = id,
        followerId = followerId,
        followedUserId = followedUserId,
        createdAt = createdAt.toDate()
    )
}