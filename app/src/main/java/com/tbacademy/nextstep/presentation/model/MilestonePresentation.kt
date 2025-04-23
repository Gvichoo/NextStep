package com.tbacademy.nextstep.presentation.model

import java.sql.Timestamp

data class  MilestonePresentation(
    val id: Int = 0,
    val goalId: String = "",
    val text: String = "",
    val achieved : Boolean = false,
    val achievedAt: Timestamp? = null,
    val status: String = ""
)