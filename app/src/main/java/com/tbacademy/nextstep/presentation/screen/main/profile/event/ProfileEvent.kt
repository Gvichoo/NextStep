package com.tbacademy.nextstep.presentation.screen.main.profile.event

sealed interface ProfileEvent {
    data class SetProfileState(val userId: String? = null): ProfileEvent
}