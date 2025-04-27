package com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal.event

sealed interface CompleteGoalEvent {
    data class DescriptionChanged(val description: String) : CompleteGoalEvent

    data object PickImage : CompleteGoalEvent
    data object ClearImage : CompleteGoalEvent

    data class Submit(val goalId: String) : CompleteGoalEvent

    data object Return : CompleteGoalEvent
}