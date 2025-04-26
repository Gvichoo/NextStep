package com.tbacademy.nextstep.presentation.screen.main.post.effect

sealed interface PostEffect {
    data class ShowErrorMessage(val errorMessageRes: Int): PostEffect
    data class OpenCommentsBottomSheet(val postId: String): PostEffect
    data object NavigateBack: PostEffect
}