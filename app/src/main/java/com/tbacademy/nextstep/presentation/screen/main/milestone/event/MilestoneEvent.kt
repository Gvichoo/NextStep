package com.tbacademy.nextstep.presentation.screen.main.milestone.event


sealed interface MilestoneEvent {
    data class LoadMilestones(val goalId: String) : MilestoneEvent
    data class MarkMilestoneAsDone(val goalId: String,  val milestoneId: Int) : MilestoneEvent
    data class OpenMilestone(val milestoneId: String,val text : String) : MilestoneEvent
}
