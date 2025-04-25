package com.tbacademy.nextstep.presentation.screen.main.milestone.effect


sealed interface MilestoneEffect {
    data class ShowError(val message: String) : MilestoneEffect
    data class NavigateToMilestonePost(val milestoneId : String,val text : String) : MilestoneEffect
    data object NavigateBack : MilestoneEffect
}
