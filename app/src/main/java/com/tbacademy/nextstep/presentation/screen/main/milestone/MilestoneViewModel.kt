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
            // Collect current user ID using the use case
            getAuthUserIdUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val currentUserId = result.data // Current logged-in user ID
                        val authorId = state.value.authorId // The authorId of the goal

                        // Log both values to check if they match
                        Log.d("MilestoneViewModel", "Current User ID: $currentUserId, Goal Author ID: $authorId")

                        // Compare current user ID with the authorId
                        if (currentUserId == authorId) {
                            // Proceed to mark the milestone as done
                            val updatedMilestones = state.value.milestoneList.map {
                                if (it.id == milestoneId) {
                                    it.copy(
                                        achieved = true,
                                        achievedAt = Timestamp.now(),
                                        isPostVisible = true, // make post button visible
                                        isAuthor = true
                                    )
                                } else it
                            }

                            // Call the use case to update the goal
                            updateGoalUseCase(goalId, updatedMilestones.map { it.toMilestoneItem() })
                                .collectLatest { updateResult ->
                                    when (updateResult) {
                                        is Resource.Success -> {
                                            // Log success and update UI
                                            Log.d("MilestoneViewModel", "Milestone marked as done successfully.")
                                            updateState { copy(milestoneList = updatedMilestones) }
                                        }
                                        is Resource.Error -> {
                                            emitEffect(MilestoneEffect.ShowError(updateResult.error.toMessageRes().toString()))
                                        }
                                        is Resource.Loading -> {
                                            updateState { copy(isLoading = updateResult.loading) }
                                        }
                                    }
                                }
                        } else {
                            Log.d("MilestoneViewModel", "User is not the author of this goal.")
                            emitEffect(MilestoneEffect.ShowError("You are not the author of this goal."))
                        }
                    }
                    is Resource.Error -> {
                        // Handle error if user ID retrieval fails
                        Log.d("MilestoneViewModel", "Error retrieving user ID")
                        emitEffect(MilestoneEffect.ShowError("Error retrieving user ID"))
                    }
                    is Resource.Loading -> {
                        // Handle loading state if needed
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
                        Log.d("MilestoneViewModel", "Goal data: ${resource.data}")

                        val milestones = resource.data.milestone ?: emptyList()
                        val presentations = milestones.toPresentationList()
                        Log.d("MilestoneViewModel", "${resource.data}")
                        updateState {
                            copy(
                                milestoneList = presentations,
                                authorId = resource.data.authorId,
                                targetDate = resource.data.targetDate
                            )
                        }
                    }
                }
            }
        }
    }
}