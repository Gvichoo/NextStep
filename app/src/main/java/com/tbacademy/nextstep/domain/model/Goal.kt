package com.tbacademy.nextstep.domain.model

import android.net.Uri
import com.tbacademy.nextstep.presentation.model.MilestoneItem

data class Goal(
    val id: String = "",
    val title: String = "",
    val description: String? = null,
    val isMetricBased: Boolean = false,
    val metricTarget: String? = null,
    val metricUnit: String? = null,
    val targetDate: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val imageUri: Uri? = null,
    val milestone: List<MilestoneItem>? = emptyList()
){
    val imageUrl: String? = null
}