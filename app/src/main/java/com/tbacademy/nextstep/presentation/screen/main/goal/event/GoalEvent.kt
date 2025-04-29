package com.tbacademy.nextstep.presentation.screen.main.goal.event

sealed interface GoalEvent {
    data class FetchGoalPosts(val goalId: String) : GoalEvent
    data class CommentSelected(val postId: String) : GoalEvent
    data class CheckGoalAuthor(val isOwnGoal: Boolean, val hasMilestones: Boolean) : GoalEvent
    data object OpenCompleteGoalSheet : GoalEvent
    data object CloseCompleteGoalSheet : GoalEvent
    data object ProceedWithGoalCompletion: GoalEvent
    data class MilestonesSelected(val goalId: String): GoalEvent
    data object Return: GoalEvent
}