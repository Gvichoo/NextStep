package com.tbacademy.nextstep.presentation.screen.main.milestone

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.usecase.goal.UpdateGoalUseCase
import com.tbacademy.nextstep.domain.usecase.auth.GetAuthUserIdFlowUseCase
import com.tbacademy.nextstep.domain.usecase.goal.GetMilestoneUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.model.toMilestoneItem
import com.tbacademy.nextstep.presentation.model.toPresentation
import com.tbacademy.nextstep.presentation.screen.main.milestone.effect.MilestoneEffect
import com.tbacademy.nextstep.presentation.screen.main.milestone.event.MilestoneEvent
import com.tbacademy.nextstep.presentation.screen.main.milestone.state.MilestoneState
import com.tbacademy.nextstep.presentation.screen.main.milestone.state.MilestoneUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MilestoneViewModel @Inject constructor(
    private val getGoalMilestones: GetMilestoneUseCase,
    private val updateGoalUseCase: UpdateGoalUseCase,
    private val getAuthUserIdUseCase: GetAuthUserIdFlowUseCase
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

            is MilestoneEvent.OpenMilestone -> onPostMilestoneClicked(
                event.milestoneId,
                event.text,
                event.goalId
            )
        }
    }

    private fun onPostMilestoneClicked(milestoneId: String, text: String, goalId: String) {
        viewModelScope.launch {
            emitEffect(
                MilestoneEffect.NavigateToMilestonePost(
                    milestoneId = milestoneId,
                    text = text,
                    goalId = goalId
                )
            )
        }
    }

    private fun markMilestoneAsDone(goalId: String, milestoneId: Int) {
        viewModelScope.launch {
            getAuthUserIdUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val currentUserId = result.data
                        val authorId = state.value.authorId

                        Log.d(
                            "MilestoneViewModel",
                            "Current User ID: $currentUserId, Goal Author ID: $authorId"
                        )

                        if (currentUserId == authorId) {
                            val updatedMilestones = state.value.milestoneList.map {
                                if (it.id == milestoneId) {

                                    Log.d(
                                        "MilestoneViewModel",
                                        "Milestone updated: ${it.text}, isAuthor: true"
                                    )
                                    it.copy(
                                        achieved = true,
                                        achievedAt = Timestamp.now(),
                                        isPostVisible = true,
                                        // Ensure this remains true after marking as done
                                    )
                                } else it
                            }

                            updateGoalUseCase(
                                goalId,
                                updatedMilestones.map { it.toMilestoneItem() })
                                .collectLatest { updateResult ->
                                    when (updateResult) {
                                        is Resource.Success -> {
                                            Log.d(
                                                "MilestoneViewModel",
                                                "Milestone marked as done successfully."
                                            )
                                            updateState { copy(milestoneList = updatedMilestones) } // Update the state with the new milestone list
                                        }

                                        is Resource.Error -> {
                                            emitEffect(
                                                MilestoneEffect.ShowError(
                                                    updateResult.error.toMessageRes().toString()
                                                )
                                            )
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
            var currentUserId: String? = null

            getAuthUserIdUseCase().collect { result ->
                if (result is Resource.Success) {
                    currentUserId = result.data
                }
            }
            getGoalMilestones(goalId = goalId).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> updateState { this.copy(errorMessage = resource.error.toMessageRes()) }
                    is Resource.Loading -> updateState { this.copy(isLoading = resource.loading) }

                    is Resource.Success -> {
                        Log.d("MilestoneViewModel", "Goal data: ${resource.data}")

                        val milestones = resource.data.milestone ?: emptyList()
                        val authorId = resource.data.authorId // Get the authorId from the goal data
                        val targetDateDate = Date(resource.data.targetDate) // Convert Long to Date
                        val targetDate = Timestamp(targetDateDate)

                        // Map the milestones to include authorId and isAuthor flag
                        val presentations = milestones.map { milestone ->
                            milestone.toPresentation().copy(
                                authorId = authorId, // Ensure authorId is correctly passed
                                isAuthor = (currentUserId == authorId),
                                // Compare authorId with milestone authorId
                                targetDate = targetDate
                            )
                        }

                        updateState {
                            copy(
                                milestoneList = presentations,
                                authorId = authorId, // Set authorId in the state
                                targetDate = resource.data.targetDate
                            )
                        }
                    }
                }
            }
        }
    }
}