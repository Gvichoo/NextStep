package com.tbacademy.nextstep.domain.model

import java.util.Date

data class Post(
    val id: String,
    val authorId: String,
    val authorUsername: String,
    val goalId: String = "",
    val title: String,
    val description: String,
    val reactionCount: Int,
    val reactionFireCount: Int = 0,
    val reactionHeartCount: Int = 0,
    val reactionCookieCount: Int = 0,
    val reactionCheerCount: Int = 0,
    val reactionDisappointedCount: Int = 0,
    val userReaction: ReactionType? = null,
    val isUserFollowing: FollowType? = null,
    val commentCount: Int,
    val type: String,
    val imageUrl: String? = null,
    val createdAt: Date
)