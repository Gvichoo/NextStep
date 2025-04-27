package com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal

import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal.effect.CompleteGoalEffect
import com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal.event.CompleteGoalEvent
import com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal.state.CompleteGoalState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompleteGoalViewModel @Inject constructor(

) : BaseViewModel<CompleteGoalState, CompleteGoalEvent, CompleteGoalEffect, Unit>(
    initialState = CompleteGoalState(),
    initialUiState = Unit
) {
    override fun onEvent(event: CompleteGoalEvent) {

    }
}