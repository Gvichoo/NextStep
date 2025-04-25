package com.tbacademy.nextstep.presentation.screen.main.notification.event

sealed interface NotificationEvent {
    data object GetNotifications: NotificationEvent
}