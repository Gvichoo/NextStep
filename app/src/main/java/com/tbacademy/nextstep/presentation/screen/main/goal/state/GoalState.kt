package com.tbacademy.nextstep.presentation.screen.main.goal.state

import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation

data class GoalState(
    val posts: List<PostPresentation>? = null,
    val isLoading: Boolean = false,
    val errorRes: Int? = null,
    val isOwnGoal: Boolean = false,
    val hasMilestones: Boolean = false,
    val isGoalCompleteBottomSheetVisible: Boolean = false,
    val goalTitle: String? = null
)