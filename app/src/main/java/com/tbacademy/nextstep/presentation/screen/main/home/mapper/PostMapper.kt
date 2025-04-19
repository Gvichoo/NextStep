package com.tbacademy.nextstep.presentation.screen.main.home.mapper

import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.presentation.common.extension.toTimeAgo
import com.tbacademy.nextstep.presentation.screen.main.home.model.FollowStatus
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation

fun Post.toPresentation(): PostPresentation {
    return PostPresentation(
        id,
        authorId,
        authorUsername,
        title,
        description,
        reactionCount,
        reactionFireCount,
        reactionHeartCount,
        reactionCookieCount,
        reactionCheerCount,
        reactionTaskCount,
        commentCount,
        type,
        imageUrl,
        createdAt.toTimeAgo(),
        userReaction = userReaction?.toPresentation(),
        isUserFollowing = if (isUserFollowing) FollowStatus.FOLLOWED else FollowStatus.TO_FOLLOW
    )
}