package com.tbacademy.nextstep.presentation.screen.main.user_search.mapper

import com.tbacademy.nextstep.domain.model.User
import com.tbacademy.nextstep.presentation.screen.main.user_search.model.UserPresentation

fun User.toPresentation(): UserPresentation {
    return UserPresentation(
        id = uid,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}