package com.tbacademy.nextstep.presentation.screen.main.home.event

import com.tbacademy.nextstep.presentation.screen.main.home.model.ReactionTypePresentation
import com.tbacademy.nextstep.presentation.screen.main.home.state.FeedState

sealed interface HomeEvent {
    data object FetchPosts : HomeEvent
    data class HandleReactToPost(val postId: String, val reactionType: ReactionTypePresentation?) :
        HomeEvent

    data object StartSearch: HomeEvent
    data class ToggleReactionsSelector(val postId: String, val visible: Boolean) : HomeEvent
    data class ToggleFollowGoal(val postId: String) : HomeEvent
    data class OpenPostComments(val postId: String, val typeActive: Boolean = false) : HomeEvent
    data class UserSelected(val userId: String) : HomeEvent
    data class FeedStateSelected(val feedState: FeedState) : HomeEvent
    data class OpenMilestone(val goalId: String) : HomeEvent
    data class OpenGoalFragment(val goalId: String) : HomeEvent
}