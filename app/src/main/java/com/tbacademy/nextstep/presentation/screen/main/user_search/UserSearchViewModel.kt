package com.tbacademy.nextstep.presentation.screen.main.user_search

import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.usecase.user.SearchUsersUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.screen.main.user_search.effect.UserSearchEffect
import com.tbacademy.nextstep.presentation.screen.main.user_search.event.UserSearchEvent
import com.tbacademy.nextstep.presentation.screen.main.user_search.mapper.toPresentation
import com.tbacademy.nextstep.presentation.screen.main.user_search.state.UserSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSearchViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase
) : BaseViewModel<UserSearchState, UserSearchEvent, UserSearchEffect, Unit>(
    initialState = UserSearchState(),
    initialUiState = Unit
) {
    override fun onEvent(event: UserSearchEvent) {
        when (event) {
            is UserSearchEvent.QueryChanged -> onQueryChange(query = event.value)
            is UserSearchEvent.BackRequest -> onBackRequest()
            is UserSearchEvent.UserSelected -> onUserSelected(userId = event.userId)
        }
    }

    private var searchJob: Job? = null

    private fun onQueryChange(query: String) {
        updateState { this.copy(searchQuery = query) }
        debounceSearch(query = query)
    }

    private fun onUserSelected(userId: String) {
        viewModelScope.launch {
            emitEffect(UserSearchEffect.NavigateToProfile(userId = userId))
        }
    }

    private fun onBackRequest() {
        viewModelScope.launch {
            emitEffect(UserSearchEffect.NavigateBack)
        }
    }

    private fun debounceSearch(query: String) {
        searchJob?.cancel()

        if (query.isBlank()) {
            updateState { this.copy(users = emptyList(), loading = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(300)
            searchUsers(query = query)
        }
    }

    private fun searchUsers(query: String) {
        viewModelScope.launch {
            searchUsersUseCase(query = query).collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> updateState { this.copy(loading = resource.loading) }
                    is Resource.Success -> updateState { this.copy(users = resource.data.map { it.toPresentation() }) }
                    is Resource.Error -> updateState { this.copy(errorRes = resource.error.toMessageRes()) }
                }
            }
        }
    }
}