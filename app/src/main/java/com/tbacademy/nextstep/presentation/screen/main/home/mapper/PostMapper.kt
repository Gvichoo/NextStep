package com.tbacademy.nextstep.presentation.screen.main.home.mapper

import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.presentation.common.extension.toTimeAgo
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostType

fun Post.toPresentation(): PostPresentation {
    return PostPresentation(
        id = id,
        authorId = authorId,
        authorUsername = authorUsername,
        title = title,
        description = description,
        goalId = goalId,
        reactionCount = reactionCount,
        reactionFireCount = reactionFireCount,
        reactionHeartCount = reactionHeartCount,
        reactionCookieCount = reactionCookieCount,
        reactionCheerCount = reactionCheerCount,
        reactionDisappointmentCount = reactionDisappointedCount,
        commentCount = commentCount,
        imageUrl = imageUrl,
        createdAt = createdAt.toTimeAgo(),
        isUserFollowing = isUserFollowing,
        isOwnPost = isOwnPost,
        userReaction = userReaction?.toPresentation(),
        type = PostType.MILESTONE
    )
}