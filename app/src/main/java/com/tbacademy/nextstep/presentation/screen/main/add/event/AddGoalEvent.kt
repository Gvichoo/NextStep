package com.tbacademy.nextstep.presentation.screen.main.add.event

import android.net.Uri
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import java.util.Date

sealed interface AddGoalEvent {
    data class GoalTitleChanged(val title: String) : AddGoalEvent
    data class GoalDescriptionChanged(val description: String) : AddGoalEvent
    data class GoalDateChanged(val date: Date) : AddGoalEvent
    data class GoalMetricTargetChanged(val metricTarget: String) : AddGoalEvent
    data class GoalMetricUnitChanged(val metricUnit: String) : AddGoalEvent

    data class MetricToggle(val enabled: Boolean) : AddGoalEvent


    data class OnMilestoneTextChanged(val id: Int, val text: String) : AddGoalEvent

    data object OnAddMilestoneButtonClicked : AddGoalEvent
    data object OnMinusMileStoneButtonClicked : AddGoalEvent
    data class MileStoneToggle(val enabled: Boolean) : AddGoalEvent

    data object PickImageClicked : AddGoalEvent
    data class ImageSelected(val imageUri: Uri) : AddGoalEvent
    data object ImageCleared : AddGoalEvent




    data object Submit : AddGoalEvent

    data object OnCreateGoalBtnClicked : AddGoalEvent

    data class CreateGoal(
        val title: String,
        val description: String,
        val goalDate: Date,
        val metricTarget: String,
        val metricUnit: String,
        val isMetricEnabled: Boolean,
        val imageUrl: Uri,
        val isMilestoneEnabled : Boolean,
        val milestone : List<MilestoneItem>
    ) : AddGoalEvent


}