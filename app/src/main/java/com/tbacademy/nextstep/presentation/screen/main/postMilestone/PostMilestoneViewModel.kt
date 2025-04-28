package com.tbacademy.nextstep.presentation.screen.main.postMilestone

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.core.model.error.InputValidationResult
import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.model.PostType
import com.tbacademy.nextstep.domain.usecase.post.CreatePostUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDescriptionUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateImageUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.extension.getErrorMessageResId
import com.tbacademy.nextstep.presentation.screen.main.postMilestone.effect.PostMilestoneEffect
import com.tbacademy.nextstep.presentation.screen.main.postMilestone.event.PostMilestoneEvent
import com.tbacademy.nextstep.presentation.screen.main.postMilestone.state.PostMilestoneState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostMilestoneViewModel @Inject constructor(
    private val validateDescriptionUseCase: ValidateAddGoalDescriptionUseCase,
    private val validateImageUseCase: ValidateImageUseCase,
    private val createPostUseCase: CreatePostUseCase

) : BaseViewModel<PostMilestoneState, PostMilestoneEvent, PostMilestoneEffect, Unit>(
    initialState = PostMilestoneState(),
    initialUiState = Unit
) {
    override fun onEvent(event: PostMilestoneEvent) {
        when (event) {
            is PostMilestoneEvent.DescriptionChanged -> onDescriptionChanged(event.description)

            is PostMilestoneEvent.ImageCleared -> updateState { this.copy(imageUri = null) }
            is PostMilestoneEvent.ImageSelected -> updateState { this.copy(imageUri = event.imageUri) }
            is PostMilestoneEvent.PickImageClicked -> viewModelScope.launch {
                emitEffect(
                    PostMilestoneEffect.LaunchMediaPicker
                )
            }

            is PostMilestoneEvent.Submit -> submitMilestonePostForm(goalId = event.goalId)
            is PostMilestoneEvent.SetTitle -> updateState { copy(title = event.title) }
            is PostMilestoneEvent.NavigateBack -> onBackRequest()
        }
    }


    private fun onBackRequest() {
        viewModelScope.launch {
            emitEffect(PostMilestoneEffect.NavigateBack)
        }
    }

    private fun submitMilestonePostForm(goalId: String) {
        val formIsValid = validateForm(
            description = state.value.description,
            imageUri = state.value.imageUri,
        )

        val imageUri: Uri? = state.value.imageUri

        Log.d("SUBMIT_FORM", "Form is valid: $formIsValid")  // Log whether form is valid or not

        if (formIsValid && imageUri != null) {
            Log.d(
                "POST_CREATION",
                "Creating Milestone Post: Title = ${state.value.title}, Description = ${state.value.description}, ImageUri = ${state.value.imageUri}"
            )  // Log here
            // If the form is valid, call the createMilestonePost function
            createMilestonePost(
                title = state.value.title,
                description = state.value.description,
                imageUri = imageUri,
                goalId = goalId
            )
        } else {
            // If the form is invalid, update the state and stop the loader
            updateState { this.copy(formBeenSubmitted = true, isLoading = false) }
            Log.d("SUBMIT_FORM", "Validation failed, checking errors...")
        }
    }

    private fun createMilestonePost(
        title: String,
        description: String,
        imageUri: Uri,
        goalId: String
    ) {
        viewModelScope.launch {

            createPostUseCase(
                title = title,
                description = description,
                imageUri = imageUri,
                type = PostType.MILESTONE,
                goalId = goalId
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        updateState { copy(isLoading = result.loading) }
                    }

                    is Resource.Success -> {
                        emitEffect(PostMilestoneEffect.NavigateToPosts)
                    }

                    is Resource.Error -> {
                    }
                }
            }

        }
    }

    private fun validateForm(
        description: String,
        imageUri: Uri?,
    ): Boolean {

        // Validate Inputs
        val descriptionValidationForm =
            validateDescriptionUseCase(description = description).getErrorMessageResId()

        val imageValidationError = validateImageUseCase(imageUri?.toString()).getErrorMessageResId()
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
        updateState { this.copy(descriptionErrorMessage = descriptionErrorMessage) }
    }

    private fun validateInputOnChange(validator: () -> InputValidationResult): InputValidationResult? {
        return if (state.value.formBeenSubmitted)
            validator()
        else null
    }

}