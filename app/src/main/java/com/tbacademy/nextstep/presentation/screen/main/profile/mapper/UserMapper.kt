package com.tbacademy.nextstep.presentation.screen.main.profile.mapper

import com.tbacademy.nextstep.domain.model.User
import com.tbacademy.nextstep.presentation.screen.main.profile.model.UserPresentation

fun User.toPresentation(): UserPresentation {
    return UserPresentation(
        uid = uid,
        email = email,
        username = username,
        isOwnUser = isOwnUser,
        isUserFollowed = isUserFollowed,
        profilePictureUrl = profilePictureUrl
    )
}