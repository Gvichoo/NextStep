package com.tbacademy.nextstep.presentation.screen.main.milestone

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.usecase.UpdateGoalUseCase
import com.tbacademy.nextstep.domain.usecase.auth.GetAuthUserIdUseCase
import com.tbacademy.nextstep.domain.usecase.goal.GetMilestoneUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.model.toMilestoneItem
import com.tbacademy.nextstep.presentation.model.toPresentationList
import com.tbacademy.nextstep.presentation.screen.main.milestone.effect.MilestoneEffect
import com.tbacademy.nextstep.presentation.screen.main.milestone.event.MilestoneEvent
import com.tbacademy.nextstep.presentation.screen.main.milestone.state.MilestoneState
import com.tbacademy.nextstep.presentation.screen.main.milestone.state.MilestoneUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MilestoneViewModel @Inject constructor(
    private val getGoalMilestones: GetMilestoneUseCase,
    private val updateGoalUseCase: UpdateGoalUseCase,
    private val getAuthUserIdUseCase: GetAuthUserIdUseCase
) : BaseViewModel<MilestoneState, MilestoneEvent, MilestoneEffect, MilestoneUiState>(
    initialState = MilestoneState(),
    initialUiState = MilestoneUiState()
) {

    override fun onEvent(event: MilestoneEvent) {
        when (event) {
            is MilestoneEvent.LoadMilestones -> getMilestoneById(goalId = event.goalId)
            is MilestoneEvent.MarkMilestoneAsDone -> {
                markMilestoneAsDone(event.goalId, event.milestoneId)

            }
        }
    }


    private fun markMilestoneAsDone(goalId: String, milestoneId: Int) {
        viewModelScope.launch {
            // Collect the current user's ID from the Flow
            getAuthUserIdUseCase().collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val currentUserId = resource.data // This is the current user's ID
                        // Check if the current user is the author of the goal
                        if (currentUserId == state.value.authorId) {
                            val updatedMilestones = state.value.milestoneList.map {
                                if (it.id == milestoneId) {
                                    it.copy(
                                        achieved = true,
                                        achievedAt = Timestamp.now()
                                    )
                                } else it
                            }

                            // Update the milestone in the database (or server)
                            updateGoalUseCase(
                                goalId,
                                updatedMilestones.map { it.toMilestoneItem() })
                                .collectLatest { result ->
                                    when (result) {
                                        is Resource.Success -> {
                                            updateState { copy(milestoneList = updatedMilestones) }
                                        }

                                        is Resource.Error -> {
                                            emitEffect(
                                                MilestoneEffect.ShowError(
                                                    result.error.toMessageRes().toString()
                                                )
                                            )
                                        }

                                        is Resource.Loading -> {
                                            updateState { copy(isLoading = result.loading) }
                                        }
                                    }
                                }
                        } else {
                            // If the current user is not the author, show an error
                            emitEffect(MilestoneEffect.ShowError("You are not the author of this goal."))
                        }
                    }

                    is Resource.Error -> {
                        emitEffect(MilestoneEffect.ShowError("Error retrieving user ID"))
                    }

                    is Resource.Loading -> {
                        updateState { copy(isLoading = resource.loading) }
                    }
                }
            }
        }
    }


    private fun getMilestoneById(goalId: String) {
        viewModelScope.launch {
            getGoalMilestones(goalId = goalId).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> updateState { this.copy(errorMessage = resource.error.toMessageRes()) }
                    is Resource.Loading -> updateState { this.copy(isLoading = resource.loading) }

                    is Resource.Success -> {
                        val milestones = resource.data.milestone ?: emptyList()
                        val presentations = milestones.toPresentationList()
                        Log.d("MilestoneViewModel", "${resource.data}")
                        updateState {
                            copy(
                                milestoneList = presentations,
                                authorId = authorId
                            )
                        }
                    }
                }
            }
        }
    }
}