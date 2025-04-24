package com.tbacademy.nextstep.data.common.mapper

import com.tbacademy.nextstep.data.model.MilestoneItemDto
import com.tbacademy.nextstep.presentation.model.MilestoneItem

fun MilestoneItemDto.toDomain(): MilestoneItem {
    return MilestoneItem(
        id = id,
        text = text,
        errorMessage = errorMessage,
        achieved = achieved,
        achievedAt = achievedAt
    )
}