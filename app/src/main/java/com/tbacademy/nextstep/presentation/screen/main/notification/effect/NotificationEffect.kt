package com.tbacademy.nextstep.presentation.screen.main.notification.effect

sealed interface NotificationEffect {
    data class ShowErrorMessage(val errorMessageRes: Int): NotificationEffect
}