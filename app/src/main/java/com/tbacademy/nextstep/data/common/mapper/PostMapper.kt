package com.tbacademy.nextstep.data.common.mapper

import com.tbacademy.nextstep.data.remote.dto.PostDto
import com.tbacademy.nextstep.domain.model.Post

fun PostDto.toDomain(): Post {
    return Post(
        id = id,
        title = title,
        description = description,
        authorUsername = authorUsername,
        authorId = authorId,
        goalId = goalId,
        commentCount = commentCount,
        reactionCount = reactionCount,
        reactionFireCount = reactionFireCount,
        reactionHeartCount = reactionHeartCount,
        reactionCookieCount = reactionCookieCount,
        reactionCheerCount = reactionCheerCount,
        reactionDisappointedCount = reactionDisappointmentCount,
        imageUrl = imageUrl,
        createdAt = createdAt.toDate(),
        type = type
    )
}

