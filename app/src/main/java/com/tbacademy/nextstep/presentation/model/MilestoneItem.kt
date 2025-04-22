package com.tbacademy.nextstep.presentation.model

import java.security.Timestamp

data class MilestoneItem(
    val id: Int,
    var text: String,
    val errorMessage: Int? = null,
    val achieved: Boolean = false,
    val achievedAt: Timestamp? = null
)