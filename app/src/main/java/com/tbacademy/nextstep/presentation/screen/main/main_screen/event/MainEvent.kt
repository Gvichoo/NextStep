package com.tbacademy.nextstep.presentation.screen.main.main_screen.event

sealed interface MainEvent {
    data object StartListeningForUnreadNotifications: MainEvent
}