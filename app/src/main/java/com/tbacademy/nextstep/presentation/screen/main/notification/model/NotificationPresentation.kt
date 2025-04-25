package com.tbacademy.nextstep.presentation.screen.main.notification.model

import com.tbacademy.nextstep.presentation.screen.main.home.model.ReactionTypePresentation
import java.util.Date

data class NotificationPresentation(
    val id: String,
    val fromId: String,
    val toId: String,
    val type: NotificationTypePresentation,
    val reactionType: ReactionTypePresentation? = null,
    val postId: String? = null,
    val isRead: Boolean = false,
    val authorUsername: String,
    val authorProfilePictureUrl: String? = null,
    val createdAt: String
)

enum class NotificationTypePresentation {
    POST_REACTED
}