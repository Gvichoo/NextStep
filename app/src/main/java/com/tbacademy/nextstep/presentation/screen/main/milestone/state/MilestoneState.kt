package com.tbacademy.nextstep.presentation.screen.main.milestone.state

import com.tbacademy.nextstep.presentation.model.MilestoneItem

data class MilestoneState(
    val isLoading: Boolean = false,
    val milestones: List<MilestoneItem> = emptyList(),
    val errorMessage: Int? = null,
    val isSuccess : Boolean = false
)
