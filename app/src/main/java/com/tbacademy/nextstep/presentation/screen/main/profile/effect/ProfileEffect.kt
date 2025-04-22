package com.tbacademy.nextstep.presentation.screen.main.profile.effect

sealed interface ProfileEffect {
    data object NavigateBack : ProfileEffect
}