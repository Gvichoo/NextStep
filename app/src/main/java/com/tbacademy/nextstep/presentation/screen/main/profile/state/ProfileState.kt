package com.tbacademy.nextstep.presentation.screen.main.profile.state

import com.tbacademy.nextstep.domain.model.User

data class ProfileState(
    val isOwnProfile: Boolean = true,
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorRes: Int? = null
)