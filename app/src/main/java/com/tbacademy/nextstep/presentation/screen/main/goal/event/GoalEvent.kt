package com.tbacademy.nextstep.presentation.screen.main.goal.event

sealed interface GoalEvent {
    data class FetchGoalPosts(val goalId: String) : GoalEvent
    data class CommentSelected(val postId: String) : GoalEvent
    data class CheckGoalAuthor(val isOwnGoal: Boolean) : GoalEvent
    data object OpenCompleteGoalSheet : GoalEvent
    data object CloseCompleteGoalSheet : GoalEvent
    data object ProceedWithGoalCompletion: GoalEvent
}