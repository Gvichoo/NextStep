package com.tbacademy.nextstep.presentation.screen.main.add.state

data class WorkerStatusState(
    val isLoading:Boolean = false,
    val uploadedSuccessfully:Unit? = null,
    val failedMessage:String? = null,
    val blocked:Unit? = null,
    val wasCanceled:Unit? = null
)