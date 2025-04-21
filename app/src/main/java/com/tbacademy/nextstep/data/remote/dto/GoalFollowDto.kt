package com.tbacademy.nextstep.data.remote.dto

import com.google.firebase.Timestamp

data class GoalFollowDto(
    val id: String = "",
    val followerId: String = "",
    val followedId: String = "",
    val createdAt: Timestamp = Timestamp.now()
)