package com.tbacademy.nextstep.domain.model

import java.util.Date

data class UserFollow(
    val id: String,
    val followerId: String,
    val followedId: String,
    val createdAt: Date
)