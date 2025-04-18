package com.tbacademy.nextstep.presentation.screen.main.home.effect

sealed interface HomeEffect {
    data class ShowError(val errorRes: Int?) : HomeEffect
    data class OpenComments(val postId: String, val typeActive: Boolean = false): HomeEffect
}