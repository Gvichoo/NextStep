package com.tbacademy.nextstep.presentation.model

import com.google.firebase.Timestamp

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

fun MilestonePresentation.toMilestoneItem() = MilestoneItem(
    id = id,
    text = text,
    achieved = achieved,
    achievedAt = achievedAt,
    errorMessage = null
)

fun List<MilestoneItem>.toPresentationList(): List<MilestonePresentation> {
    return this.map { it.toPresentation() }
}
