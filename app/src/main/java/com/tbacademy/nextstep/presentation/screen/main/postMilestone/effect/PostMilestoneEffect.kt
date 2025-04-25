package com.tbacademy.nextstep.presentation.screen.main.postMilestone.effect


sealed interface PostMilestoneEffect {
    data object LaunchMediaPicker : PostMilestoneEffect
}