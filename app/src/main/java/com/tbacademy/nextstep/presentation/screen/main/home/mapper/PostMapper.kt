package com.tbacademy.nextstep.presentation.screen.main.home.mapper

import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.presentation.common.extension.toTimeAgo
import com.tbacademy.nextstep.presentation.screen.main.home.model.FollowStatus
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation

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
        reactionDisappointmentCount = reactionDissapointedCount,
        commentCount = commentCount,
        type = type,
        imageUrl = imageUrl,
        createdAt = createdAt.toTimeAgo(),
        isUserFollowing = if (isUserFollowing != null) FollowStatus.FOLLOWED else FollowStatus.TO_FOLLOW,
        followType = isUserFollowing,
        userReaction = userReaction?.toPresentation(),
    )
}