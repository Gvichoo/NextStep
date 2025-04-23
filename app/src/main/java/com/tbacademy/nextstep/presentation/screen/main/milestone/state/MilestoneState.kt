package com.tbacademy.nextstep.presentation.screen.main.milestone.state

import com.tbacademy.nextstep.presentation.model.MilestonePresentation

data class MilestoneState(
    val isLoading: Boolean = false,
    val milestoneList: List<MilestonePresentation> = emptyList(),
    val errorMessage: Int? = null,
    val isSuccess : Boolean = false
)
