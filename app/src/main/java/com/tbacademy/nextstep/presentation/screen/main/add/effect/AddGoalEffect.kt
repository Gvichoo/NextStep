package com.tbacademy.nextstep.presentation.screen.main.add.effect

sealed interface AddGoalEffect {
    data class ShowError(val message : Int) : AddGoalEffect
    data object NavToHomeFragment : AddGoalEffect
    data object LaunchMediaPicker : AddGoalEffect
    data object SuccessfulUpload:AddGoalEffect
    data object FailedUpload:AddGoalEffect

}