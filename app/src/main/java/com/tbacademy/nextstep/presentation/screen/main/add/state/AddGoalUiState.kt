package com.tbacademy.nextstep.presentation.screen.main.add.state

import android.net.Uri
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import java.util.Date

data class AddGoalUiState (
    val title : String = "",
    val description: String = "",
    val goalDate : Date? = null,
    val metricTarget : String = "",
    val metricUnit : String = "",
    val isMetricEnabled: Boolean = false,
    val imageUri: Uri? = null,
    val isSubmitted: Boolean = false,

    val isMileStoneEnabled : Boolean = false,
    val milestones: List<MilestoneItem> = listOf(
        MilestoneItem(id = 0, text = "")
    ),
    val milestoneIdCounter: Int = 1,

    val uploadedSuccessfully:Unit? = null,
    val failedMessage:String? = null,
    val blocked:Unit? = null,
    val wasCanceled:Unit? = null,



    val goalImageErrorMessage: Int? = null

)

