package com.tbacademy.nextstep.presentation.screen.main.notification.effect

sealed interface NotificationEffect {
    data class ShowErrorMessage(val errorMessageRes: Int): NotificationEffect
    data class NavigateToPost(val postId: String, val isComment: Boolean = false): NotificationEffect
    data class NavigateToUserProfile(val userId: String): NotificationEffect
}