package com.tbacademy.nextstep.domain.model

data class User(
    val uid: String,
    val email: String,
    val username: String,
    val isOwnUser: Boolean = false,
    val isUserFollowed: Boolean = false,
    val profilePictureUrl: String? = null,
)
