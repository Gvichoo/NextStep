package com.tbacademy.nextstep.presentation.screen.main.profile.state

import com.tbacademy.nextstep.domain.model.User

data class ProfileState(
    val isOwnProfile: Boolean = true,
    val withBottomNav: Boolean = true,
    val isUserFollowed: Boolean = false,
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorRes: Int? = null
)