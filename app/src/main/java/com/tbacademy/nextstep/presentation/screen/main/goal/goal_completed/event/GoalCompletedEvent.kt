package com.tbacademy.nextstep.presentation.screen.main.goal.goal_completed.event

sealed interface GoalCompletedEvent {
    data object StartAnimation: GoalCompletedEvent
    data object Continue: GoalCompletedEvent
}