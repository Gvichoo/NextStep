package com.tbacademy.nextstep.presentation.screen.main.profile.event

sealed interface ProfileEvent {
    data class FetchUserInfo(val userId: String? = null): ProfileEvent
}