package com.tbacademy.nextstep.presentation.screen.main.notification.event

sealed interface NotificationEvent {
    data class GetNotifications(val refresh: Boolean = false): NotificationEvent
    data class ReactNotificationSelected(val postId: String): NotificationEvent
}