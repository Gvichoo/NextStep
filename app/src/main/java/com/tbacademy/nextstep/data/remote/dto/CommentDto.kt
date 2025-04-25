package com.tbacademy.nextstep.data.remote.dto

import com.google.firebase.Timestamp

data class CommentDto(
    val id: String = "",
    val postId: String = "",
    val authorId: String = "",
    val authorUsername: String = "",
    val authorProfilePictureUrl: String? = null,
    val text: String = "",
    val createdAt: Timestamp = Timestamp.now()
)