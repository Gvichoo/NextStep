package com.tbacademy.nextstep.domain.model

import java.sql.Date

data class Notification(
    val id: String,
    val fromId: String,
    val toId: String,
    val type: NotificationType,
    val reactionType: ReactionType? = null,
    val postId: String? = null,
    val isRead: Boolean = false,
    val createdAt: Date
)

enum class NotificationType {
    POST_REACTED
}