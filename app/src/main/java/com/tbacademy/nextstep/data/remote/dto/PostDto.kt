package com.tbacademy.nextstep.data.remote.dto

import com.google.firebase.Timestamp
import com.tbacademy.nextstep.domain.model.PostType
import java.util.Date

data class PostDto(
    val id: String = "",
    val authorId: String = "",
    val goalId: String = "",
    val authorUsername: String = "",
    val authorProfilePictureUrl: String = "",
    val title: String = "",
    val description: String = "",
    val reactionCount: Int = 0,
    val reactionFireCount: Int = 0,
    val reactionHeartCount: Int = 0,
    val reactionCookieCount: Int = 0,
    val reactionCheerCount: Int = 0,
    val reactionDisappointmentCount: Int = 0,
    val commentCount: Int = 0,
    val imageUrl: String = "",
    val createdAt: Timestamp = Timestamp(Date()),
    val type: PostType = PostType.MILESTONE
)
