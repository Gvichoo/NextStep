package com.tbacademy.nextstep.data.common.mapper

import com.tbacademy.nextstep.data.remote.dto.UserDto
import com.tbacademy.nextstep.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        uid, email, username, profilePictureUrl
    )
}