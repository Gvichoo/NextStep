package com.tbacademy.nextstep.presentation.model

import com.google.firebase.Timestamp

data class  MilestonePresentation(
    val id: Int = 0,
    val goalId: String = "",
    val text: String = "",
    val achieved : Boolean = false,
    val achievedAt: Timestamp? = null,
    val isPostVisible: Boolean = false,
    val isAuthor: Boolean = false,
    val authorId: String? = null,
    val targetDate: Timestamp?
)