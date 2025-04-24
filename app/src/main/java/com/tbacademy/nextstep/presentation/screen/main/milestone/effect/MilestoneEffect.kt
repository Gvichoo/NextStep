package com.tbacademy.nextstep.presentation.screen.main.milestone.effect

sealed interface MilestoneEffect {
    data class ShowError(val message: String) : MilestoneEffect
    data object NavigateBack : MilestoneEffect
}
