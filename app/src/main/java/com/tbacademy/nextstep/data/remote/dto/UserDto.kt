package com.tbacademy.nextstep.data.remote.dto

data class UserDto(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val profilePictureUrl: String? = null
)