package com.tbacademy.nextstep.presentation.screen.main.goal.goal_completed

import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.base.launchEffect
import com.tbacademy.nextstep.presentation.screen.main.goal.goal_completed.effect.GoalCompletedEffect
import com.tbacademy.nextstep.presentation.screen.main.goal.goal_completed.event.GoalCompletedEvent
import com.tbacademy.nextstep.presentation.screen.main.goal.goal_completed.state.GoalCompletedState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoalCompletedViewModel @Inject constructor() :
    BaseViewModel<GoalCompletedState, GoalCompletedEvent, GoalCompletedEffect>
        (initialState = GoalCompletedState()) {
    override fun onEvent(event: GoalCompletedEvent) {
        when (event) {
            is GoalCompletedEvent.StartAnimation -> {
                updateState {
                    copy(messageVisible = true)
                }
            }

            is GoalCompletedEvent.Continue -> launchEffect(effect = GoalCompletedEffect.NavigateToHome)
        }
    }

}