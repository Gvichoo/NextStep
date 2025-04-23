package com.tbacademy.nextstep.presentation.screen.main.milestone

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.usecase.goal.GetMilestoneUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.model.toPresentationList
import com.tbacademy.nextstep.presentation.screen.main.milestone.effect.MilestoneEffect
import com.tbacademy.nextstep.presentation.screen.main.milestone.event.MilestoneEvent
import com.tbacademy.nextstep.presentation.screen.main.milestone.state.MilestoneState
import com.tbacademy.nextstep.presentation.screen.main.milestone.state.MilestoneUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.sql.Timestamp
import javax.inject.Inject

@HiltViewModel
class MilestoneViewModel @Inject constructor(
    private val getGoalMilestones : GetMilestoneUseCase,
) : BaseViewModel<MilestoneState, MilestoneEvent, MilestoneEffect, MilestoneUiState>(
    initialState = MilestoneState(),
    initialUiState = MilestoneUiState()
) {

    override fun onEvent(event: MilestoneEvent) {
        when (event) {
            is MilestoneEvent.LoadMilestones -> getMilestoneById(goalId = event.goalId )
            is MilestoneEvent.MarkMilestoneAsDone -> {
                val currentTimestamp = Timestamp(System.currentTimeMillis())
                updateState {
                    val updatedList = milestoneList.map {
                        if (it.id == event.milestoneId) {
                            it.copy(achieved = true, achievedAt = currentTimestamp)
                        } else {
                            it
                        }
                    }
                    copy(milestoneList = updatedList)
                }
            }
        }
    }

    private fun getMilestoneById(goalId : String){
        viewModelScope.launch {
            getGoalMilestones(goalId = goalId).collectLatest { resource ->
                when(resource){
                    is Resource.Error -> updateState { this.copy(errorMessage = resource.error.toMessageRes()) }
                    is Resource.Loading -> updateState { this.copy(isLoading = resource.loading) }

                    is Resource.Success -> {
                        val milestones = resource.data.milestone ?: emptyList()
                        val presentations = milestones.toPresentationList()
                        Log.d("MilestoneViewModel", "${resource.data}")
                        updateState {
                            copy(milestoneList = presentations)
                        }
                    }
                }
            }
        }
    }
}