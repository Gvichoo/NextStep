package com.tbacademy.nextstep.presentation.screen.main.goal.effect

sealed interface GoalEffect {
    data class OpenComments(val postId: String): GoalEffect
    data class NavigateToCompleteGoal(val goalTitle: String): GoalEffect
}