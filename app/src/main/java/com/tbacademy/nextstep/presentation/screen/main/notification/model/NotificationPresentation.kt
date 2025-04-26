package com.tbacademy.nextstep.presentation.screen.main.notification.model

import com.tbacademy.nextstep.R
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

enum class NotificationTypePresentation(
    val messageRes: Int,
    val iconRes: Int? = null,
    val backgroundRes: Int? = null
) {
    POST_REACTED(messageRes = R.string.reacted_to_your_post),
    POST_COMMENTED(
        messageRes = R.string.commented_on_your_post,
        iconRes = R.drawable.ic_comment_24px,
        backgroundRes = R.drawable.bg_reaction_task
    ),
    USER_FOLLOWED(
        messageRes = R.string.started_following_you,
        iconRes = R.drawable.ic_follow,
        backgroundRes = R.drawable.bg_follow_notification
    )
}