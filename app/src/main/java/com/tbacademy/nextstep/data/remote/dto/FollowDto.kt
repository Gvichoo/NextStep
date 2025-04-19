package com.tbacademy.nextstep.data.remote.dto

import com.google.firebase.Timestamp

data class FollowDto(
    val id: String = "",
    val followerId: String = "",
    val followType: FollowTypeDto = FollowTypeDto.USER,
    val followedId: String = "",
    val createdAt: Timestamp = Timestamp.now()
)

enum class FollowTypeDto {
    USER,
    GOAL
}