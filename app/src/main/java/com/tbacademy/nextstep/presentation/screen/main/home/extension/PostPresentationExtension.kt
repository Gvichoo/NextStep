package com.tbacademy.nextstep.presentation.screen.main.home.extension

import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation
import com.tbacademy.nextstep.presentation.screen.main.home.model.ReactionTypePresentation

fun PostPresentation.topReactions(): List<Pair<ReactionTypePresentation, Int>> {
    return listOf(
        ReactionTypePresentation.FIRE to reactionFireCount,
        ReactionTypePresentation.HEART to reactionHeartCount,
        ReactionTypePresentation.COOKIE to reactionCookieCount,
        ReactionTypePresentation.CHEER to reactionCheerCount,
        ReactionTypePresentation.DISAPPOINTMENT to reactionDisappointmentCount
    ).filter { it.second > 0 }
        .sortedByDescending { it.second }
        .take(3)
}