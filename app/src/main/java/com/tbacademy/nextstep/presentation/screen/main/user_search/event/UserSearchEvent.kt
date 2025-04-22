package com.tbacademy.nextstep.presentation.screen.main.user_search.event

sealed interface UserSearchEvent {
    data class QueryChanged(val value: String): UserSearchEvent
    data object ClearSearch : UserSearchEvent
    data object BackRequest: UserSearchEvent
}