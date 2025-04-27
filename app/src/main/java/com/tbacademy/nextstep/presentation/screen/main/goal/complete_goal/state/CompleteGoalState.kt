package com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal.state

import android.net.Uri

data class CompleteGoalState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val formBeenSubmitted: Boolean = false,

    val descriptionErrorMessage: Int? = null,
    val imageErrorMessage: Int? = null,

    val description: String = "",
    val imageUri: Uri? = null,
    val isSubmitted: Boolean = false,
) {
    val isSubmitEnabled: Boolean
        get() = descriptionErrorMessage == null && imageErrorMessage == null
}