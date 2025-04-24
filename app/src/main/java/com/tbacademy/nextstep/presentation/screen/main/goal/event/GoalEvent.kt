package com.tbacademy.nextstep.presentation.screen.main.goal.event

sealed interface GoalEvent {
    data class FetchGoalPosts(val goalId: String): GoalEvent
    data class CommentSelected(val postId: String): GoalEvent
}