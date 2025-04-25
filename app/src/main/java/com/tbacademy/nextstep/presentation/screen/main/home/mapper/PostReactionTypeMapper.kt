package com.tbacademy.nextstep.presentation.screen.main.home.mapper

import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.presentation.screen.main.home.model.ReactionTypePresentation

fun ReactionTypePresentation.toDomain(): ReactionType = when (this) {
    ReactionTypePresentation.FIRE -> ReactionType.FIRE
    ReactionTypePresentation.HEART -> ReactionType.HEART
    ReactionTypePresentation.COOKIE -> ReactionType.COOKIE
    ReactionTypePresentation.CHEER -> ReactionType.CHEER
    ReactionTypePresentation.DISAPPOINTMENT -> ReactionType.DISAPPOINTMENT
}

fun ReactionType.toPresentation(): ReactionTypePresentation = when (this) {
    ReactionType.FIRE -> ReactionTypePresentation.FIRE
    ReactionType.HEART -> ReactionTypePresentation.HEART
    ReactionType.COOKIE -> ReactionTypePresentation.COOKIE
    ReactionType.CHEER -> ReactionTypePresentation.CHEER
    ReactionType.DISAPPOINTMENT -> ReactionTypePresentation.DISAPPOINTMENT
}