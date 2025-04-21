package com.tbacademy.nextstep.data.remote.dto

import com.google.firebase.Timestamp

class UserFollowDto(
    val id: String = "",
    val followerId: String = "",
    val followedUserId: String = "",
    val createdAt: Timestamp = Timestamp.now()
)