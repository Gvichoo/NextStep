package com.tbacademy.nextstep.presentation.screen.main.home.event

import com.tbacademy.nextstep.presentation.screen.main.home.model.PostReactionType

sealed interface HomeEvent {
    data object FetchGlobalPosts : HomeEvent
    data class HandleReactToPost(val postId: String, val reactionType: PostReactionType?) : HomeEvent
    data class ToggleReactionsSelector(val postId: String, val visible: Boolean) : HomeEvent
    data class ToggleFollowGoal(val postId: String): HomeEvent
    data class OpenPostComments(val postId: String, val typeActive: Boolean = false): HomeEvent
    data class UserSelected(val userId: String): HomeEvent
}