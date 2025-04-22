package com.tbacademy.nextstep.domain.model

import java.util.Date

data class UserFollow(
    val id: String,
    val followerId: String,
    val followedUserId: String,
    val createdAt: Date
)