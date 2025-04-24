package com.tbacademy.nextstep.presentation.screen.main.add.state

data class AddGoalState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val formBeenSubmitted: Boolean = false,

    val goalTitleErrorMessage: Int? = null,
    val goalDescriptionErrorMessage: Int? = null,
    val goalDateErrorMessage: Int? = null,
    val goalMetricTargetErrorMessage: Int? = null,
    val goalMetricUnitErrorMessage: Int? = null,
) {
    val isCreateGoalEnabled: Boolean
        get() = goalTitleErrorMessage == null && goalDescriptionErrorMessage == null
}