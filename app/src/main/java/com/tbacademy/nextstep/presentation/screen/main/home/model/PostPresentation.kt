package com.tbacademy.nextstep.presentation.screen.main.home.model

import com.tbacademy.nextstep.R

data class PostPresentation(
    val id: String,
    val authorId: String,
    val authorUsername: String? = null,
    val authorProfilePictureUrl: String? = null,
    val goalId: String,
    val title: String,
    val description: String,
    val reactionCount: Int,
    val reactionFireCount: Int = 0,
    val reactionHeartCount: Int = 0,
    val reactionCookieCount: Int = 0,
    val reactionCheerCount: Int = 0,
    val reactionDisappointmentCount: Int = 0,
    val commentCount: Int,
    val imageUrl: String? = null,
    val createdAt: String,
    val userReaction: ReactionTypePresentation? = null,
    val isUserFollowing: Boolean = false,
    val isOwnPost: Boolean,
    val isReactionsPopUpVisible: Boolean = false,
    val type: PostTypePresentation
)

enum class PostTypePresentation(val messageRes: Int) {
    GOAL(messageRes = R.string.created_new_goal),
    MILESTONE(messageRes = R.string.achieved_new_milestone),
    GOAL_COMPLETE(messageRes = R.string.completed_goal)
}