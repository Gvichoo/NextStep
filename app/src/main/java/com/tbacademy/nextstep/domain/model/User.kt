package com.tbacademy.nextstep.domain.model

data class User(
    val uid: String,
    val email: String,
    val username: String,
    val profilePictureUrl: String? = null,
)
