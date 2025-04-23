package com.tbacademy.nextstep.presentation.screen.main.profile.effect

sealed interface ProfileEffect {
    data object NavigateBack : ProfileEffect
    data class ShowErrorMessage(val errorRes: Int): ProfileEffect
    data object ShowUpdateImageDialog: ProfileEffect
    data object LaunchMediaPicker : ProfileEffect
    data object LaunchCameraPicker : ProfileEffect
}