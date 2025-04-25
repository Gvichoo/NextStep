package com.tbacademy.nextstep.data.remote.dto

import com.google.firebase.Timestamp
import com.tbacademy.nextstep.domain.model.ReactionType
import java.util.Date

data class ReactionDto(
    val id: String,
    val postId: String,
    val authorId: String,
    val type: ReactionType,
    val createdAt: Timestamp = Timestamp(Date())
)