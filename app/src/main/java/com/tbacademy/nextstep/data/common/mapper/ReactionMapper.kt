package com.tbacademy.nextstep.data.common.mapper

import com.tbacademy.nextstep.data.remote.dto.NotificationDto
import com.tbacademy.nextstep.data.remote.dto.ReactionTypeDto
import com.tbacademy.nextstep.domain.model.Notification
import com.tbacademy.nextstep.domain.model.ReactionType


fun ReactionType.toDto(): ReactionTypeDto = when (this) {
    ReactionType.FIRE -> ReactionTypeDto.FIRE
    ReactionType.HEART -> ReactionTypeDto.HEART
    ReactionType.COOKIE -> ReactionTypeDto.COOKIE
    ReactionType.CHEER -> ReactionTypeDto.CHEER
    ReactionType.DISAPPOINTMENT -> ReactionTypeDto.DISAPPOINTMENT
}

fun ReactionTypeDto.toDomain(): ReactionType = when (this) {
    ReactionTypeDto.FIRE -> ReactionType.FIRE
    ReactionTypeDto.HEART -> ReactionType.HEART
    ReactionTypeDto.COOKIE -> ReactionType.COOKIE
    ReactionTypeDto.CHEER -> ReactionType.CHEER
    ReactionTypeDto.DISAPPOINTMENT -> ReactionType.DISAPPOINTMENT
}