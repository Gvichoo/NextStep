package com.tbacademy.nextstep.presentation.screen.main.profile.model

data class UserPresentation(
    val uid: String,
    val email: String,
    val username: String,
    val isOwnUser: Boolean = false,
    val isUserFollowed: Boolean = false,
    val profilePictureUrl: String? = null,
)