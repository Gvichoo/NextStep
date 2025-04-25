package com.tbacademy.nextstep.presentation.screen.main.profile.event

import android.net.Uri
sealed interface ProfileEvent {
    data class SetProfileState(val userId: String? = null) : ProfileEvent
    data object ToggleFollowUser : ProfileEvent
    data object BackRequest: ProfileEvent
    data class GoalSelected(val goalId: String, val goalTitle: String): ProfileEvent

    data object UpdateImage: ProfileEvent
    data object CameraSelected: ProfileEvent
    data object GallerySelected: ProfileEvent
    data class ImageSelected(val imageUri: Uri) : ProfileEvent

}
