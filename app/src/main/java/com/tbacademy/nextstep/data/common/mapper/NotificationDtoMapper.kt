package com.tbacademy.nextstep.data.common.mapper

import com.tbacademy.nextstep.data.remote.dto.NotificationDto
import com.tbacademy.nextstep.data.remote.dto.NotificationTypeDto
import com.tbacademy.nextstep.domain.model.Notification
import com.tbacademy.nextstep.domain.model.NotificationType

fun NotificationDto.toDomain(): Notification {
    return Notification(
        id = id,
        fromId = fromId,
        toId = toId,
        type = type.toDomain(),
        reactionType = reactionType?.toDomain(),
        postId = postId,
        isRead = isRead,
        authorUsername = authorUsername,
        authorProfilePictureUrl = authorProfilePictureUrl,
        createdAt = createdAt.toDate()
    )
}

fun NotificationTypeDto.toDomain(): NotificationType = when (this) {
    NotificationTypeDto.POST_REACTED -> NotificationType.POST_REACTED
    NotificationTypeDto.POST_COMMENTED -> NotificationType.POST_COMMENTED
    NotificationTypeDto.USER_FOLLOWED -> NotificationType.USER_FOLLOWED
}