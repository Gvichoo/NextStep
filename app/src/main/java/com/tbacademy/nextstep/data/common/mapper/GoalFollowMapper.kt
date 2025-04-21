package com.tbacademy.nextstep.data.common.mapper

import com.tbacademy.nextstep.data.remote.dto.GoalFollowDto
import com.tbacademy.nextstep.domain.model.GoalFollow

fun GoalFollowDto.toDomain(): GoalFollow {
    return GoalFollow(
        id = this.id,
        followerId = this.followerId,
        followedId = this.followedId,
        createdAt = this.createdAt.toDate()
    )
}