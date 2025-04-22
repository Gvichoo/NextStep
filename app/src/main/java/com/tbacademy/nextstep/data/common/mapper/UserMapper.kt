package com.tbacademy.nextstep.data.common.mapper

import com.tbacademy.nextstep.data.remote.dto.UserDto
import com.tbacademy.nextstep.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        uid = uid,
        email = email,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        uid = uid,
        email = email,
        username = username,
        usernameLower = username.lowercase(),
        profilePictureUrl = profilePictureUrl
    )
}