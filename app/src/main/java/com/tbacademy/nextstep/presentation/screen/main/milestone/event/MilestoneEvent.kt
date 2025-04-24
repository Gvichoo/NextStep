package com.tbacademy.nextstep.presentation.screen.main.milestone.event


sealed interface MilestoneEvent {
    data class LoadMilestones(val goalId: String) : MilestoneEvent
    data class MarkMilestoneAsDone(val goalId: String,  val id: Int) : MilestoneEvent
}
