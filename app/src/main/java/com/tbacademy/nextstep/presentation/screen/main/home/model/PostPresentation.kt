package com.tbacademy.nextstep.presentation.screen.main.home.model

data class PostPresentation(
    val id: String,
    val authorId: String,
    val authorUsername: String,
    val title: String,
    val description: String,
    val reactionCount: Int,
    val reactionFireCount: Int = 0,
    val reactionHeartCount: Int = 0,
    val reactionCookieCount: Int = 0,
    val reactionCheerCount: Int = 0,
    val reactionDisappointmentCount: Int = 0,
    val commentCount: Int,
    val type: String,
    val imageUrl: String? = null,
    val createdAt: String,
    val userReaction: PostReactionType? = null,
    val isUserFollowing: FollowStatus,
    val isReactionsPopUpVisible: Boolean = false,
)