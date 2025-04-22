package com.tbacademy.nextstep.presentation.screen.main.user_search.effect

sealed interface UserSearchEffect {
    data class ShowMessage(val messageRes: Int): UserSearchEffect
    data class NavigateToProfile(val userId: String): UserSearchEffect
    data object NavigateBack: UserSearchEffect
}