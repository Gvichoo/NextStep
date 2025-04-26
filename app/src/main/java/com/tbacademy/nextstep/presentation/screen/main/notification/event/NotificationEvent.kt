package com.tbacademy.nextstep.presentation.screen.main.notification.event

sealed interface NotificationEvent {
    data class GetNotifications(val refresh: Boolean = false): NotificationEvent
    data class PostNotificationSelected(val postId: String, val isComment: Boolean = false): NotificationEvent
    data class FollowNotificationSelected(val userId: String): NotificationEvent
}