package com.tbacademy.nextstep.presentation.screen.main.home.effect

import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation

sealed interface HomeEffect {
    data class ShowError(val errorRes: Int?) : HomeEffect
    data class OpenComments(val postId: String, val typeActive: Boolean = false): HomeEffect
    data class NavigateToUserProfile(val userId: String): HomeEffect
}