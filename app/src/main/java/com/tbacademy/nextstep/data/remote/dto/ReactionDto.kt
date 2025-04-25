package com.tbacademy.nextstep.data.remote.dto

import com.google.firebase.Timestamp
import java.util.Date

data class ReactionDto(
    val id: String,
    val postId: String,
    val authorId: String,
    val type: ReactionTypeDto,
    val createdAt: Timestamp = Timestamp(Date())
)

enum class ReactionTypeDto {
    FIRE,
    HEART,
    COOKIE,
    CHEER,
    DISAPPOINTMENT
}