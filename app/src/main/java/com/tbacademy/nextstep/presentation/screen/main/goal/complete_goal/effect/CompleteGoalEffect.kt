package com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal.effect

sealed interface CompleteGoalEffect {
    data class ShowErrorMessage(val errorRes: Int): CompleteGoalEffect

    data object ShowUpdateImageDialog: CompleteGoalEffect
    data object LaunchMediaPicker : CompleteGoalEffect
    data object LaunchCameraPicker : CompleteGoalEffect

    data object NavigateBack: CompleteGoalEffect
    data object NavigateToGoalCompleted: CompleteGoalEffect
}