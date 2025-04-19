package com.tbacademy.nextstep.presentation.screen.main.profile

import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.core.onSuccess
import com.tbacademy.nextstep.domain.usecase.auth.GetAuthUserIdUseCase
import com.tbacademy.nextstep.domain.usecase.user.GetUserInfoUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
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
    private val getAuthUserIdUseCase: GetAuthUserIdUseCase
) : BaseViewModel<ProfileState, ProfileEvent, ProfileEffect, Unit>(
    initialState = ProfileState(),
    initialUiState = Unit
) {
    override fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.SetProfileState -> setProfileInfo(userId = event.userId)
        }
    }

    private fun setProfileInfo(userId: String?) {
        viewModelScope.launch {
            getAuthUserIdUseCase().collectLatest { resource ->
                resource
                    .onSuccess { authId ->
                        val resolvedUserId = userId ?: authId
                        val isOwnProfile = resolvedUserId == authId
                        updateState { this.copy(isOwnProfile = isOwnProfile) }

                        getUserInfo(userId = resolvedUserId)
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