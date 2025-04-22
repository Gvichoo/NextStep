package com.tbacademy.nextstep.presentation.screen.main.milestone

import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.usecase.goalId.GetGoalIdUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import com.tbacademy.nextstep.presentation.screen.main.milestone.effect.MilestoneEffect
import com.tbacademy.nextstep.presentation.screen.main.milestone.event.MilestoneEvent
import com.tbacademy.nextstep.presentation.screen.main.milestone.state.MilestoneState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MilestoneViewModel @Inject constructor(
    private val getGoalByIdUseCase: GetGoalIdUseCase
): BaseViewModel<MilestoneState,MilestoneEvent,MilestoneEffect,Unit>(
    initialState = MilestoneState(),
    initialUiState = Unit
) {
    override fun onEvent(event: MilestoneEvent) {
        when(event){
            MilestoneEvent.LoadMilestones -> TODO()
        }

    }

    private fun fetchMilestones(goalId: String) {
        viewModelScope.launch {
            getGoalByIdUseCase(goalId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        updateState { copy(isLoading = result.loading) }
                    }

                    is Resource.Success -> {
                        val milestones = result.data.milestone?.map { milestone ->
                            MilestoneItem(
                                id = milestone.id,
                                text = milestone.text,
                                achieved = milestone.achieved,
                                achievedAt = milestone.achievedAt
                            )
                        } ?: emptyList()

                        updateState {
                            copy(
                                milestones = milestones,
                                isSuccess = true,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        updateState {
                            copy(
                                errorMessage = result.error.toMessageRes(),
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}