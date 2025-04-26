package com.tbacademy.nextstep.presentation.screen.main.post.state

import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation

data class PostState(
    val post: PostPresentation? = null,
    val isLoading: Boolean = false,
    val errorMessageRes: Int? = null,
    val commentRequested: Boolean = false,
)
