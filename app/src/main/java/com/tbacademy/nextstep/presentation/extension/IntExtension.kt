package com.tbacademy.nextstep.presentation.extension

import com.tbacademy.nextstep.presentation.screen.main.home.model.ReactionTypePresentation

fun Int.adjustCount(
    old: ReactionTypePresentation?,
    new: ReactionTypePresentation?,
    target: ReactionTypePresentation
): Int {
    return when {
        old == target && new != target -> this - 1
        old != target && new == target -> this + 1
        else -> this
    }
}