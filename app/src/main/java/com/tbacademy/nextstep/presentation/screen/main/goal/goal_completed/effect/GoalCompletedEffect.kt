package com.tbacademy.nextstep.presentation.screen.main.goal.goal_completed.effect

sealed interface GoalCompletedEffect {
    data object NavigateToHome: GoalCompletedEffect
}