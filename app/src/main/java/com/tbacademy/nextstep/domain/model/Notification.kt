package com.tbacademy.nextstep.domain.model

import java.util.Date

data class Notification(
    val id: String,
    val fromId: String,
    val toId: String,
    val type: NotificationType,
    val reactionType: ReactionType? = null,
    val postId: String? = null,
    val isRead: Boolean = false,
    val authorUsername: String,
    val authorProfilePictureUrl: String? = null,
    val createdAt: Date
)

enum class NotificationType {
    POST_REACTED
}