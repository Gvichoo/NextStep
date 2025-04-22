package com.tbacademy.nextstep.presentation.screen.main.user_search.state

import com.tbacademy.nextstep.presentation.screen.main.user_search.model.UserPresentation

data class UserSearchState(
    val loading: Boolean = false,
    val users: List<UserPresentation>? = null,
    val errorRes: Int? = null,
    val searchQuery: String = ""
)