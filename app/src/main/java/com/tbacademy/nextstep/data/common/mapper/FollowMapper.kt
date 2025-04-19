package com.tbacademy.nextstep.data.common.mapper

import com.tbacademy.nextstep.data.remote.dto.FollowDto
import com.tbacademy.nextstep.data.remote.dto.FollowTypeDto
import com.tbacademy.nextstep.domain.model.Follow
import com.tbacademy.nextstep.domain.model.FollowType

fun FollowType.toDto(): FollowTypeDto = when (this) {
    FollowType.USER -> FollowTypeDto.USER
    FollowType.GOAL -> FollowTypeDto.GOAL
}

fun FollowDto.toDomain(): Follow {
    return Follow(
        id = this.id,
        followerId = this.followerId,
        followType = this.followType.toDomain(),
        followedId = this.followedId,
        createdAt = this.createdAt.toDate()
    )
}

fun FollowTypeDto.toDomain(): FollowType = when (this) {
    FollowTypeDto.USER -> FollowType.USER
    FollowTypeDto.GOAL -> FollowType.GOAL
}