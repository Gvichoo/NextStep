package com.tbacademy.nextstep.presentation.model

import java.sql.Timestamp

data class MilestoneItem(
    val id: Int = 0,
    var text: String = "",
    val errorMessage: Int? = null,
    val achieved: Boolean = false,
    val achievedAt: Timestamp? = null
)


fun MilestoneItem.toPresentation(): MilestonePresentation {
    return MilestonePresentation(
        id = id,
        text = text,
        achieved = achieved,
        achievedAt = achievedAt
    )
}

fun List<MilestoneItem>.toPresentationList(): List<MilestonePresentation> {
    return this.map { it.toPresentation() }
}
