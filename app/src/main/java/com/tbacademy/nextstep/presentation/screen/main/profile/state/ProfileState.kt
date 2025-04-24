package com.tbacademy.nextstep.presentation.screen.main.profile.state

import com.tbacademy.nextstep.presentation.screen.main.profile.model.GoalPresentation
import com.tbacademy.nextstep.presentation.screen.main.profile.model.UserPresentation

data class ProfileState(
    val withBottomNav: Boolean = false,
    val user: UserPresentation? = null,
    val userGoals: List<GoalPresentation>? = null,
    val goalsLoading: Boolean = false,
    val isLoading: Boolean = false,
    val isImageLoading: Boolean = false,
    val errorRes: Int? = null
)