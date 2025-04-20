package com.tbacademy.nextstep.domain.model

import java.util.Date


data class Follow(
    val id: String,
    val followerId: String,
    val followType: FollowType,
    val followedId: String,
    val createdAt: Date
)

enum class FollowType {
    USER,
    GOAL
}