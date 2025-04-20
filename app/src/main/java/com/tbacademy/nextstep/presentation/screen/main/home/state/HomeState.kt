package com.tbacademy.nextstep.presentation.screen.main.home.state

import com.tbacademy.nextstep.domain.core.ApiError
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation

data class HomeState(
    val isLoading: Boolean = false,
    val posts: List<PostPresentation>? = null,
    val error: ApiError? = null,
    val feedState: FeedState = FeedState.GLOBAL
)

enum class FeedState {
    GLOBAL,
    FOLLOWED
}