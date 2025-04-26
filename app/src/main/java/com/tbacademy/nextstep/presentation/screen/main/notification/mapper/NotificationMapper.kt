package com.tbacademy.nextstep.presentation.screen.main.notification.mapper

import com.tbacademy.nextstep.domain.model.Notification
import com.tbacademy.nextstep.domain.model.NotificationType
import com.tbacademy.nextstep.presentation.common.extension.toTimeAgo
import com.tbacademy.nextstep.presentation.screen.main.home.mapper.toPresentation
import com.tbacademy.nextstep.presentation.screen.main.notification.model.NotificationPresentation
import com.tbacademy.nextstep.presentation.screen.main.notification.model.NotificationTypePresentation

fun Notification.toPresentation(): NotificationPresentation {
    return NotificationPresentation(
        id = id,
        fromId = fromId,
        toId = toId,
        type = type.toPresentation(),
        reactionType = reactionType?.toPresentation(),
        postId = postId,
        isRead = isRead,
        authorUsername = authorUsername,
        authorProfilePictureUrl = authorProfilePictureUrl,
        createdAt = createdAt.toTimeAgo()
    )
}

fun NotificationType.toPresentation(): NotificationTypePresentation = when (this) {
    NotificationType.POST_REACTED -> NotificationTypePresentation.POST_REACTED
    NotificationType.POST_COMMENTED -> NotificationTypePresentation.POST_COMMENTED
    NotificationType.USER_FOLLOWED -> NotificationTypePresentation.USER_FOLLOWED
}