package com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.core.InputValidationResult
import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.usecase.goal.CompleteGoalUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDescriptionUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateImageUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.base.launchEffect
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.extension.getErrorMessageResId
import com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal.effect.CompleteGoalEffect
import com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal.event.CompleteGoalEvent
import com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal.state.CompleteGoalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteGoalViewModel @Inject constructor(
    private val validateDescriptionUseCase: ValidateAddGoalDescriptionUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase,
    private val validateImage: ValidateImageUseCase,
) : BaseViewModel<CompleteGoalState, CompleteGoalEvent, CompleteGoalEffect, Unit>(
    initialState = CompleteGoalState(),
    initialUiState = Unit
) {
    override fun onEvent(event: CompleteGoalEvent) {
        when (event) {
            is CompleteGoalEvent.SetGoalInfo -> updateState {
                copy(
                    goalId = event.goalId,
                    goalTitle = event.goalTitle
                )
            }

            is CompleteGoalEvent.DescriptionChanged -> onDescriptionChanged(description = event.description)
            is CompleteGoalEvent.PickImage -> launchEffect(effect = CompleteGoalEffect.ShowUpdateImageDialog)
            is CompleteGoalEvent.ImageSelected -> updateState { this.copy(imageUri = event.imageUri) }
            is CompleteGoalEvent.CameraSelected -> launchEffect(effect = CompleteGoalEffect.LaunchCameraPicker)
            is CompleteGoalEvent.GallerySelected -> launchEffect(effect = CompleteGoalEffect.LaunchMediaPicker)
            is CompleteGoalEvent.Submit -> submitPost()
            is CompleteGoalEvent.Return -> launchEffect(effect = CompleteGoalEffect.NavigateBack)
        }
    }

    private fun submitPost() {
        val formIsValid = validateForm(
            description = state.value.description,
            imageUri = state.value.imageUri,
        )

        val imageUri: Uri? = state.value.imageUri
        val goalId: String? = state.value.goalId
        val goalTitle: String? = state.value.goalTitle

        if (formIsValid && imageUri != null && goalId != null && goalTitle != null) {
            completeGoal(
                title = goalTitle,
                goalId = goalId,
                imageUri = imageUri,
                description = state.value.description
            )
        } else {
            updateState { this.copy(formBeenSubmitted = true, isLoading = false) }
        }
    }

    private fun validateForm(
        description: String,
        imageUri: Uri?,
    ): Boolean {

        // Validate Inputs
        val descriptionValidationForm =
            validateDescriptionUseCase(description = description).getErrorMessageResId()

        val imageValidationError = validateImage(imageUri?.toString()).getErrorMessageResId()
        // Update states of errors
        updateState {
            copy(
                descriptionErrorMessage = descriptionValidationForm,
                imageErrorMessage = imageValidationError
            )
        }

        // Check if form if valid
        val errors: List<Int?> = listOf(
            descriptionValidationForm,
            imageValidationError
        )

        return errors.all { it == null }
    }

    // On Description Update
    private fun onDescriptionChanged(description: String) {
        updateState { this.copy(description = description) }

        val descriptionValidationResult =
            validateInputOnChange { validateDescriptionUseCase(description = description) }

        val descriptionErrorMessage: Int? = descriptionValidationResult?.getErrorMessageResId()
        updateState { copy(descriptionErrorMessage = descriptionErrorMessage) }
    }

    private fun validateInputOnChange(validator: () -> InputValidationResult): InputValidationResult? {
        return if (state.value.formBeenSubmitted)
            validator()
        else null
    }

    // Api Call

    private fun completeGoal(
        title: String,
        description: String,
        imageUri: Uri,
        goalId: String
    ) {
        viewModelScope.launch {
            completeGoalUseCase(title, description, imageUri, goalId).collectLatest { resource ->
                Log.d("RESOURCE_STATE", "$resource")
                when(resource) {
                    is Resource.Error -> launchEffect(effect = CompleteGoalEffect.ShowErrorMessage(errorRes = resource.error.toMessageRes()))

                    is Resource.Success -> {
                        launchEffect(effect = CompleteGoalEffect.NavigateToGoalCompleted)
                    }
                    is Resource.Loading -> updateState { copy(isLoading = resource.loading) }
                }
            }
        }

    }

    private fun postGoalCompletion() {

    }
}