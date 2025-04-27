package com.tbacademy.nextstep.presentation.screen.main.home.effect

sealed interface HomeEffect {
    data class ShowError(val errorRes: Int?) : HomeEffect
    data class OpenComments(val postId: String, val typeActive: Boolean = false) : HomeEffect
    data class NavigateToUserProfile(val userId: String) : HomeEffect
    data object NavigateToUserSearch : HomeEffect
    data class NavigateToMilestone(val goalId: String) : HomeEffect
    data class NavigateToGoal(val goalId: String, val isOwnGoal: Boolean) : HomeEffect
}