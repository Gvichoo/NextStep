package com.tbacademy.nextstep.domain.model

import android.net.Uri
import java.util.Date

data class Post(
    val id: String,
    val authorId: String,
    val authorUsername : String ,
    val authorProfilePictureUrl: String? = null,
    val goalId: String = "",
    val goalTitle: String = "",
    val title: String,
    val description: String ,
    val reactionCount: Int,
    val reactionFireCount: Int = 0,
    val reactionHeartCount: Int = 0,
    val reactionCookieCount: Int = 0,
    val reactionCheerCount: Int = 0,
    val reactionDisappointedCount: Int = 0,
    val userReaction: ReactionType? = null,
    val isUserFollowing: Boolean = false,
    val isOwnPost: Boolean = false,
    val commentCount: Int,
    val imageUrl: String? = null,
    val imageUri: Uri? = null,
    val createdAt: Date,
    val type: PostType
)

enum class PostType {
    GOAL,
    MILESTONE,
    GOAL_COMPLETE
}