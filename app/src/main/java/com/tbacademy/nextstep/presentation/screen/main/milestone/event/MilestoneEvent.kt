package com.tbacademy.nextstep.presentation.screen.main.milestone.event

sealed interface MilestoneEvent {
    data object LoadMilestones : MilestoneEvent
}
