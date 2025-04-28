package com.tbacademy.nextstep.presentation.screen.main.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.core.ApiError
import com.tbacademy.core.Resource
import com.tbacademy.core.onSuccess
import com.tbacademy.nextstep.domain.manager.auth.AuthManager
import com.tbacademy.nextstep.domain.usecase.auth.GetAuthUserIdUseCase
import com.tbacademy.nextstep.domain.usecase.goal.GetUserGoalsUseCase
import com.tbacademy.nextstep.domain.usecase.user.GetUserInfoUseCase
import com.tbacademy.nextstep.domain.usecase.user.UpdateUserImageUseCase
import com.tbacademy.nextstep.domain.usecase.user_follow.CreateUserFollowUseCase
import com.tbacademy.nextstep.domain.usecase.user_follow.DeleteUserFollowUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.base.launchEffect
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.screen.main.profile.effect.ProfileEffect
import com.tbacademy.nextstep.presentation.screen.main.profile.event.ProfileEvent
import com.tbacademy.nextstep.presentation.screen.main.profile.mapper.toPresentation
import com.tbacademy.nextstep.presentation.screen.main.profile.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getAuthUserIdUseCase: GetAuthUserIdUseCase,
    private val authManager: AuthManager,
    private val createUserFollowUseCase: CreateUserFollowUseCase,
    private val deleteUserFollowUseCase: DeleteUserFollowUseCase,
    private val updateUserImageUseCase: UpdateUserImageUseCase,
    private val getUserGoalsUseCase: GetUserGoalsUseCase
) : BaseViewModel<ProfileState, ProfileEvent, ProfileEffect, Unit>(
    initialState = ProfileState(),
    initialUiState = Unit
) {
    override fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.SetProfileState -> setProfileInfo(userId = event.userId)
            is ProfileEvent.ToggleFollowUser -> toggleFollowUser()
            is ProfileEvent.BackRequest -> launchEffect(effect = ProfileEffect.NavigateBack)
            is ProfileEvent.UpdateImage -> launchEffect(effect = ProfileEffect.ShowUpdateImageDialog)
            is ProfileEvent.CameraSelected -> launchEffect(effect = ProfileEffect.LaunchCameraPicker)
            is ProfileEvent.GallerySelected -> launchEffect(effect = ProfileEffect.LaunchMediaPicker)
            is ProfileEvent.ImageSelected -> updateUserImage(imageUri = event.imageUri)
            is ProfileEvent.GoalSelected -> onGoalSelected(
                goalId = event.goalId,
                goalTitle = event.goalTitle,
                isActive = event.isActive
            )
        }
    }

    init {
        updateState { this.copy(isLoading = true) }
    }

    private fun setProfileInfo(userId: String?) {
        viewModelScope.launch {
            if (userId == null)
                updateState { this.copy(withBottomNav = true) }

            // ☢️ TO Be Fixed!!!
            val uid: String? = userId ?: authManager.getCurrentUserId()
            if (uid == null) {
                emitEffect(ProfileEffect.ShowErrorMessage(errorRes = ApiError.UserNotFound.toMessageRes()))
            } else {
                getUserInfo(userId = uid)
                getUserGoals(userId = uid)
            }
        }
    }

    private fun toggleFollowUser() {
        state.value.user?.let { user ->
            if (!user.isUserFollowed) {
                createFollow()
                true
            } else {
                deleteFollow()
                false
            }
        }
    }

    private fun onGoalSelected(goalId: String, goalTitle: String, isActive: Boolean) {
        launchEffect(
            effect = ProfileEffect.NavigateToGoalScreen(
                goalId = goalId,
                goalTitle = goalTitle,
                ownGoal = state.value.withBottomNav,
                isActive = isActive
            )
        )
    }

    private fun createFollow() {
        viewModelScope.launch {
            state.value.user?.let { user ->
                createUserFollowUseCase(
                    followedId = user.uid,
                ).collectLatest { resource ->
                    resource.onSuccess {
                        updateState { this.copy(user = user.copy(isUserFollowed = true)) }
                    }
                }
            }
        }
    }

    private fun deleteFollow() {
        viewModelScope.launch {
            state.value.user?.let { user ->
                deleteUserFollowUseCase(
                    followedId = user.uid,
                ).collectLatest { resource ->
                    resource.onSuccess {
                        updateState { this.copy(user = user.copy(isUserFollowed = false)) }
                    }
                }
            }
        }
    }

    // Api Calls
    private fun getUserInfo(userId: String) {
        viewModelScope.launch {
            getUserInfoUseCase(userId = userId).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> updateState { this.copy(errorRes = resource.error.toMessageRes()) }
                    is Resource.Success -> updateState {
                        Log.d("USER_STATE", "${resource.data.toPresentation()}")
                        this.copy(user = resource.data.toPresentation())
                    }

                    is Resource.Loading -> updateState { this.copy(isLoading = resource.loading) }
                }
            }
        }
    }

    private fun getUserGoals(userId: String) {
        viewModelScope.launch {
            getUserGoalsUseCase(userId = userId).collectLatest { resource ->
                Log.d("USER_GOAL_RESOURCE", "${resource}")
                when (resource) {
                    is Resource.Success -> updateState { this.copy(userGoals = resource.data.map { it.toPresentation() }) }
                    is Resource.Loading -> updateState { this.copy(isLoading = resource.loading) }
                    is Resource.Error -> updateState { this.copy(errorRes = resource.error.toMessageRes()) }
                }
            }
        }
    }

    private fun updateUserImage(imageUri: Uri) {
        viewModelScope.launch {
            state.value.user?.let { user ->
                updateUserImageUseCase(imageUri = imageUri).collectLatest { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            emitEffect(ProfileEffect.ShowErrorMessage(errorRes = resource.error.toMessageRes()))
                        }

                        is Resource.Success -> updateState {
                            this.copy(
                                user = user.copy(
                                    profilePictureUrl = resource.data
                                )
                            )
                        }

                        is Resource.Loading -> updateState { this.copy(isImageLoading = resource.loading) }
                    }
                }
            }
        }
    }
}