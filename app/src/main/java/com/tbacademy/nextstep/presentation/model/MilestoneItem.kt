package com.tbacademy.nextstep.presentation.model

import com.google.firebase.Timestamp


data class MilestoneItem(
    val id: Int = 0,
    var text: String = "",
    val errorMessage: Int? = null,
    val achieved: Boolean = false,
    val achievedAt: Timestamp? = null,
)