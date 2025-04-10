package com.tbacademy.nextstep.presentation.screen.main.add.state

import java.util.Date

data class AddGoalUiState (
    val title : String = "",
    val description: String = "",
    val goalDate : Date? = null,
    val metricTarget : Int = 0,
    val metricUnit : String = "",
    val isMetricEnabled: Boolean = false,

)

