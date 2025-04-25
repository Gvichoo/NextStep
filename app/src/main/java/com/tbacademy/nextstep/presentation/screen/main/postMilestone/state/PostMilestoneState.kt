package com.tbacademy.nextstep.presentation.screen.main.postMilestone.state

import android.net.Uri

data class PostMilestoneState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val formBeenSubmitted: Boolean = false,

    val titleErrorMessage: Int? = null,
    val descriptionErrorMessage: Int? = null,
    val imageErrorMessage: Int? = null,

    val title : String = "",
    val description: String = "",
    val imageUri: Uri? = null, val isSubmitted: Boolean = false,
){
    val isPostButtonEnable: Boolean
        get() = titleErrorMessage == null && descriptionErrorMessage == null
}