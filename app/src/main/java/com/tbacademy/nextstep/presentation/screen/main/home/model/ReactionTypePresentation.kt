package com.tbacademy.nextstep.presentation.screen.main.home.model

import com.tbacademy.nextstep.R

enum class ReactionTypePresentation(
    val iconRes: Int,
    val titleRes: Int = R.string.react,
    val backgroundRes: Int
) {
    FIRE(
        iconRes = R.drawable.ic_reaction_fire_24px,
        titleRes = R.string.cooked,
        backgroundRes = R.drawable.bg_reaction_fire
    ),
    HEART(
        iconRes = R.drawable.ic_reaction_heart_24px,
        titleRes = R.string.heart,
        backgroundRes = R.drawable.bg_reaction_heart
    ),
    COOKIE(
        iconRes = R.drawable.ic_reaction_cookie_24px,
        titleRes = R.string.cookie,
        backgroundRes = R.drawable.bg_reaction_cookie
    ),
    CHEER(
        iconRes = R.drawable.ic_reaction_cheer_24px,
        titleRes = R.string.cheer,
        backgroundRes = R.drawable.bg_reaction_cheer
    ),
    DISAPPOINTMENT(
        iconRes = R.drawable.ic_reaction_dissapointment,
        titleRes = R.string.dissapointed,
        backgroundRes = R.drawable.bg_reaction_task
    )
}