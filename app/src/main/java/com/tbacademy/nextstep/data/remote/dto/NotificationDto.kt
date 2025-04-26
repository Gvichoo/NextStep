package com.tbacademy.nextstep.data.remote.dto

import com.google.firebase.Timestamp

data class NotificationDto(
    val id: String = "",
    val fromId: String = "",
    val toId: String = "",
    val type: NotificationTypeDto = NotificationTypeDto.POST_REACTED,
    val reactionType: ReactionTypeDto? = null,
    val postId: String? = null,
    val isRead: Boolean = false,
    val authorUsername: String = "",
    val authorProfilePictureUrl: String? = null,
    val createdAt: Timestamp = Timestamp.now()
)

enum class NotificationTypeDto {
    POST_REACTED,
    POST_COMMENTED,
    USER_FOLLOWED
}