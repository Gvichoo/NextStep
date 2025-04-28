package com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal.event

import android.net.Uri

sealed interface CompleteGoalEvent {
    data class SetGoalInfo(val goalId: String, val goalTitle: String) : CompleteGoalEvent

    data class DescriptionChanged(val description: String) : CompleteGoalEvent

    data object PickImage : CompleteGoalEvent

    data object CameraSelected: CompleteGoalEvent
    data object GallerySelected: CompleteGoalEvent
    data class ImageSelected(val imageUri: Uri) : CompleteGoalEvent

    data class Submit(val goalId: String) : CompleteGoalEvent

    data object Return : CompleteGoalEvent
}