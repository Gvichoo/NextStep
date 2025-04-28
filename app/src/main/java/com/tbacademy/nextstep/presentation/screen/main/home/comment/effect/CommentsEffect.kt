package com.tbacademy.nextstep.presentation.screen.main.home.comment.effect

import com.tbacademy.core.model.error.ApiError

sealed interface CommentsEffect {
    data object StartTyping: CommentsEffect
    data class ShowError(val error: ApiError): CommentsEffect
    data object CommentCreated: CommentsEffect
}