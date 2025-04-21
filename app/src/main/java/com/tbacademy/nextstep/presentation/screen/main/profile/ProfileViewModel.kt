package com.tbacademy.nextstep.presentation.screen.main.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.core.onSuccess
import com.tbacademy.nextstep.domain.usecase.auth.GetAuthUserIdUseCase
import com.tbacademy.nextstep.domain.usecase.user.GetUserInfoUseCase
import com.tbacademy.nextstep.domain.usecase.user_follow.CreateUserFollowUseCase
import com.tbacademy.nextstep.domain.usecase.user_follow.DeleteUserFollowUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.screen.main.home.model.FollowStatus
import com.tbacademy.nextstep.presentation.screen.main.profile.effect.ProfileEffect
import com.tbacademy.nextstep.presentation.screen.main.profile.event.ProfileEvent
import com.tbacademy.nextstep.presentation.screen.main.profile.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getAuthUserIdUseCase: GetAuthUserIdUseCase,
    private val createUserFollowUseCase: CreateUserFollowUseCase,
    private val deleteUserFollowUseCase: DeleteUserFollowUseCase
) : BaseViewModel<ProfileState, ProfileEvent, ProfileEffect, Unit>(
    initialState = ProfileState(),
    initialUiState = Unit
) {
    override fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.SetProfileState -> setProfileInfo(userId = event.userId)
            is ProfileEvent.ToggleFollowUser -> toggleFollowUser()
        }
    }

    private fun setProfileInfo(userId: String?) {
        viewModelScope.launch {
            getAuthUserIdUseCase().collectLatest { resource ->
                resource
                    .onSuccess { authId ->
                        if (userId != null) {
                            updateState { this.copy(withBottomNav = false) }
                        }

                        val resolvedUserId = userId ?: authId
                        val isOwnProfile = resolvedUserId == authId

                        updateState { this.copy(isOwnProfile = isOwnProfile) }

                        getUserInfo(userId = resolvedUserId)
                    }
            }
        }
    }

    private fun toggleFollowUser() {
        val newFollowState = if (!state.value.isUserFollowed) {
            createFollow()
            true
        } else {
            deleteFollow()
            false
        }
        updateState { this.copy(isUserFollowed = newFollowState) }
    }


    private fun createFollow() {
        viewModelScope.launch {
            state.value.user?.let { user ->
                createUserFollowUseCase(
                    followedId = user.uid,
                ).collectLatest { resource ->
                    resource.onSuccess {
                        updateState { copy(isUserFollowed = true) }
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
                        updateState { copy(isUserFollowed = false) }
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
                    is Resource.Success -> updateState { this.copy(user = resource.data) }
                    is Resource.Loading -> updateState { this.copy(isLoading = resource.loading) }
                }
            }
        }
    }
}