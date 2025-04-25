package com.tbacademy.nextstep.presentation.screen.main.notification.state

import com.tbacademy.nextstep.presentation.screen.main.notification.model.NotificationPresentation

data class NotificationState(
    val notifications: List<NotificationPresentation>? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessageRes: Int? = null,
)