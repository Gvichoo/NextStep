package com.tbacademy.nextstep.presentation.screen.main.postMilestone.event

import android.net.Uri


sealed interface PostMilestoneEvent {
    data class SetTitle(val title: String) : PostMilestoneEvent

    data class DescriptionChanged(val description: String) : PostMilestoneEvent

    data object PickImageClicked : PostMilestoneEvent
    data class ImageSelected(val imageUri: Uri) : PostMilestoneEvent
    data object ImageCleared : PostMilestoneEvent

    data class Submit(val goalId: String) : PostMilestoneEvent

    data object NavigateBack : PostMilestoneEvent
}