package com.tbacademy.nextstep.presentation.screen.main.profile.state

import com.tbacademy.nextstep.domain.model.User
import com.tbacademy.nextstep.presentation.screen.main.home.model.FollowStatus

data class ProfileState(
    val isOwnProfile: Boolean = true,
    val isUserFollowed: FollowStatus = FollowStatus.TO_FOLLOW,
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorRes: Int? = null
)