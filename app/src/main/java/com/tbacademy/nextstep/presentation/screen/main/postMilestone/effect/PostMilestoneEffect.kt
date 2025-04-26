package com.tbacademy.nextstep.presentation.screen.main.postMilestone.effect


sealed interface PostMilestoneEffect {
    data object LaunchMediaPicker : PostMilestoneEffect
    data object NavigateToPosts : PostMilestoneEffect
    data object NavigateBack : PostMilestoneEffect
}